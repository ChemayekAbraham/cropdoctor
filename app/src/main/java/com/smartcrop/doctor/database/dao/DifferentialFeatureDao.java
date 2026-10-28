package com.smartcrop.doctor.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartcrop.doctor.database.entity.DifferentialFeature;

import java.util.List;

@Dao
public interface DifferentialFeatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeatures(List<DifferentialFeature> features);

    @Query("SELECT * FROM differential_features WHERE cropId = :cropId")
    List<DifferentialFeature> getFeaturesByCrop(String cropId);

    @Query("SELECT * FROM differential_features WHERE (diseaseAId = :d1 AND diseaseBId = :d2) OR (diseaseAId = :d2 AND diseaseBId = :d1)")
    List<DifferentialFeature> getFeaturesForPair(String d1, String d2);

    @Query("SELECT * FROM differential_features WHERE cropId = :cropId")
    LiveData<List<DifferentialFeature>> getFeaturesByCropLiveData(String cropId);
}
