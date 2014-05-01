package com.znv.linkup.view;

import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.znv.linkup.GameActivity;
import com.znv.linkup.R;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.util.AnimationUtil;

/**
 * 游戏菜单类（暂时未用）
 * 
 * @author yzb
 * 
 */
public class GameMenu {

    // private int screenWidth;
    private int screenHeight;
    private int btnWidth = LevelCfg.globalCfg.getMenuWidth();
    private int originX;
    private int originY;
    private int radius;

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

    /**
     * 创建菜单
     */
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

    /**
     * 显示菜单
     */
    public void show() {
        if (isShowDetail) {
            hideDetail();
        }
        AnimationUtil.animTranslate(ibMenu, originX, originX, 50, originY, 500);
    }

    private ImageButton addMenuItem(int resId) {
        ImageButton btn = new ImageButton(act);
        btn.setBackgroundResource(resId);
        btn.setLayoutParams(params);
        root.addView(btn);
        return btn;
    }

    /**
     * 显示详细子菜单
     */
    public void showDetail() {
        isShowDetail = true;
        // hide main
        AnimationUtil.animAlpha(ibMenu, 1, 0.5f, 500);
        AnimationUtil.animRotate(ibMenu, 0, 90);
        AnimationUtil.animAlpha(ibRestart, 0, 1, 200);
        AnimationUtil.animAlpha(ibPrompt, 0, 1, 300);
        AnimationUtil.animAlpha(ibRefresh, 0, 1, 400);
        AnimationUtil.animAlpha(ibBack, 0, 1, 500);

        AnimationUtil.animTranslate(ibRestart, originX, originX, originY, originY - radius, 200);
        AnimationUtil.animTranslate(ibPrompt, originX, originX + (float) (radius * 0.5 * Math.sqrt(3)), originY, originY - radius * 0.5f, 300);
        AnimationUtil.animTranslate(ibRefresh, originX, originX + (float) (radius * 0.5 * Math.sqrt(3)), originY, originY + radius * 0.5f, 400);
        AnimationUtil.animTranslate(ibBack, originX, originX, originY, originY + radius, 500);

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

    /**
     * 隐藏详细子菜单
     */
    public void hideDetail() {
        isShowDetail = false;
        // show main
        AnimationUtil.animAlpha(ibMenu, 0.5f, 1, 500);
        AnimationUtil.animRotate(ibMenu, 90, 0);
        AnimationUtil.animAlpha(ibRestart, 1, 0, 500);
        AnimationUtil.animAlpha(ibPrompt, 1, 0, 400);
        AnimationUtil.animAlpha(ibRefresh, 1, 0, 300);
        AnimationUtil.animAlpha(ibBack, 1, 0, 200);

        AnimationUtil.animTranslate(ibRestart, originX, originX, originY - radius, originY, 500);
        AnimationUtil.animTranslate(ibPrompt, originX + (float) (radius * 0.5 * Math.sqrt(3)), originX, originY - radius * 0.5f, originY, 400);
        AnimationUtil.animTranslate(ibRefresh, originX + (float) (radius * 0.5 * Math.sqrt(3)), originX, originY + radius * 0.5f, originY, 300);
        AnimationUtil.animTranslate(ibBack, originX, originX, originY + radius, originY, 200);

        ibRestart.setOnClickListener(null);
        ibPrompt.setOnClickListener(null);
        ibRefresh.setOnClickListener(null);
        ibBack.setOnClickListener(null);
    }
}
