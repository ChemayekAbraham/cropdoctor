package com.smartcrop.doctor.reasoning;

import java.io.Serializable;

/**
 * Captures a single question response in the follow-up inspection checklist.
 */
public class ChecklistObservation implements Serializable {

    private final String questionId;
    private final String questionText;
    private final String answer; // "YES", "NO", "NOT_SURE"
    private final String targetDiseaseId;

    public ChecklistObservation(String questionId, String questionText, String answer, String targetDiseaseId) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.answer = answer;
        this.targetDiseaseId = targetDiseaseId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public String getTargetDiseaseId() {
        return targetDiseaseId;
    }
}
