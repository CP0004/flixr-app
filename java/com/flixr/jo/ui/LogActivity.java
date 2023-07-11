package com.flixr.jo.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.flixr.jo.R;
import com.flixr.jo.databinding.ActivityLogBinding;
import com.flixr.jo.enums.StateServer;
import com.flixr.jo.tool.Tools;

public class LogActivity extends AppCompatActivity {

    BaseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLogBinding binding = ActivityLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (viewModel == null)
            viewModel = SplashActivity.viewModel;

        viewModel.stateLog.observe(LogActivity.this, integer -> {
            if (integer == StateServer.INVALID.ordinal()) {
                binding.LogImgStateLog.setImageResource(R.drawable.ic_link_invald);
                binding.LogTvStateLog.setText(R.string.Text_link_invald);
            } else if (integer == StateServer.ERROR.ordinal()) {
                binding.LogImgStateLog.setImageResource(R.drawable.ic_error);
                binding.LogTvStateLog.setText(R.string.Text_error);
            } else if (integer == StateServer.EXCEPTION.ordinal()) {
                binding.LogImgStateLog.setImageResource(R.drawable.ic_server_exception);
                binding.LogTvStateLog.setText(R.string.Text_server_excepiton);
            } else if (integer == StateServer.UPDATE.ordinal()) {
                binding.LogImgStateLog.setImageResource(R.drawable.ic_update_app);
                binding.LogTvStateLog.setText(R.string.Text_update_app);
            } else if (integer == StateServer.MAINTENANCE.ordinal()) {
                binding.LogImgStateLog.setImageResource(R.drawable.ic_server_maintenance);
                binding.LogTvStateLog.setText(R.string.Text_server_maintenance);
            } else if (integer == StateServer.DOWN.ordinal()) {
                binding.LogImgStateLog.setImageResource(R.drawable.ic_server_down);
                binding.LogTvStateLog.setText(R.string.Text_server_down);
            }
        });

        binding.LogImgInstagram.setOnClickListener(v -> Tools.GoToURL(LogActivity.this, BaseViewModel.instagram));
        binding.LogImgTelegram.setOnClickListener(v -> Tools.GoToURL(LogActivity.this, BaseViewModel.telegram));
        binding.LogImgTiktok.setOnClickListener(v -> Tools.GoToURL(LogActivity.this, BaseViewModel.tiktok));
    }
}