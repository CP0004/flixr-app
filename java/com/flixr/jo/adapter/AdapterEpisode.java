package com.flixr.jo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.flixr.jo.R;
import com.flixr.jo.database.DatabaseCategorises;
import com.flixr.jo.databinding.AdapterCategoriesBinding;
import com.flixr.jo.databinding.AdapterEpisodeBinding;
import com.flixr.jo.listener.OnClickItemEpisode;
import com.flixr.jo.tool.Animation;
import com.flixr.jo.tool.DiffUtilCallbackData;
import com.flixr.jo.tool.Tools;

import java.util.List;

public class AdapterEpisode extends RecyclerView.Adapter<AdapterEpisode.ViewHolder> {

    @SuppressLint("StaticFieldLeak")
    public Context context;
    private List<DatabaseCategorises> dataEpisode;
    private final OnClickItemEpisode onClickItemEpisode;

    public AdapterEpisode(Context context, List<DatabaseCategorises> dataEpisode, OnClickItemEpisode onClickItemEpisode) {
        this.context = context;
        this.dataEpisode = dataEpisode;
        this.onClickItemEpisode = onClickItemEpisode;
    }

    public void UpdateData(List<DatabaseCategorises> newList) {
        DiffUtilCallbackData diffUtilCallbackData = new DiffUtilCallbackData(dataEpisode, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallbackData);
        dataEpisode.clear();
        dataEpisode.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

    }

    public List<DatabaseCategorises> getApp() {
        return dataEpisode;
    }

    public void setApp(List<DatabaseCategorises> dataEpisode) {
        this.dataEpisode = dataEpisode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episode, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tools.GetPhotoUrl(context, dataEpisode, holder.binding.EpisodeImgThumbnail, position);
        int count = position+1;
        holder.binding.EpisodeTvCountEpisode.setText(""+count);
        holder.binding.getRoot().setOnClickListener(v -> {
            Animation.AnimateBounce(holder.binding.getRoot(), context);
            onClickItemEpisode.onClickItemEpisode(dataEpisode.get(position).getUrl(), position);
        });
    }

    @Override
    public int getItemCount() {
        return dataEpisode.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AdapterEpisodeBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AdapterEpisodeBinding.bind(itemView);
        }
    }

}
