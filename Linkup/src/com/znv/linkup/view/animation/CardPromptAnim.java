package com.znv.linkup.view.animation;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * 卡片提示动画效果
 * 
 * @author yzb
 * 
 */
public class CardPromptAnim extends Animation {

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        setDuration(3000);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        t.setAlpha((float) Math.abs(Math.sin(interpolatedTime * 4 * Math.PI)));
        super.applyTransformation(interpolatedTime, t);
    }

}
