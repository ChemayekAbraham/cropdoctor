package com.smartcrop.doctor.ui.results;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.classifier.Recognition;

import java.util.List;

public class PredictionsAdapter extends RecyclerView.Adapter<PredictionsAdapter.PredictionViewHolder> {

    public interface OnCandidateClickListener {
        void onViewDiseaseInfo(Recognition recognition);
        void onCheckSymptoms(Recognition recognition);
    }

    private final List<Recognition> candidates;
    private final OnCandidateClickListener listener;

    public PredictionsAdapter(List<Recognition> candidates, OnCandidateClickListener listener) {
        this.candidates = candidates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prediction_candidate, parent, false);
        return new PredictionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionViewHolder holder, int position) {
        Recognition item = candidates.get(position);
        holder.tvRank.setText("#" + (position + 1));
        holder.tvTitle.setText(item.getTitle());
        holder.tvCrop.setText(item.getCropName() + (item.isHealthy() ? " • Healthy Foliage" : " • Potential Disease"));
        
        int percent = item.getPercentage();
        holder.tvScore.setText(percent + "%");
        holder.progressBarScore.setProgress(percent);

        if (item.getDistinguishingFeature() != null && !item.getDistinguishingFeature().isEmpty()) {
            holder.tvDistinguishing.setText("Distinguishing: " + item.getDistinguishingFeature());
            holder.tvDistinguishing.setVisibility(View.VISIBLE);
        } else {
            holder.tvDistinguishing.setVisibility(View.GONE);
        }

        holder.btnViewInfo.setOnClickListener(v -> {
            if (listener != null) listener.onViewDiseaseInfo(item);
        });

        holder.btnCheckSymptoms.setOnClickListener(v -> {
            if (listener != null) listener.onCheckSymptoms(item);
        });
    }

    @Override
    public int getItemCount() {
        return candidates != null ? candidates.size() : 0;
    }

    static class PredictionViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvTitle, tvCrop, tvScore, tvDistinguishing;
        ProgressBar progressBarScore;
        View btnViewInfo, btnCheckSymptoms;

        public PredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvCandidateRank);
            tvTitle = itemView.findViewById(R.id.tvCandidateTitle);
            tvCrop = itemView.findViewById(R.id.tvCandidateCrop);
            tvScore = itemView.findViewById(R.id.tvCandidateScore);
            tvDistinguishing = itemView.findViewById(R.id.tvDistinguishingFeature);
            progressBarScore = itemView.findViewById(R.id.progressBarScore);
            btnViewInfo = itemView.findViewById(R.id.btnViewDiseaseInfo);
            btnCheckSymptoms = itemView.findViewById(R.id.btnCheckSymptomsCandidate);
        }
    }
}
