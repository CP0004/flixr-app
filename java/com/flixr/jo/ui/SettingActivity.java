package com.flixr.jo.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.flixr.jo.databinding.ActivitySettingBinding;
import com.flixr.jo.tool.Tools;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingBinding binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.SettingImgInstagram.setOnClickListener(v -> Tools.GoToURL(SettingActivity.this, BaseViewModel.instagram));
        binding.SettingImgTelegram.setOnClickListener(v -> Tools.GoToURL(SettingActivity.this, BaseViewModel.telegram));
        binding.SettingImgTikTok.setOnClickListener(v -> Tools.GoToURL(SettingActivity.this, BaseViewModel.tiktok));

    }
}