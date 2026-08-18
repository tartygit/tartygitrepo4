package com.cth.sdm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "document_phases")
public class DocumentPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phase_number", nullable = false, unique = true)
    private Integer phaseNumber;

    @Column(name = "phase_name", nullable = false, length = 100)
    private String phaseName;

    private String description;

    public DocumentPhase() {}

    public DocumentPhase(Integer phaseNumber, String phaseName, String description) {
        this.phaseNumber = phaseNumber;
        this.phaseName = phaseName;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPhaseNumber() { return phaseNumber; }
    public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; }

    public String getPhaseName() { return phaseName; }
    public void setPhaseName(String phaseName) { this.phaseName = phaseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
