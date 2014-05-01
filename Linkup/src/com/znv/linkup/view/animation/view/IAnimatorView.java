package com.znv.linkup.view.animation.view;

import com.znv.linkup.view.animation.path.PathPoint;

/**
 * 视图动画接口
 * 
 * @author yzb
 * 
 */
public interface IAnimatorView {
    /**
     * 设置定位点
     * 
     * @param pp
     *            路径点
     */
    void setLocation(PathPoint pp);

    /**
     * 设置透明度
     * 
     * @param alpha
     *            alpha值，0~1f
     */
    void setAlpha(float alpha);
}
