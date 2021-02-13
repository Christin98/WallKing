package com.kcdeveloperss.wallpapers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ImageView app_logo;
    TextView app_slogan;
    TextView app_powered_by1;
    TextView app_powered_by2;

    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setUpWindowAnimation();

        app_logo = findViewById(R.id.app_logo);
        app_slogan = findViewById(R.id.app_slogan);
        app_powered_by1 = findViewById(R.id.app_powered_by1);
        app_powered_by2 = findViewById(R.id.app_powered_by2);
        progressBar = findViewById(R.id.progressBar);

        Animation topAnimation = AnimationUtils.loadAnimation(this, R.anim.start_top_animation);
        Animation bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.start_bottom_animation);

        progressBar.setVisibility(View.INVISIBLE);

        app_logo.startAnimation(topAnimation);
        app_slogan.startAnimation(bottomAnimation);
        app_powered_by1.startAnimation(bottomAnimation);
        app_powered_by2.startAnimation(bottomAnimation);

        new Handler().postDelayed(() -> progressBar.setVisibility(View.VISIBLE),2000);

        new Handler().postDelayed(this::enterMain, 5000);

    }

    private void enterMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpWindowAnimation() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }
}