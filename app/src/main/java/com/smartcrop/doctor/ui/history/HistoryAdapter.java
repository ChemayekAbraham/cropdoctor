package com.smartcrop.doctor.ui.history;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.entity.DiagnosisHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryActionListener {
        void onItemClick(DiagnosisHistory history);
        void onItemDelete(DiagnosisHistory history);
    }

    private final List<DiagnosisHistory> list;
    private final OnHistoryActionListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault());

    public HistoryAdapter(List<DiagnosisHistory> list, OnHistoryActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_card, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        DiagnosisHistory item = list.get(position);

        holder.tvDate.setText(dateFormat.format(new Date(item.getTimestamp())));
        holder.tvDiseaseName.setText(item.getLeadingDiseaseName());

        String cropLabel = item.getSelectedCropId() != null
                ? item.getSelectedCropId().substring(0, 1).toUpperCase() + item.getSelectedCropId().substring(1)
                : "Crop Leaf";
        holder.tvCropAndScore.setText(String.format("%s • %.0f%% Model Score", cropLabel, item.getModelConfidence()));

        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            holder.ivThumb.setImageURI(Uri.parse(item.getImagePath()));
        } else {
            holder.ivThumb.setImageResource(R.drawable.crop_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onItemDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvDate, tvDiseaseName, tvCropAndScore;
        View btnDelete;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.ivHistoryThumb);
            tvDate = itemView.findViewById(R.id.tvHistoryDate);
            tvDiseaseName = itemView.findViewById(R.id.tvHistoryDiseaseName);
            tvCropAndScore = itemView.findViewById(R.id.tvHistoryCropAndScore);
            btnDelete = itemView.findViewById(R.id.btnDeleteHistoryItem);
        }
    }
}
