package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing management, cultural, biological, and verified chemical treatment recommendations.
 */
@Entity(
    tableName = "treatments",
    foreignKeys = @ForeignKey(
        entity = Disease.class,
        parentColumns = "diseaseId",
        childColumns = "diseaseId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "diseaseId")}
)
public class Treatment {

    @PrimaryKey(autoGenerate = true)
    private long treatmentId;

    @NonNull
    private String diseaseId;

    @NonNull
    private String treatmentType; // "Immediate Action", "Cultural & Sanitation", "Biological Control", "Chemical Control"

    @NonNull
    private String title;

    @NonNull
    private String instructions;

    private String safetyPrecautions;

    private String registrationNotice; // Warning regarding local chemical registration and PPE

    public Treatment(@NonNull String diseaseId, @NonNull String treatmentType,
                     @NonNull String title, @NonNull String instructions,
                     String safetyPrecautions, String registrationNotice) {
        this.diseaseId = diseaseId;
        this.treatmentType = treatmentType;
        this.title = title;
        this.instructions = instructions;
        this.safetyPrecautions = safetyPrecautions;
        this.registrationNotice = registrationNotice;
    }

    public long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(long treatmentId) {
        this.treatmentId = treatmentId;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @NonNull
    public String getTreatmentType() {
        return treatmentType;
    }

    public void setTreatmentType(@NonNull String treatmentType) {
        this.treatmentType = treatmentType;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(@NonNull String instructions) {
        this.instructions = instructions;
    }

    public String getSafetyPrecautions() {
        return safetyPrecautions;
    }

    public void setSafetyPrecautions(String safetyPrecautions) {
        this.safetyPrecautions = safetyPrecautions;
    }

    public String getRegistrationNotice() {
        return registrationNotice;
    }

    public void setRegistrationNotice(String registrationNotice) {
        this.registrationNotice = registrationNotice;
    }
}
