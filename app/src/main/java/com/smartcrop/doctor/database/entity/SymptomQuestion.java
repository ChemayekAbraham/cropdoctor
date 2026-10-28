package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing a crop-specific follow up inspection question for differential diagnosis.
 */
@Entity(
    tableName = "symptom_questions",
    indices = {@Index(value = "cropId")}
)
public class SymptomQuestion {

    @PrimaryKey
    @NonNull
    private String questionId; // e.g. "tom_q_concentric_rings"

    @NonNull
    private String cropId; // "tomato", "maize", "cassava", etc.

    @NonNull
    private String questionText;

    private String helpExplanation;

    private String targetDiseaseId; // Specific disease this question tests, or "general"

    private int orderIndex;

    public SymptomQuestion(@NonNull String questionId, @NonNull String cropId,
                           @NonNull String questionText, String helpExplanation,
                           String targetDiseaseId, int orderIndex) {
        this.questionId = questionId;
        this.cropId = cropId;
        this.questionText = questionText;
        this.helpExplanation = helpExplanation;
        this.targetDiseaseId = targetDiseaseId;
        this.orderIndex = orderIndex;
    }

    @NonNull
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(@NonNull String questionId) {
        this.questionId = questionId;
    }

    @NonNull
    public String getCropId() {
        return cropId;
    }

    public void setCropId(@NonNull String cropId) {
        this.cropId = cropId;
    }

    @NonNull
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(@NonNull String questionText) {
        this.questionText = questionText;
    }

    public String getHelpExplanation() {
        return helpExplanation;
    }

    public void setHelpExplanation(String helpExplanation) {
        this.helpExplanation = helpExplanation;
    }

    public String getTargetDiseaseId() {
        return targetDiseaseId;
    }

    public void setTargetDiseaseId(String targetDiseaseId) {
        this.targetDiseaseId = targetDiseaseId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
