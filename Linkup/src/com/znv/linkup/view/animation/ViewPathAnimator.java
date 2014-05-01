package com.znv.linkup.view.animation;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.graphics.Point;

import com.znv.linkup.view.animation.path.AnimatorPath;
import com.znv.linkup.view.animation.path.PathEvaluator;
import com.znv.linkup.view.animation.view.IAnimatorView;

/**
 * 文字或者图片的动画
 * 
 * @author yzb
 * 
 */
public class ViewPathAnimator implements AnimatorListener {

    private IAnimatorView view = null;
    private int duration = 400;

    public ViewPathAnimator(IAnimatorView view) {
        this.view = view;
    }

    /**
     * 设置动画的起始点
     * 
     * @param start
     *            动画起点
     * @param end
     *            动画终点
     */
    public void animatePath(Point start, Point end) {
        List<Point> pathPoints = new ArrayList<Point>();
        pathPoints.add(start);
        pathPoints.add(end);
        animatePath(pathPoints);
    }

    /**
     * 设置动画的起始点
     * 
     * @param pathPoints
     *            路径点
     */
    public void animatePath(List<Point> pathPoints) {
        if (pathPoints == null || pathPoints.size() == 0) {
            return;
        }
        AnimatorPath path = new AnimatorPath();
        path.moveTo(pathPoints.get(0).x, pathPoints.get(0).y);
        for (int i = 1; i < pathPoints.size(); i++) {
            path.lineTo(pathPoints.get(i).x, pathPoints.get(i).y);
        }
        ObjectAnimator anim = ObjectAnimator.ofObject(view, "location", new PathEvaluator(), path.getPoints().toArray());
        anim.addListener(this);
        anim.setDuration(duration);
        anim.start();
    }

    @Override
    public void onAnimationCancel(Animator arg0) {
        view.setAlpha(0f);
    }

    @Override
    public void onAnimationEnd(Animator arg0) {
        view.setAlpha(0f);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationStart(Animator animation) {
        view.setAlpha(1f);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public IAnimatorView getView() {
        return view;
    }

}
