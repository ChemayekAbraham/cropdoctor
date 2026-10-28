package com.smartcrop.doctor.ui.differential;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.classifier.Recognition;
import com.smartcrop.doctor.database.entity.Disease;

import java.util.List;
import java.util.Map;

public class DifferentialComparisonAdapter extends RecyclerView.Adapter<DifferentialComparisonAdapter.DiffViewHolder> {

    private final List<Recognition> candidates;
    private final Map<String, Disease> diseaseMap;

    public DifferentialComparisonAdapter(List<Recognition> candidates, Map<String, Disease> diseaseMap) {
        this.candidates = candidates;
        this.diseaseMap = diseaseMap;
    }

    @NonNull
    @Override
    public DiffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_differential_comparison, parent, false);
        return new DiffViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiffViewHolder holder, int position) {
        Recognition item = candidates.get(position);
        Disease record = diseaseMap.get(item.getDiseaseId());

        holder.tvRank.setText("#" + (position + 1));
        holder.tvName.setText(item.getTitle());
        holder.tvScore.setText(item.getPercentage() + "%");
        holder.pbScore.setProgress(item.getPercentage());

        if (record != null) {
            holder.tvScientific.setText(record.getScientificName() != null ? record.getScientificName() + " • " + record.getCategory() : record.getCategory());
            holder.tvDistinguishing.setText(record.getDistinguishingFeature() != null ? record.getDistinguishingFeature() : "Examine leaf surface closely.");
            holder.tvSymptoms.setText(record.getTypicalSymptoms() != null ? record.getTypicalSymptoms() : "Spotting and discoloration.");
            holder.tvPlantParts.setText(record.getRecommendedInspectionPoints() != null ? record.getRecommendedInspectionPoints() : "Leaves and stems.");
            holder.tvNextAction.setText(record.getManagementRecommendations() != null ? record.getManagementRecommendations() : "Observe progression.");
        } else {
            holder.tvScientific.setText("Candidate Condition");
            holder.tvDistinguishing.setText(item.getDistinguishingFeature() != null ? item.getDistinguishingFeature() : "Examine foliage carefully.");
            holder.tvSymptoms.setText(item.getCommonSymptoms() != null ? item.getCommonSymptoms() : "Foliar spotting.");
            holder.tvPlantParts.setText("Leaves");
            holder.tvNextAction.setText("Isolate affected leaves and monitor.");
        }
    }

    @Override
    public int getItemCount() {
        return candidates != null ? candidates.size() : 0;
    }

    static class DiffViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvScientific, tvScore, tvDistinguishing, tvSymptoms, tvPlantParts, tvAdditional, tvNextAction;
        ProgressBar pbScore;

        public DiffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvDiffRank);
            tvName = itemView.findViewById(R.id.tvDiffDiseaseName);
            tvScientific = itemView.findViewById(R.id.tvDiffScientificName);
            tvScore = itemView.findViewById(R.id.tvDiffScorePercent);
            pbScore = itemView.findViewById(R.id.pbDiffScore);
            tvDistinguishing = itemView.findViewById(R.id.tvDiffDistinguishingSign);
            tvSymptoms = itemView.findViewById(R.id.tvDiffTypicalSymptoms);
            tvPlantParts = itemView.findViewById(R.id.tvDiffPlantParts);
            tvAdditional = itemView.findViewById(R.id.tvDiffAdditionalObservation);
            tvNextAction = itemView.findViewById(R.id.tvDiffNextAction);
        }
    }
}
