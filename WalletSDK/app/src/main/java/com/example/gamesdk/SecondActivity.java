package com.example.gamesdk;

import android.app.Activity;
import android.os.Bundle;

import org.jetbrains.annotations.Nullable;


/**
 * Created by Android Studio.
 * User: liujh
 * Date: 2019/11/13
 * Time: 11:17
 */
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
    }
}
