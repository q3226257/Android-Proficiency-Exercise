package com.example.myapplication.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.base.view.BaseActivity;
import com.example.myapplication.present.present_i.IBasePresent;
import com.example.myapplication.util.NetUtil;

import java.util.Locale;
import java.util.regex.Pattern;

public class DetailActivity extends BaseActivity {
    private static final String KEY_URL_ = "com.example.myapplication.View.KEY_URL_";
    private static final String PATTERN_URL =
            "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";

    private WebView mWbDetail;
    private RelativeLayout mLlDetailRoot;
    private ProgressBar mPromptProgress;

    private String mUrl;

    public static void newInstance(String url, Context context){
        if (checkUrl(url)) return;

        Intent intent = new Intent(context,DetailActivity.class);
        intent.putExtra(KEY_URL_,url);
        context.startActivity(intent);
    }

    private static boolean checkUrl(String url) {
        if(url ==null|| url.isEmpty()){
            return true;
        }
        Pattern pattern = Pattern.compile(PATTERN_URL);
        if (!pattern.matcher(url).matches()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getStringExtra(KEY_URL_);
        setContentView(R.layout.activity_detail);

    }

    @Override
    protected void initView() {
        super.initView();
        mWbDetail = (WebView) findViewById(R.id.wb_detail_);
        mLlDetailRoot = (RelativeLayout) findViewById(R.id.ll_detail_root);
        mPromptProgress = (ProgressBar) findViewById(R.id.pb_prompt);

        WebSettings settings = mWbDetail.getSettings();
        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        if (NetUtil.isNetAvailable()) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        mWbDetail.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mPromptProgress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPromptProgress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                showToast(String.format(Locale.getDefault(),"加载出错: %s",error.toString()));
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showToast(String.format(Locale.getDefault(),"加载出错: %s 代码 %d",description,errorCode));
            }

        });

        mWbDetail.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mPromptProgress.setProgress(newProgress);

            }
        });

        mWbDetail.loadUrl(mUrl);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onPause() {
        mWbDetail.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mWbDetail.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLlDetailRoot.removeAllViews();//将webview 先移除 再销毁
        mWbDetail.destroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(mWbDetail.canGoBack()){
            mWbDetail.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public IBasePresent getBasePresent() {
        return null;
    }

    @Override
    public void showLoading(String type) {

    }

    @Override
    public void showError(String type) {

    }
}
