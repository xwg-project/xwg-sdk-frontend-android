package com.example.sdk.floatwindows.runner;


import android.content.Context;

public interface ICarrier {
    Context getContext();

    void onMove(int lastX, int lastY, int curX, int curY);

    void onDone();

    boolean post(Runnable runnable);

    boolean removeCallbacks(Runnable action);
}