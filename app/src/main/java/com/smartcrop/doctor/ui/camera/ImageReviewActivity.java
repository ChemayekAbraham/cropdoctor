package com.smartcrop.doctor.ui.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.classifier.ImageQualityAnalyzer;
import com.smartcrop.doctor.ui.results.DiseaseResultsActivity;

import java.io.IOException;

public class ImageReviewActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_URI = "extra_image_uri";

    private ImageView ivReviewPhoto;
    private ImageView ivQualityIcon;
    private TextView tvQualityMessage;
    private CardView cardQualityFeedback;
    private Spinner spinnerCropSelect;

    private Uri imageUri;
    private Bitmap currentBitmap;
    private ImageQualityAnalyzer.QualityReport qualityReport;

    private final String[] cropOptions = {
            "Auto-Detect Crop",
            "Maize (Corn)",
            "Tomato",
            "Cassava",
            "Beans",
            "Potato",
            "Groundnuts",
            "Banana",
            "Coffee",
            "Rice",
            "Pepper",
            "Sorghum",
            "Sweet Potato"
    };

    private final String[] cropIds = {
            "auto",
            "maize",
            "tomato",
            "cassava",
            "beans",
            "potato",
            "groundnuts",
            "banana",
            "coffee",
            "rice",
            "pepper",
            "sorghum",
            "sweet_potato"
    };

    private final ActivityResultLauncher<String> galleryPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    this.imageUri = uri;
                    loadAndAnalyzeImage();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_review);

        ivReviewPhoto = findViewById(R.id.ivReviewPhoto);
        ivQualityIcon = findViewById(R.id.ivQualityIcon);
        tvQualityMessage = findViewById(R.id.tvQualityMessage);
        cardQualityFeedback = findViewById(R.id.cardQualityFeedback);
        spinnerCropSelect = findViewById(R.id.spinnerCropSelect);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cropOptions);
        spinnerCropSelect.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnChooseAnother).setOnClickListener(v -> galleryPickerLauncher.launch("image/*"));

        findViewById(R.id.btnIdentifyDisease).setOnClickListener(v -> proceedToIdentification());

        String uriString = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        if (uriString != null) {
            imageUri = Uri.parse(uriString);
            loadAndAnalyzeImage();
        } else {
            Toast.makeText(this, "No image found to review.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadAndAnalyzeImage() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                currentBitmap = ImageDecoder.decodeBitmap(source, (decoder, info, s) -> decoder.setMutableRequired(true));
            } else {
                currentBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            }

            ivReviewPhoto.setImageBitmap(currentBitmap);

            // Run offline image quality check
            qualityReport = ImageQualityAnalyzer.analyze(currentBitmap);

            if (qualityReport.isAcceptable) {
                cardQualityFeedback.setCardBackgroundColor(ContextCompat.getColor(this, R.color.status_healthy_bg));
                ivQualityIcon.setImageResource(R.drawable.ic_check_circle);
                ivQualityIcon.setColorFilter(ContextCompat.getColor(this, R.color.status_healthy));
                tvQualityMessage.setText(qualityReport.userGuidance);
            } else {
                cardQualityFeedback.setCardBackgroundColor(ContextCompat.getColor(this, R.color.status_warning_bg));
                ivQualityIcon.setImageResource(R.drawable.ic_warning);
                ivQualityIcon.setColorFilter(ContextCompat.getColor(this, R.color.status_warning));
                tvQualityMessage.setText(qualityReport.userGuidance);
            }

        } catch (IOException e) {
            Toast.makeText(this, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void proceedToIdentification() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPos = spinnerCropSelect.getSelectedItemPosition();
        String selectedCropId = cropIds[selectedPos];

        Intent intent = new Intent(this, DiseaseResultsActivity.class);
        intent.putExtra(DiseaseResultsActivity.EXTRA_IMAGE_URI, imageUri.toString());
        intent.putExtra(DiseaseResultsActivity.EXTRA_SELECTED_CROP, selectedCropId);
        startActivity(intent);
    }
}
