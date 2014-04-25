package com.znv.linkup.view.animation.path;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.graphics.Point;

public class CoinPathAnimator implements AnimatorListener {

    private CoinImageView imageView = null;
    private ObjectAnimator anim = null;

    public CoinPathAnimator(CoinImageView coinImageView) {
        this.imageView = coinImageView;
    }

    public void animatePath(Point start, Point end) {
        AnimatorPath path = new AnimatorPath();
        path.moveTo(start.x, start.y);
        path.lineTo(end.x, end.y);
        anim = ObjectAnimator.ofObject(imageView, "imageLoc", new PathEvaluator(), path.getPoints().toArray());
        anim.addListener(this);
        anim.setDuration(500);
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
