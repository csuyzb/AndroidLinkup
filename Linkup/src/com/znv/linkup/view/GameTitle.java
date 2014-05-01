package com.znv.linkup.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import com.znv.linkup.R;

/**
 * 游戏标题，包括动画
 * 
 * @author yzb
 * 
 */
public class GameTitle extends LinearLayout implements Animator.AnimatorListener {

    private static LayoutParams params = null;
    private AnimatorSet animation = null;

    public GameTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        addWords(context);
    }

    static {
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 20, 4, 20);
    }

    /**
     * 增加游戏标题文字图片
     * 
     * @param context
     */
    private void addWords(Context context) {
        ImageView ivXiu = new ImageView(context);
        ivXiu.setLayoutParams(params);
        ivXiu.setBackgroundResource(R.drawable.xiu);
        this.addView(ivXiu);

        ImageView ivXian = new ImageView(context);
        ivXian.setLayoutParams(params);
        ivXian.setBackgroundResource(R.drawable.xian);
        this.addView(ivXian);

        ImageView ivLian1 = new ImageView(context);
        ivLian1.setLayoutParams(params);
        ivLian1.setBackgroundResource(R.drawable.lian);
        this.addView(ivLian1);

        ImageView ivLian2 = new ImageView(context);
        ivLian2.setLayoutParams(params);
        ivLian2.setBackgroundResource(R.drawable.lian);
        this.addView(ivLian2);

        ImageView ivKan = new ImageView(context);
        ivKan.setLayoutParams(params);
        ivKan.setBackgroundResource(R.drawable.kan);
        this.addView(ivKan);
    }

    /**
     * 创建动画
     */
    private void createAnimation() {
        if (animation == null) {
            List<Animator> appear = new ArrayList<Animator>();
            for (int i = 0; i < 5; i++) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(this.getChildAt(i), "translationX", i * -120, 0);
                anim.setDuration(1000);

                ObjectAnimator anim2 = ObjectAnimator.ofFloat(this.getChildAt(i), "alpha", 0f, 1f);
                anim2.setDuration(1000);
                AnimatorSet as = new AnimatorSet();
                as.playTogether(anim, anim2);

                appear.add(as);
            }
            AnimatorSet s1 = new AnimatorSet();
            s1.playSequentially(appear);

            List<Animator> disappear = new ArrayList<Animator>();
            for (int i = 0; i < 5; i++) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(this.getChildAt(i), "alpha", 1f, 0.4f);
                anim.setDuration(1800);

                ObjectAnimator anim2 = ObjectAnimator.ofFloat(this.getChildAt(i), "alpha", 0.4f, 0f);
                anim2.setDuration(200);
                AnimatorSet as = new AnimatorSet();
                as.playSequentially(anim, anim2);
                disappear.add(as);
            }
            AnimatorSet s2 = new AnimatorSet();
            s2.playTogether(disappear);

            List<Animator> always = new ArrayList<Animator>();
            for (int i = 0; i < 5; i++) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(this.getChildAt(i), "translationX", 0, 0);
                anim.setDuration(3000);

                ObjectAnimator anim2 = ObjectAnimator.ofFloat(this.getChildAt(i), "alpha", 1f, 0.9f, 1f);
                anim2.setDuration(3000);

                AnimatorSet as = new AnimatorSet();
                as.playTogether(anim, anim2);

                always.add(as);
            }
            AnimatorSet s3 = new AnimatorSet();
            s3.playTogether(always);

            animation = new AnimatorSet();
            animation.playSequentially(s3, s2, s1);
        }
    }

    public void startAnimation() {
        createAnimation();
        animation.addListener(this);
        animation.start();
    }

    public void stopAnimation() {
        if (animation != null) {
            animation.removeAllListeners();
            animation.end();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation instanceof AnimatorSet) {
            startAnimation();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

}
