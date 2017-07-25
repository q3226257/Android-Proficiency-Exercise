package com.example.myapplication.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by cfadmin on 2017/7/19.
 */
public class BaseApplication extends Application {
    static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }
}
