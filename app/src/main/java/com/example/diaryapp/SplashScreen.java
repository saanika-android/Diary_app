package com.example.diaryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
Animation middleanimation;
TextView diary;
Handler handler;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        middleanimation= AnimationUtils.loadAnimation(this,R.anim.middleanimation);
        diary=findViewById(R.id.diary);
        diary.setAnimation(middleanimation);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                        Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                }
        },4000);

        }
        }