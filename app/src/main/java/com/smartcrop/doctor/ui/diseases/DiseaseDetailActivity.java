package com.smartcrop.doctor.ui.diseases;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.Disease;

public class DiseaseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DISEASE_ID = "extra_disease_id";

    private TextView tvDetailCropTag;
    private TextView tvDetailSeverityTag;
    private TextView tvDetailModelSupported;
    private TextView tvDetailTitle;
    private TextView tvDetailScientific;
    private TextView tvDetailDescription;
    private View cardLeafLimitationNotice;
    private TextView tvDetailDistinguishing;
    private TextView tvDetailSymptoms;
    private TextView tvDetailCauses;
    private TextView tvDetailManagement;
    private TextView tvDetailPrevention;
    private TextView tvDetailSafety;
    private TextView tvDetailInspections;
    private TextView tvDetailReferral;
    private TextView tvDetailReferences;

    private String diseaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        initViews();

        findViewById(R.id.btnBackDiseaseDetail).setOnClickListener(v -> finish());

        diseaseId = getIntent().getStringExtra(EXTRA_DISEASE_ID);
        if (diseaseId != null) {
            loadDiseaseData();
        } else {
            Toast.makeText(this, "Disease not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvDetailCropTag = findViewById(R.id.tvDetailCropTag);
        tvDetailSeverityTag = findViewById(R.id.tvDetailSeverityTag);
        tvDetailModelSupported = findViewById(R.id.tvDetailModelSupported);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailScientific = findViewById(R.id.tvDetailScientific);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        cardLeafLimitationNotice = findViewById(R.id.cardLeafLimitationNotice);
        tvDetailDistinguishing = findViewById(R.id.tvDetailDistinguishing);
        tvDetailSymptoms = findViewById(R.id.tvDetailSymptoms);
        tvDetailCauses = findViewById(R.id.tvDetailCauses);
        tvDetailManagement = findViewById(R.id.tvDetailManagement);
        tvDetailPrevention = findViewById(R.id.tvDetailPrevention);
        tvDetailSafety = findViewById(R.id.tvDetailSafety);
        tvDetailInspections = findViewById(R.id.tvDetailInspections);
        tvDetailReferral = findViewById(R.id.tvDetailReferral);
        tvDetailReferences = findViewById(R.id.tvDetailReferences);
    }

    private void loadDiseaseData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Disease d = db.diseaseDao().getDiseaseById(diseaseId);

            runOnUiThread(() -> {
                if (d != null) {
                    tvDetailCropTag.setText(d.getCropId().toUpperCase());
                    tvDetailSeverityTag.setText(d.getSeverityLevel() + " Severity");
                    tvDetailTitle.setText(d.getName());
                    tvDetailScientific.setText((d.getScientificName() != null ? d.getScientificName() : "") + " • " + d.getCategory());
                    tvDetailDescription.setText(d.getDescription());

                    if (!d.isLeafOnlyDiagnosticReliable()) {
                        cardLeafLimitationNotice.setVisibility(View.VISIBLE);
                    } else {
                        cardLeafLimitationNotice.setVisibility(View.GONE);
                    }

                    tvDetailDistinguishing.setText(d.getDistinguishingFeature());
                    tvDetailSymptoms.setText(d.getTypicalSymptoms());
                    tvDetailCauses.setText(d.getPossibleCauses());
                    tvDetailManagement.setText(d.getManagementRecommendations());
                    tvDetailPrevention.setText(d.getPreventionMethods());
                    tvDetailSafety.setText(d.getTreatmentSafetyNotes());
                    tvDetailInspections.setText(d.getRecommendedInspectionPoints());
                    tvDetailReferral.setText(d.getExpertReferralConditions());
                    tvDetailReferences.setText("Verified Sources: " + d.getReferenceSources() + " • Reviewed: " + d.getLastReviewedDate());

                    if (d.isModelSupported()) {
                        tvDetailModelSupported.setText("AI Supported");
                        tvDetailModelSupported.setTextColor(getColor(R.color.primary));
                    } else {
                        tvDetailModelSupported.setText("Catalogue Reference Only");
                        tvDetailModelSupported.setTextColor(getColor(R.color.text_tertiary));
                    }
                }
            });
        });
    }
}
