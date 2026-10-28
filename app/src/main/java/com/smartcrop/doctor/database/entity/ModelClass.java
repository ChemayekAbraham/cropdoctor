package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity tracking supported AI model classes and their confidence calibration thresholds.
 */
@Entity(tableName = "model_classes")
public class ModelClass {

    @PrimaryKey
    @NonNull
    private String classLabel; // e.g., "tomato_early_blight", "maize_healthy"

    @NonNull
    private String cropId;

    @NonNull
    private String diseaseId;

    private float confidenceThreshold; // Minimum score to suggest candidate

    private boolean isHealthyClass;

    public ModelClass(@NonNull String classLabel, @NonNull String cropId,
                      @NonNull String diseaseId, float confidenceThreshold,
                      boolean isHealthyClass) {
        this.classLabel = classLabel;
        this.cropId = cropId;
        this.diseaseId = diseaseId;
        this.confidenceThreshold = confidenceThreshold;
        this.isHealthyClass = isHealthyClass;
    }

    @NonNull
    public String getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(@NonNull String classLabel) {
        this.classLabel = classLabel;
    }

    @NonNull
    public String getCropId() {
        return cropId;
    }

    public void setCropId(@NonNull String cropId) {
        this.cropId = cropId;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public float getConfidenceThreshold() {
        return confidenceThreshold;
    }

    public void setConfidenceThreshold(float confidenceThreshold) {
        this.confidenceThreshold = confidenceThreshold;
    }

    public boolean isHealthyClass() {
        return isHealthyClass;
    }

    public void setHealthyClass(boolean healthyClass) {
        isHealthyClass = healthyClass;
    }
}
