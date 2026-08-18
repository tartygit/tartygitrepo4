package com.cth.sdm;

import com.cth.sdm.entity.Document;
import com.cth.sdm.repository.DocumentRepository;
import com.cth.sdm.service.ApprovalWorkflowService;
import com.cth.sdm.service.DocumentIdGenerator;
import com.cth.sdm.service.DocumentProcessingService;
import com.cth.sdm.service.UserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private DocumentIdGenerator idGenerator;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ApprovalWorkflowService approvalWorkflowService;

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentProcessingService documentProcessingService;

    @Test
    void testSequentialDocumentIdGeneration() {
        String p101 = idGenerator.generateNextDocumentId(1);
        assertEquals("P101", p101);

        Document doc = new Document();
        doc.setDocumentId(p101);
        doc.setDocumentCode("CODE-101");
        doc.setAppCode("SDM");
        doc.setTitle("Phase 1 Spec");
        doc.setFileName("spec.docx");
        doc.setFileType("docx");
        doc.setFilePath("/tmp/spec.docx");
        doc.setVersionNumber("1.0");
        doc.setPhaseNumber(1);
        doc.setStatus("PENDING_APPROVAL");
        doc.setMakerUsername("maker");
        documentRepository.save(doc);

        String p102 = idGenerator.generateNextDocumentId(1);
        assertEquals("P102", p102);

        String p201 = idGenerator.generateNextDocumentId(2);
        assertEquals("P201", p201);
    }

    @Test
    void testMakerCheckerWorkflow() {
        Document doc = new Document();
        doc.setDocumentId("P301");
        doc.setDocumentCode("CODE-301");
        doc.setAppCode("SDM");
        doc.setTitle("Architecture Doc");
        doc.setFileName("arch.pdf");
        doc.setFileType("pdf");
        doc.setFilePath("/tmp/arch.pdf");
        doc.setVersionNumber("1.0");
        doc.setPhaseNumber(3);

        Document submitted = approvalWorkflowService.submitDocument(doc, "maker");
        assertEquals("PENDING_APPROVAL", submitted.getStatus());

        Document approved = approvalWorkflowService.approveDocument(submitted.getId(), "checker");
        assertEquals("APPROVED", approved.getStatus());
        assertEquals("checker", approved.getCheckerUsername());
    }

    @Test
    void testUserManagement() {
        assertTrue(userService.findByUsername("admin").isPresent());
        assertTrue(userService.findByUsername("maker").isPresent());
        assertTrue(userService.findByUsername("checker").isPresent());
    }

    @Test
    void testTemplateParsing() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet();
            Row r0 = sheet.createRow(0);
            r0.createCell(0).setCellValue("Project Name");
            r0.createCell(1).setCellValue("Software Development Environment");

            Row r1 = sheet.createRow(1);
            r1.createCell(0).setCellValue("Version");
            r1.createCell(1).setCellValue("1.0.0");

            wb.write(out);
        }

        ByteArrayInputStream is = new ByteArrayInputStream(out.toByteArray());
        Map<String, String> parsed = documentProcessingService.parseStandardTemplateForm(is, "template.xlsx");
        assertNotNull(parsed);
        assertEquals("Software Development Environment", parsed.get("Project Name"));
        assertEquals("1.0.0", parsed.get("Version"));
    }
}
