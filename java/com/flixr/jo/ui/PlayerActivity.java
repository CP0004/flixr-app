package com.flixr.jo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.os.Bundle;

import com.flixr.jo.databinding.ActivityPlayerBinding;
import com.flixr.jo.tool.Value;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

public class PlayerActivity extends AppCompatActivity {
    ActivityPlayerBinding binding;
    private ExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());


        Intent intent = getIntent();
        PlayContent(intent.getStringExtra(Value.APP_REQUEST_DATA_URL));

    }

    public void PlayContent(String url) {
        exoPlayer = new ExoPlayer.Builder(this).build();
        binding.PlayerPyVideo.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.stop();
    }

}