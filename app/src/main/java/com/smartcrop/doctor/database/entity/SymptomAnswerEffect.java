package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity mapping an answer ("YES", "NO", "NOT_SURE") to its diagnostic weight and explanation.
 */
@Entity(
    tableName = "symptom_answer_effects",
    indices = {@Index(value = {"questionId", "diseaseId", "answerValue"})}
)
public class SymptomAnswerEffect {

    @PrimaryKey(autoGenerate = true)
    private long effectId;

    @NonNull
    private String questionId;

    @NonNull
    private String diseaseId;

    @NonNull
    private String answerValue; // "YES", "NO", "NOT_SURE"

    private int weightScore; // e.g. +2 (strongly supports), +1 (supports), -2 (refutes), 0 (neutral)

    private String explanationMessage; // e.g. "Concentric rings are a classic diagnostic hallmark of Early Blight."

    public SymptomAnswerEffect(@NonNull String questionId, @NonNull String diseaseId,
                               @NonNull String answerValue, int weightScore,
                               String explanationMessage) {
        this.questionId = questionId;
        this.diseaseId = diseaseId;
        this.answerValue = answerValue;
        this.weightScore = weightScore;
        this.explanationMessage = explanationMessage;
    }

    public long getEffectId() {
        return effectId;
    }

    public void setEffectId(long effectId) {
        this.effectId = effectId;
    }

    @NonNull
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(@NonNull String questionId) {
        this.questionId = questionId;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @NonNull
    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(@NonNull String answerValue) {
        this.answerValue = answerValue;
    }

    public int getWeightScore() {
        return weightScore;
    }

    public void setWeightScore(int weightScore) {
        this.weightScore = weightScore;
    }

    public String getExplanationMessage() {
        return explanationMessage;
    }

    public void setExplanationMessage(String explanationMessage) {
        this.explanationMessage = explanationMessage;
    }
}
