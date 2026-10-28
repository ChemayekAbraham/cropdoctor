package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity storing local diagnosis records for the farmer.
 * Works completely offline without requiring any user registration.
 */
@Entity(tableName = "diagnosis_history")
public class DiagnosisHistory {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long timestamp;

    private String imagePath;

    private String selectedCropId;

    @NonNull
    private String leadingDiseaseId;

    @NonNull
    private String leadingDiseaseName;

    private float modelConfidence; // Raw model confidence percentage (0 - 100)

    private String rankedCandidatesJson; // JSON string of candidate predictions and scores

    private String checklistResponsesJson; // Farmer's Yes/No/NotSure answers to questions

    private String reasoningExplanationsJson; // Output of diagnostic reasoning engine

    private String recommendationsSummary;

    private String modelVersion;

    private boolean isExpertReferralRecommended;

    private boolean isUncertain;

    public DiagnosisHistory(long timestamp, String imagePath, String selectedCropId,
                            @NonNull String leadingDiseaseId, @NonNull String leadingDiseaseName,
                            float modelConfidence, String rankedCandidatesJson,
                            String checklistResponsesJson, String reasoningExplanationsJson,
                            String recommendationsSummary, String modelVersion,
                            boolean isExpertReferralRecommended, boolean isUncertain) {
        this.timestamp = timestamp;
        this.imagePath = imagePath;
        this.selectedCropId = selectedCropId;
        this.leadingDiseaseId = leadingDiseaseId;
        this.leadingDiseaseName = leadingDiseaseName;
        this.modelConfidence = modelConfidence;
        this.rankedCandidatesJson = rankedCandidatesJson;
        this.checklistResponsesJson = checklistResponsesJson;
        this.reasoningExplanationsJson = reasoningExplanationsJson;
        this.recommendationsSummary = recommendationsSummary;
        this.modelVersion = modelVersion;
        this.isExpertReferralRecommended = isExpertReferralRecommended;
        this.isUncertain = isUncertain;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSelectedCropId() {
        return selectedCropId;
    }

    public void setSelectedCropId(String selectedCropId) {
        this.selectedCropId = selectedCropId;
    }

    @NonNull
    public String getLeadingDiseaseId() {
        return leadingDiseaseId;
    }

    public void setLeadingDiseaseId(@NonNull String leadingDiseaseId) {
        this.leadingDiseaseId = leadingDiseaseId;
    }

    @NonNull
    public String getLeadingDiseaseName() {
        return leadingDiseaseName;
    }

    public void setLeadingDiseaseName(@NonNull String leadingDiseaseName) {
        this.leadingDiseaseName = leadingDiseaseName;
    }

    public float getModelConfidence() {
        return modelConfidence;
    }

    public void setModelConfidence(float modelConfidence) {
        this.modelConfidence = modelConfidence;
    }

    public String getRankedCandidatesJson() {
        return rankedCandidatesJson;
    }

    public void setRankedCandidatesJson(String rankedCandidatesJson) {
        this.rankedCandidatesJson = rankedCandidatesJson;
    }

    public String getChecklistResponsesJson() {
        return checklistResponsesJson;
    }

    public void setChecklistResponsesJson(String checklistResponsesJson) {
        this.checklistResponsesJson = checklistResponsesJson;
    }

    public String getReasoningExplanationsJson() {
        return reasoningExplanationsJson;
    }

    public void setReasoningExplanationsJson(String reasoningExplanationsJson) {
        this.reasoningExplanationsJson = reasoningExplanationsJson;
    }

    public String getRecommendationsSummary() {
        return recommendationsSummary;
    }

    public void setRecommendationsSummary(String recommendationsSummary) {
        this.recommendationsSummary = recommendationsSummary;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public boolean isExpertReferralRecommended() {
        return isExpertReferralRecommended;
    }

    public void setExpertReferralRecommended(boolean expertReferralRecommended) {
        isExpertReferralRecommended = expertReferralRecommended;
    }

    public boolean isUncertain() {
        return isUncertain;
    }

    public void setUncertain(boolean uncertain) {
        isUncertain = uncertain;
    }
}
