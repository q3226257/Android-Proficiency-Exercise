package com.example.myapplication.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.myapplication.Base.BaseApplication;

/**
 * Created by cfadmin on 2017/7/21.
 */
public class NetUtil {
    /**
     * 判断是否有网络连接
     * @return
     */
    public static boolean isNetAvailable() {
        Context context = BaseApplication.getContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }
}
