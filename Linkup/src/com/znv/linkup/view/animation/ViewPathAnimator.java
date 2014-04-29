package com.znv.linkup.view.animation;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.graphics.Point;

import com.znv.linkup.view.animation.path.AnimatorPath;
import com.znv.linkup.view.animation.path.PathEvaluator;
import com.znv.linkup.view.animation.view.IAnimatorView;

/**
 * 收集金币的动画
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

    public void animatePath(Point start, Point end) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
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
