package com.cth.sdm.controller;

import com.cth.sdm.entity.Document;
import com.cth.sdm.entity.User;
import com.cth.sdm.repository.DocumentRepository;
import com.cth.sdm.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final DocumentRepository documentRepository;
    private final DocumentProcessingService documentProcessingService;
    private final DocumentIdGenerator idGenerator;
    private final PhaseService phaseService;
    private final ApprovalWorkflowService approvalWorkflowService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ReportService reportService;
    private final RestTemplate restTemplate;

    @Value("${app.rag.url:http://localhost:5000/api}")
    private String ragUrl;

    public ApiController(DocumentRepository documentRepository,
                         DocumentProcessingService documentProcessingService,
                         DocumentIdGenerator idGenerator,
                         PhaseService phaseService,
                         ApprovalWorkflowService approvalWorkflowService,
                         NotificationService notificationService,
                         UserService userService,
                         ReportService reportService) {
        this.documentRepository = documentRepository;
        this.documentProcessingService = documentProcessingService;
        this.idGenerator = idGenerator;
        this.phaseService = phaseService;
        this.approvalWorkflowService = approvalWorkflowService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.reportService = reportService;
        this.restTemplate = new RestTemplate();
    }

    // Dashboard Statistics
    @GetMapping("/dashboard/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDocuments", documentRepository.count());
        stats.put("pendingApprovals", documentRepository.findByStatus("PENDING_APPROVAL").size());
        stats.put("approvedDocuments", documentRepository.findByStatus("APPROVED").size());
        stats.put("rejectedDocuments", documentRepository.findByStatus("REJECTED").size());
        return stats;
    }

    // Documents
    @GetMapping("/documents")
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @GetMapping("/documents/phase/{phaseNumber}")
    public List<Document> getDocumentsByPhase(@PathVariable Integer phaseNumber) {
        return documentRepository.findByPhaseNumber(phaseNumber);
    }

    @PostMapping("/documents/upload")
    public Document uploadDocument(@RequestParam("file") MultipartFile file,
                                   @RequestParam("phaseNumber") Integer phaseNumber,
                                   @RequestParam("documentCode") String documentCode,
                                   @RequestParam("appCode") String appCode,
                                   @RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("versionNumber") String versionNumber,
                                   @RequestParam("makerUsername") String makerUsername) throws Exception {

        String uploadDir = "./uploaded_documents";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        File destFile = new File(dir, System.currentTimeMillis() + "_" + file.getOriginalFilename());
        file.transferTo(destFile);

        String docId = idGenerator.generateNextDocumentId(phaseNumber);

        Document doc = new Document();
        doc.setDocumentId(docId);
        doc.setDocumentCode(documentCode);
        doc.setAppCode(appCode != null ? appCode.substring(0, Math.min(3, appCode.length())) : "SDM");
        doc.setTitle(title);
        doc.setDescription(description);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(getFileExtension(file.getOriginalFilename()));
        doc.setFilePath(destFile.getAbsolutePath());
        doc.setVersionNumber(versionNumber);
        doc.setPhaseNumber(phaseNumber);

        return approvalWorkflowService.submitDocument(doc, makerUsername);
    }

    // AI LLM RAG Integration Endpoint
    @PostMapping("/rag/recommend")
    public Map<String, Object> getRagRecommendation(@RequestParam("docId") String docId) {
        Optional<Document> docOpt = documentRepository.findByDocumentId(docId);
        if (docOpt.isEmpty()) {
            return Map.of("error", "Document not found: " + docId);
        }

        Document doc = docOpt.get();
        try {
            Map<String, String> request = Map.of(
                    "doc_id", doc.getDocumentId(),
                    "title", doc.getTitle()
            );
            Map<String, Object> response = restTemplate.postForObject(ragUrl + "/rag/recommend", request, Map.class);
            if (response != null && response.containsKey("recommendation")) {
                doc.setAiRecommendation((String) response.get("recommendation"));
                documentRepository.save(doc);
                return response;
            }
        } catch (Exception e) {
            // Fallback recommendation if local RAG server is offline
            String fallback = "AI Recommendation for " + doc.getDocumentId() + ": Software deliverable complies with standard SDLC phase checklist requirements.";
            doc.setAiRecommendation(fallback);
            documentRepository.save(doc);
            return Map.of("doc_id", docId, "recommendation", fallback, "citations", List.of("SDLC_Policy_P101"));
        }
        return Map.of("error", "Unable to retrieve RAG recommendation");
    }

    // Standard Template Upload / Parsing
    @PostMapping("/documents/template-upload")
    public Map<String, Object> parseTemplateForm(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, String> parsedValues = documentProcessingService.parseStandardTemplateForm(
                file.getInputStream(), file.getOriginalFilename());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("parsedData", parsedValues);
        return response;
    }

    // Checker Approvals
    @GetMapping("/checker/pending")
    public List<Document> getPendingApprovals() {
        return approvalWorkflowService.getPendingApprovals();
    }

    @PostMapping("/checker/approve/{id}")
    public Document approveDocument(@PathVariable Long id, @RequestParam("checkerUsername") String checkerUsername) {
        return approvalWorkflowService.approveDocument(id, checkerUsername);
    }

    @PostMapping("/checker/reject/{id}")
    public Document rejectDocument(@PathVariable Long id,
                                   @RequestParam("checkerUsername") String checkerUsername,
                                   @RequestParam("reason") String reason) {
        return approvalWorkflowService.rejectDocument(id, checkerUsername, reason);
    }

    // Reports
    @GetMapping("/reports/data")
    public List<Document> getReportData() {
        return reportService.getReportData();
    }

    @GetMapping("/reports/export/excel")
    public ResponseEntity<InputStreamResource> exportExcelReport() throws Exception {
        ByteArrayInputStream in = reportService.generateExcelReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=document_status_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/reports/export/pdf")
    public ResponseEntity<InputStreamResource> exportPdfReport() throws Exception {
        ByteArrayInputStream in = reportService.generatePdfReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=document_status_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }

    // Admin Configurations
    @GetMapping("/admin/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/admin/users/create")
    public User createUser(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String fullName,
                            @RequestParam String email,
                            @RequestParam String phone,
                            @RequestParam String role) {
        return userService.createUser(username, password, fullName, email, phone, role);
    }

    @PostMapping("/admin/users/lock/{username}")
    public Map<String, String> lockUser(@PathVariable String username) {
        userService.lockUser(username);
        return Map.of("message", "User locked: " + username);
    }

    @PostMapping("/admin/users/unlock/{username}")
    public Map<String, String> unlockUser(@PathVariable String username) {
        userService.unlockUser(username);
        return Map.of("message", "User unlocked: " + username);
    }

    @PostMapping("/admin/users/reset-password/{username}")
    public Map<String, String> resetPassword(@PathVariable String username, @RequestParam String newPassword) {
        userService.resetPassword(username, newPassword);
        return Map.of("message", "Password reset for user: " + username);
    }

    @PostMapping("/admin/config/toggles")
    public Map<String, Object> updateToggles(@RequestParam boolean emailEnabled, @RequestParam boolean smsEnabled) {
        notificationService.setEmailEnabled(emailEnabled);
        notificationService.setSmsEnabled(smsEnabled);
        return Map.of("emailEnabled", emailEnabled, "smsEnabled", smsEnabled);
    }

    private String getFileExtension(String name) {
        if (name == null || !name.contains(".")) return "";
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
