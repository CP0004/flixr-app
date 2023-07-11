package com.flixr.jo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.flixr.jo.R;
import com.flixr.jo.adapter.AdapterEpisode;
import com.flixr.jo.databinding.ActivityEpisodeBinding;
import com.flixr.jo.tool.Dialog;
import com.flixr.jo.tool.Value;

import java.util.ArrayList;

public class EpisodeActivity extends AppCompatActivity {

    private AdapterEpisode adapterEpisode;
    private ArrayAdapter<String> arrayAdapterSeason;
    BaseViewModel viewModel;
    private Dialog dialog;
    Intent getIntents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEpisodeBinding binding = ActivityEpisodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntents = getIntent();
        binding.EpisodeTvTitle.setText(getIntents.getStringExtra(Value.APP_REQUEST_DATA_TITLE));

        //-- Reference
        dialog = new Dialog(EpisodeActivity.this);
        if (viewModel == null)
            viewModel = SplashActivity.viewModel;
        //Creating the ArrayAdapter instance having the bank name list
        arrayAdapterSeason = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());


        adapterEpisode = new AdapterEpisode(EpisodeActivity.this, new ArrayList<>(), (urlCategories, position) -> {
            Intent intent = new Intent(EpisodeActivity.this, PlayerActivity.class);
            intent.putExtra(Value.APP_REQUEST_DATA_URL, urlCategories);
            startActivity(intent);
        });

        //-- Default App Setting Activity
        binding.EpisodeRvViewEpisode.setAdapter(adapterEpisode);
        binding.EpisodeRvViewEpisode.setLayoutManager(new GridLayoutManager(EpisodeActivity.this, (int) getResources().getDimension(R.dimen.dimen_colum_adapter)));
        binding.EpisodeRvViewEpisode.setHasFixedSize(true);
        binding.EpisodeRvViewEpisode.clearOnScrollListeners();
        arrayAdapterSeason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.EpisodeSpSeason.setAdapter(arrayAdapterSeason);
        viewModel.getDataCategorisesEpisode(EpisodeActivity.this, getIntents.getStringExtra(Value.APP_REQUEST_DATA_TITLE));

        viewModel.setCategorisesEpisodeToAdapter.observe(EpisodeActivity.this, databaseCategorisesList -> {
            adapterEpisode.UpdateData(databaseCategorisesList);
        });

        viewModel.setCategorisesEpisodeForSeasonToAdapter.observe(EpisodeActivity.this, databaseSeason -> {
            arrayAdapterSeason.clear();
            arrayAdapterSeason.addAll(databaseSeason);
        });

        viewModel.loadingSetCategorisesEpisodeToAdapter.observe(EpisodeActivity.this, aBoolean -> {
            if (aBoolean) {
                dialog.show();
            } else {
                dialog.cancel();
            }
        });

        binding.EpisodeSpSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                viewModel.getDataCategorisesEpisodeForSeason(EpisodeActivity.this, getIntents.getStringExtra(Value.APP_REQUEST_DATA_TITLE), parent.getSelectedItem().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
}