package com.smartcrop.doctor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.smartcrop.doctor.database.entity.Crop;
import com.smartcrop.doctor.database.entity.DiagnosisHistory;
import com.smartcrop.doctor.database.entity.DifferentialFeature;
import com.smartcrop.doctor.database.entity.Disease;
import com.smartcrop.doctor.database.entity.RecommendedInspection;
import com.smartcrop.doctor.database.entity.SymptomQuestion;

import org.junit.Test;

public class EntityAndDatabaseModelTest {

    @Test
    public void testCropEntityCreation() {
        Crop maize = new Crop("maize", "Maize (Corn)", "Zea mays",
                "Staple cereal grain.", "Clean parallel veins.", "crop_maize", true);
        assertEquals("maize", maize.getCropId());
        assertEquals("Maize (Corn)", maize.getCommonName());
        assertTrue(maize.isModelSupported());

        Crop banana = new Crop("banana", "Banana", "Musa acuminata",
                "Herbaceous perennial.", "Broad paddle leaf.", "crop_banana", false);
        assertFalse("Banana should be reference catalogue only without trained model classes", banana.isModelSupported());
    }

    @Test
    public void testDiseaseEntityLeafLimitationFlag() {
        // Early Blight is reliable from leaf alone
        Disease earlyBlight = new Disease("tomato_early_blight", "tomato", "Early Blight",
                "Alternaria solani", "Fungal", "Foliar blight", "Airborne spores",
                "Target rings", "Concentric rings", "Moderate", "Prune leaves",
                "Crop rotation", "Wear PPE", "Lower leaves", "Girdling lesions",
                "CABI", "2026-01-15", true, "tomato_early_blight", true);
        assertTrue(earlyBlight.isLeafOnlyDiagnosticReliable());

        // Cassava Brown Streak Disease requires tuber root inspection
        Disease cbsd = new Disease("cassava_brown_streak_disease", "cassava", "Cassava Brown Streak",
                "CBSV", "Viral", "Insidious virus", "Whiteflies", "Vein yellowing",
                "Feathery yellowing", "Critical", "Rogue vines", "Clean cuttings",
                "No chemicals", "Slice open roots", "Root rot",
                "IITA", "2026-01-15", true, "cassava_brown_streak_disease", false);
        assertFalse("CBSD leaf photograph alone is insufficient for confirmed diagnosis", cbsd.isLeafOnlyDiagnosticReliable());
    }

    @Test
    public void testRecommendedInspectionInterval() {
        RecommendedInspection lateBlightInsp = new RecommendedInspection(
                "tomato_late_blight", "Leaf Underside Check", "Lower canopy", 3,
                "Check every 3 days during wet spells.", "Contact extension if >5% plants affected."
        );
        assertEquals(3, lateBlightInsp.getFollowUpIntervalDays());
        assertEquals("tomato_late_blight", lateBlightInsp.getDiseaseId());
    }

    @Test
    public void testDifferentialFeaturePairing() {
        DifferentialFeature diff = new DifferentialFeature(
                "tomato", "tomato_early_blight", "tomato_septoria_leaf_spot",
                "Spot Size & Target Rings", "Early blight has target rings; Septoria has pale spots with black dots.",
                "Lower leaves", "Use magnifying glass."
        );
        assertEquals("tomato", diff.getCropId());
        assertEquals("tomato_early_blight", diff.getDiseaseAId());
        assertEquals("tomato_septoria_leaf_spot", diff.getDiseaseBId());
        assertNotNull(diff.getComparisonSummary());
    }

    @Test
    public void testDiagnosisHistoryModel() {
        DiagnosisHistory history = new DiagnosisHistory(
                1700000000000L,
                "file:///path/to/leaf.jpg",
                "tomato",
                "tomato_early_blight",
                "Early Blight",
                65.0f,
                "[]",
                "[]",
                "[]",
                "Apply mulch and prune lower leaves.",
                "MobileNetV2-1.0.0",
                false,
                false
        );
        assertEquals("tomato", history.getSelectedCropId());
        assertEquals("Early Blight", history.getLeadingDiseaseName());
        assertEquals(65.0f, history.getModelConfidence(), 0.01f);
        assertFalse(history.isUncertain());
    }
}
