package com.example.myapplication.present.present_i;

import android.support.annotation.StringDef;

import com.example.myapplication.mode.bean.DataEntity;

/**
 * Created by cfadmin on 2017/7/22.
 */
public interface IMainPresent {
    String ANDROID = "Android";
    String IOS = "iOS";
    String WEB = "前端";

    @StringDef({ANDROID, IOS, WEB})
    @interface TYPE {

    }

    //重新从网络获取数据
    void refreshData(@IMainPresent.TYPE String type);

    //直接提取内存数据
    DataEntity getData(String type);

    //从网络获取下一批数据
    void nextPageData(@IMainPresent.TYPE String type);
}
