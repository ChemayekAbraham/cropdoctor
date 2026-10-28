package com.smartcrop.doctor.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.auth.SessionManager;
import com.smartcrop.doctor.ui.auth.LoginActivity;
import com.smartcrop.doctor.ui.auth.ProfileActivity;
import com.smartcrop.doctor.ui.camera.CameraActivity;
import com.smartcrop.doctor.ui.camera.ImageReviewActivity;
import com.smartcrop.doctor.ui.crops.CropLibraryActivity;
import com.smartcrop.doctor.ui.diseases.DiseaseLibraryActivity;
import com.smartcrop.doctor.ui.history.DiagnosisHistoryActivity;
import com.smartcrop.doctor.ui.info.AboutActivity;
import com.smartcrop.doctor.ui.info.HowToUseActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> galleryPickerLauncher;
    private SessionManager sessionManager;
    private TextView tvHeaderProfileName;
    private TextView tvMenuProfileTitle;
    private TextView tvMenuProfileSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = SessionManager.getInstance(this);
        // If the user has not logged in or chosen guest mode, prompt for authentication
        if (!sessionManager.hasActiveSession()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        tvHeaderProfileName = findViewById(R.id.tvHeaderProfileName);
        tvMenuProfileTitle = findViewById(R.id.tvMenuProfileTitle);
        tvMenuProfileSubtitle = findViewById(R.id.tvMenuProfileSubtitle);

        // Register Photo Picker for gallery image selection
        galleryPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Intent intent = new Intent(MainActivity.this, ImageReviewActivity.class);
                        intent.putExtra(ImageReviewActivity.EXTRA_IMAGE_URI, uri.toString());
                        startActivity(intent);
                    }
                }
        );

        setupClickListeners();
        updateUserHeader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null) {
            updateUserHeader();
        }
    }

    private void updateUserHeader() {
        if (tvHeaderProfileName == null) return;

        if (sessionManager.isGuest()) {
            tvHeaderProfileName.setText("Guest");
            if (tvMenuProfileTitle != null) {
                tvMenuProfileTitle.setText("Farmer Account & Profile");
            }
            if (tvMenuProfileSubtitle != null) {
                tvMenuProfileSubtitle.setText("Guest Mode • Tap to create account or log in");
            }
        } else if (sessionManager.isLoggedIn()) {
            String name = sessionManager.getUserName();
            tvHeaderProfileName.setText(name);
            if (tvMenuProfileTitle != null) {
                tvMenuProfileTitle.setText("Farmer Profile (" + name + ")");
            }
            if (tvMenuProfileSubtitle != null) {
                String farm = sessionManager.getFarmName();
                if (!farm.isEmpty()) {
                    tvMenuProfileSubtitle.setText(farm + " • Tap to view profile");
                } else {
                    tvMenuProfileSubtitle.setText("Signed in • Tap to view profile");
                }
            }
        }
    }

    private void openProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void setupClickListeners() {
        // Profile actions
        findViewById(R.id.cardHeaderProfile).setOnClickListener(v -> openProfile());
        findViewById(R.id.cardFarmerProfile).setOnClickListener(v -> openProfile());

        // Take Photo
        findViewById(R.id.btnTakePhoto).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        // Choose from Gallery
        findViewById(R.id.btnChooseGallery).setOnClickListener(v -> {
            galleryPickerLauncher.launch("image/*");
        });

        // Scan hero card defaults to camera
        findViewById(R.id.cardScanHero).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        // Browse 12 Crops
        findViewById(R.id.cardBrowseCrops).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CropLibraryActivity.class);
            startActivity(intent);
        });

        // Disease Library
        findViewById(R.id.cardDiseaseLibrary).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
            startActivity(intent);
        });

        // Diagnosis History
        findViewById(R.id.cardDiagnosisHistory).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiagnosisHistoryActivity.class);
            startActivity(intent);
        });

        // How to Use
        findViewById(R.id.cardHowToUse).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HowToUseActivity.class);
            startActivity(intent);
        });

        // About
        findViewById(R.id.cardAbout).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Offline indicator click info
        findViewById(R.id.layoutOfflineIndicator).setOnClickListener(v -> {
            Toast.makeText(this, "AI classification and all agricultural reference guides run completely offline on your device.", Toast.LENGTH_LONG).show();
        });
    }
}
