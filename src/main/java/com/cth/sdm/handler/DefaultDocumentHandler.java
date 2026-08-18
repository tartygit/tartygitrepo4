package com.cth.sdm.handler;

import com.cth.sdm.entity.Document;
import com.cth.sdm.repository.DocumentRepository;
import com.cth.sdm.service.DocumentIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DefaultDocumentHandler implements DocumentHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultDocumentHandler.class);

    private final DocumentRepository documentRepository;
    private final DocumentIdGenerator idGenerator;

    public DefaultDocumentHandler(DocumentRepository documentRepository, DocumentIdGenerator idGenerator) {
        this.documentRepository = documentRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public String getHandlerName() {
        return "DEFAULT_HANDLER";
    }

    @Override
    public boolean supports(File file) {
        return file != null && file.exists() && file.isFile();
    }

    @Override
    public void process(File file) {
        log.info("Processing file via DefaultDocumentHandler: {}", file.getName());
        try {
            int phaseNumber = 1; // Default to Phase 1 for incoming watched files
            String docId = idGenerator.generateNextDocumentId(phaseNumber);

            Document doc = new Document();
            doc.setDocumentId(docId);
            doc.setDocumentCode("AUTO_PICKUP");
            doc.setAppCode("SDM");
            doc.setTitle("Auto Picked File: " + file.getName());
            doc.setDescription("Picked up automatically from watched directory");
            doc.setFileName(file.getName());
            doc.setFileType(getFileExtension(file.getName()));
            doc.setFilePath(file.getAbsolutePath());
            doc.setVersionNumber("1.0");
            doc.setPhaseNumber(phaseNumber);
            doc.setStatus("PENDING_APPROVAL");
            doc.setMakerUsername("FOLDER_WATCHER");

            documentRepository.save(doc);
            log.info("Successfully ingested document {}", docId);
        } catch (Exception e) {
            log.error("Error processing watched file {}: {}", file.getName(), e.getMessage(), e);
        }
    }

    private String getFileExtension(String name) {
        if (name == null || !name.contains(".")) return "unknown";
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
