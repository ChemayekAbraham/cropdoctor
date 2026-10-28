package com.smartcrop.doctor.ui.results;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.smartcrop.doctor.R;
import com.smartcrop.doctor.classifier.DiseaseClassifier;
import com.smartcrop.doctor.classifier.ImageQualityAnalyzer;
import com.smartcrop.doctor.classifier.Recognition;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.DiagnosisHistory;
import com.smartcrop.doctor.database.entity.Disease;
import com.smartcrop.doctor.database.entity.RecommendedInspection;
import com.smartcrop.doctor.reasoning.ChecklistObservation;
import com.smartcrop.doctor.reasoning.DiagnosticReasoningEngine;
import com.smartcrop.doctor.reasoning.ReasoningResult;
import com.smartcrop.doctor.repository.DiagnosisRepository;
import com.smartcrop.doctor.ui.camera.CameraActivity;
import com.smartcrop.doctor.ui.checklist.SymptomChecklistActivity;
import com.smartcrop.doctor.ui.differential.DifferentialDiagnosisActivity;
import com.smartcrop.doctor.ui.diseases.DiseaseDetailActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiseaseResultsActivity extends AppCompatActivity implements PredictionsAdapter.OnCandidateClickListener {

    public static final String EXTRA_IMAGE_URI = "extra_image_uri";
    public static final String EXTRA_SELECTED_CROP = "extra_selected_crop";

    private ImageView ivResultLeafThumb;
    private TextView tvResultCropBadge;
    private TextView tvLeadingDiseaseTitle;
    private TextView tvLeadingScientificName;
    private ProgressBar pbLeadingScore;
    private TextView tvLeadingScorePercent;

    private CardView cardCertaintyStatus;
    private ImageView ivCertaintyIcon;
    private TextView tvCertaintyMessage;

    private TextView tvReasoningExplanations;
    private RecyclerView rvPredictions;

    private TextView tvTypicalSymptoms;
    private TextView tvPossibleCauses;
    private TextView tvManagementActions;
    private TextView tvPreventionMethods;
    private TextView tvTreatmentSafety;

    private Uri imageUri;
    private String selectedCropId;
    private Bitmap currentBitmap;

    private DiseaseClassifier classifier;
    private DiagnosticReasoningEngine reasoningEngine;
    private DiagnosisRepository diagnosisRepository;

    private List<Recognition> candidates = new ArrayList<>();
    private ReasoningResult currentReasoning;
    private Disease leadingDiseaseRecord;
    private final List<ChecklistObservation> userObservations = new ArrayList<>();
    private boolean isSaved = false;

    private final ActivityResultLauncher<Intent> checklistLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    @SuppressWarnings("unchecked")
                    List<ChecklistObservation> returned = (List<ChecklistObservation>)
                            result.getData().getSerializableExtra(SymptomChecklistActivity.EXTRA_RETURNED_OBSERVATIONS);
                    if (returned != null) {
                        userObservations.clear();
                        userObservations.addAll(returned);
                        reEvaluateReasoning();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_results);

        initViews();

        classifier = new DiseaseClassifier(this);
        reasoningEngine = new DiagnosticReasoningEngine(this);
        diagnosisRepository = new DiagnosisRepository(getApplication());

        String uriStr = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        selectedCropId = getIntent().getStringExtra(EXTRA_SELECTED_CROP);
        if (selectedCropId == null || "auto".equalsIgnoreCase(selectedCropId)) {
            selectedCropId = null;
        }

        if (uriStr != null) {
            imageUri = Uri.parse(uriStr);
            runAnalysisPipeline();
        } else {
            Toast.makeText(this, "No image to analyze.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        ivResultLeafThumb = findViewById(R.id.ivResultLeafThumb);
        tvResultCropBadge = findViewById(R.id.tvResultCropBadge);
        tvLeadingDiseaseTitle = findViewById(R.id.tvLeadingDiseaseTitle);
        tvLeadingScientificName = findViewById(R.id.tvLeadingScientificName);
        pbLeadingScore = findViewById(R.id.pbLeadingScore);
        tvLeadingScorePercent = findViewById(R.id.tvLeadingScorePercent);

        cardCertaintyStatus = findViewById(R.id.cardCertaintyStatus);
        ivCertaintyIcon = findViewById(R.id.ivCertaintyIcon);
        tvCertaintyMessage = findViewById(R.id.tvCertaintyMessage);

        tvReasoningExplanations = findViewById(R.id.tvReasoningExplanations);
        rvPredictions = findViewById(R.id.rvPredictions);
        rvPredictions.setLayoutManager(new LinearLayoutManager(this));

        tvTypicalSymptoms = findViewById(R.id.tvTypicalSymptoms);
        tvPossibleCauses = findViewById(R.id.tvPossibleCauses);
        tvManagementActions = findViewById(R.id.tvManagementActions);
        tvPreventionMethods = findViewById(R.id.tvPreventionMethods);
        tvTreatmentSafety = findViewById(R.id.tvTreatmentSafety);

        findViewById(R.id.btnBackResults).setOnClickListener(v -> finish());
        findViewById(R.id.btnSaveTop).setOnClickListener(v -> saveDiagnosisToHistory());
        findViewById(R.id.btnSaveDiagnosisBottom).setOnClickListener(v -> saveDiagnosisToHistory());

        findViewById(R.id.btnScanAgain).setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnCheckSymptoms).setOnClickListener(v -> launchSymptomChecklist());

        findViewById(R.id.btnCompareDiseases).setOnClickListener(v -> {
            if (candidates.size() >= 2) {
                Intent intent = new Intent(this, DifferentialDiagnosisActivity.class);
                intent.putExtra(DifferentialDiagnosisActivity.EXTRA_CANDIDATES, (Serializable) candidates);
                intent.putExtra(DifferentialDiagnosisActivity.EXTRA_CROP_ID, selectedCropId != null ? selectedCropId : candidates.get(0).getCropId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Need at least 2 candidate diseases to compare.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnRecommendedInspection).setOnClickListener(v -> showRecommendedInspectionDialog());
    }

    private void runAnalysisPipeline() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                currentBitmap = ImageDecoder.decodeBitmap(source, (decoder, info, s) -> decoder.setMutableRequired(true));
            } else {
                currentBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            }
            ivResultLeafThumb.setImageBitmap(currentBitmap);

            // 1. Analyze image quality
            ImageQualityAnalyzer.QualityReport qualityReport = ImageQualityAnalyzer.analyze(currentBitmap);

            // 2. Run real TFLite AI classification (top 3 to 5 candidates)
            List<Recognition> rawCandidates = classifier.classify(currentBitmap, 5);

            // Filter by crop if farmer explicitly pre-selected a target crop
            if (selectedCropId != null && !selectedCropId.isEmpty() && !"auto".equalsIgnoreCase(selectedCropId)) {
                List<Recognition> filtered = new ArrayList<>();
                for (Recognition r : rawCandidates) {
                    if (selectedCropId.equalsIgnoreCase(r.getCropId())) {
                        filtered.add(r);
                    }
                }
                candidates = !filtered.isEmpty() ? filtered : rawCandidates;
            } else {
                candidates = rawCandidates;
            }

            if (candidates.isEmpty()) {
                showEmptyFallback();
                return;
            }

            // 3. Load DB records for distinguishing features
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                for (Recognition r : candidates) {
                    Disease d = db.diseaseDao().getDiseaseById(r.getDiseaseId());
                    if (d != null) {
                        r.setDistinguishingFeature(d.getDistinguishingFeature());
                        r.setCommonSymptoms(d.getTypicalSymptoms());
                    }
                }

                // 4. Run Diagnostic Reasoning Engine
                currentReasoning = reasoningEngine.evaluate(candidates, selectedCropId, userObservations, qualityReport);
                leadingDiseaseRecord = db.diseaseDao().getDiseaseById(currentReasoning.getRecommendedFocusDiseaseId());

                runOnUiThread(this::updateUIWithResults);
            });

        } catch (IOException e) {
            Toast.makeText(this, "Failed to load leaf image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void reEvaluateReasoning() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            ImageQualityAnalyzer.QualityReport qr = ImageQualityAnalyzer.analyze(currentBitmap);
            currentReasoning = reasoningEngine.evaluate(candidates, selectedCropId, userObservations, qr);
            leadingDiseaseRecord = db.diseaseDao().getDiseaseById(currentReasoning.getRecommendedFocusDiseaseId());

            runOnUiThread(() -> {
                updateCertaintyBanner();
                updateReasoningBox();
                Toast.makeText(this, "Diagnosis updated with your observations!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void updateUIWithResults() {
        if (leadingDiseaseRecord != null) {
            tvResultCropBadge.setText(leadingDiseaseRecord.getCropId().toUpperCase() + " LEAF");
            tvLeadingDiseaseTitle.setText(leadingDiseaseRecord.getName());
            tvLeadingScientificName.setText(leadingDiseaseRecord.getScientificName() != null ? leadingDiseaseRecord.getScientificName() : "Plant Condition");

            tvTypicalSymptoms.setText(leadingDiseaseRecord.getTypicalSymptoms());
            tvPossibleCauses.setText(leadingDiseaseRecord.getPossibleCauses());
            tvManagementActions.setText(leadingDiseaseRecord.getManagementRecommendations());
            tvPreventionMethods.setText(leadingDiseaseRecord.getPreventionMethods());
            tvTreatmentSafety.setText(leadingDiseaseRecord.getTreatmentSafetyNotes());
        } else if (!candidates.isEmpty()) {
            Recognition top = candidates.get(0);
            tvResultCropBadge.setText(top.getCropName() + " LEAF");
            tvLeadingDiseaseTitle.setText(top.getTitle());
            tvLeadingScientificName.setText("AI Diagnostic Class");
        }

        if (!candidates.isEmpty()) {
            int scorePercent = candidates.get(0).getPercentage();
            pbLeadingScore.setProgress(scorePercent);
            tvLeadingScorePercent.setText(scorePercent + "% Model Confidence");
        }

        updateCertaintyBanner();
        updateReasoningBox();

        // Candidates list adapter
        PredictionsAdapter adapter = new PredictionsAdapter(candidates, this);
        rvPredictions.setAdapter(adapter);
    }

    private void updateCertaintyBanner() {
        if (currentReasoning == null) return;

        if (currentReasoning.isModelObservationDisagreement()) {
            cardCertaintyStatus.setCardBackgroundColor(ContextCompat.getColor(this, R.color.status_critical_bg));
            ivCertaintyIcon.setImageResource(R.drawable.ic_warning);
            ivCertaintyIcon.setColorFilter(ContextCompat.getColor(this, R.color.status_critical));
            tvCertaintyMessage.setText("AI model and farmer observations disagree. Verify symptoms carefully before applying management actions.");
        } else if (currentReasoning.isUncertain()) {
            cardCertaintyStatus.setCardBackgroundColor(ContextCompat.getColor(this, R.color.status_warning_bg));
            ivCertaintyIcon.setImageResource(R.drawable.ic_warning);
            ivCertaintyIcon.setColorFilter(ContextCompat.getColor(this, R.color.status_warning));
            tvCertaintyMessage.setText("Diagnosis has overlapping candidates. Review the symptom checklist below.");
        } else {
            cardCertaintyStatus.setCardBackgroundColor(ContextCompat.getColor(this, R.color.status_healthy_bg));
            ivCertaintyIcon.setImageResource(R.drawable.ic_check_circle);
            ivCertaintyIcon.setColorFilter(ContextCompat.getColor(this, R.color.status_healthy));
            tvCertaintyMessage.setText("High confidence assessment with clear visual and observational agreement.");
        }
    }

    private void updateReasoningBox() {
        if (currentReasoning == null) return;
        List<String> explanations = currentReasoning.getReasoningExplanations();
        if (explanations.isEmpty()) {
            tvReasoningExplanations.setText("• No additional observations recorded yet. Click 'Check Symptoms More Closely' to refine the diagnosis.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String exp : explanations) {
                sb.append("• ").append(exp).append("\n\n");
            }
            tvReasoningExplanations.setText(sb.toString().trim());
        }
    }

    private void showRecommendedInspectionDialog() {
        if (leadingDiseaseRecord == null) {
            Toast.makeText(this, "No inspection details available.", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<RecommendedInspection> list = db.diseaseDao().getInspectionsByDisease(leadingDiseaseRecord.getDiseaseId());

            runOnUiThread(() -> {
                if (list == null || list.isEmpty()) {
                    new AlertDialog.Builder(this)
                            .setTitle("General Crop Inspection")
                            .setMessage("Walk your field in a zig-zag pattern once every 7 days. Inspect lower and middle leaves, paying attention to lesion spread and moisture conditions.")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    RecommendedInspection ins = list.get(0);
                    new AlertDialog.Builder(this)
                            .setTitle(ins.getInspectionTitle())
                            .setMessage(String.format("Focus Part: %s\nFollow-up Interval: Every %d days\n\nInstructions:\n%s\n\nExtension Referral:\n%s",
                                    ins.getFocusPlantPart(), ins.getFollowUpIntervalDays(), ins.getActionableInstructions(), ins.getReferralTriggerNote()))
                            .setPositiveButton("Understood", null)
                            .show();
                }
            });
        });
    }

    private void launchSymptomChecklist() {
        if (candidates.isEmpty()) return;
        String cropId = selectedCropId != null ? selectedCropId : candidates.get(0).getCropId();
        Intent intent = new Intent(this, SymptomChecklistActivity.class);
        intent.putExtra(SymptomChecklistActivity.EXTRA_CROP_ID, cropId);
        intent.putExtra(SymptomChecklistActivity.EXTRA_CANDIDATES, (Serializable) candidates);
        intent.putExtra(SymptomChecklistActivity.EXTRA_EXISTING_OBSERVATIONS, (Serializable) userObservations);
        checklistLauncher.launch(intent);
    }

    private void saveDiagnosisToHistory() {
        if (isSaved) {
            Toast.makeText(this, "Diagnosis already saved locally.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (candidates.isEmpty()) return;

        Gson gson = new Gson();
        Recognition top = candidates.get(0);
        String cropId = selectedCropId != null ? selectedCropId : top.getCropId();
        String diseaseId = leadingDiseaseRecord != null ? leadingDiseaseRecord.getDiseaseId() : top.getDiseaseId();
        String diseaseName = leadingDiseaseRecord != null ? leadingDiseaseRecord.getName() : top.getTitle();

        DiagnosisHistory history = new DiagnosisHistory(
                System.currentTimeMillis(),
                imageUri != null ? imageUri.toString() : "",
                cropId,
                diseaseId,
                diseaseName,
                top.getConfidence() * 100f,
                gson.toJson(candidates),
                gson.toJson(userObservations),
                gson.toJson(currentReasoning != null ? currentReasoning.getReasoningExplanations() : new ArrayList<>()),
                leadingDiseaseRecord != null ? leadingDiseaseRecord.getManagementRecommendations() : "",
                "MobileNetV2-1.0.0",
                currentReasoning != null && currentReasoning.isExpertReferralRecommended(),
                currentReasoning != null && currentReasoning.isUncertain()
        );

        diagnosisRepository.insert(history, newId -> runOnUiThread(() -> {
            isSaved = true;
            Toast.makeText(this, "Diagnosis saved to your offline history!", Toast.LENGTH_SHORT).show();
        }));
    }

    private void showEmptyFallback() {
        tvLeadingDiseaseTitle.setText("Uncertain Leaf Image");
        tvCertaintyMessage.setText("No supported disease candidates identified. Please capture another image with clearer lighting.");
    }

    @Override
    public void onViewDiseaseInfo(Recognition recognition) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra(DiseaseDetailActivity.EXTRA_DISEASE_ID, recognition.getDiseaseId());
        startActivity(intent);
    }

    @Override
    public void onCheckSymptoms(Recognition recognition) {
        launchSymptomChecklist();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (classifier != null) {
            classifier.close();
        }
    }
}
