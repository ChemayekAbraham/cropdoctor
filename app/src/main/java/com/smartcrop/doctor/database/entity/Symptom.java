package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing an observable symptom linked to a disease.
 */
@Entity(
    tableName = "symptoms",
    foreignKeys = @ForeignKey(
        entity = Disease.class,
        parentColumns = "diseaseId",
        childColumns = "diseaseId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "diseaseId")}
)
public class Symptom {

    @PrimaryKey(autoGenerate = true)
    private long symptomId;

    @NonNull
    private String diseaseId;

    @NonNull
    private String description;

    private boolean isDistinguishing;

    private String affectedPlantPart; // "Leaf", "Stem", "Fruit", "Root", "Whole Plant"

    public Symptom(@NonNull String diseaseId, @NonNull String description,
                   boolean isDistinguishing, String affectedPlantPart) {
        this.diseaseId = diseaseId;
        this.description = description;
        this.isDistinguishing = isDistinguishing;
        this.affectedPlantPart = affectedPlantPart;
    }

    public long getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(long symptomId) {
        this.symptomId = symptomId;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public boolean isDistinguishing() {
        return isDistinguishing;
    }

    public void setDistinguishing(boolean distinguishing) {
        isDistinguishing = distinguishing;
    }

    public String getAffectedPlantPart() {
        return affectedPlantPart;
    }

    public void setAffectedPlantPart(String affectedPlantPart) {
        this.affectedPlantPart = affectedPlantPart;
    }
}
