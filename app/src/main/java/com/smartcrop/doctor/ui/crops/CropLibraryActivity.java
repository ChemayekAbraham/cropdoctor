package com.smartcrop.doctor.ui.crops;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.entity.Crop;
import com.smartcrop.doctor.repository.CropRepository;
import com.smartcrop.doctor.ui.diseases.DiseaseLibraryActivity;

public class CropLibraryActivity extends AppCompatActivity implements CropAdapter.OnCropActionListener {

    private RecyclerView rvCropLibrary;
    private EditText etSearchCrops;
    private CropAdapter adapter;
    private CropRepository cropRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_library);

        findViewById(R.id.btnBackCrops).setOnClickListener(v -> finish());

        rvCropLibrary = findViewById(R.id.rvCropLibrary);
        rvCropLibrary.setLayoutManager(new LinearLayoutManager(this));
        etSearchCrops = findViewById(R.id.etSearchCrops);

        cropRepository = new CropRepository(getApplication());
        cropRepository.getAllCrops().observe(this, crops -> {
            if (crops != null) {
                adapter = new CropAdapter(crops, this);
                rvCropLibrary.setAdapter(adapter);
            }
        });

        etSearchCrops.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onViewCropInfo(Crop crop) {
        Intent intent = new Intent(this, CropDetailActivity.class);
        intent.putExtra(CropDetailActivity.EXTRA_CROP_ID, crop.getCropId());
        startActivity(intent);
    }

    @Override
    public void onViewAssociatedDiseases(Crop crop) {
        Intent intent = new Intent(this, DiseaseLibraryActivity.class);
        intent.putExtra(DiseaseLibraryActivity.EXTRA_FILTER_CROP_ID, crop.getCropId());
        startActivity(intent);
    }
}
