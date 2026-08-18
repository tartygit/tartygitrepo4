-- SQL Server Database DDL Schema Script
-- Software Development Document Environment

CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    enabled BIT DEFAULT 1 NOT NULL,
    account_locked BIT DEFAULT 0 NOT NULL,
    failed_attempts INT DEFAULT 0 NOT NULL,
    password_expiry_date DATETIME2,
    created_at DATETIME2 DEFAULT GETDATE() NOT NULL
);

CREATE TABLE system_config (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value VARCHAR(500) NOT NULL,
    description VARCHAR(255),
    updated_at DATETIME2 DEFAULT GETDATE() NOT NULL
);

CREATE TABLE document_phases (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    phase_number INT NOT NULL UNIQUE,
    phase_name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE document_templates (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL,
    file_type VARCHAR(20) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    uploaded_by VARCHAR(50) NOT NULL,
    uploaded_at DATETIME2 DEFAULT GETDATE() NOT NULL
);

CREATE TABLE documents (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    document_id VARCHAR(50) NOT NULL UNIQUE,
    document_code VARCHAR(50) NOT NULL,
    app_code VARCHAR(3) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(20) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    version_number VARCHAR(20) NOT NULL,
    phase_number INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    maker_username VARCHAR(50) NOT NULL,
    checker_username VARCHAR(50),
    rejection_reason VARCHAR(500),
    ai_recommendation NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE() NOT NULL,
    updated_at DATETIME2 DEFAULT GETDATE() NOT NULL
);

CREATE TABLE audit_trails (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    timestamp DATETIME2 DEFAULT GETDATE() NOT NULL,
    username VARCHAR(50) NOT NULL,
    action VARCHAR(100) NOT NULL,
    details NVARCHAR(MAX),
    ip_address VARCHAR(50)
);
