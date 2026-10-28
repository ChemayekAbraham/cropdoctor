package com.smartcrop.doctor.ui.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.SymptomQuestion;
import com.smartcrop.doctor.reasoning.ChecklistObservation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SymptomChecklistActivity extends AppCompatActivity {

    public static final String EXTRA_CROP_ID = "extra_crop_id";
    public static final String EXTRA_CANDIDATES = "extra_candidates";
    public static final String EXTRA_EXISTING_OBSERVATIONS = "extra_existing_observations";
    public static final String EXTRA_RETURNED_OBSERVATIONS = "extra_returned_observations";

    private RecyclerView rvQuestions;
    private TextView tvChecklistTitle;
    private TextView tvChecklistSubtitle;
    private ChecklistQuestionAdapter adapter;

    private String cropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checklist);

        rvQuestions = findViewById(R.id.rvQuestions);
        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        tvChecklistTitle = findViewById(R.id.tvChecklistTitle);
        tvChecklistSubtitle = findViewById(R.id.tvChecklistSubtitle);

        findViewById(R.id.btnBackChecklist).setOnClickListener(v -> finish());

        cropId = getIntent().getStringExtra(EXTRA_CROP_ID);
        if (cropId == null || cropId.isEmpty()) {
            cropId = "tomato"; // default fallback
        }

        @SuppressWarnings("unchecked")
        List<ChecklistObservation> existing = (List<ChecklistObservation>) getIntent().getSerializableExtra(EXTRA_EXISTING_OBSERVATIONS);

        loadQuestions(existing);

        findViewById(R.id.btnApplyChecklist).setOnClickListener(v -> {
            if (adapter != null) {
                List<ChecklistObservation> observations = adapter.getObservations();
                Intent resultData = new Intent();
                resultData.putExtra(EXTRA_RETURNED_OBSERVATIONS, (Serializable) observations);
                setResult(RESULT_OK, resultData);
                finish();
            }
        });
    }

    private void loadQuestions(List<ChecklistObservation> existing) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<SymptomQuestion> questions = db.symptomQuestionDao().getQuestionsForCrop(cropId);

            runOnUiThread(() -> {
                String cropDisplay = cropId.substring(0, 1).toUpperCase() + cropId.substring(1);
                tvChecklistTitle.setText(cropDisplay + " Symptom Checklist");
                tvChecklistSubtitle.setText(String.format("Follow-up inspection for %s", cropDisplay));

                if (questions == null || questions.isEmpty()) {
                    Toast.makeText(this, "No specific checklist questions for this crop yet.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                adapter = new ChecklistQuestionAdapter(questions, existing);
                rvQuestions.setAdapter(adapter);
            });
        });
    }
}
