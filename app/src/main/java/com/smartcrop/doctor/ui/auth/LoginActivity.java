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
import com.smartcrop.doctor.auth.SessionManager;
import com.smartcrop.doctor.database.entity.User;
import com.smartcrop.doctor.repository.UserRepository;
import com.smartcrop.doctor.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_ALLOW_BACK = "extra_allow_back";

    private TextInputEditText etEmailOrPhone;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private MaterialButton btnContinueGuest;
    private ProgressBar progressBarLogin;
    private View cardGoToRegister;

    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = new UserRepository(getApplication());
        sessionManager = SessionManager.getInstance(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmailOrPhone = findViewById(R.id.etEmailOrPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnContinueGuest = findViewById(R.id.btnContinueGuest);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        cardGoToRegister = findViewById(R.id.cardGoToRegister);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> performLogin());

        btnContinueGuest.setOnClickListener(v -> {
            sessionManager.setGuestMode(true);
            Toast.makeText(this, "Welcome! You are exploring in offline Guest Mode.", Toast.LENGTH_SHORT).show();
            navigateToMain();
        });

        cardGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String contact = etEmailOrPhone.getText() != null ? etEmailOrPhone.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (contact.isEmpty()) {
            etEmailOrPhone.setError("Please enter your email or phone");
            etEmailOrPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        setLoading(true);

        userRepository.login(contact, password, new UserRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, "Welcome back, " + user.getFullName() + "!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onError(String errorMessage) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressBarLogin.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!isLoading);
        btnContinueGuest.setEnabled(!isLoading);
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
