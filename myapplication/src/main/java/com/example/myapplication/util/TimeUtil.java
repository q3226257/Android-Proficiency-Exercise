package com.example.myapplication.util;

/**
 * Created by cfadmin on 2017/7/21.
 */
public class TimeUtil {

    public static String formatTime(String timeStr) {
        String s = timeStr.split("\\.")[0];
        return s.replaceAll("T", " ");
    }
}
