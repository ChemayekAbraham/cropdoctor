package com.smartcrop.doctor.ui.info;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.smartcrop.doctor.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.btnBackAbout).setOnClickListener(v -> finish());
    }
}
