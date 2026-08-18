# Software Development Document Environment (com.cth.sdm)

## Overview
**Software Development Document Environment** is an enterprise-grade Java 21 Spring Boot web application designed for managing, processing, viewing, approving, and auditing software development lifecycle deliverables across 7 distinct phases.

It supports multi-database deployments (Oracle, PostgreSQL, SQL Server, and H2), automatic file directory watcher handlers, Maker/Checker approval workflow, toggleable email/SMS notifications, standard template parsing, PDF/Excel status reports, and an integrated local Python Flask/FAISS/Ollama AI LLM RAG engine for automated recommendations.

---

## Technical Stack & Prerequisites
- **Java**: 21
- **Build Tool**: Apache Maven 3.9+
- **Framework**: Spring Boot 3.2.3, Spring Data JPA, Spring Security
- **Document Processing**: Apache POI 5.2.5 (Excel, Word, PowerPoint), iText 7 (PDF)
- **Database**: Configurable for Oracle (`-Dspring.profiles.active=oracle`), PostgreSQL (`-Dspring.profiles.active=postgres`), SQL Server (`-Dspring.profiles.active=sqlserver`), or Default H2 In-Memory
- **API Documentation**: Springdoc OpenAPI / Swagger UI at `/swagger-ui/index.html`
- **AI RAG Subsystem**: Python 3.12, Flask, FAISS vector index, Ollama (`llama3.2:latest` & `nomic-embed-text`)

---

## Execution & Lifecycle Scripts

### Windows Environment
- **Build Package**: `build.bat` (Compiles source and builds executable JAR)
- **Start Application**: `start.bat`
- **Stop Application**: `stop.bat`
- **Run Test Suite**: `test.bat`

### Linux / Unix Environment
- **Build Package**: `./build.sh`
- **Start Application**: `./start.sh`
- **Stop Application**: `./stop.sh`
- **Run Test Suite**: `./test.sh`

---

## Key Features & User Workflows

1. **7 Phase Lifecycle & Deliverables**:
   - Sequential Document Auto ID: `P101`, `P102` (Phase 1), `P201`, `P202` (Phase 2), ..., `P701`, `P702` (Phase 7).
   - Configurable Document Codes, Version Numbers, and 3-character Application Code (`SDM`).

2. **Folder Watcher Application Handler**:
   - Monitored folder: `./incoming_documents`
   - Automatically ingests new documents, triggers registered `DocumentHandler` beans under `com.cth.sdm.handler`, and moves completed files to `./processed_documents`.

3. **Maker / Checker Approval Workflow**:
   - **Maker**: Submits document deliverables.
   - **Checker**: Reviews document and approves/rejects with comments.
   - **Notifications**: Email & SMS alerts upon submission and sign-off (Toggleable in Admin UI).

4. **Standard Template Ingestion**:
   - Upload Excel (`.xlsx`) or Word (`.docx`) forms to automatically extract key-value form fields into application forms.

5. **Online & Downloadable Reports**:
   - Real-time online status view.
   - One-click export to **Excel** (`/api/reports/export/excel`) or **PDF** (`/api/reports/export/pdf`) containing approver names and approval status.

6. **AI LLM RAG Recommendation Engine**:
   - Integrates with local Python RAG web app (`rag_app/`) utilizing FAISS vector search and Ollama LLM to produce semantic analysis and citations for each document.
