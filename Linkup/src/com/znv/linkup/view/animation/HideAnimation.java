package com.znv.linkup.view.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 动画开始时显示，动画结束时隐藏
 * 
 * @author yzb
 * 
 */
public class HideAnimation implements AnimationListener {

    private View view = null;

    public HideAnimation(View view) {
        this.view = view;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
        view.setVisibility(View.VISIBLE);
    }

}
