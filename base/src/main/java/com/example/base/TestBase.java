package com.example.base;

import android.util.Log;

/**
 * Created by paul on 16/6/20.
 */
public class TestBase {
    private static String defaultParam = "default";
    public static void setParam(String param){
        defaultParam = param;
    }
    public static String getDefaultParam() {

        return defaultParam;
    }
}
