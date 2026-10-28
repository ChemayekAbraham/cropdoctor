package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing a plant disease or healthy condition in the catalogue.
 */
@Entity(
    tableName = "diseases",
    foreignKeys = @ForeignKey(
        entity = Crop.class,
        parentColumns = "cropId",
        childColumns = "cropId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "cropId"), @Index(value = "modelClassLabel")}
)
public class Disease {

    @PrimaryKey
    @NonNull
    private String diseaseId; // e.g. "tomato_early_blight"

    @NonNull
    private String cropId;

    @NonNull
    private String name;

    private String scientificName;

    private String category; // "Fungal", "Bacterial", "Viral", "Healthy", "Physiological"

    private String description;

    private String possibleCauses;

    private String typicalSymptoms;

    private String distinguishingFeature;

    private String severityLevel; // "Low", "Moderate", "High", "Critical", "Normal"

    private String managementRecommendations;

    private String preventionMethods;

    private String treatmentSafetyNotes;

    private String recommendedInspectionPoints;

    private String expertReferralConditions;

    private String referenceSources;

    private String lastReviewedDate;

    private boolean isModelSupported; // Whether current AI classifier can detect this

    private String modelClassLabel; // Corresponding output label from model if supported

    private boolean leafOnlyDiagnosticReliable; // True if leaf is sufficient, false if root/stem inspection needed

    public Disease(@NonNull String diseaseId, @NonNull String cropId, @NonNull String name,
                   String scientificName, String category, String description,
                   String possibleCauses, String typicalSymptoms, String distinguishingFeature,
                   String severityLevel, String managementRecommendations,
                   String preventionMethods, String treatmentSafetyNotes,
                   String recommendedInspectionPoints, String expertReferralConditions,
                   String referenceSources, String lastReviewedDate,
                   boolean isModelSupported, String modelClassLabel,
                   boolean leafOnlyDiagnosticReliable) {
        this.diseaseId = diseaseId;
        this.cropId = cropId;
        this.name = name;
        this.scientificName = scientificName;
        this.category = category;
        this.description = description;
        this.possibleCauses = possibleCauses;
        this.typicalSymptoms = typicalSymptoms;
        this.distinguishingFeature = distinguishingFeature;
        this.severityLevel = severityLevel;
        this.managementRecommendations = managementRecommendations;
        this.preventionMethods = preventionMethods;
        this.treatmentSafetyNotes = treatmentSafetyNotes;
        this.recommendedInspectionPoints = recommendedInspectionPoints;
        this.expertReferralConditions = expertReferralConditions;
        this.referenceSources = referenceSources;
        this.lastReviewedDate = lastReviewedDate;
        this.isModelSupported = isModelSupported;
        this.modelClassLabel = modelClassLabel;
        this.leafOnlyDiagnosticReliable = leafOnlyDiagnosticReliable;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @NonNull
    public String getCropId() {
        return cropId;
    }

    public void setCropId(@NonNull String cropId) {
        this.cropId = cropId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPossibleCauses() {
        return possibleCauses;
    }

    public void setPossibleCauses(String possibleCauses) {
        this.possibleCauses = possibleCauses;
    }

    public String getTypicalSymptoms() {
        return typicalSymptoms;
    }

    public void setTypicalSymptoms(String typicalSymptoms) {
        this.typicalSymptoms = typicalSymptoms;
    }

    public String getDistinguishingFeature() {
        return distinguishingFeature;
    }

    public void setDistinguishingFeature(String distinguishingFeature) {
        this.distinguishingFeature = distinguishingFeature;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public String getManagementRecommendations() {
        return managementRecommendations;
    }

    public void setManagementRecommendations(String managementRecommendations) {
        this.managementRecommendations = managementRecommendations;
    }

    public String getPreventionMethods() {
        return preventionMethods;
    }

    public void setPreventionMethods(String preventionMethods) {
        this.preventionMethods = preventionMethods;
    }

    public String getTreatmentSafetyNotes() {
        return treatmentSafetyNotes;
    }

    public void setTreatmentSafetyNotes(String treatmentSafetyNotes) {
        this.treatmentSafetyNotes = treatmentSafetyNotes;
    }

    public String getRecommendedInspectionPoints() {
        return recommendedInspectionPoints;
    }

    public void setRecommendedInspectionPoints(String recommendedInspectionPoints) {
        this.recommendedInspectionPoints = recommendedInspectionPoints;
    }

    public String getExpertReferralConditions() {
        return expertReferralConditions;
    }

    public void setExpertReferralConditions(String expertReferralConditions) {
        this.expertReferralConditions = expertReferralConditions;
    }

    public String getReferenceSources() {
        return referenceSources;
    }

    public void setReferenceSources(String referenceSources) {
        this.referenceSources = referenceSources;
    }

    public String getLastReviewedDate() {
        return lastReviewedDate;
    }

    public void setLastReviewedDate(String lastReviewedDate) {
        this.lastReviewedDate = lastReviewedDate;
    }

    public boolean isModelSupported() {
        return isModelSupported;
    }

    public void setModelSupported(boolean modelSupported) {
        isModelSupported = modelSupported;
    }

    public String getModelClassLabel() {
        return modelClassLabel;
    }

    public void setModelClassLabel(String modelClassLabel) {
        this.modelClassLabel = modelClassLabel;
    }

    public boolean isLeafOnlyDiagnosticReliable() {
        return leafOnlyDiagnosticReliable;
    }

    public void setLeafOnlyDiagnosticReliable(boolean leafOnlyDiagnosticReliable) {
        this.leafOnlyDiagnosticReliable = leafOnlyDiagnosticReliable;
    }
}
