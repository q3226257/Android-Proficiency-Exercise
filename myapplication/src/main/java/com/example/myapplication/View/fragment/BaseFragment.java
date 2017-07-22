package com.example.myapplication.View.fragment;

import android.support.v4.app.Fragment;

import com.example.myapplication.View.view_i.IBaseA2F;

/**
 * Created by cfadmin on 2017/7/20.
 */
public abstract class BaseFragment<T> extends Fragment implements IBaseA2F<T> {
    public String mFragmentTitle = "默认";
}
