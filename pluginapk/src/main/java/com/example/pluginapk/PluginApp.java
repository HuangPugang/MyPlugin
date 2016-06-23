package com.example.pluginapk;

import android.app.Application;
import android.util.Log;

/**
 * Created by paul on 16/6/20.
 */
public class PluginApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("PluginApp","onCreate()");
    }
}
