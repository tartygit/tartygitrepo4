package com.cth.sdm;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

public class TemplateGenerator {

    public static void main(String[] args) throws Exception {
        File tmplDir = new File("src/main/resources/static/templates");
        if (!tmplDir.exists()) tmplDir.mkdirs();

        generateExcelTemplate(new File(tmplDir, "standard_template.xlsx"));
        generateWordTemplate(new File(tmplDir, "standard_template.docx"));

        System.out.println("Standard templates generated successfully in static/templates/");
    }

    private static void generateExcelTemplate(File file) throws Exception {
        try (Workbook wb = new XSSFWorkbook(); FileOutputStream out = new FileOutputStream(file)) {
            Sheet sheet = wb.createSheet("Template Form");

            Row r0 = sheet.createRow(0);
            r0.createCell(0).setCellValue("Field Name");
            r0.createCell(1).setCellValue("Field Value");

            String[][] fields = {
                {"Project Name", "Software Development Environment"},
                {"Document Code", "DOC-2026-TMPL"},
                {"Application Code", "SDM"},
                {"Phase Number", "1"},
                {"Version Number", "1.0"},
                {"Document Title", "Standard Project Initiation Form"},
                {"Description", "Automated template form data for direct upload and save."}
            };

            for (int i = 0; i < fields.length; i++) {
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(fields[i][0]);
                r.createCell(1).setCellValue(fields[i][1]);
            }

            wb.write(out);
        }
    }

    private static void generateWordTemplate(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(); FileOutputStream out = new FileOutputStream(file)) {
            XWPFParagraph title = doc.createParagraph();
            XWPFRun rTitle = title.createRun();
            rTitle.setBold(true);
            rTitle.setFontSize(16);
            rTitle.setText("Standard Project Deliverable Form Template");
            title.createRun().addBreak();

            String[] lines = {
                "Project Name: Software Development Environment",
                "Document Code: DOC-2026-TMPL",
                "Application Code: SDM",
                "Phase Number: 1",
                "Version Number: 1.0",
                "Document Title: Standard Project Initiation Form",
                "Description: Automated template form data for direct upload and save."
            };

            for (String line : lines) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(line);
            }

            doc.write(out);
        }
    }
}
