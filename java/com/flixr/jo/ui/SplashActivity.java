package com.flixr.jo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.flixr.jo.databinding.ActivitySplashBinding;
import com.flixr.jo.tool.Preference;
import com.flixr.jo.tool.Value;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    public static BaseViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Default App Setting Activity
        if (!Preference.getValueBoolean(SplashActivity.this, Value.SAVE_IS_CREATE_DEFAULT_SAVED)) {
            Preference.setValue(SplashActivity.this, Value.SAVE_IS_CREATE_DEFAULT_SAVED, true);
            Preference.setValue(SplashActivity.this, Value.SAVE_VERSION_CATEGORISES, "0");
        }
        binding.SplashTvHint.setVisibility(View.GONE);
        binding.SplashTvNameReadCategorises.setVisibility(View.VISIBLE);
        binding.SplashPgDownloadLoading.setVisibility(View.VISIBLE);

        //-- Reference
        if (viewModel == null)
            viewModel = new ViewModelProvider(SplashActivity.this).get(BaseViewModel.class);

        //-- Login on Server
        viewModel.defaultGetData(SplashActivity.this);

        viewModel.showCategorisesName.observe(SplashActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null)
                    binding.SplashTvHint.setVisibility(View.VISIBLE);
                else
                    binding.SplashTvHint.setVisibility(View.GONE);

                binding.SplashTvNameReadCategorises.setText(s);
            }
        });


        viewModel.loadingCategorisesDate.observe(SplashActivity.this, aBoolean -> {
            if (!aBoolean) {
                binding.SplashTvNameReadCategorises.setVisibility(View.VISIBLE);
                binding.SplashPgDownloadLoading.setVisibility(View.VISIBLE);
            } else {
                binding.SplashTvNameReadCategorises.setVisibility(View.GONE);
                binding.SplashPgDownloadLoading.setVisibility(View.GONE);
            }
        });

    }
}