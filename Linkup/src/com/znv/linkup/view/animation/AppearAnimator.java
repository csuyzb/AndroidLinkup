package com.znv.linkup.view.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.view.View;

/**
 * 动画开始时显示，动画结束时隐藏
 * 
 * @author yzb
 * 
 */
public class AppearAnimator implements AnimatorListener {

    private View view = null;

    public AppearAnimator(View view) {
        this.view = view;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    @Override
    public void onAnimationStart(Animator animation) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

}
