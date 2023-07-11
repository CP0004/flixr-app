package com.flixr.jo.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flixr.jo.R;
import com.flixr.jo.adapter.AdapterCategories;
import com.flixr.jo.databinding.ActivitySearchBinding;
import com.flixr.jo.tool.Animation;
import com.flixr.jo.tool.Dialog;
import com.flixr.jo.tool.Tools;
import com.flixr.jo.tool.Value;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private AdapterCategories adapterCategories;
    BaseViewModel viewModel;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //-- Reference
        dialog = new Dialog(SearchActivity.this);
        if (viewModel == null)
            viewModel = SplashActivity.viewModel;
        adapterCategories = new AdapterCategories(SearchActivity.this, new ArrayList<>(), (dataCategoriesList, position) -> {
            if (dataCategoriesList.get(position).getTypeCategorises().equals(Value.FILTER_CATEGORIES_SERIES)) {
                Intent intent = new Intent(SearchActivity.this, EpisodeActivity.class);
                intent.putExtra(Value.APP_REQUEST_DATA_TITLE, Tools.FiltersName(dataCategoriesList.get(position).getName()));
                startActivity(intent);
            } else {
                Intent intent = new Intent(SearchActivity.this, PlayerActivity.class);
                intent.putExtra(Value.APP_REQUEST_DATA_URL, dataCategoriesList.get(position).getUrl());
                startActivity(intent);
            }
        });

        //-- Default App Setting Activity
        binding.SearchRvViewSearch.setAdapter(adapterCategories);
        binding.SearchRvViewSearch.setLayoutManager(new GridLayoutManager(SearchActivity.this, (int) getResources().getDimension(R.dimen.dimen_colum_adapter)));
        binding.SearchRvViewSearch.setHasFixedSize(true);
        binding.SearchRvViewSearch.clearOnScrollListeners();

        viewModel.loadingSetCategorisesSearchToAdapter.observe(SearchActivity.this, aBoolean -> {
            if (aBoolean) {
                dialog.show();
            } else {
                dialog.cancel();
            }
        });

        viewModel.setCategorisesSearchToAdapter.observe(SearchActivity.this, databaseCategorisesList -> {
            adapterCategories.UpdateData(databaseCategorisesList);
        });

        binding.SearchBtnSearchVideo.setOnClickListener(v -> {
            Animation.AnimateBounce(binding.SearchBtnSearchVideo, SearchActivity.this);
            if (!binding.SearchEtSearchVideo.getText().toString().trim().isEmpty()) {
                viewModel.getDataCategorisesSearch(SearchActivity.this, binding.SearchEtSearchVideo.getText().toString().trim());
            }
        });

        binding.SearchRvViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    viewModel.addDataCategorisesSearch();
                }
            }
        });

    }


}