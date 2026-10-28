package com.smartcrop.doctor.classifier;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Encapsulates a candidate prediction returned by the AI classifier.
 */
public class Recognition implements Serializable {

    private final String id;
    private final String title;
    private final String cropName;
    private final String cropId;
    private final String diseaseId;
    private final float confidence; // [0.0, 1.0]
    private final boolean isHealthy;
    private String distinguishingFeature;
    private String commonSymptoms;

    public Recognition(String id, String title, String cropName, String cropId,
                       String diseaseId, float confidence, boolean isHealthy) {
        this.id = id;
        this.title = title;
        this.cropName = cropName;
        this.cropId = cropId;
        this.diseaseId = diseaseId;
        this.confidence = confidence;
        this.isHealthy = isHealthy;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCropName() {
        return cropName;
    }

    public String getCropId() {
        return cropId;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public float getConfidence() {
        return confidence;
    }

    public int getPercentage() {
        return Math.round(confidence * 100f);
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public String getDistinguishingFeature() {
        return distinguishingFeature;
    }

    public void setDistinguishingFeature(String distinguishingFeature) {
        this.distinguishingFeature = distinguishingFeature;
    }

    public String getCommonSymptoms() {
        return commonSymptoms;
    }

    public void setCommonSymptoms(String commonSymptoms) {
        this.commonSymptoms = commonSymptoms;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s (%s): %.1f%%", title, cropName, confidence * 100f);
    }
}
