package com.smartcrop.doctor.reasoning;

import android.content.Context;
import android.util.Log;

import com.smartcrop.doctor.classifier.DiseaseClassifier;
import com.smartcrop.doctor.classifier.ImageQualityAnalyzer;
import com.smartcrop.doctor.classifier.Recognition;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.Disease;
import com.smartcrop.doctor.database.entity.SymptomAnswerEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Diagnostic Reasoning Engine combining AI model predictions, crop context,
 * farmer-reported symptom checklist observations, and plant pathology rules.
 *
 * CRITICAL RULE: AI model percentages are strictly preserved and never silently
 * overwritten by symptom scores.
 */
public class DiagnosticReasoningEngine {

    private static final String TAG = "ReasoningEngine";
    private final Context context;

    public DiagnosticReasoningEngine(Context context) {
        this.context = context.getApplicationContext();
    }

    public ReasoningResult evaluate(List<Recognition> candidates,
                                    String selectedCropId,
                                    List<ChecklistObservation> observations,
                                    ImageQualityAnalyzer.QualityReport qualityReport) {
        List<String> explanations = new ArrayList<>();
        List<String> nextActions = new ArrayList<>();
        Map<String, Integer> symptomScores = new HashMap<>();

        if (candidates == null || candidates.isEmpty()) {
            explanations.add("No supported disease candidates could be identified.");
            nextActions.add("Consult the offline Crop Library or seek assistance from your local agricultural extension officer.");
            return new ReasoningResult(null, null, "Uncertain", true, false, true, explanations, nextActions, symptomScores);
        }

        Recognition topCandidate = candidates.get(0);
        float topScore = topCandidate.getConfidence();

        // 1. Image Quality Assessment
        boolean isUncertain = false;
        if (qualityReport != null && !qualityReport.isAcceptable) {
            isUncertain = true;
            explanations.add("Image Quality Warning: " + qualityReport.userGuidance);
            nextActions.add("Capture a new photograph with better lighting, holding the camera steady 15-20cm from the leaf.");
        }

        // 2. Model Confidence Assessment
        if (topScore < DiseaseClassifier.MIN_CONFIDENCE_THRESHOLD) {
            isUncertain = true;
            explanations.add(String.format("AI Model Confidence is low (%.1f%%). The visual patterns do not closely match typical reference profiles.", topScore * 100f));
            nextActions.add("Compare similar conditions in the Differential Diagnosis screen.");
        } else if (candidates.size() > 1) {
            Recognition runnerUp = candidates.get(1);
            float scoreMargin = topScore - runnerUp.getConfidence();
            if (scoreMargin < 0.12f) {
                isUncertain = true;
                explanations.add(String.format("Close visual similarity between %s (%.1f%%) and %s (%.1f%%). Additional observation is recommended.",
                        topCandidate.getTitle(), topScore * 100f, runnerUp.getTitle(), runnerUp.getConfidence() * 100f));
                nextActions.add("Use the Follow-up Symptom Checklist to distinguish these conditions.");
            }
        }

        // 3. Process Farmer Symptom Checklist Observations
        AppDatabase db = AppDatabase.getInstance(context);
        boolean hasObservations = (observations != null && !observations.isEmpty());

        if (hasObservations) {
            for (ChecklistObservation obs : observations) {
                if ("NOT_SURE".equalsIgnoreCase(obs.getAnswer())) {
                    continue;
                }

                List<SymptomAnswerEffect> effects = db.symptomQuestionDao().getEffectsForAnswer(obs.getQuestionId(), obs.getAnswer().toUpperCase());
                if (effects != null) {
                    for (SymptomAnswerEffect effect : effects) {
                        String dId = effect.getDiseaseId();
                        int current = symptomScores.containsKey(dId) ? symptomScores.get(dId) : 0;
                        symptomScores.put(dId, current + effect.getWeightScore());

                        if (effect.getExplanationMessage() != null && !effect.getExplanationMessage().isEmpty()) {
                            if (!explanations.contains(effect.getExplanationMessage())) {
                                explanations.add(effect.getExplanationMessage());
                            }
                        }
                    }
                }
            }
        }

        // 4. Check for Disagreement Between AI and Observation
        String recommendedDiseaseId = topCandidate.getDiseaseId();
        String recommendedDiseaseName = topCandidate.getTitle();
        boolean isDisagreement = false;

        if (hasObservations && !symptomScores.isEmpty()) {
            // Find highest scored disease by observations
            String bestObservedDiseaseId = null;
            int maxObservedScore = Integer.MIN_VALUE;
            for (Map.Entry<String, Integer> entry : symptomScores.entrySet()) {
                if (entry.getValue() > maxObservedScore) {
                    maxObservedScore = entry.getValue();
                    bestObservedDiseaseId = entry.getKey();
                }
            }

            int topCandidateObsScore = symptomScores.containsKey(topCandidate.getDiseaseId())
                    ? symptomScores.get(topCandidate.getDiseaseId()) : 0;

            if (bestObservedDiseaseId != null && !bestObservedDiseaseId.equals(topCandidate.getDiseaseId())
                    && maxObservedScore >= 3 && topCandidateObsScore <= 0) {
                isDisagreement = true;
                isUncertain = true;

                Disease alternate = db.diseaseDao().getDiseaseById(bestObservedDiseaseId);
                String altName = alternate != null ? alternate.getName() : bestObservedDiseaseId;

                explanations.add(String.format("Discrepancy Noted: While the AI model assigned higher initial probability to %s (%.1f%%), your physical observations strongly align with %s. Do not apply chemical treatments until confirmed.",
                        topCandidate.getTitle(), topScore * 100f, altName));

                nextActions.add("Inspect multiple plants across the field to see if symptoms consistently match " + altName + ".");
                recommendedDiseaseId = bestObservedDiseaseId;
                recommendedDiseaseName = altName;
            } else if (topCandidateObsScore > 0) {
                explanations.add("Your checklist observations are consistent with the predicted " + topCandidate.getTitle() + ".");
            }
        }

        // 5. Check if Leaf-Only Photograph is Insufficient
        Disease detectedDisease = db.diseaseDao().getDiseaseById(recommendedDiseaseId);
        if (detectedDisease != null && !detectedDisease.isLeafOnlyDiagnosticReliable()) {
            explanations.add("Important Plant Health Notice: " + detectedDisease.getName() + " cannot be reliably confirmed from a leaf photograph alone. Examination of stems, roots, or fruit is required.");
            nextActions.add("Inspect non-foliar parts (e.g. slice open roots or examine fruit clusters) as detailed in the Recommended Inspection guide.");
        }

        // 6. Expert Referral Evaluation
        boolean isExpertReferral = false;
        if (detectedDisease != null) {
            String sev = detectedDisease.getSeverityLevel();
            if ("Critical".equalsIgnoreCase(sev) || "High".equalsIgnoreCase(sev)) {
                if (detectedDisease.getExpertReferralConditions() != null) {
                    explanations.add("Extension Trigger: " + detectedDisease.getExpertReferralConditions());
                    nextActions.add("Contact your local agricultural extension service or certified plant doctor.");
                    isExpertReferral = true;
                }
            }
        }

        if (nextActions.isEmpty()) {
            nextActions.add("Review the recommended cultural sanitation actions in the disease details.");
            nextActions.add("Monitor the plant again in 5 to 7 days for changes.");
        }

        return new ReasoningResult(
                topCandidate,
                recommendedDiseaseId,
                recommendedDiseaseName,
                isUncertain,
                isDisagreement,
                isExpertReferral,
                explanations,
                nextActions,
                symptomScores
        );
    }
}
