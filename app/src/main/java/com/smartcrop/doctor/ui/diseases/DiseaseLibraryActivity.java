package com.smartcrop.doctor.ui.diseases;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.Disease;

import java.util.List;

public class DiseaseLibraryActivity extends AppCompatActivity implements DiseaseAdapter.OnDiseaseClickListener {

    public static final String EXTRA_FILTER_CROP_ID = "extra_filter_crop_id";

    private RecyclerView rvDiseaseLibrary;
    private EditText etSearchDiseases;
    private DiseaseAdapter adapter;
    private String filterCropId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_library);

        findViewById(R.id.btnBackDiseases).setOnClickListener(v -> finish());

        rvDiseaseLibrary = findViewById(R.id.rvDiseaseLibrary);
        rvDiseaseLibrary.setLayoutManager(new LinearLayoutManager(this));
        etSearchDiseases = findViewById(R.id.etSearchDiseases);

        filterCropId = getIntent().getStringExtra(EXTRA_FILTER_CROP_ID);

        loadDiseases();

        etSearchDiseases.addTextChangedListener(new TextWatcher() {
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

    private void loadDiseases() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<Disease> list;
            if (filterCropId != null && !filterCropId.isEmpty()) {
                list = db.diseaseDao().getDiseasesByCropSync(filterCropId);
            } else {
                list = db.diseaseDao().getAllDiseasesSync();
            }

            runOnUiThread(() -> {
                if (list != null) {
                    adapter = new DiseaseAdapter(list, this);
                    rvDiseaseLibrary.setAdapter(adapter);
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
