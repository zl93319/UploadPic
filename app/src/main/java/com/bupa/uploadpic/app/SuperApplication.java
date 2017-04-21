package com.bupa.uploadpic.app;

import android.app.Application;
import android.graphics.Color;

import com.bupa.uploadpic.R;
import com.bupa.uploadpic.util.UIUtils;
import com.bupa.uploadpic.view.GlideImageLoader;
import com.bupa.uploadpic.view.GlidePauseOnScrollListener;
import com.bupa.uploadpic.view.MyToast;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;


public class SuperApplication extends Application {


    @Override
    public void onCreate() {
        UIUtils.init(this);
        MyToast.init(getApplicationContext(),true,true);
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), new GlideImageLoader(), new ThemeConfig.Builder()
                .setTitleBarBgColor(R.color.get_record_text_selected_color)
                .setTitleBarTextColor(Color.BLACK)
                .setTitleBarIconColor(Color.BLACK)
                .setFabNornalColor(Color.RED)
                .setFabPressedColor(Color.BLUE)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.BLACK)
                .setIconBack(R.mipmap.ic_action_previous_eitem)
                .setIconRotate(R.mipmap.ic_action_erepeat)
                .setIconCrop(R.mipmap.ic_action_ecrop)
                .setIconCamera(R.mipmap.ic_action_ecamera)
                .build())
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }
}
