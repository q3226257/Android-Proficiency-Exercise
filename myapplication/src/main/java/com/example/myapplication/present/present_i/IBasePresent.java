package com.example.myapplication.present.present_i;

import com.example.myapplication.mode.http.http_request_i.IHttpCallBack;

/**
 * Created by cfadmin on 2017/7/18.
 */
public interface IBasePresent<T> extends IHttpCallBack<T> {

    void onDestroy();
    void onPause();
    void onResume();
}
