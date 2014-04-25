package com.znv.linkup.view.animation.path;

import com.znv.linkup.view.animation.AnimatorProxy;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CoinImageView extends ImageView {

    private AnimatorProxy proxy = null;

    public CoinImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        proxy = AnimatorProxy.wrap(this);
    }

    public void setImageLoc(PathPoint pp) {
        proxy.setTranslationX(pp.mX);
        proxy.setTranslationY(pp.mY);
    }

}
