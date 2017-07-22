package com.example.myapplication.present;

import com.example.myapplication.View.view_i.IBaseView;
import com.example.myapplication.mode.http.HttpHelper;
import com.example.myapplication.present.present_i.IBasePresent;

/**
 * Created by cfadmin on 2017/7/22.
 */
public abstract class BasePresenter<T> implements IBasePresent<T>{
    protected IBaseView mBaseView;

    public BasePresenter(IBaseView mBaseView) {
        this.mBaseView = mBaseView;
    }

    @Override
    public void onDestroy() {
        HttpHelper.getInstance().cancel(this);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
