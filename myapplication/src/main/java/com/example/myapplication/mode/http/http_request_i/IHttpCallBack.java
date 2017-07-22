package com.example.myapplication.mode.http.http_request_i;

import com.example.myapplication.present.present_i.IMainPresent;

import retrofit2.Call;

public interface IHttpCallBack<T> {

  void handleSuccess(@IMainPresent.TYPE String type, T t);
  void handleFail(@IMainPresent.TYPE String type, Call<T> call, Throwable t);
}