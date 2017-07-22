package com.example.myapplication.mode.mode_i;

/**
 * Created by cfadmin on 2017/7/20.
 */
public interface IDataOption<T> {
    //将数据添加
    void injectData(T data);
    //刷新数据
    void refreshData(T data);
}
