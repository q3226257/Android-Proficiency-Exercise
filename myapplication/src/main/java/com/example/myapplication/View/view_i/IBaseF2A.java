package com.example.myapplication.View.view_i;

import com.example.myapplication.present.present_i.IMainPresent;

/**
 * 定义从 fragment 向 activity 发送指令的接口
 */
public interface IBaseF2A {
    void refreshData(@IMainPresent.TYPE String type);
    void nextPageData(@IMainPresent.TYPE String type);
}
