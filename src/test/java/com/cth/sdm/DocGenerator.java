package com.cth.sdm;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

public class DocGenerator {

    public static void main(String[] args) throws Exception {
        File docsDir = new File("docs");
        if (!docsDir.exists()) docsDir.mkdirs();

        generateFunctionalSpec(new File(docsDir, "Functional_Specification.docx"));
        generateInstallationInstructions(new File(docsDir, "Installation_Instructions.docx"));
        generateProductPaper(new File(docsDir, "Product_Paper.docx"));
        generateWalkthroughPpt(new File(docsDir, "Walkthrough.pptx"));
        generateSystemInstructionsXls(new File(docsDir, "System_Instructions.xlsx"));

        System.out.println("Documents generated successfully in docs/");
    }

    private static void generateFunctionalSpec(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(); FileOutputStream out = new FileOutputStream(file)) {
            XWPFParagraph p1 = doc.createParagraph();
            XWPFRun r1 = p1.createRun();
            r1.setBold(true);
            r1.setFontSize(20);
            r1.setText("Functional Specification Document (FSD)");
            p1.createRun().addBreak();

            XWPFParagraph p2 = doc.createParagraph();
            XWPFRun r2 = p2.createRun();
            r2.setFontSize(12);
            r2.setText("System: Software Development Document Environment (com.cth.sdm)");
            r2.addBreak();
            r2.setText("1. Purpose: Centralized repository for 7 SDLC Phase deliverable documents.");
            r2.addBreak();
            r2.setText("2. Workflows: Maker submission, Checker approval, Folder Watcher ingestion, AI LLM RAG recommendation.");
            r2.addBreak();
            r2.setText("3. Security & Admin: Eye icon password toggle, LDAP / Non-LDAP switch, user lock/unlock/reset, toggleable email/SMS alerts.");

            doc.write(out);
        }
    }

    private static void generateInstallationInstructions(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(); FileOutputStream out = new FileOutputStream(file)) {
            XWPFParagraph p1 = doc.createParagraph();
            XWPFRun r1 = p1.createRun();
            r1.setBold(true);
            r1.setFontSize(20);
            r1.setText("Installation & Setup Instructions Document");
            p1.createRun().addBreak();

            XWPFParagraph p2 = doc.createParagraph();
            XWPFRun r2 = p2.createRun();
            r2.setFontSize(12);
            r2.setText("System: Software Development Document Environment (com.cth.sdm)");
            r2.addBreak();
            r2.setText("Prerequisites: Java 21, Maven 3.9+, Python 3.12 (for RAG subproject).");
            r2.addBreak();
            r2.setText("1. Building Package: Run 'build.bat' on Windows or './build.sh' on Linux.");
            r2.addBreak();
            r2.setText("2. Launching Application: Run 'start.bat' or './start.sh'. Access at http://localhost:8080.");
            r2.addBreak();
            r2.setText("3. Stopping Application: Run 'stop.bat' or './stop.sh'.");
            r2.addBreak();
            r2.setText("4. Testing Suite: Run 'test.bat' or './test.sh'.");

            doc.write(out);
        }
    }

    private static void generateProductPaper(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(); FileOutputStream out = new FileOutputStream(file)) {
            XWPFParagraph p1 = doc.createParagraph();
            XWPFRun r1 = p1.createRun();
            r1.setBold(true);
            r1.setFontSize(20);
            r1.setText("Product Whitepaper & Technical Architecture");
            p1.createRun().addBreak();

            XWPFParagraph p2 = doc.createParagraph();
            XWPFRun r2 = p2.createRun();
            r2.setFontSize(12);
            r2.setText("Title: Enterprise SDLC Document Environment with Local AI LLM RAG Capabilities");
            r2.addBreak();
            r2.setText("Abstract: Software Development Document Environment (com.cth.sdm) provides end-to-end SDLC document management with multi-database compatibility, automated folder watchers, Maker/Checker governance, and local Ollama + FAISS RAG intelligent analysis.");

            doc.write(out);
        }
    }

    private static void generateWalkthroughPpt(File file) throws Exception {
        try (XMLSlideShow ppt = new XMLSlideShow(); FileOutputStream out = new FileOutputStream(file)) {
            XSLFSlide slide1 = ppt.createSlide();
            XSLFTextBox titleBox = slide1.createTextBox();
            titleBox.setText("Software Development Document Environment Walkthrough");

            XSLFSlide slide2 = ppt.createSlide();
            XSLFTextBox bodyBox = slide2.createTextBox();
            bodyBox.setText("Key Features:\n- Multi-Database Support (Oracle, Postgres, SQL Server)\n- Folder Watcher Handler\n- Maker/Checker Workflow\n- AI LLM RAG Analysis");

            ppt.write(out);
        }
    }

    private static void generateSystemInstructionsXls(File file) throws Exception {
        try (Workbook wb = new XSSFWorkbook(); FileOutputStream out = new FileOutputStream(file)) {
            Sheet sheet = wb.createSheet("System Instructions");
            Row r0 = sheet.createRow(0);
            r0.createCell(0).setCellValue("Step");
            r0.createCell(1).setCellValue("Instruction");

            Row r1 = sheet.createRow(1);
            r1.createCell(0).setCellValue("1");
            r1.createCell(1).setCellValue("Run build.bat or ./build.sh to package.");

            Row r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue("2");
            r2.createCell(1).setCellValue("Run start.bat or ./start.sh to launch server on port 8080.");

            wb.write(out);
        }
    }
}
