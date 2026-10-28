package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity capturing distinct differences between pairs of commonly confused diseases.
 */
@Entity(
    tableName = "differential_features",
    indices = {@Index(value = {"cropId", "diseaseAId", "diseaseBId"})}
)
public class DifferentialFeature {

    @PrimaryKey(autoGenerate = true)
    private long featureId;

    @NonNull
    private String cropId;

    @NonNull
    private String diseaseAId;

    @NonNull
    private String diseaseBId;

    @NonNull
    private String distinguishingTrait; // e.g. "Spot geometry & concentric rings"

    @NonNull
    private String comparisonSummary; // e.g. "Early blight shows larger target-board rings, while Septoria spots are tiny with pale centers and dark borders."

    private String plantPartToExamine; // "Lower leaves, then stems"

    private String additionalObservationNeeded;

    public DifferentialFeature(@NonNull String cropId, @NonNull String diseaseAId,
                               @NonNull String diseaseBId, @NonNull String distinguishingTrait,
                               @NonNull String comparisonSummary, String plantPartToExamine,
                               String additionalObservationNeeded) {
        this.cropId = cropId;
        this.diseaseAId = diseaseAId;
        this.diseaseBId = diseaseBId;
        this.distinguishingTrait = distinguishingTrait;
        this.comparisonSummary = comparisonSummary;
        this.plantPartToExamine = plantPartToExamine;
        this.additionalObservationNeeded = additionalObservationNeeded;
    }

    public long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(long featureId) {
        this.featureId = featureId;
    }

    @NonNull
    public String getCropId() {
        return cropId;
    }

    public void setCropId(@NonNull String cropId) {
        this.cropId = cropId;
    }

    @NonNull
    public String getDiseaseAId() {
        return diseaseAId;
    }

    public void setDiseaseAId(@NonNull String diseaseAId) {
        this.diseaseAId = diseaseAId;
    }

    @NonNull
    public String getDiseaseBId() {
        return diseaseBId;
    }

    public void setDiseaseBId(@NonNull String diseaseBId) {
        this.diseaseBId = diseaseBId;
    }

    @NonNull
    public String getDistinguishingTrait() {
        return distinguishingTrait;
    }

    public void setDistinguishingTrait(@NonNull String distinguishingTrait) {
        this.distinguishingTrait = distinguishingTrait;
    }

    @NonNull
    public String getComparisonSummary() {
        return comparisonSummary;
    }

    public void setComparisonSummary(@NonNull String comparisonSummary) {
        this.comparisonSummary = comparisonSummary;
    }

    public String getPlantPartToExamine() {
        return plantPartToExamine;
    }

    public void setPlantPartToExamine(String plantPartToExamine) {
        this.plantPartToExamine = plantPartToExamine;
    }

    public String getAdditionalObservationNeeded() {
        return additionalObservationNeeded;
    }

    public void setAdditionalObservationNeeded(String additionalObservationNeeded) {
        this.additionalObservationNeeded = additionalObservationNeeded;
    }
}
