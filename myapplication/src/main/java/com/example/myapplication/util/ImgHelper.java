package com.example.myapplication.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;


/**
 * Created by cfadmin on 2017/7/21.
 */
public class ImgHelper {
    private static final int ERROR_IMG = R.mipmap.ic_launcher;

    /**
     * 对 glide 简单的做一层封装
     * @param context
     * @param errorImg
     * @param url
     * @param imgeView
     */
    public static void showImageIntoView( String url,
                                     ImageView imgeView) {
        Glide.with(imgeView.getContext())
                .load(url)// 加载图片
                .error(ERROR_IMG)// 设置错误图片
                .crossFade()// 设置淡入淡出效果，默认300ms，可以传参
                .placeholder(ERROR_IMG)// 设置占位图
                .into(imgeView);

    }
}
