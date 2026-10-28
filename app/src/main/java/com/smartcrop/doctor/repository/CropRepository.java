package com.smartcrop.doctor.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.dao.CropDao;
import com.smartcrop.doctor.database.entity.Crop;

import java.util.List;

public class CropRepository {

    private final CropDao cropDao;

    public CropRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.cropDao = db.cropDao();
    }

    public LiveData<List<Crop>> getAllCrops() {
        return cropDao.getAllCropsLiveData();
    }

    public List<Crop> getAllCropsSync() {
        return cropDao.getAllCropsSync();
    }

    public LiveData<Crop> getCropById(String cropId) {
        return cropDao.getCropByIdLiveData(cropId);
    }

    public Crop getCropByIdSync(String cropId) {
        return cropDao.getCropById(cropId);
    }
}
