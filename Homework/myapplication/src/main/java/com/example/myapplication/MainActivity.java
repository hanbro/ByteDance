package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);

        startActivity(new Intent(MainActivity.this, page1.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                findViewById(R.id.imageView),
                "rotation", 0, 360);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(8000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewPagerActivity.class));
            }
        });
//        ImageView rocketImage = (ImageView) findViewById(R.id.rocket_image); 
//        rocketImage.setBackgroundResource(R.drawable.rocket); 
//        AnimationDrawable rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
//        rocketAnimation.start();
    }
}
