package com.znv.linkup.view.animation.path;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.znv.linkup.R;

/**
 * 收集金币的动画
 * 
 * @author yzb
 * 
 */
public class ViewPathAnimator implements AnimatorListener {

    private AnimatorView view = null;

//    private static FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    public ViewPathAnimator(AnimatorView view) {
//        view = new AnimatorView(root.getContext());
//        view.setImageResource(R.drawable.coin);
//        view.setLayoutParams(params);
//        view.setAlpha(0f);
//        root.addView(view);
        this.view = view;
    }

    public void animatePath(Point start, Point end) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
        ObjectAnimator anim = ObjectAnimator.ofObject(view, "location", new PathEvaluator(), path.getPoints().toArray());
        anim.addListener(this);
        anim.setDuration(400);
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

}
