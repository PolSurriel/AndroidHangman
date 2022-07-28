package com.example.fragmentnavigation1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

    }

    int animationTime = 0;
    Interval test;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startAnimation(){

        animationTime = 0;
        test = new Interval(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                float t = Math.min(((float)animationTime) /200f, 1f);

                findViewById(R.id.root).setBackgroundColor(Color.rgb(
                    255f * (1f-t),255f * t,0f
                ));
                animationTime += 30;

                if(animationTime > 200){
                    test.stopInterval();

                }
            }
        }, 30);

        test.startInterval();
    }
}