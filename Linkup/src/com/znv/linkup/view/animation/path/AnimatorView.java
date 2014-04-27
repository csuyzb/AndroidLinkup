package com.znv.linkup.view.animation.path;

import android.content.Context;
import android.widget.ImageView;

import com.znv.linkup.view.animation.AnimatorProxy;

public class AnimatorView extends ImageView {

    private AnimatorProxy proxy = null;

    public AnimatorView(Context ctx) {
        super(ctx);
        proxy = AnimatorProxy.wrap(this);
    }

    public void setLocation(PathPoint pp) {
        proxy.setTranslationX(pp.mX);
        proxy.setTranslationY(pp.mY);
    }

}
