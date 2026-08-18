package com.cth.sdm.service;

import com.cth.sdm.entity.DocumentPhase;
import com.cth.sdm.repository.DocumentPhaseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PhaseService {

    private final DocumentPhaseRepository phaseRepository;

    public PhaseService(DocumentPhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    public static final Map<Integer, List<String>> PHASE_DELIVERABLES = Map.of(
        1, List.of(
            "Software Development Request Form",
            "Initial Assessment & Feasibility Study"
        ),
        2, List.of(
            "Business Requirement Document (BRD)",
            "System Requirement Specification (SRS)",
            "Functional Specification Document (FSD)"
        ),
        3, List.of(
            "System Architecture & Design Document (SADD)",
            "Database Schema Design & ERD",
            "API Interface Specification"
        ),
        4, List.of(
            "Source Code Repository & Build Scripts",
            "Code Review & Static Security Analysis Report",
            "Unit Test Execution Results"
        ),
        5, List.of(
            "System Integration Test (SIT) Plan & Results",
            "User Acceptance Test (UAT) Sign-off Document",
            "Performance & Load Testing Report"
        ),
        6, List.of(
            "Deployment Plan & Checklist",
            "User Manual & System Administration Guide",
            "Production Release Sign-off Form"
        ),
        7, List.of(
            "Post Implementation Review (PIR) Report",
            "Service Level Agreement (SLA) & Support Handover",
            "Maintenance & Incident Log Matrix"
        )
    );

    @PostConstruct
    public void initDefaultPhases() {
        if (phaseRepository.count() == 0) {
            String[] phaseNames = {
                "Phase 1: Project Initiation & Planning",
                "Phase 2: Requirements & Functional Design",
                "Phase 3: System Architecture & Technical Design",
                "Phase 4: Software Construction & Coding",
                "Phase 5: System Integration & Acceptance Testing",
                "Phase 6: Deployment & Handover",
                "Phase 7: Maintenance & Post-Implementation Review"
            };

            for (int i = 1; i <= 7; i++) {
                DocumentPhase phase = new DocumentPhase(
                    i,
                    phaseNames[i - 1],
                    "Phase " + i + " deliverable documents tracking and verification."
                );
                phaseRepository.save(phase);
            }
        }
    }

    public List<DocumentPhase> getAllPhases() {
        return phaseRepository.findAll();
    }

    public List<String> getDeliverablesForPhase(int phaseNumber) {
        return PHASE_DELIVERABLES.getOrDefault(phaseNumber, List.of());
    }
}
