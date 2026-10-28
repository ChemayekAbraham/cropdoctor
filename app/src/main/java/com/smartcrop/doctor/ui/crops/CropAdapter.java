package com.smartcrop.doctor.ui.crops;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartcrop.doctor.R;
import com.smartcrop.doctor.database.entity.Crop;

import java.util.ArrayList;
import java.util.List;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {

    public interface OnCropActionListener {
        void onViewCropInfo(Crop crop);
        void onViewAssociatedDiseases(Crop crop);
    }

    private final List<Crop> originalList;
    private final List<Crop> displayList;
    private final OnCropActionListener listener;

    public CropAdapter(List<Crop> crops, OnCropActionListener listener) {
        this.originalList = crops != null ? crops : new ArrayList<>();
        this.displayList = new ArrayList<>(this.originalList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crop_card, parent, false);
        return new CropViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = displayList.get(position);
        Context ctx = holder.itemView.getContext();

        holder.tvCommonName.setText(crop.getCommonName());
        holder.tvBotanicalName.setText(crop.getBotanicalName());
        holder.tvDescription.setText(crop.getDescription());

        // Resolve drawable resource
        int resId = 0;
        if (crop.getRepresentativeImage() != null) {
            resId = ctx.getResources().getIdentifier(crop.getRepresentativeImage(), "drawable", ctx.getPackageName());
        }
        if (resId != 0) {
            holder.ivRepresentative.setImageResource(resId);
        } else {
            holder.ivRepresentative.setImageResource(R.drawable.crop_placeholder);
        }

        if (crop.isModelSupported()) {
            holder.tvModelSupport.setText("AI Detection Supported");
            holder.tvModelSupport.setTextColor(ctx.getColor(R.color.primary));
        } else {
            holder.tvModelSupport.setText("Reference Catalogue Only");
            holder.tvModelSupport.setTextColor(ctx.getColor(R.color.text_tertiary));
        }

        holder.btnCropInfo.setOnClickListener(v -> {
            if (listener != null) listener.onViewCropInfo(crop);
        });

        holder.btnAssociatedDiseases.setOnClickListener(v -> {
            if (listener != null) listener.onViewAssociatedDiseases(crop);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onViewCropInfo(crop);
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
            for (Crop c : originalList) {
                if (c.getCommonName().toLowerCase().contains(lower) ||
                        (c.getBotanicalName() != null && c.getBotanicalName().toLowerCase().contains(lower))) {
                    displayList.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class CropViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRepresentative;
        TextView tvCommonName, tvBotanicalName, tvModelSupport, tvDescription;
        View btnCropInfo, btnAssociatedDiseases;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRepresentative = itemView.findViewById(R.id.ivCropRepresentative);
            tvCommonName = itemView.findViewById(R.id.tvCropCommonName);
            tvBotanicalName = itemView.findViewById(R.id.tvCropBotanicalName);
            tvModelSupport = itemView.findViewById(R.id.tvModelSupportTag);
            tvDescription = itemView.findViewById(R.id.tvCropDescription);
            btnCropInfo = itemView.findViewById(R.id.btnViewCropInfo);
            btnAssociatedDiseases = itemView.findViewById(R.id.btnViewAssociatedDiseases);
        }
    }
}
