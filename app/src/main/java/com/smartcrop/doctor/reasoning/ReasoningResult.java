package com.smartcrop.doctor.reasoning;

import com.smartcrop.doctor.classifier.Recognition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Result produced by the Diagnostic Reasoning Engine, preserving separate AI scores
 * and transparent observational rule evaluations.
 */
public class ReasoningResult implements Serializable {

    // Raw model top prediction
    private final Recognition leadingModelCandidate;

    // Evaluated candidate adjusted by observational evidence (without changing model scores)
    private final String recommendedFocusDiseaseId;
    private final String recommendedFocusDiseaseName;

    // Diagnostic certainty status
    private final boolean isUncertain;
    private final boolean isModelObservationDisagreement;
    private final boolean isExpertReferralRecommended;

    // Human-readable explanations
    private final List<String> reasoningExplanations;
    private final List<String> recommendedNextActions;
    private final Map<String, Integer> symptomSupportScores; // rule-based points tally per disease

    public ReasoningResult(Recognition leadingModelCandidate,
                           String recommendedFocusDiseaseId,
                           String recommendedFocusDiseaseName,
                           boolean isUncertain,
                           boolean isModelObservationDisagreement,
                           boolean isExpertReferralRecommended,
                           List<String> reasoningExplanations,
                           List<String> recommendedNextActions,
                           Map<String, Integer> symptomSupportScores) {
        this.leadingModelCandidate = leadingModelCandidate;
        this.recommendedFocusDiseaseId = recommendedFocusDiseaseId;
        this.recommendedFocusDiseaseName = recommendedFocusDiseaseName;
        this.isUncertain = isUncertain;
        this.isModelObservationDisagreement = isModelObservationDisagreement;
        this.isExpertReferralRecommended = isExpertReferralRecommended;
        this.reasoningExplanations = reasoningExplanations != null ? reasoningExplanations : new ArrayList<>();
        this.recommendedNextActions = recommendedNextActions != null ? recommendedNextActions : new ArrayList<>();
        this.symptomSupportScores = symptomSupportScores != null ? symptomSupportScores : new HashMap<>();
    }

    public Recognition getLeadingModelCandidate() {
        return leadingModelCandidate;
    }

    public String getRecommendedFocusDiseaseId() {
        return recommendedFocusDiseaseId;
    }

    public String getRecommendedFocusDiseaseName() {
        return recommendedFocusDiseaseName;
    }

    public boolean isUncertain() {
        return isUncertain;
    }

    public boolean isModelObservationDisagreement() {
        return isModelObservationDisagreement;
    }

    public boolean isExpertReferralRecommended() {
        return isExpertReferralRecommended;
    }

    public List<String> getReasoningExplanations() {
        return reasoningExplanations;
    }

    public List<String> getRecommendedNextActions() {
        return recommendedNextActions;
    }

    public Map<String, Integer> getSymptomSupportScores() {
        return symptomSupportScores;
    }
}
