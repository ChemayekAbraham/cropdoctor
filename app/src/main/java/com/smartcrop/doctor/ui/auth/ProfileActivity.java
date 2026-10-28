package com.smartcrop.doctor.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.smartcrop.doctor.R;
import com.smartcrop.doctor.auth.SessionManager;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.ui.history.DiagnosisHistoryActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName;
    private TextView tvProfileFarm;
    private TextView tvProfileContact;
    private TextView tvAccountBadge;
    private TextView tvTotalScans;
    private ImageView ivBadgeIcon;
    private View cardGuestUpgradePrompt;
    private MaterialButton btnUpgradeAccount;
    private MaterialButton btnLogout;
    private View cardViewHistory;
    private View btnBackProfile;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = SessionManager.getInstance(this);

        initViews();
        setupListeners();
        populateProfile();
        loadDiagnosisStats();
    }

    private void initViews() {
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileFarm = findViewById(R.id.tvProfileFarm);
        tvProfileContact = findViewById(R.id.tvProfileContact);
        tvAccountBadge = findViewById(R.id.tvAccountBadge);
        tvTotalScans = findViewById(R.id.tvTotalScans);
        ivBadgeIcon = findViewById(R.id.ivBadgeIcon);
        cardGuestUpgradePrompt = findViewById(R.id.cardGuestUpgradePrompt);
        btnUpgradeAccount = findViewById(R.id.btnUpgradeAccount);
        btnLogout = findViewById(R.id.btnLogout);
        cardViewHistory = findViewById(R.id.cardViewHistory);
        btnBackProfile = findViewById(R.id.btnBackProfile);
    }

    private void setupListeners() {
        btnBackProfile.setOnClickListener(v -> finish());

        cardViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DiagnosisHistoryActivity.class);
            startActivity(intent);
        });

        btnUpgradeAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            boolean wasGuest = sessionManager.isGuest();
            sessionManager.logout();
            Toast.makeText(this, wasGuest ? "Exited guest mode" : "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void populateProfile() {
        if (sessionManager.isGuest()) {
            tvProfileName.setText("Guest Farmer");
            tvProfileFarm.setText("Offline Diagnostics Session");
            tvProfileContact.setText("No account linked");
            tvAccountBadge.setText("Guest Mode Active");
            ivBadgeIcon.setImageResource(R.drawable.ic_leaf);
            cardGuestUpgradePrompt.setVisibility(View.VISIBLE);
            btnLogout.setText("Exit Guest Mode / Sign In");
        } else if (sessionManager.isLoggedIn()) {
            tvProfileName.setText(sessionManager.getUserName());
            String farm = sessionManager.getFarmName();
            tvProfileFarm.setText(farm.isEmpty() ? "Farm Profile Active" : farm);
            tvProfileContact.setText(sessionManager.getUserEmailOrPhone());
            tvAccountBadge.setText("Registered Farmer Account (Offline)");
            ivBadgeIcon.setImageResource(R.drawable.ic_check_circle);
            cardGuestUpgradePrompt.setVisibility(View.GONE);
            btnLogout.setText("Log Out / Switch Account");
        } else {
            tvProfileName.setText("Farmer");
            tvProfileFarm.setText("Not signed in");
            tvProfileContact.setText("");
            cardGuestUpgradePrompt.setVisibility(View.VISIBLE);
        }
    }

    private void loadDiagnosisStats() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int count = AppDatabase.getInstance(this).diagnosisHistoryDao().getHistoryCount();
            runOnUiThread(() -> tvTotalScans.setText(String.valueOf(count)));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateProfile();
        loadDiagnosisStats();
    }
}
