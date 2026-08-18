# API Documentation - Software Development Document Environment

## Base URL
`http://localhost:8080/api`

## Swagger UI Documentation
Open browser at: `http://localhost:8080/swagger-ui/index.html`

---

## REST Endpoints Summary

### 1. Dashboard Statistics
- **GET** `/api/dashboard/stats`
  - **Description**: Returns total documents count, pending approvals, approved count, and rejected count.
  - **Response**:
    ```json
    {
      "totalDocuments": 10,
      "pendingApprovals": 2,
      "approvedDocuments": 7,
      "rejectedDocuments": 1
    }
    ```

### 2. Document Management
- **GET** `/api/documents`
  - **Description**: Retrieves list of all documents across all phases.
- **GET** `/api/documents/phase/{phaseNumber}`
  - **Description**: Retrieves list of documents filtered by phase number (1 to 7).
- **POST** `/api/documents/upload`
  - **Description**: Submits a new deliverable document (Excel, Word, PowerPoint, XML, PDF).
  - **Form Parameters**: `file`, `phaseNumber`, `documentCode`, `appCode`, `title`, `description`, `versionNumber`, `makerUsername`.
- **POST** `/api/documents/template-upload`
  - **Description**: Uploads standard Excel or Word template form to auto-fill extracted field values.

### 3. Maker / Checker Approval Workflow
- **GET** `/api/checker/pending`
  - **Description**: List all documents pending approval.
- **POST** `/api/checker/approve/{id}`
  - **Query Parameter**: `checkerUsername`
  - **Description**: Approves specified document.
- **POST** `/api/checker/reject/{id}`
  - **Query Parameters**: `checkerUsername`, `reason`
  - **Description**: Rejects specified document with reason.

### 4. Configurable Reporting
- **GET** `/api/reports/data`
  - **Description**: Online report data list with maker/checker details.
- **GET** `/api/reports/export/excel`
  - **Description**: Download status report in Excel spreadsheet format (`.xlsx`).
- **GET** `/api/reports/export/pdf`
  - **Description**: Download status report in PDF document format (`.pdf`).

### 5. Administration & User Maintenance
- **GET** `/api/admin/users`
  - **Description**: List system users.
- **POST** `/api/admin/users/create`
  - **Parameters**: `username`, `password`, `fullName`, `email`, `phone`, `role`.
- **POST** `/api/admin/users/lock/{username}`
- **POST** `/api/admin/users/unlock/{username}`
- **POST** `/api/admin/users/reset-password/{username}`
- **POST** `/api/admin/config/toggles`
  - **Parameters**: `emailEnabled`, `smsEnabled`
