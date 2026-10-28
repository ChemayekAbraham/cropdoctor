package com.smartcrop.doctor.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity storing verified citations, agricultural extension bulletins, and plant health authorities.
 */
@Entity(
    tableName = "disease_references",
    foreignKeys = @ForeignKey(
        entity = Disease.class,
        parentColumns = "diseaseId",
        childColumns = "diseaseId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = "diseaseId")}
)
public class DiseaseReference {

    @PrimaryKey(autoGenerate = true)
    private long referenceId;

    @NonNull
    private String diseaseId;

    @NonNull
    private String authorityName; // e.g. "CABI Plantwise", "FAO Plant Production", "IITA", "IRRI", "USDA ARS"

    @NonNull
    private String titleOrBulletin;

    private String urlOrCitation;

    public DiseaseReference(@NonNull String diseaseId, @NonNull String authorityName,
                            @NonNull String titleOrBulletin, String urlOrCitation) {
        this.diseaseId = diseaseId;
        this.authorityName = authorityName;
        this.titleOrBulletin = titleOrBulletin;
        this.urlOrCitation = urlOrCitation;
    }

    public long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }

    @NonNull
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(@NonNull String diseaseId) {
        this.diseaseId = diseaseId;
    }

    @NonNull
    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(@NonNull String authorityName) {
        this.authorityName = authorityName;
    }

    @NonNull
    public String getTitleOrBulletin() {
        return titleOrBulletin;
    }

    public void setTitleOrBulletin(@NonNull String titleOrBulletin) {
        this.titleOrBulletin = titleOrBulletin;
    }

    public String getUrlOrCitation() {
        return urlOrCitation;
    }

    public void setUrlOrCitation(String urlOrCitation) {
        this.urlOrCitation = urlOrCitation;
    }
}
