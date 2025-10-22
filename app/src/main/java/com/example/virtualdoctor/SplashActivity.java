package com.example.virtualdoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.MediaController;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private VideoView splashVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashVideo = findViewById(R.id.splashVideoView);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video);
        splashVideo.setVideoURI(videoUri);
        splashVideo.setOnCompletionListener(mp -> {
            // Go to MainActivity after video finishes
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });

        splashVideo.start();
    }
}

