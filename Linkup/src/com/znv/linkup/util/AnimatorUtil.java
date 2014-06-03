package com.znv.linkup.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.znv.linkup.view.animation.HideAnimator;

/**
 * 动画辅助类，包括褪色，缩放，旋转和平移
 * 
 * @author yzb
 * 
 */
public class AnimatorUtil {

    private static int defaultDuration = 500;

    /**
     * 设置view的透明度
     * 
     * @param view
     *            参与动画的view
     * @param fromAlpha
     *            起始透明度
     * @param toAlpha
     *            结束透明度
     */
    public static void animAlpha(View view, float fromAlpha, float toAlpha) {
        animAlpha(view, fromAlpha, toAlpha, defaultDuration);
    }

    /**
     * 设置view的透明度
     * 
     * @param view
     *            参与动画的view
     * @param fromAlpha
     *            起始透明度
     * @param toAlpha
     *            结束透明度
     * @param duration
     *            动画时长
     */
    public static void animAlpha(View view, float fromAlpha, float toAlpha, int duration) {
        ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha).setDuration(duration).start();
    }

    /**
     * 设置view的透明度
     * 
     * @param view
     *            参与动画的view
     * @param fromAlpha
     *            起始透明度
     * @param toAlpha
     *            结束透明度
     * @param duration
     *            动画时长
     * @param delay
     *            动画延迟
     */
    public static void animAlpha(View view, float fromAlpha, float toAlpha, int duration, int delay) {
        Animator anim = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha).setDuration(duration);
        anim.setStartDelay(delay);
        anim.start();
    }

    /**
     * 缩放动画
     * 
     * @param view
     *            执行动画的view
     * @param fromX
     *            X开始的比例
     * @param toX
     *            X缩放比例
     * @param fromY
     *            Y开始的比例
     * @param toY
     *            Y缩放比例
     */
    public static void animScale(View view, float fromX, float toX, float fromY, float toY) {
        animScale(view, fromX, toX, fromY, toY, defaultDuration);
    }

    /**
     * 缩放动画
     * 
     * @param view
     *            执行动画的view
     * @param fromX
     *            X开始的比例
     * @param toX
     *            X缩放比例
     * @param fromY
     *            Y开始的比例
     * @param toY
     *            Y缩放比例
     * @param duration
     *            动画时长
     */
    public static void animScale(View view, float fromX, float toX, float fromY, float toY, int duration) {
        ObjectAnimator.ofFloat(view, "scaleX", fromX, toX).setDuration(duration).start();
        ObjectAnimator.ofFloat(view, "scaleY", fromY, toY).setDuration(duration).start();
    }

    /**
     * 旋转动画
     * 
     * @param view
     *            执行动画的view
     * @param fromDegrees
     *            起始角度
     * @param toDegrees
     *            结束角度
     */
    public static void animRotate(View view, float fromDegrees, float toDegrees) {
        animRotate(view, fromDegrees, toDegrees, defaultDuration);
    }

    /**
     * 旋转动画
     * 
     * @param view
     *            执行动画的view
     * @param fromDegrees
     *            起始角度
     * @param toDegrees
     *            结束角度
     * @param duration
     *            动画时长
     */
    public static void animRotate(View view, float fromDegrees, float toDegrees, int duration) {
        ObjectAnimator.ofFloat(view, "rotation", fromDegrees, toDegrees).setDuration(duration).start();
    }

    /**
     * 平移动画
     * 
     * @param view
     *            执行动画的view
     * @param fromX
     *            起始X坐标
     * @param toX
     *            结束X坐标
     * @param fromY
     *            起始Y坐标
     * @param toY
     *            结束Y坐标
     */
    public static void animTranslate(View view, float fromX, float toX, float fromY, float toY) {
        animTranslate(view, fromX, toX, fromY, toY, defaultDuration, false);
    }

    /**
     * 平移动画
     * 
     * @param view
     *            执行动画的view
     * @param fromX
     *            起始X坐标
     * @param toX
     *            结束X坐标
     * @param fromY
     *            起始Y坐标
     * @param toY
     *            结束Y坐标
     * @param duration
     *            动画时长
     */
    public static void animTranslate(View view, float fromX, float toX, float fromY, float toY, int duration, boolean isHide) {
        Animator animX = ObjectAnimator.ofFloat(view, "translationX", fromX, toX).setDuration(duration);
        Animator animY = ObjectAnimator.ofFloat(view, "translationY", fromY, toY).setDuration(duration);

        animX.start();
        if (isHide) {
            animY.addListener(new HideAnimator(view));
            animY.start();
        }
    }

}
