package com.cth.sdm.service;

import com.cth.sdm.entity.Document;
import com.cth.sdm.repository.DocumentRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

    private final DocumentRepository documentRepository;

    public ReportService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> getReportData() {
        return documentRepository.findAll();
    }

    public ByteArrayInputStream generateExcelReport() throws Exception {
        List<Document> docs = documentRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Document Status Report");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Doc ID", "Code", "App", "Title", "Phase", "Version", "Status", "Maker", "Checker", "Updated At"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (Document doc : docs) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(doc.getDocumentId());
                row.createCell(1).setCellValue(doc.getDocumentCode());
                row.createCell(2).setCellValue(doc.getAppCode());
                row.createCell(3).setCellValue(doc.getTitle());
                row.createCell(4).setCellValue(doc.getPhaseNumber());
                row.createCell(5).setCellValue(doc.getVersionNumber());
                row.createCell(6).setCellValue(doc.getStatus());
                row.createCell(7).setCellValue(doc.getMakerUsername() != null ? doc.getMakerUsername() : "");
                row.createCell(8).setCellValue(doc.getCheckerUsername() != null ? doc.getCheckerUsername() : "N/A");
                row.createCell(9).setCellValue(doc.getUpdatedAt() != null ? doc.getUpdatedAt().toString() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream generatePdfReport() throws Exception {
        List<Document> docs = documentRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        com.itextpdf.layout.Document docLayout = new com.itextpdf.layout.Document(pdf);

        docLayout.add(new Paragraph("Software Development Document Environment - Status Report"));
        docLayout.add(new Paragraph("Generated Report with Approval Details\n\n"));

        Table table = new Table(new float[]{1, 1, 2, 1, 1, 1, 1});
        table.addHeaderCell("Doc ID");
        table.addHeaderCell("Code");
        table.addHeaderCell("Title");
        table.addHeaderCell("Phase");
        table.addHeaderCell("Status");
        table.addHeaderCell("Maker");
        table.addHeaderCell("Checker");

        for (Document d : docs) {
            table.addCell(d.getDocumentId());
            table.addCell(d.getDocumentCode());
            table.addCell(d.getTitle());
            table.addCell(String.valueOf(d.getPhaseNumber()));
            table.addCell(d.getStatus());
            table.addCell(d.getMakerUsername() != null ? d.getMakerUsername() : "");
            table.addCell(d.getCheckerUsername() != null ? d.getCheckerUsername() : "N/A");
        }

        docLayout.add(table);
        docLayout.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
