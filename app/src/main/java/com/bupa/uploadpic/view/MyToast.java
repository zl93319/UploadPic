package com.bupa.uploadpic.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class MyToast {
    private static Context context;
    private static boolean isDebug;

    /**
     *
     * @param context applicationcontext
     * @param isDebug 是测试环境还是正式环境
     * @param showInCenter 显示在什么地方.默认在底部,可以设置为屏幕中央.全局起作用
     */
    public static void init(Context context,boolean isDebug,boolean showInCenter){
        MyToast.context = context;
        MyToast.isDebug = isDebug;
        if(showInCenter){
            Toasty.gravty = Gravity.CENTER;
        }
    }

    public static void success(CharSequence text){
        Toasty.success(context, text, Toast.LENGTH_SHORT, true).show();
    }

    public static void error(CharSequence text){
        Toasty.error(context, text, Toast.LENGTH_SHORT, true).show();
    }

    public static void info(CharSequence text){
        Toasty.info(context, text, Toast.LENGTH_SHORT, true).show();
    }
    public static void warn(CharSequence text){
        Toasty.warning(context, text, Toast.LENGTH_SHORT, true).show();
    }
    public static void show(CharSequence text){
        Toasty.normal(context, text, Toast.LENGTH_SHORT).show();
    }
    public static void show(CharSequence text , int resId){
        Toasty.normal(context, text, context.getResources().getDrawable(resId)).show();
    }



    public static void debug(CharSequence text) {
        if(isDebug){
            show(text);
        }

    }
}
