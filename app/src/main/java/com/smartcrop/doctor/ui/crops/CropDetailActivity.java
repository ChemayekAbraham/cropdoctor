package com.smartcrop.doctor.ui.crops;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.Crop;
import com.smartcrop.doctor.database.entity.Disease;
import com.smartcrop.doctor.ui.camera.CameraActivity;
import com.smartcrop.doctor.ui.diseases.DiseaseAdapter;
import com.smartcrop.doctor.ui.diseases.DiseaseDetailActivity;

import java.util.List;

public class CropDetailActivity extends AppCompatActivity implements DiseaseAdapter.OnDiseaseClickListener {

    public static final String EXTRA_CROP_ID = "extra_crop_id";

    private ImageView ivCropDetailImage;
    private TextView tvHeaderCropTitle;
    private TextView tvCropDetailName;
    private TextView tvCropDetailBotanical;
    private TextView tvCropDetailModelBadge;
    private TextView tvCropDetailDescription;
    private TextView tvHealthyLeafGuidance;
    private RecyclerView rvAssociatedDiseases;

    private String cropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail);

        initViews();

        cropId = getIntent().getStringExtra(EXTRA_CROP_ID);
        if (cropId != null) {
            loadCropData();
        } else {
            Toast.makeText(this, "Crop not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        ivCropDetailImage = findViewById(R.id.ivCropDetailImage);
        tvHeaderCropTitle = findViewById(R.id.tvHeaderCropTitle);
        tvCropDetailName = findViewById(R.id.tvCropDetailName);
        tvCropDetailBotanical = findViewById(R.id.tvCropDetailBotanical);
        tvCropDetailModelBadge = findViewById(R.id.tvCropDetailModelBadge);
        tvCropDetailDescription = findViewById(R.id.tvCropDetailDescription);
        tvHealthyLeafGuidance = findViewById(R.id.tvHealthyLeafGuidance);
        rvAssociatedDiseases = findViewById(R.id.rvAssociatedDiseases);
        rvAssociatedDiseases.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackCropDetail).setOnClickListener(v -> finish());

        findViewById(R.id.btnScanThisCrop).setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        });
    }

    private void loadCropData() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Crop crop = db.cropDao().getCropById(cropId);
            List<Disease> diseases = db.diseaseDao().getDiseasesByCropSync(cropId);

            runOnUiThread(() -> {
                if (crop != null) {
                    tvHeaderCropTitle.setText(crop.getCommonName());
                    tvCropDetailName.setText(crop.getCommonName());
                    tvCropDetailBotanical.setText(crop.getBotanicalName());
                    tvCropDetailDescription.setText(crop.getDescription());
                    tvHealthyLeafGuidance.setText(crop.getHealthyLeafGuidance());

                    if (crop.getRepresentativeImage() != null) {
                        int resId = getResources().getIdentifier(crop.getRepresentativeImage(), "drawable", getPackageName());
                        if (resId != 0) ivCropDetailImage.setImageResource(resId);
                    }

                    if (crop.isModelSupported()) {
                        tvCropDetailModelBadge.setText("AI Detection Active for this Crop");
                        tvCropDetailModelBadge.setTextColor(getColor(R.color.primary));
                    } else {
                        tvCropDetailModelBadge.setText("Reference Knowledge Catalogue");
                        tvCropDetailModelBadge.setTextColor(getColor(R.color.text_tertiary));
                    }
                }

                if (diseases != null) {
                    DiseaseAdapter adapter = new DiseaseAdapter(diseases, this);
                    rvAssociatedDiseases.setAdapter(adapter);
                }
            });
        });
    }

    @Override
    public void onDiseaseClick(Disease disease) {
        Intent intent = new Intent(this, DiseaseDetailActivity.class);
        intent.putExtra(DiseaseDetailActivity.EXTRA_DISEASE_ID, disease.getDiseaseId());
        startActivity(intent);
    }
}
