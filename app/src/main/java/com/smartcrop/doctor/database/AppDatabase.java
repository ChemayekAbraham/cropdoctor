package com.smartcrop.doctor.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.smartcrop.doctor.database.dao.CropDao;
import com.smartcrop.doctor.database.dao.DiagnosisHistoryDao;
import com.smartcrop.doctor.database.dao.DifferentialFeatureDao;
import com.smartcrop.doctor.database.dao.DiseaseDao;
import com.smartcrop.doctor.database.dao.SymptomQuestionDao;
import com.smartcrop.doctor.database.entity.Crop;
import com.smartcrop.doctor.database.entity.DiagnosisHistory;
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
import com.smartcrop.doctor.database.seed.DatabaseSeeder;

import com.smartcrop.doctor.database.dao.UserDao;
import com.smartcrop.doctor.database.entity.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
        Crop.class,
        Disease.class,
        Symptom.class,
        Treatment.class,
        PreventionTip.class,
        DiagnosisHistory.class,
        ModelClass.class,
        DifferentialFeature.class,
        SymptomQuestion.class,
        SymptomAnswerEffect.class,
        RecommendedInspection.class,
        DiseaseReference.class,
        User.class
    },
    version = 2,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "smartcrop_doctor.db";
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract CropDao cropDao();
    public abstract DiseaseDao diseaseDao();
    public abstract DiagnosisHistoryDao diagnosisHistoryDao();
    public abstract DifferentialFeatureDao differentialFeatureDao();
    public abstract SymptomQuestionDao symptomQuestionDao();
    public abstract UserDao userDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            databaseWriteExecutor.execute(() -> {
                                DatabaseSeeder.seedDatabase(INSTANCE);
                            });
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
