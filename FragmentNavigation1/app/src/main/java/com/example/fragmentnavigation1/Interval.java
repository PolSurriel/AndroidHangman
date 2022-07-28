package com.example.fragmentnavigation1;

import android.os.Looper;
import android.util.Log;
import android.widget.Button;

public class Interval {

    private Runnable method;
    private int intervalTimeMilliseconds;
    private boolean isRunning = false;

    private Runnable loopMethod = new Runnable() {
        @Override
        public void run() {
            if(!isRunning){
                return;
            }

            method.run();
            new android.os.Handler(Looper.getMainLooper()).postDelayed(loopMethod, intervalTimeMilliseconds);
        }
    };

    Interval(Runnable method, int intervalTimeMilliseconds){
        this.method = method;
        this.intervalTimeMilliseconds = intervalTimeMilliseconds;
    }

    public void startInterval() {
        isRunning = true;
        loopMethod.run();
    }

    public void stopInterval() {
        isRunning = false;
    }

}
