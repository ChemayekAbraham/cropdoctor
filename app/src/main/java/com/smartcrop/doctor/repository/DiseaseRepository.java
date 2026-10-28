package com.smartcrop.doctor.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.dao.DifferentialFeatureDao;
import com.smartcrop.doctor.database.dao.DiseaseDao;
import com.smartcrop.doctor.database.dao.SymptomQuestionDao;
import com.smartcrop.doctor.database.entity.DifferentialFeature;
import com.smartcrop.doctor.database.entity.Disease;
import com.smartcrop.doctor.database.entity.DiseaseReference;
import com.smartcrop.doctor.database.entity.ModelClass;
import com.smartcrop.doctor.database.entity.PreventionTip;
import com.smartcrop.doctor.database.entity.RecommendedInspection;
import com.smartcrop.doctor.database.entity.Symptom;
import com.smartcrop.doctor.database.entity.SymptomAnswerEffect;
import com.smartcrop.doctor.database.entity.SymptomQuestion;
import com.smartcrop.doctor.database.entity.Treatment;

import java.util.List;

public class DiseaseRepository {

    private final DiseaseDao diseaseDao;
    private final DifferentialFeatureDao differentialFeatureDao;
    private final SymptomQuestionDao symptomQuestionDao;

    public DiseaseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.diseaseDao = db.diseaseDao();
        this.differentialFeatureDao = db.differentialFeatureDao();
        this.symptomQuestionDao = db.symptomQuestionDao();
    }

    public LiveData<List<Disease>> getAllDiseases() {
        return diseaseDao.getAllDiseasesLiveData();
    }

    public List<Disease> getAllDiseasesSync() {
        return diseaseDao.getAllDiseasesSync();
    }

    public LiveData<List<Disease>> getDiseasesByCrop(String cropId) {
        return diseaseDao.getDiseasesByCropLiveData(cropId);
    }

    public List<Disease> getDiseasesByCropSync(String cropId) {
        return diseaseDao.getDiseasesByCropSync(cropId);
    }

    public LiveData<Disease> getDiseaseById(String diseaseId) {
        return diseaseDao.getDiseaseByIdLiveData(diseaseId);
    }

    public Disease getDiseaseByIdSync(String diseaseId) {
        return diseaseDao.getDiseaseById(diseaseId);
    }

    public Disease getDiseaseByModelLabel(String label) {
        return diseaseDao.getDiseaseByModelLabel(label);
    }

    public List<Symptom> getSymptoms(String diseaseId) {
        return diseaseDao.getSymptomsByDisease(diseaseId);
    }

    public LiveData<List<Symptom>> getSymptomsLiveData(String diseaseId) {
        return diseaseDao.getSymptomsByDiseaseLiveData(diseaseId);
    }

    public List<Treatment> getTreatments(String diseaseId) {
        return diseaseDao.getTreatmentsByDisease(diseaseId);
    }

    public LiveData<List<Treatment>> getTreatmentsLiveData(String diseaseId) {
        return diseaseDao.getTreatmentsByDiseaseLiveData(diseaseId);
    }

    public List<PreventionTip> getPreventionTips(String diseaseId) {
        return diseaseDao.getPreventionTipsByDisease(diseaseId);
    }

    public LiveData<List<PreventionTip>> getPreventionTipsLiveData(String diseaseId) {
        return diseaseDao.getPreventionTipsByDiseaseLiveData(diseaseId);
    }

    public List<RecommendedInspection> getInspections(String diseaseId) {
        return diseaseDao.getInspectionsByDisease(diseaseId);
    }

    public LiveData<List<RecommendedInspection>> getInspectionsLiveData(String diseaseId) {
        return diseaseDao.getInspectionsByDiseaseLiveData(diseaseId);
    }

    public List<DiseaseReference> getReferences(String diseaseId) {
        return diseaseDao.getReferencesByDisease(diseaseId);
    }

    public List<DifferentialFeature> getFeaturesByCrop(String cropId) {
        return differentialFeatureDao.getFeaturesByCrop(cropId);
    }

    public List<DifferentialFeature> getFeaturesForPair(String d1, String d2) {
        return differentialFeatureDao.getFeaturesForPair(d1, d2);
    }

    public List<SymptomQuestion> getQuestionsForCrop(String cropId) {
        return symptomQuestionDao.getQuestionsForCrop(cropId);
    }

    public List<SymptomAnswerEffect> getEffectsForAnswer(String questionId, String answerValue) {
        return symptomQuestionDao.getEffectsForAnswer(questionId, answerValue);
    }

    public ModelClass getModelClass(String label) {
        return diseaseDao.getModelClass(label);
    }
}
