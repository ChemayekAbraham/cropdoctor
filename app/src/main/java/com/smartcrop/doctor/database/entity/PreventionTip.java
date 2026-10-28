package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing preventative measures to prevent disease introduction or spread.
 */
@Entity(
    tableName = "prevention_tips",
    foreignKeys = @ForeignKey(
        entity = Disease.class,
        parentColumns = "diseaseId",
        childColumns = "diseaseId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "diseaseId")}
)
public class PreventionTip {

    @PrimaryKey(autoGenerate = true)
    private long tipId;

    @NonNull
    private String diseaseId;

    @NonNull
    private String tipText;

    private String category; // "Seed Selection", "Crop Rotation", "Field Hygiene", "Watering Practices", "Resistant Varieties"

    public PreventionTip(@NonNull String diseaseId, @NonNull String tipText, String category) {
        this.diseaseId = diseaseId;
        this.tipText = tipText;
        this.category = category;
    }

    public long getTipId() {
        return tipId;
    }

    public void setTipId(long tipId) {
        this.tipId = tipId;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @NonNull
    public String getTipText() {
        return tipText;
    }

    public void setTipText(@NonNull String tipText) {
        this.tipText = tipText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
