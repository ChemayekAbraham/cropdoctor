package com.smartcrop.doctor.ui.differential;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.classifier.Recognition;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.DifferentialFeature;
import com.smartcrop.doctor.database.entity.Disease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DifferentialDiagnosisActivity extends AppCompatActivity {

    public static final String EXTRA_CANDIDATES = "extra_candidates";
    public static final String EXTRA_CROP_ID = "extra_crop_id";

    private RecyclerView rvDifferentialCards;
    private TextView tvPairwiseDifferences;

    private List<Recognition> candidates;
    private String cropId;
    private final Map<String, Disease> diseaseMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differential_diagnosis);

        findViewById(R.id.btnBackDiff).setOnClickListener(v -> finish());
        findViewById(R.id.btnOpenChecklistFromDiff).setOnClickListener(v -> finish()); // Return to results and launch checklist

        rvDifferentialCards = findViewById(R.id.rvDifferentialCards);
        rvDifferentialCards.setLayoutManager(new LinearLayoutManager(this));
        tvPairwiseDifferences = findViewById(R.id.tvPairwiseDifferences);

        @SuppressWarnings("unchecked")
        List<Recognition> received = (List<Recognition>) getIntent().getSerializableExtra(EXTRA_CANDIDATES);
        cropId = getIntent().getStringExtra(EXTRA_CROP_ID);

        if (received != null && !received.isEmpty()) {
            candidates = received;
            loadDataAndPopulate();
        } else {
            Toast.makeText(this, "No candidates to compare.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadDataAndPopulate() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            for (Recognition r : candidates) {
                Disease d = db.diseaseDao().getDiseaseById(r.getDiseaseId());
                if (d != null) {
                    diseaseMap.put(r.getDiseaseId(), d);
                }
            }

            // Retrieve pairwise differential features
            List<DifferentialFeature> pairwise = db.differentialFeatureDao().getFeaturesByCrop(cropId != null ? cropId : "tomato");
            StringBuilder diffText = new StringBuilder();

            if (pairwise != null && !pairwise.isEmpty()) {
                for (DifferentialFeature f : pairwise) {
                    diffText.append("• ").append(f.getDistinguishingTrait()).append(":\n")
                            .append(f.getComparisonSummary()).append("\n\n");
                }
            } else {
                diffText.append("• Look for concentric target rings (Early Blight) vs small spots with gray centers and black dots (Septoria) vs angular water-soaking (Bacterial Spot).");
            }

            runOnUiThread(() -> {
                DifferentialComparisonAdapter adapter = new DifferentialComparisonAdapter(candidates, diseaseMap);
                rvDifferentialCards.setAdapter(adapter);
                tvPairwiseDifferences.setText(diffText.toString().trim());
            });
        });
    }
}
