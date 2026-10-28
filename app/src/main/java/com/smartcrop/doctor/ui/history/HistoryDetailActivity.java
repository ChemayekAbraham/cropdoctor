package com.smartcrop.doctor.ui.history;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.DiagnosisHistory;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryDetailActivity extends AppCompatActivity {

    public static final String EXTRA_HISTORY_ID = "extra_history_id";

    private ImageView ivHistoryDetailPhoto;
    private TextView tvHistoryDetailDate;
    private TextView tvHistoryDetailDisease;
    private TextView tvHistoryDetailCropAndScore;
    private TextView tvHistoryDetailReasoning;
    private TextView tvHistoryDetailRecommendations;

    private long historyId;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy • HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        ivHistoryDetailPhoto = findViewById(R.id.ivHistoryDetailPhoto);
        tvHistoryDetailDate = findViewById(R.id.tvHistoryDetailDate);
        tvHistoryDetailDisease = findViewById(R.id.tvHistoryDetailDisease);
        tvHistoryDetailCropAndScore = findViewById(R.id.tvHistoryDetailCropAndScore);
        tvHistoryDetailReasoning = findViewById(R.id.tvHistoryDetailReasoning);
        tvHistoryDetailRecommendations = findViewById(R.id.tvHistoryDetailRecommendations);

        findViewById(R.id.btnBackHistoryDetail).setOnClickListener(v -> finish());

        historyId = getIntent().getLongExtra(EXTRA_HISTORY_ID, -1);
        if (historyId != -1) {
            loadHistoryRecord();
        } else {
            Toast.makeText(this, "Record not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadHistoryRecord() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            DiagnosisHistory h = db.diagnosisHistoryDao().getHistoryById(historyId);

            runOnUiThread(() -> {
                if (h != null) {
                    tvHistoryDetailDate.setText(dateFormat.format(new Date(h.getTimestamp())) + " • " + h.getModelVersion());
                    tvHistoryDetailDisease.setText(h.getLeadingDiseaseName());

                    String cropName = h.getSelectedCropId() != null
                            ? h.getSelectedCropId().substring(0, 1).toUpperCase() + h.getSelectedCropId().substring(1)
                            : "Crop";
                    tvHistoryDetailCropAndScore.setText(String.format("%s • %.0f%% Model Score", cropName, h.getModelConfidence()));

                    if (h.getImagePath() != null && !h.getImagePath().isEmpty()) {
                        ivHistoryDetailPhoto.setImageURI(Uri.parse(h.getImagePath()));
                    } else {
                        ivHistoryDetailPhoto.setImageResource(R.drawable.crop_placeholder);
                    }

                    // Parse reasoning explanations
                    if (h.getReasoningExplanationsJson() != null) {
                        Type type = new TypeToken<List<String>>(){}.getType();
                        List<String> list = new Gson().fromJson(h.getReasoningExplanationsJson(), type);
                        if (list != null && !list.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (String s : list) {
                                sb.append("• ").append(s).append("\n\n");
                            }
                            tvHistoryDetailReasoning.setText(sb.toString().trim());
                        } else {
                            tvHistoryDetailReasoning.setText("• Initial visual assessment.");
                        }
                    }

                    tvHistoryDetailRecommendations.setText(h.getRecommendationsSummary() != null
                            ? h.getRecommendationsSummary()
                            : "No management notes saved.");
                }
            });
        });
    }
}
