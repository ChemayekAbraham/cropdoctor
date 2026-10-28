package com.smartcrop.doctor.ui.info;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.smartcrop.doctor.R;

public class HowToUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        findViewById(R.id.btnBackHowToUse).setOnClickListener(v -> finish());
    }
}
