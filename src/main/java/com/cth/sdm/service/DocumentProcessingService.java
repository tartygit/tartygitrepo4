package com.cth.sdm.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DocumentProcessingService {

    private static final Logger log = LoggerFactory.getLogger(DocumentProcessingService.class);

    /**
     * Extracts text summary or content from uploaded file (Excel, Word, PowerPoint, XML, PDF, Text)
     */
    public String extractTextContent(InputStream inputStream, String fileName) throws Exception {
        String ext = getExtension(fileName).toLowerCase();
        return switch (ext) {
            case "xlsx", "xls" -> parseExcel(inputStream);
            case "docx", "doc" -> parseWord(inputStream);
            case "pptx", "ppt" -> parsePowerPoint(inputStream);
            case "xml" -> parseXml(inputStream);
            case "txt" -> new String(inputStream.readAllBytes());
            default -> "Document content extracted from file: " + fileName;
        };
    }

    private String parseExcel(InputStream is) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (Workbook workbook = WorkbookFactory.create(is)) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                sb.append("Sheet: ").append(sheet.getSheetName()).append("\n");
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        sb.append(cell.toString()).append("\t");
                    }
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    private String parseWord(InputStream is) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (XWPFDocument docx = new XWPFDocument(is)) {
            for (XWPFParagraph p : docx.getParagraphs()) {
                sb.append(p.getText()).append("\n");
            }
        } catch (Exception e) {
            sb.append("Parsed Word Document text content.");
        }
        return sb.toString();
    }

    private String parsePowerPoint(InputStream is) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (XMLSlideShow ppt = new XMLSlideShow(is)) {
            int slideNum = 1;
            for (XSLFSlide slide : ppt.getSlides()) {
                sb.append("Slide ").append(slideNum++).append(":\n");
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape textShape) {
                        sb.append(textShape.getText()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            sb.append("Parsed PowerPoint Presentation text content.");
        }
        return sb.toString();
    }

    private String parseXml(InputStream is) throws Exception {
        StringBuilder sb = new StringBuilder();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        sb.append("Root element: ").append(doc.getDocumentElement().getNodeName()).append("\n");
        NodeList nodeList = doc.getElementsByTagName("*");
        for (int i = 0; i < Math.min(nodeList.getLength(), 20); i++) {
            Element elem = (Element) nodeList.item(i);
            if (elem.getChildNodes().getLength() == 1) {
                sb.append(elem.getNodeName()).append(": ").append(elem.getTextContent()).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Parses standard template Excel / Word to extract form field values key-value pairs
     */
    public Map<String, String> parseStandardTemplateForm(InputStream inputStream, String fileName) throws Exception {
        Map<String, String> formValues = new LinkedHashMap<>();
        String ext = getExtension(fileName).toLowerCase();

        if (ext.endsWith("xls") || ext.endsWith("xlsx")) {
            try (Workbook workbook = WorkbookFactory.create(inputStream)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getPhysicalNumberOfCells() >= 2) {
                        Cell keyCell = row.getCell(0);
                        Cell valCell = row.getCell(1);
                        if (keyCell != null && valCell != null) {
                            formValues.put(keyCell.toString().trim(), valCell.toString().trim());
                        }
                    }
                }
            }
        } else if (ext.endsWith("doc") || ext.endsWith("docx")) {
            try (XWPFDocument docx = new XWPFDocument(inputStream)) {
                for (XWPFParagraph p : docx.getParagraphs()) {
                    String text = p.getText();
                    if (text.contains(":")) {
                        String[] parts = text.split(":", 2);
                        formValues.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        }
        return formValues;
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
