package com.example.myapplication.Base.view;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.View.view_i.IBaseView;
import com.example.myapplication.present.present_i.IBasePresent;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAblMain;
    private Toolbar mTbMainBar;
    private IBasePresent mBasePresent;
    private ViewGroup mContentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        mBasePresent = getBasePresent();
    }

    protected abstract void initData();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base);
        LayoutInflater.from(this).inflate(layoutResID, (ViewGroup) findViewById(R.id.coordinatorLayout));
        initView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(R.layout.activity_base);
        mContentContainerView = ((ViewGroup) findViewById(R.id.coordinatorLayout));
        mContentContainerView.addView(view);
        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(R.layout.activity_base);
        mContentContainerView = ((ViewGroup) findViewById(R.id.coordinatorLayout));
        mContentContainerView.addView(view, params);
        initView();
    }

    protected void initView() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mAblMain = (AppBarLayout) findViewById(R.id.abl_main);
        mTbMainBar = (Toolbar) findViewById(R.id.tb_main_bar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        setSupportActionBar(mTbMainBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.base_bar_, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    public void addBarLayout(View view) {
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(-1, -2);
        mAblMain.addView(view, params);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (mBasePresent != null) {
            mBasePresent.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mBasePresent != null) {
            mBasePresent.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBasePresent != null) {
            mBasePresent.onResume();
        }
        super.onResume();
    }

    public abstract IBasePresent getBasePresent();
}
