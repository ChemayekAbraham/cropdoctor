package com.smartcrop.doctor.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartcrop.doctor.database.entity.Crop;

import java.util.List;

@Dao
public interface CropDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCrop(Crop crop);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCrops(List<Crop> crops);

    @Query("SELECT * FROM crops ORDER BY commonName ASC")
    LiveData<List<Crop>> getAllCropsLiveData();

    @Query("SELECT * FROM crops ORDER BY commonName ASC")
    List<Crop> getAllCropsSync();

    @Query("SELECT * FROM crops WHERE cropId = :cropId LIMIT 1")
    Crop getCropById(String cropId);

    @Query("SELECT * FROM crops WHERE cropId = :cropId LIMIT 1")
    LiveData<Crop> getCropByIdLiveData(String cropId);

    @Query("SELECT COUNT(*) FROM crops")
    int getCropCount();
}
