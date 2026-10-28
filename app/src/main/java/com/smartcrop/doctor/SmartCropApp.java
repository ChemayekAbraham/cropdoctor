package com.smartcrop.doctor;

import android.app.Application;

import com.smartcrop.doctor.database.AppDatabase;

public class SmartCropApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Warm up Room database and trigger seed data population
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getInstance(this).cropDao().getCropCount();
        });
    }
}
