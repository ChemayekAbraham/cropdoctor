package com.smartcrop.doctor.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartcrop.doctor.database.entity.DiagnosisHistory;

import java.util.List;

@Dao
public interface DiagnosisHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DiagnosisHistory history);

    @Delete
    void delete(DiagnosisHistory history);

    @Query("DELETE FROM diagnosis_history WHERE id = :id")
    void deleteById(long id);

    @Query("DELETE FROM diagnosis_history")
    void deleteAll();

    @Query("SELECT * FROM diagnosis_history ORDER BY timestamp DESC")
    LiveData<List<DiagnosisHistory>> getAllHistoryLiveData();

    @Query("SELECT * FROM diagnosis_history ORDER BY timestamp DESC")
    List<DiagnosisHistory> getAllHistorySync();

    @Query("SELECT * FROM diagnosis_history WHERE id = :id LIMIT 1")
    DiagnosisHistory getHistoryById(long id);

    @Query("SELECT * FROM diagnosis_history WHERE id = :id LIMIT 1")
    LiveData<DiagnosisHistory> getHistoryByIdLiveData(long id);

    @Query("SELECT COUNT(*) FROM diagnosis_history")
    int getHistoryCount();
}
