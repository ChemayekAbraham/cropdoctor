package com.smartcrop.doctor.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartcrop.doctor.database.entity.SymptomAnswerEffect;
import com.smartcrop.doctor.database.entity.SymptomQuestion;

import java.util.List;

@Dao
public interface SymptomQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<SymptomQuestion> questions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnswerEffects(List<SymptomAnswerEffect> effects);

    @Query("SELECT * FROM symptom_questions WHERE cropId = :cropId ORDER BY orderIndex ASC")
    List<SymptomQuestion> getQuestionsForCrop(String cropId);

    @Query("SELECT * FROM symptom_questions WHERE cropId = :cropId ORDER BY orderIndex ASC")
    LiveData<List<SymptomQuestion>> getQuestionsForCropLiveData(String cropId);

    @Query("SELECT * FROM symptom_answer_effects WHERE questionId = :questionId AND answerValue = :answerValue")
    List<SymptomAnswerEffect> getEffectsForAnswer(String questionId, String answerValue);

    @Query("SELECT * FROM symptom_answer_effects WHERE questionId IN (:questionIds)")
    List<SymptomAnswerEffect> getEffectsForQuestions(List<String> questionIds);
}
