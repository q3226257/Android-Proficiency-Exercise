package com.example.myapplication.mode.http;

import com.example.myapplication.base.BaseApplication;
import com.example.myapplication.base.Constant.AppUrls;
import com.example.myapplication.mode.bean.DataEntity;
import com.example.myapplication.mode.http.http_request_i.DataService;
import com.example.myapplication.mode.http.http_request_i.IHttpCallBack;
import com.example.myapplication.present.present_i.IMainPresent;
import com.example.myapplication.util.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cfadmin on 2017/7/18.
 */
public class HttpHelper {
    private static volatile HttpHelper sInstance;
    private final Retrofit mRetrofit;
    //用来取消请求 防止内存泄露
    private WeakHashMap<Object, List<Call>> mCallMap = new WeakHashMap<>();

    private HttpHelper() {
        File cacheDir = BaseApplication.getContext().getCacheDir();
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
        Cache cache = new Cache(cacheDir,1024*1024*50);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(new AppInterceptor())
                .addNetworkInterceptor(new NetWorkInterceptor())
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(AppUrls.BASE_URL)
                .addConverterFactory(BaseConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static HttpHelper getInstance() {
        if (sInstance == null) {
            synchronized (HttpHelper.class) {
                if (sInstance == null) {
                    sInstance = new HttpHelper();
                }
            }
        }
        return sInstance;
    }

    public void getData(@IMainPresent.TYPE final String type, int page, final IHttpCallBack<DataEntity> callBack) {
        DataService dataService = mRetrofit.create(DataService.class);
        Call<DataEntity> data = dataService.getData(type, page);
        Callback<DataEntity> callback = new CustomCallBack<DataEntity>(type, callBack, data);
        data.enqueue(callback);
    }

    private void addCallRequest(Object o, Call<?> call) {
        List<Call> calls = mCallMap.get(o);
        if (calls == null) {
            calls = new ArrayList<>();
            mCallMap.put(o, calls);
        }
        calls.add(call);
    }

    public void cancel(Object key) {
        List<Call> calls = mCallMap.get(key);
        if (calls != null) {
            for (Call call : calls) {
                call.cancel();
            }
            calls.clear();
        }
    }

    private void removeCall(Object key, Call<?> call) {
        List<Call> calls = mCallMap.get(key);
        if (calls != null) {
            calls.remove(call);
            call.cancel();
        }
    }

    public void cancelAll() {
        Collection<List<Call>> values = mCallMap.values();
        for (List<Call> value : values) {
            for (Call call : value) {
                call.cancel();
            }
            mCallMap.clear();
        }
    }

    class CustomCallBack<T> implements Callback<T> {
        IHttpCallBack<T> callBack;
        Call<T> call;
        String type;

        public CustomCallBack(String type, IHttpCallBack<T> callBack, Call<T> call) {
            this.callBack = callBack;
            this.call = call;
            this.type = type;
            addCallRequest(callBack, call);
        }


        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (call.isCanceled()) return;
            T body = response.body();
            if (body != null) {
                callBack.handleSuccess(type, body);
            } else {
                callBack.handleFail(type, call, null);
            }
            removeCall(callBack, call);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (call.isCanceled()) return;
            callBack.handleFail(type, call, t);
            removeCall(callBack, call);
        }
    }


    static class NetWorkInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            if (NetUtil.isNetAvailable()) {
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();

            } else {
                int maxStale = 60 * 60 * 1;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;

        }
    }

    static class AppInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            return chain.proceed(request);
        }
    }
}
