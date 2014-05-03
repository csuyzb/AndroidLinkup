package com.znv.linkup.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 全局Toast的帮助类
 * 
 * @author yzb
 * 
 */
@SuppressLint("ShowToast")
public class ToastUtil {

    /**
     * 根据字符串资源id获取toast
     * 
     * @param ctx
     *            上下文
     * @param resId
     *            资源id
     * @return Toast对象
     */
    public static Toast getToast(Context ctx, int resId) {
        String msg = ctx.getResources().getString(resId);
        return getToast(ctx, msg);
    }

    /**
     * 根据字符串获取toast
     * 
     * @param ctx
     *            上下文
     * @param msg
     *            字符串
     * @return Toast对象
     */
    public static Toast getToast(Context ctx, String msg) {
        Toast mToast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        mToast.setText(msg);
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 20);
        mToast.setDuration(Toast.LENGTH_SHORT);
        return mToast;
    }
}
