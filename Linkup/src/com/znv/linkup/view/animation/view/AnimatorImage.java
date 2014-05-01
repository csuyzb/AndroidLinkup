package com.znv.linkup.view.animation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.znv.linkup.view.animation.AnimatorProxy;
import com.znv.linkup.view.animation.path.PathPoint;

/**
 * 图片动画视图
 * 
 * @author yzb
 * 
 */
public class AnimatorImage extends ImageView implements IAnimatorView {

    private AnimatorProxy proxy = null;

    public AnimatorImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        proxy = AnimatorProxy.wrap(this);
    }

    /**
     * 设置定位路径点
     * 
     * @param pp
     *            路径点
     */
    public void setLocation(PathPoint pp) {
        proxy.setTranslationX(pp.mX);
        proxy.setTranslationY(pp.mY);
    }

}
