package com.znv.linkup.view;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;
import com.znv.linkup.core.config.LevelCfg;

public class GameMenu {

    // private int screenWidth;
    private int screenHeight;
    private int btnWidth = LevelCfg.globalCfg.getMenuWidth();
    private int originX;
    private int originY;
    private int radius;
    private int duration = 500;

    private GameActivity act = null;
    private FrameLayout root = null;
    private FrameLayout.LayoutParams params = null;

    private ImageButton ibMenu, ibRestart, ibPrompt, ibRefresh, ibBack;

    private boolean isShowDetail = false;

    public GameMenu(GameActivity act) {
        this.act = act;

        Display mDisplay = act.getWindowManager().getDefaultDisplay();
         Point size = new Point();
         mDisplay.getSize(size);
        screenHeight = size.y;
        originX = 0;
        originY = (int) ((screenHeight - btnWidth) * 0.5f);
        radius = (int) (btnWidth * 1.5f);

        root = act.getRoot();
        params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        create();
    }

    private void create() {

        ibRestart = addMenuItem(R.drawable.restart);
        ibRestart.setAlpha(0f);
        ibRestart.setY(originY);

        ibPrompt = addMenuItem(R.drawable.prompt);
        ibPrompt.setAlpha(0f);
        ibPrompt.setY(originY);

        ibRefresh = addMenuItem(R.drawable.refresh);
        ibRefresh.setAlpha(0f);
        ibRefresh.setY(originY);

        ibBack = addMenuItem(R.drawable.back);
        ibBack.setAlpha(0f);
        ibBack.setY(originY);

        ibMenu = addMenuItem(R.drawable.menu);
        ibMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isShowDetail) {
                    hideDetail();
                } else {
                    showDetail();
                }
            }
        });
    }

    public void show() {
        if (isShowDetail) {
            hideDetail();
        }
        animTranslate(ibMenu, originX, originX, 50, originY, 500);
    }

    private ImageButton addMenuItem(int resId) {
        ImageButton btn = new ImageButton(act);
        btn.setBackgroundResource(resId);
        btn.setLayoutParams(params);
        root.addView(btn);
        return btn;
    }

    public void showDetail() {
        isShowDetail = true;
        // hide main
        animAlpha(ibMenu, 1, 0.5f, 500);
        animRotate(ibMenu, 0, 90);
        animAlpha(ibRestart, 0, 1, 200);
        animAlpha(ibPrompt, 0, 1, 300);
        animAlpha(ibRefresh, 0, 1, 400);
        animAlpha(ibBack, 0, 1, 500);

        animTranslate(ibRestart, originX, originX, originY, originY - radius, 200);
        animTranslate(ibPrompt, originX, originX + (float) (radius * 0.5 * Math.sqrt(3)), originY, originY - radius * 0.5f, 300);
        animTranslate(ibRefresh, originX, originX + (float) (radius * 0.5 * Math.sqrt(3)), originY, originY + radius * 0.5f, 400);
        animTranslate(ibBack, originX, originX, originY, originY + radius, 500);

        ibRestart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideDetail();
                act.restart(v);
            }
        });

        ibPrompt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideDetail();
                act.prompt(v);
            }
        });

        ibRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideDetail();
                act.refresh(v);
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideDetail();
                act.back(v);
            }
        });
    }

    public void hideDetail() {
        isShowDetail = false;
        // show main
        animAlpha(ibMenu, 0.5f, 1, 500);
        animRotate(ibMenu, 90, 0);
        animAlpha(ibRestart, 1, 0, 500);
        animAlpha(ibPrompt, 1, 0, 400);
        animAlpha(ibRefresh, 1, 0, 300);
        animAlpha(ibBack, 1, 0, 200);

        animTranslate(ibRestart, originX, originX, originY - radius, originY, 500);
        animTranslate(ibPrompt, originX + (float) (radius * 0.5 * Math.sqrt(3)), originX, originY - radius * 0.5f, originY, 400);
        animTranslate(ibRefresh, originX + (float) (radius * 0.5 * Math.sqrt(3)), originX, originY + radius * 0.5f, originY, 300);
        animTranslate(ibBack, originX, originX, originY + radius, originY, 200);

        ibRestart.setOnClickListener(null);
        ibPrompt.setOnClickListener(null);
        ibRefresh.setOnClickListener(null);
        ibBack.setOnClickListener(null);
    }

    protected void animAlpha(ImageButton ibtn, float fromAlpha, float toAlpha, int duration) {
        ObjectAnimator.ofFloat(ibtn, "alpha", fromAlpha, toAlpha).setDuration(duration).start();
    }

    protected void animScale(ImageButton ibtn, float toX, float toY) {
        ObjectAnimator.ofFloat(ibtn, "scaleX", 1, toX).setDuration(duration).start();
        ObjectAnimator.ofFloat(ibtn, "scaleY", 1, toY).setDuration(duration).start();
    }

    protected void animRotate(ImageButton ibtn, float fromDegrees, float toDegrees) {
        ObjectAnimator.ofFloat(ibtn, "rotation", fromDegrees, toDegrees).setDuration(duration).start();
    }

    protected void animTranslate(ImageButton ibtn, float fromX, float toX, float fromY, float toY, int duration) {
        ObjectAnimator.ofFloat(ibtn, "translationX", fromX, toX).setDuration(duration).start();
        ObjectAnimator.ofFloat(ibtn, "translationY", fromY, toY).setDuration(duration).start();
    }

}
