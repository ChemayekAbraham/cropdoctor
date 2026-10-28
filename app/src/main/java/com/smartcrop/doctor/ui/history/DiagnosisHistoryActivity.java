package com.smartcrop.doctor.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.entity.DiagnosisHistory;
import com.smartcrop.doctor.repository.DiagnosisRepository;

import java.util.List;

public class DiagnosisHistoryActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryActionListener {

    private RecyclerView rvHistoryList;
    private View layoutEmptyHistory;
    private HistoryAdapter adapter;
    private DiagnosisRepository diagnosisRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_history);

        findViewById(R.id.btnBackHistory).setOnClickListener(v -> finish());

        rvHistoryList = findViewById(R.id.rvHistoryList);
        rvHistoryList.setLayoutManager(new LinearLayoutManager(this));
        layoutEmptyHistory = findViewById(R.id.layoutEmptyHistory);

        findViewById(R.id.btnClearAllHistory).setOnClickListener(v -> confirmClearAll());

        diagnosisRepository = new DiagnosisRepository(getApplication());
        diagnosisRepository.getAllHistory().observe(this, this::updateList);
    }

    private void updateList(List<DiagnosisHistory> list) {
        if (list == null || list.isEmpty()) {
            layoutEmptyHistory.setVisibility(View.VISIBLE);
            rvHistoryList.setVisibility(View.GONE);
        } else {
            layoutEmptyHistory.setVisibility(View.GONE);
            rvHistoryList.setVisibility(View.VISIBLE);
            adapter = new HistoryAdapter(list, this);
            rvHistoryList.setAdapter(adapter);
        }
    }

    private void confirmClearAll() {
        new AlertDialog.Builder(this)
                .setTitle("Clear All History?")
                .setMessage("Are you sure you want to delete all saved diagnosis records? This action cannot be undone.")
                .setPositiveButton("Clear All", (dialog, which) -> {
                    diagnosisRepository.deleteAll();
                    Toast.makeText(this, "History cleared.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onItemClick(DiagnosisHistory history) {
        Intent intent = new Intent(this, HistoryDetailActivity.class);
        intent.putExtra(HistoryDetailActivity.EXTRA_HISTORY_ID, history.getId());
        startActivity(intent);
    }

    @Override
    public void onItemDelete(DiagnosisHistory history) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Record?")
                .setMessage("Delete diagnosis record for " + history.getLeadingDiseaseName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    diagnosisRepository.delete(history);
                    Toast.makeText(this, "Record deleted.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
