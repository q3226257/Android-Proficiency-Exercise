package com.example.myapplication.mode.data;

import com.example.myapplication.mode.bean.DataEntity;
import com.example.myapplication.mode.http.HttpHelper;
import com.example.myapplication.mode.http.http_request_i.IHttpCallBack;
import com.example.myapplication.present.present_i.IMainPresent;

/**
 * 获取数据的交互类
 */
public class RequestData {


    public static void getData(@IMainPresent.TYPE String type, int page , IHttpCallBack<DataEntity> callBack) {
        HttpHelper.getInstance().getData(type,page,callBack);
    }
}
