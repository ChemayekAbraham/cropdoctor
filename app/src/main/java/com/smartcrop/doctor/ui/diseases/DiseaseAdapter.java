package com.smartcrop.doctor.ui.diseases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.entity.Disease;

import java.util.ArrayList;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    public interface OnDiseaseClickListener {
        void onDiseaseClick(Disease disease);
    }

    private final List<Disease> originalList;
    private final List<Disease> displayList;
    private final OnDiseaseClickListener listener;

    public DiseaseAdapter(List<Disease> diseases, OnDiseaseClickListener listener) {
        this.originalList = diseases != null ? diseases : new ArrayList<>();
        this.displayList = new ArrayList<>(this.originalList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease_card, parent, false);
        return new DiseaseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease d = displayList.get(position);
        Context ctx = holder.itemView.getContext();

        holder.tvName.setText(d.getName());
        holder.tvScientific.setText((d.getScientificName() != null ? d.getScientificName() : "") + " • " + d.getCategory());
        holder.tvSymptoms.setText(d.getTypicalSymptoms());

        if (d.isModelSupported()) {
            holder.tvSupport.setText("AI Supported");
            holder.tvSupport.setTextColor(ctx.getColor(R.color.primary));
        } else {
            holder.tvSupport.setText("Catalogue Reference");
            holder.tvSupport.setTextColor(ctx.getColor(R.color.text_tertiary));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onDiseaseClick(d);
        });
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    public void filter(String query) {
        displayList.clear();
        if (query == null || query.trim().isEmpty()) {
            displayList.addAll(originalList);
        } else {
            String lower = query.toLowerCase().trim();
            for (Disease d : originalList) {
                if (d.getName().toLowerCase().contains(lower) ||
                        d.getCropId().toLowerCase().contains(lower) ||
                        (d.getScientificName() != null && d.getScientificName().toLowerCase().contains(lower))) {
                    displayList.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvScientific, tvSupport, tvSymptoms;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDiseaseCardName);
            tvScientific = itemView.findViewById(R.id.tvDiseaseCardScientific);
            tvSupport = itemView.findViewById(R.id.tvDiseaseCardSupport);
            tvSymptoms = itemView.findViewById(R.id.tvDiseaseCardSymptoms);
        }
    }
}
