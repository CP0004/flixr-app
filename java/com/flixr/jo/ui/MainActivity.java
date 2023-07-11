package com.flixr.jo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.flixr.jo.R;
import com.flixr.jo.adapter.AdapterCategories;
import com.flixr.jo.databinding.ActivityMainBinding;
import com.flixr.jo.tool.Dialog;
import com.flixr.jo.tool.Tools;
import com.flixr.jo.tool.Value;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AdapterCategories adapterCategories;
    BaseViewModel viewModel;
    private Dialog dialog;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://2332ervfdg32rebeqw.pythonanywhere.com/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {

                    Log.d("TAG_TEST", "Response" + response);
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject object = response.getJSONObject(i);
//
////                            String name = object.getString("name");
////                            int age = object.getInt("age");
//
//                            Log.d("TAG_TEST", "" + response);
//                        }
                },
                error -> Log.e("TAG_TEST", "Error: " + error.getMessage()));
        queue.add(request);


        //-- Reference
        dialog = new Dialog(MainActivity.this);
        if (viewModel == null)
            viewModel = SplashActivity.viewModel;
        adapterCategories = new AdapterCategories(MainActivity.this, new ArrayList<>(), (dataCategoriesList, position) -> {
            if (dataCategoriesList.get(position).getTypeCategorises().equals(Value.FILTER_CATEGORIES_SERIES)) {
                Intent intent = new Intent(MainActivity.this, EpisodeActivity.class);
                intent.putExtra(Value.APP_REQUEST_DATA_TITLE, Tools.FiltersName(dataCategoriesList.get(position).getName()));
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra(Value.APP_REQUEST_DATA_URL, dataCategoriesList.get(position).getUrl());
                startActivity(intent);
            }
        });

        //-- Default App Setting Activity
        binding.MainRvViewCategorises.setAdapter(adapterCategories);
        binding.MainRvViewCategorises.setLayoutManager(new GridLayoutManager(MainActivity.this, (int) getResources().getDimension(R.dimen.dimen_colum_adapter)));
        binding.MainRvViewCategorises.setHasFixedSize(true);
        binding.MainRvViewCategorises.clearOnScrollListeners();
        binding.MainImgEmptyCategorises.setVisibility(View.GONE);
        viewModel.getDefaultDataCategorises(MainActivity.this);

        //-- onClick
        viewModel.setCategorisesToAdapter.observe(MainActivity.this, databaseCategorisesList -> {
            if (databaseCategorisesList.size() == 0)
                binding.MainImgEmptyCategorises.setVisibility(View.VISIBLE);
            else
                binding.MainImgEmptyCategorises.setVisibility(View.GONE);
            adapterCategories.UpdateData(databaseCategorisesList);
        });

        viewModel.loadingSetCategorisesToAdapter.observe(MainActivity.this, aBoolean -> {
            if (aBoolean) {
                dialog.show();
            } else {
                dialog.cancel();
            }
        });

        if (binding.MainBtnActionbarBottom != null) {
            binding.MainBtnActionbarBottom.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.Menu_btn_Movie:
                        if (!item.isChecked()) {
                            BaseViewModel.nameCategorises = new StringBuilder();
                            viewModel.getDataCategorises(MainActivity.this, Value.FILTER_CATEGORIES_MOVIE);
                        }
                        break;
                    case R.id.Menu_btn_Series:
                        if (!item.isChecked()) {
                            BaseViewModel.nameCategorises = new StringBuilder();
                            viewModel.getDataCategorises(MainActivity.this, Value.FILTER_CATEGORIES_SERIES);
                        }
                        break;
                    case R.id.Menu_btn_Tv:
                        if (!item.isChecked()) {
                            BaseViewModel.nameCategorises = new StringBuilder();
                            viewModel.getDataCategorises(MainActivity.this, Value.FILTER_CATEGORIES_TV);
                        }
                        break;
                    case R.id.Menu_btn_search:
                        if (!item.isChecked()) {
                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        }
                        break;
                    case R.id.Menu_btn_setting:
                        if (!item.isChecked()) {
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        }
                        break;
                }
                return true;
            });
        } else {
            if (binding.MainBtnActionbarBottomLand != null) {
                binding.MainBtnActionbarBottomLand.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.Menu_btn_Movie:
                            if (!item.isChecked()) {
                                BaseViewModel.nameCategorises = new StringBuilder();
                                viewModel.getDataCategorises(MainActivity.this, Value.FILTER_CATEGORIES_MOVIE);
                            }
                            break;
                        case R.id.Menu_btn_Series:
                            if (!item.isChecked()) {
                                BaseViewModel.nameCategorises = new StringBuilder();
                                viewModel.getDataCategorises(MainActivity.this, Value.FILTER_CATEGORIES_SERIES);
                            }
                            break;
                        case R.id.Menu_btn_Tv:
                            if (!item.isChecked()) {
                                BaseViewModel.nameCategorises = new StringBuilder();
                                viewModel.getDataCategorises(MainActivity.this, Value.FILTER_CATEGORIES_TV);
                            }
                            break;
                        case R.id.Menu_btn_search:
                            if (!item.isChecked()) {
                                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                            }
                            break;
                        case R.id.Menu_btn_setting:
                            if (!item.isChecked()) {
                                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            }
                            break;
                    }
                    return true;
                });
            }
        }


        binding.MainRvViewCategorises.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    viewModel.addDataCategorises();
                }
            }
        });

    }
}