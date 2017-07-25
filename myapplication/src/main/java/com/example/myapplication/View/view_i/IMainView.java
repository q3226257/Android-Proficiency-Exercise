package com.example.myapplication.view.view_i;

import com.example.myapplication.present.present_i.IMainPresent;

/**
 * Created by cfadmin on 2017/7/18.
 */
public interface IMainView<T>{
    void refreshView(@IMainPresent.TYPE String type,T modifiedData,T modifyData);
}
