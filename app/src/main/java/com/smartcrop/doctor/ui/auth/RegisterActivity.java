package com.smartcrop.doctor.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.entity.User;
import com.smartcrop.doctor.repository.UserRepository;
import com.smartcrop.doctor.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName;
    private TextInputEditText etFarmName;
    private TextInputEditText etEmailOrPhone;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBarRegister;
    private View cardGoToLogin;
    private View btnBack;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepository = new UserRepository(getApplication());

        initViews();
        setupListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etFarmName = findViewById(R.id.etFarmName);
        etEmailOrPhone = findViewById(R.id.etEmailOrPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBarRegister = findViewById(R.id.progressBarRegister);
        cardGoToLogin = findViewById(R.id.cardGoToLogin);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        cardGoToLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String farmName = etFarmName.getText() != null ? etFarmName.getText().toString().trim() : "";
        String contact = etEmailOrPhone.getText() != null ? etEmailOrPhone.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        if (fullName.isEmpty()) {
            etFullName.setError("Please enter your name");
            etFullName.requestFocus();
            return;
        }

        if (contact.isEmpty()) {
            etEmailOrPhone.setError("Please enter your email or phone number");
            etEmailOrPhone.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        setLoading(true);

        userRepository.register(fullName, contact, farmName, password, new UserRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                setLoading(false);
                Toast.makeText(RegisterActivity.this, "Account created! Welcome, " + user.getFullName() + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBarRegister.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
    }
}
