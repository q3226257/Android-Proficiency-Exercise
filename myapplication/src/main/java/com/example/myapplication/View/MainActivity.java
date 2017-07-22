package com.example.myapplication.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewStub;

import com.example.myapplication.R;
import com.example.myapplication.View.fragment.BaseDataFragment;
import com.example.myapplication.View.fragment.BaseFragment;
import com.example.myapplication.View.view_i.IBaseA2F;
import com.example.myapplication.View.view_i.IBaseF2A;
import com.example.myapplication.View.view_i.IMainView;
import com.example.myapplication.base.view.BaseActivity;
import com.example.myapplication.mode.bean.DataEntity;
import com.example.myapplication.present.MainPresent;
import com.example.myapplication.present.present_i.IBasePresent;
import com.example.myapplication.present.present_i.IMainPresent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements IMainView<DataEntity>, IBaseF2A {

    private ViewPager mVpMainContent;
    private TabLayout mTlMainNavigation;

    private MainPresent mPresent;

    private ProgressDialog mLoadingBar;

    private MainFragmentAdapter mViewPagerAdapter;
    private BaseDataFragment fragmentAndroid;
    private BaseDataFragment fragmentWeb;
    private BaseDataFragment fragmentIos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*初始化界面*/
    protected void initView() {
        super.initView();

        mVpMainContent = (ViewPager) findViewById(R.id.vp_main_content);

        mLoadingBar = new ProgressDialog(this);

        fragmentAndroid = BaseDataFragment.newInstance
                (mPresent.getData(IMainPresent.ANDROID),"ANDROID",IMainPresent.ANDROID);
        fragmentWeb = BaseDataFragment.newInstance
                (mPresent.getData(IMainPresent.WEB),"前端",IMainPresent.WEB);
        fragmentIos = BaseDataFragment.newInstance
                (mPresent.getData(IMainPresent.IOS),"IOS",IMainPresent.IOS);

        List<BaseFragment> baseFragments = new ArrayList<>();
        baseFragments.add(fragmentAndroid);
        baseFragments.add(fragmentWeb);
        baseFragments.add(fragmentIos);
        mViewPagerAdapter = new MainFragmentAdapter(getSupportFragmentManager(), baseFragments);
        mVpMainContent.setAdapter(mViewPagerAdapter);
        mVpMainContent.setOffscreenPageLimit(3);

        //可以在适当的时候加载来和作为 viewPager 的tab
        ViewStub viewStub = (ViewStub) findViewById(R.id.vs_need_tab);
        mTlMainNavigation = (TabLayout) viewStub.inflate();
        mTlMainNavigation.setTabGravity(TabLayout.GRAVITY_FILL);
        mTlMainNavigation.setTabMode(TabLayout.MODE_FIXED);
        mTlMainNavigation.setupWithViewPager(mVpMainContent);
    }

    @Override
    public IBasePresent getBasePresent() {
        return mPresent;
    }


    /*初始化数据*/
    protected void initData() {
        mPresent = new MainPresent(this);

    }

    @Override
    public void showLoading(String type) {
        if (!mLoadingBar.isShowing()) {
            mLoadingBar.show();
        }
        switch (type) {
            case IMainPresent.ANDROID:
                fragmentAndroid.showLoading(type);
                break;
            case IMainPresent.IOS:
                fragmentIos.showLoading(type);
                break;
            case IMainPresent.WEB:
                fragmentWeb.showLoading(type);
                break;
        }
    }

    @Override
    public void showError(String type) {
        if (mLoadingBar.isShowing()) {
            mLoadingBar.cancel();
        }
        switch (type) {
            case IMainPresent.ANDROID:
                fragmentAndroid.showError(type);
                break;
            case IMainPresent.IOS:
                fragmentIos.showError(type);
                break;
            case IMainPresent.WEB:
                fragmentWeb.showError(type);
                break;
        }
        showToast(getString(R.string.request_failed));
    }

    @Override
    public void refreshData(String type) {
        mPresent.refreshData(type);
    }

    @Override
    public void nextPageData(String type) {
        mPresent.nextPageData(type);

    }

    @Override
    public void refreshView(@IMainPresent.TYPE String type, DataEntity modifiedData, DataEntity modifyData) {
        IBaseA2F<DataEntity> iBaseA2F = null;
        switch (type) {
            case IMainPresent.ANDROID:
                iBaseA2F = fragmentAndroid;
                break;
            case IMainPresent.IOS:
                iBaseA2F = fragmentIos;
                break;
            case IMainPresent.WEB:
                iBaseA2F = fragmentWeb;
                break;
        }
        refreshChildView(iBaseA2F,modifiedData, modifyData);

    }

    private void refreshChildView(IBaseA2F<DataEntity> iBaseA2F,DataEntity modifiedData, DataEntity modifyData) {
        if(iBaseA2F == null)return;

        iBaseA2F.refreshView(modifiedData,modifyData);
    }

    static class MainFragmentAdapter extends FragmentPagerAdapter {
        List<BaseFragment> items;

        public MainFragmentAdapter(FragmentManager fm, List<BaseFragment> items) {
            super(fm);
            this.items = items;
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items.get(position).mFragmentTitle;
        }
    }

}
