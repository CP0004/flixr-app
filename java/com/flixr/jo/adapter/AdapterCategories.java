package com.flixr.jo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.flixr.jo.R;
import com.flixr.jo.database.DatabaseCategorises;
import com.flixr.jo.databinding.AdapterCategoriesBinding;
import com.flixr.jo.listener.OnClickItemCategories;
import com.flixr.jo.tool.Animation;
import com.flixr.jo.tool.DiffUtilCallbackData;
import com.flixr.jo.tool.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolder> {

    @SuppressLint("StaticFieldLeak")
    public Context context;
    private List<DatabaseCategorises> dataCategories;
    private final OnClickItemCategories onClickItemCategories;

    public AdapterCategories(Context context, List<DatabaseCategorises> dataCategories, OnClickItemCategories onClickItemCategories) {
        this.context = context;
        this.dataCategories = dataCategories;
        this.onClickItemCategories = onClickItemCategories;
    }

    public void UpdateData(List<DatabaseCategorises> newList) {
        DiffUtilCallbackData diffUtilCallbackData = new DiffUtilCallbackData(dataCategories, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallbackData);
        dataCategories.clear();
        dataCategories.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

    }

    public List<DatabaseCategorises> getApp() {
        return dataCategories;
    }

    public void setApp(List<DatabaseCategorises> dataCategories) {
        this.dataCategories = dataCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tools.GetPhotoUrl(context, dataCategories, holder.binding.CategoriesImgThumbnail, position);
        holder.binding.CategoriesTvNameContent.setText(dataCategories.get(position).getName());
        holder.binding.getRoot().setOnClickListener(v -> {
            Animation.AnimateBounce(holder.binding.getRoot(), context);
            onClickItemCategories.onClickItemCategories(dataCategories, position);
        });
    }

    @Override
    public int getItemCount() {
        return dataCategories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AdapterCategoriesBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterCategoriesBinding.bind(itemView);
        }
    }

}
