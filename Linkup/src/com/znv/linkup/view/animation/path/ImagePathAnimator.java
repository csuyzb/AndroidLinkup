package com.znv.linkup.view.animation.path;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.znv.linkup.R;

/**
 * 收集金币的动画
 * 
 * @author yzb
 * 
 */
public class ImagePathAnimator implements AnimatorListener {

    private AnimatorImage imageView = null;
    private ObjectAnimator anim = null;
    private static FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    public ImagePathAnimator(FrameLayout root) {
        imageView = new AnimatorImage(root.getContext());
        imageView.setImageResource(R.drawable.coin);
        imageView.setLayoutParams(params);
        imageView.setAlpha(0f);
        root.addView(imageView);
    }

    public void animatePath(Point start, Point end) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
        anim = ObjectAnimator.ofObject(imageView, "imageLoc", new PathEvaluator(), path.getPoints().toArray());
        anim.addListener(this);
        anim.setDuration(400);
        anim.start();
    }

    @Override
    public void onAnimationCancel(Animator arg0) {
        imageView.setAlpha(0f);
    }

    @Override
    public void onAnimationEnd(Animator arg0) {
        imageView.setAlpha(0f);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationStart(Animator animation) {
        imageView.setAlpha(1f);
    }

}
