package com.example.myapplication.View.view_i;


/**
 * 定义从 fragment 向 activity 发送指令的接口
 */
public interface IBaseA2F<T> {
    void refreshView(T modifiedData,T modifyData);
}
