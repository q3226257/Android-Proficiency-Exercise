package com.example.myapplication.mode.http.http_request_i;

import com.example.myapplication.mode.bean.DataEntity;
import com.example.myapplication.present.present_i.IMainPresent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 请求数据接口
 */
public interface DataService {
    int DEFAULT_COUNT = 10;
    @GET("data/{type}/"+ DEFAULT_COUNT +"/{page}")
    Call<DataEntity> getData(@IMainPresent.TYPE @Path("type") String type, @Path("page") int page);
}
