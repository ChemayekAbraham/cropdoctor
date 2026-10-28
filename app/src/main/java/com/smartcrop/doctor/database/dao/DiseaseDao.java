package com.smartcrop.doctor.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartcrop.doctor.database.entity.Disease;
import com.smartcrop.doctor.database.entity.DiseaseReference;
import com.smartcrop.doctor.database.entity.ModelClass;
import com.smartcrop.doctor.database.entity.PreventionTip;
import com.smartcrop.doctor.database.entity.RecommendedInspection;
import com.smartcrop.doctor.database.entity.Symptom;
import com.smartcrop.doctor.database.entity.Treatment;

import java.util.List;

@Dao
public interface DiseaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiseases(List<Disease> diseases);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSymptoms(List<Symptom> symptoms);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTreatments(List<Treatment> treatments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreventionTips(List<PreventionTip> tips);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecommendedInspections(List<RecommendedInspection> inspections);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReferences(List<DiseaseReference> references);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModelClasses(List<ModelClass> modelClasses);

    @Query("SELECT * FROM diseases ORDER BY name ASC")
    LiveData<List<Disease>> getAllDiseasesLiveData();

    @Query("SELECT * FROM diseases ORDER BY name ASC")
    List<Disease> getAllDiseasesSync();

    @Query("SELECT * FROM diseases WHERE cropId = :cropId ORDER BY name ASC")
    LiveData<List<Disease>> getDiseasesByCropLiveData(String cropId);

    @Query("SELECT * FROM diseases WHERE cropId = :cropId ORDER BY name ASC")
    List<Disease> getDiseasesByCropSync(String cropId);

    @Query("SELECT * FROM diseases WHERE diseaseId = :diseaseId LIMIT 1")
    Disease getDiseaseById(String diseaseId);

    @Query("SELECT * FROM diseases WHERE diseaseId = :diseaseId LIMIT 1")
    LiveData<Disease> getDiseaseByIdLiveData(String diseaseId);

    @Query("SELECT * FROM diseases WHERE modelClassLabel = :label LIMIT 1")
    Disease getDiseaseByModelLabel(String label);

    @Query("SELECT * FROM symptoms WHERE diseaseId = :diseaseId")
    List<Symptom> getSymptomsByDisease(String diseaseId);

    @Query("SELECT * FROM symptoms WHERE diseaseId = :diseaseId")
    LiveData<List<Symptom>> getSymptomsByDiseaseLiveData(String diseaseId);

    @Query("SELECT * FROM treatments WHERE diseaseId = :diseaseId")
    List<Treatment> getTreatmentsByDisease(String diseaseId);

    @Query("SELECT * FROM treatments WHERE diseaseId = :diseaseId")
    LiveData<List<Treatment>> getTreatmentsByDiseaseLiveData(String diseaseId);

    @Query("SELECT * FROM prevention_tips WHERE diseaseId = :diseaseId")
    List<PreventionTip> getPreventionTipsByDisease(String diseaseId);

    @Query("SELECT * FROM prevention_tips WHERE diseaseId = :diseaseId")
    LiveData<List<PreventionTip>> getPreventionTipsByDiseaseLiveData(String diseaseId);

    @Query("SELECT * FROM recommended_inspections WHERE diseaseId = :diseaseId")
    List<RecommendedInspection> getInspectionsByDisease(String diseaseId);

    @Query("SELECT * FROM recommended_inspections WHERE diseaseId = :diseaseId")
    LiveData<List<RecommendedInspection>> getInspectionsByDiseaseLiveData(String diseaseId);

    @Query("SELECT * FROM disease_references WHERE diseaseId = :diseaseId")
    List<DiseaseReference> getReferencesByDisease(String diseaseId);

    @Query("SELECT * FROM model_classes WHERE classLabel = :classLabel LIMIT 1")
    ModelClass getModelClass(String classLabel);

    @Query("SELECT * FROM model_classes")
    List<ModelClass> getAllModelClasses();

    @Query("SELECT COUNT(*) FROM diseases")
    int getDiseaseCount();
}
