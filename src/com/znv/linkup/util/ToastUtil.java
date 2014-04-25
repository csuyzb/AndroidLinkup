package com.znv.linkup.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.znv.linkup.R;

@SuppressLint("ShowToast")
public class ToastUtil {

    public static Toast getToast(Context ctx, int resId) {
        String msg = ctx.getResources().getString(resId);
        return getToast(ctx, msg);
    }

    /**
     * 显示Toast
     */
    public static Toast getToast(Context ctx, String msg) {
        Toast mToast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        mToast.setText(msg);
        setToastProperty(mToast);
        return mToast;
    }

    /**
     * 显示自定义Toast
     * 
     * @param toastView
     */
    public static Toast getToast(Context ctx, View toastView, int resId) {
        String msg = ctx.getResources().getString(resId);
        return getToast(ctx, toastView, msg);
    }

    /**
     * 显示自定义Toast
     * 
     * @param toastView
     */
    public static Toast getToast(Context ctx, View toastView, String msg) {
        TextView message = (TextView) toastView.findViewById(R.id.toast_message);
        if (message != null) {
            message.setText(msg);
        }

        Toast mToast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        mToast.setView(toastView);

        setToastProperty(mToast);
        return mToast;
    }

    private static void setToastProperty(Toast mToast) {
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 20);
        mToast.setDuration(Toast.LENGTH_SHORT);
    }

}
