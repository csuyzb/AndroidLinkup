package com.znv.linkup.view.animation.view;

import android.content.Context;
import android.widget.ImageView;

import com.znv.linkup.view.animation.AnimatorProxy;
import com.znv.linkup.view.animation.path.PathPoint;

public class AnimatorImage extends ImageView implements IAnimatorView{

    private AnimatorProxy proxy = null;

    public AnimatorImage(Context ctx) {
        super(ctx);
        proxy = AnimatorProxy.wrap(this);
    }

    public void setLocation(PathPoint pp) {
        proxy.setTranslationX(pp.mX);
        proxy.setTranslationY(pp.mY);
    }

}
