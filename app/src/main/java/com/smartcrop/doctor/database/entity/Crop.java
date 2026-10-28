package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity representing an agricultural crop supported in SmartCrop Doctor.
 */
@Entity(tableName = "crops")
public class Crop {

    @PrimaryKey
    @NonNull
    private String cropId; // e.g. "maize", "tomato", "cassava"

    @NonNull
    private String commonName;

    private String botanicalName;

    private String description;

    private String healthyLeafGuidance;

    private String representativeImage; // drawable resource name or asset path

    private boolean isModelSupported; // whether AI classifier supports diseases for this crop

    public Crop(@NonNull String cropId, @NonNull String commonName, String botanicalName,
                String description, String healthyLeafGuidance, String representativeImage,
                boolean isModelSupported) {
        this.cropId = cropId;
        this.commonName = commonName;
        this.botanicalName = botanicalName;
        this.description = description;
        this.healthyLeafGuidance = healthyLeafGuidance;
        this.representativeImage = representativeImage;
        this.isModelSupported = isModelSupported;
    }

    @NonNull
    public String getCropId() {
        return cropId;
    }

    public void setCropId(@NonNull String cropId) {
        this.cropId = cropId;
    }

    @NonNull
    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(@NonNull String commonName) {
        this.commonName = commonName;
    }

    public String getBotanicalName() {
        return botanicalName;
    }

    public void setBotanicalName(String botanicalName) {
        this.botanicalName = botanicalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHealthyLeafGuidance() {
        return healthyLeafGuidance;
    }

    public void setHealthyLeafGuidance(String healthyLeafGuidance) {
        this.healthyLeafGuidance = healthyLeafGuidance;
    }

    public String getRepresentativeImage() {
        return representativeImage;
    }

    public void setRepresentativeImage(String representativeImage) {
        this.representativeImage = representativeImage;
    }

    public boolean isModelSupported() {
        return isModelSupported;
    }

    public void setModelSupported(boolean modelSupported) {
        isModelSupported = modelSupported;
    }
}
