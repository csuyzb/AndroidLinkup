package com.znv.linkup.view.animation.path;

import com.znv.linkup.view.animation.AnimatorProxy;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AnimatorImage extends ImageView {

    private AnimatorProxy proxy = null;
    
    public AnimatorImage(Context context) {
        this(context, null);
    }

    public AnimatorImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        proxy = AnimatorProxy.wrap(this);
    }

    public void setImageLoc(PathPoint pp) {
        proxy.setTranslationX(pp.mX);
        proxy.setTranslationY(pp.mY);
    }

}
