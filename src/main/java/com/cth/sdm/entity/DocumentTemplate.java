package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_templates")
public class DocumentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    @Column(name = "file_type", nullable = false, length = 20)
    private String fileType;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "uploaded_by", nullable = false, length = 50)
    private String uploadedBy;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public DocumentTemplate() {}

    public DocumentTemplate(String templateName, String fileType, String filePath, String uploadedBy) {
        this.templateName = templateName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.uploadedBy = uploadedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
