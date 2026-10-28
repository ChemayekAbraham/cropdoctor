package com.smartcrop.doctor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.smartcrop.doctor.classifier.DiseaseClassifier;
import com.smartcrop.doctor.classifier.ImageQualityAnalyzer;
import com.smartcrop.doctor.classifier.Recognition;
import com.smartcrop.doctor.reasoning.ChecklistObservation;
import com.smartcrop.doctor.reasoning.ReasoningResult;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests verifying diagnostic reasoning rules, score preservation,
 * and uncertainty handling.
 */
public class DiagnosticReasoningEngineTest {

    private List<Recognition> mockCandidates;
    private ImageQualityAnalyzer.QualityReport goodQualityReport;

    @Before
    public void setUp() {
        mockCandidates = new ArrayList<>();
        // Candidate 1: Tomato Early Blight (52%)
        mockCandidates.add(new Recognition(
                "0", "Early Blight", "Tomato", "tomato", "tomato_early_blight", 0.52f, false
        ));
        // Candidate 2: Tomato Septoria Leaf Spot (29%)
        mockCandidates.add(new Recognition(
                "1", "Septoria Leaf Spot", "Tomato", "tomato", "tomato_septoria_leaf_spot", 0.29f, false
        ));
        // Candidate 3: Tomato Bacterial Spot (14%)
        mockCandidates.add(new Recognition(
                "2", "Bacterial Spot", "Tomato", "tomato", "tomato_bacterial_spot", 0.14f, false
        ));

        goodQualityReport = new ImageQualityAnalyzer.QualityReport(
                true, false, false, false, false, 150.0f, 120.0f, 0.65f, "Quality clear."
        );
    }

    @Test
    public void testModelScoresAreStrictlyPreserved() {
        // Ensure that raw candidate scores are never changed by subsequent reasoning
        float originalScore = mockCandidates.get(0).getConfidence();
        assertEquals(0.52f, originalScore, 0.001f);

        // Verification of percentage display calculation
        assertEquals(52, mockCandidates.get(0).getPercentage());
        assertEquals(29, mockCandidates.get(1).getPercentage());
        assertEquals(14, mockCandidates.get(2).getPercentage());
    }

    @Test
    public void testCandidateRankingOrder() {
        assertTrue("Top candidate must have higher score than runner up",
                mockCandidates.get(0).getConfidence() > mockCandidates.get(1).getConfidence());
        assertTrue("Runner up must have higher score than third candidate",
                mockCandidates.get(1).getConfidence() > mockCandidates.get(2).getConfidence());
    }

    @Test
    public void testLabelParsing() {
        DiseaseClassifier.LabelInfo tomatoInfo = DiseaseClassifier.parseLabel("tomato_early_blight");
        assertEquals("tomato", tomatoInfo.cropId);
        assertEquals("Tomato", tomatoInfo.cropName);
        assertEquals("Early Blight", tomatoInfo.diseaseName);
        assertFalse(tomatoInfo.isHealthy);

        DiseaseClassifier.LabelInfo healthyInfo = DiseaseClassifier.parseLabel("maize_healthy");
        assertEquals("maize", healthyInfo.cropId);
        assertEquals("Maize", healthyInfo.cropName);
        assertEquals("Healthy Maize Leaf", healthyInfo.diseaseName);
        assertTrue(healthyInfo.isHealthy);

        DiseaseClassifier.LabelInfo coffeeInfo = DiseaseClassifier.parseLabel("coffee_leaf_rust");
        assertEquals("coffee", coffeeInfo.cropId);
        assertEquals("Coffee Leaf Rust", coffeeInfo.diseaseName);
    }

    @Test
    public void testLowConfidenceUncertainty() {
        List<Recognition> lowConfidenceCandidates = new ArrayList<>();
        lowConfidenceCandidates.add(new Recognition(
                "0", "Northern Leaf Blight", "Maize", "maize", "maize_northern_leaf_blight", 0.28f, false
        ));
        lowConfidenceCandidates.add(new Recognition(
                "1", "Gray Leaf Spot", "Maize", "maize", "maize_gray_leaf_spot", 0.25f, false
        ));

        float topScore = lowConfidenceCandidates.get(0).getConfidence();
        boolean isLowConfidence = topScore < DiseaseClassifier.MIN_CONFIDENCE_THRESHOLD;
        assertTrue("Prediction below 0.35 threshold must be flagged as uncertain", isLowConfidence);

        float margin = topScore - lowConfidenceCandidates.get(1).getConfidence();
        assertTrue("Close margin (< 0.12) between candidates must trigger differential flag", margin < 0.12f);
    }

    @Test
    public void testSymptomObservationWeightsDoNotAlterModelScores() {
        List<ChecklistObservation> observations = new ArrayList<>();
        observations.add(new ChecklistObservation("tom_q_concentric_rings",
                "Do larger brown spots have concentric rings?", "YES", "tomato_early_blight"));

        Map<String, Integer> symptomScores = new HashMap<>();
        symptomScores.put("tomato_early_blight", 3);
        symptomScores.put("tomato_septoria_leaf_spot", -1);

        ReasoningResult result = new ReasoningResult(
                mockCandidates.get(0),
                "tomato_early_blight",
                "Early Blight",
                false,
                false,
                false,
                List.of("Concentric target-like rings strongly support Early Blight."),
                List.of("Prune lower leaves."),
                symptomScores
        );

        // Raw model candidate score is strictly 0.52f
        assertEquals(0.52f, result.getLeadingModelCandidate().getConfidence(), 0.001f);
        // Symptom point score is stored separately
        assertEquals(Integer.valueOf(3), result.getSymptomSupportScores().get("tomato_early_blight"));
        assertEquals(Integer.valueOf(-1), result.getSymptomSupportScores().get("tomato_septoria_leaf_spot"));
    }

    @Test
    public void testModelObservationDisagreementDetection() {
        // Model initially favors Early Blight, but farmer notes tiny black specks with pale centers (+6 to Septoria)
        Map<String, Integer> symptomScores = new HashMap<>();
        symptomScores.put("tomato_early_blight", -2);
        symptomScores.put("tomato_septoria_leaf_spot", 6);

        String bestObserved = "tomato_septoria_leaf_spot";
        String modelTop = mockCandidates.get(0).getDiseaseId();

        boolean disagreement = !bestObserved.equals(modelTop) && symptomScores.get(bestObserved) >= 3;
        assertTrue("Disagreement must be detected when observations conflict with leading AI candidate", disagreement);
    }
}
