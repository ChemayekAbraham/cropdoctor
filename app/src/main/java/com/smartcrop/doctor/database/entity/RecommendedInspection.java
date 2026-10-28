package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing specific plant part follow-up inspections and recommended intervals.
 */
@Entity(
    tableName = "recommended_inspections",
    foreignKeys = @ForeignKey(
        entity = Disease.class,
        parentColumns = "diseaseId",
        childColumns = "diseaseId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "diseaseId")}
)
public class RecommendedInspection {

    @PrimaryKey(autoGenerate = true)
    private long inspectionId;

    @NonNull
    private String diseaseId;

    @NonNull
    private String inspectionTitle;

    private String focusPlantPart; // "Lower Leaves", "Stems/Collars", "Underside of Leaf", "Tubers/Roots", "Fruit"

    private int followUpIntervalDays; // Recommended days to re-inspect (e.g. 3, 5, 7, 14)

    private String actionableInstructions;

    private String referralTriggerNote; // If this symptom is spotted, seek agricultural extension immediately

    public RecommendedInspection(@NonNull String diseaseId, @NonNull String inspectionTitle,
                                 String focusPlantPart, int followUpIntervalDays,
                                 String actionableInstructions, String referralTriggerNote) {
        this.diseaseId = diseaseId;
        this.inspectionTitle = inspectionTitle;
        this.focusPlantPart = focusPlantPart;
        this.followUpIntervalDays = followUpIntervalDays;
        this.actionableInstructions = actionableInstructions;
        this.referralTriggerNote = referralTriggerNote;
    }

    public long getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(long inspectionId) {
        this.inspectionId = inspectionId;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @NonNull
    public String getInspectionTitle() {
        return inspectionTitle;
    }

    public void setInspectionTitle(@NonNull String inspectionTitle) {
        this.inspectionTitle = inspectionTitle;
    }

    public String getFocusPlantPart() {
        return focusPlantPart;
    }

    public void setFocusPlantPart(String focusPlantPart) {
        this.focusPlantPart = focusPlantPart;
    }

    public int getFollowUpIntervalDays() {
        return followUpIntervalDays;
    }

    public void setFollowUpIntervalDays(int followUpIntervalDays) {
        this.followUpIntervalDays = followUpIntervalDays;
    }

    public String getActionableInstructions() {
        return actionableInstructions;
    }

    public void setActionableInstructions(String actionableInstructions) {
        this.actionableInstructions = actionableInstructions;
    }

    public String getReferralTriggerNote() {
        return referralTriggerNote;
    }

    public void setReferralTriggerNote(String referralTriggerNote) {
        this.referralTriggerNote = referralTriggerNote;
    }
}
