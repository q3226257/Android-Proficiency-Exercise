package com.example.myapplication.present;


import com.example.myapplication.View.MainActivity;
import com.example.myapplication.View.view_i.IMainView;
import com.example.myapplication.mode.bean.DataEntity;
import com.example.myapplication.mode.data.RequestData;
import com.example.myapplication.present.present_i.IMainPresent;

import retrofit2.Call;

/**
 * Created by cfadmin on 2017/7/18.
 */
public class MainPresent extends BasePresenter<DataEntity> implements IMainPresent {

    private IMainView<DataEntity> mView;

    private DataEntity mAndroidData = new DataEntity();
    private DataEntity mWebData = new DataEntity();
    private DataEntity mIosData = new DataEntity();

    //当前页数
    private int mAndroidPage = 1;
    private int mWebPage = 1;
    private int mIosPage = 1;

    //正在请求的页数
    private int mLoadingAndroidPage = 1;
    private int mLoadingWebPage = 1;
    private int mLoadingIosPage = 1;

    public MainPresent(MainActivity mView) {
        super(mView);
        this.mView = mView;
    }

    @Override
    public void refreshData(@IMainPresent.TYPE String type) {
        switch (type) {
            case IMainPresent.ANDROID:
                mAndroidPage = 0;
                mLoadingAndroidPage = 1;
                break;
            case IMainPresent.IOS:
                mIosPage = 0;
                mLoadingIosPage = 1;
                break;
            case IMainPresent.WEB:
                mWebPage = 0;
                mLoadingWebPage = 1;
                break;
        }
        RequestData.getData(type, 1, this);
    }


    @Override
    public DataEntity getData(String type) {
        switch (type) {
            case IMainPresent.ANDROID:
                return mAndroidData;
            case IMainPresent.IOS:
                return mIosData;
            case IMainPresent.WEB:
                return mWebData;
        }
        throw new RuntimeException("error request data");
    }

    @Override
    public void nextPageData(@IMainPresent.TYPE String type) {
        switch (type) {
            case IMainPresent.ANDROID:
                if (mAndroidPage < mLoadingAndroidPage) return;
                ++mLoadingAndroidPage;
                RequestData.getData(type, mLoadingAndroidPage, this);
                break;
            case IMainPresent.IOS:
                if (mIosPage <  mLoadingIosPage) return;
                ++mLoadingIosPage;
                RequestData.getData(type, mLoadingIosPage, this);
                break;
            case IMainPresent.WEB:
                if (mWebPage <  mLoadingWebPage) return;
                ++mLoadingWebPage;
                RequestData.getData(type, mLoadingWebPage, this);
                break;
        }
    }

    @Override
    public void handleSuccess(String type, DataEntity dataEntity) {
        if (dataEntity == null) {
            return;
        }
        switch (type) {
            case IMainPresent.ANDROID:
                ++mAndroidPage;
                handSuccess(type,mAndroidPage, mAndroidData, dataEntity);
                break;
            case IMainPresent.IOS:
                ++mIosPage;
                handSuccess(type,mIosPage, mIosData, dataEntity);
                break;
            case IMainPresent.WEB:
                ++mWebPage;
                handSuccess(type,mWebPage, mWebData, dataEntity);
                break;
        }

    }

    private void handSuccess(String type,int page, DataEntity dataEntity, DataEntity newDataEntity) {
        if (page == 1) {
            dataEntity.refreshData(newDataEntity);
        } else {
            dataEntity.injectData(newDataEntity);
        }
        mView.refreshView(type,dataEntity,newDataEntity);
    }

    @Override
    public void handleFail(String type, Call<DataEntity> call, Throwable t) {
        mBaseView.showError(type);
        switch (type) {
            case IMainPresent.ANDROID:
                mLoadingAndroidPage = mAndroidPage;
                break;
            case IMainPresent.IOS:
                mLoadingIosPage = mIosPage;
                break;
            case IMainPresent.WEB:
                mLoadingWebPage = mWebPage;
                break;
        }
    }
}
