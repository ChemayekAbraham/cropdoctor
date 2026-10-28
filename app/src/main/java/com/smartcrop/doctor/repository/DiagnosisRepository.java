package com.smartcrop.doctor.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.dao.DiagnosisHistoryDao;
import com.smartcrop.doctor.database.entity.DiagnosisHistory;

import java.util.List;

public class DiagnosisRepository {

    private final DiagnosisHistoryDao historyDao;

    public DiagnosisRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.historyDao = db.diagnosisHistoryDao();
    }

    public LiveData<List<DiagnosisHistory>> getAllHistory() {
        return historyDao.getAllHistoryLiveData();
    }

    public List<DiagnosisHistory> getAllHistorySync() {
        return historyDao.getAllHistorySync();
    }

    public LiveData<DiagnosisHistory> getHistoryById(long id) {
        return historyDao.getHistoryByIdLiveData(id);
    }

    public void insert(DiagnosisHistory history, OnInsertedListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = historyDao.insert(history);
            if (listener != null) {
                listener.onInserted(id);
            }
        });
    }

    public void delete(DiagnosisHistory history) {
        AppDatabase.databaseWriteExecutor.execute(() -> historyDao.delete(history));
    }

    public void deleteById(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> historyDao.deleteById(id));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(historyDao::deleteAll);
    }

    public interface OnInsertedListener {
        void onInserted(long newId);
    }
}
