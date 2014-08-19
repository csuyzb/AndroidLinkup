package com.znv.linkup;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.ContactsPage;

import com.xiaomi.market.sdk.XiaomiUpdateAgent;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.rest.IUpload;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.util.CacheUtil;
import com.znv.linkup.util.ToastUtil;
import com.znv.linkup.view.GameTitle;
import com.znv.linkup.view.LevelTop;
import com.znv.linkup.view.dialog.HelpDialog;
import com.znv.linkup.view.dialog.InfoDialog;

/**
 * 欢迎界面活动处理类
 * 
 * @author yzb
 * 
 */
public class WelcomeActivity extends BaseActivity implements OnClickListener, IUpload {

    private long exitTime = 0;
    private TextView ivMusic = null;
    private TextView ivSound = null;
    private LevelTop levelTop = null;
    private TextView tsNotice = null;
    private Animator noticeAnim = null;
    private int noticeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        initClickListener();

        initMusicSetting();

        initSoundSetting();

        initLogin();

        initSDK();

        initInfoDialog();

        // 异步加载配置信息
        new LoadCfgTask().execute();

        initNotice();

        startAnimation();
    }

    /**
     * 初始化公告
     */
    private void initNotice() {
        final String[] NoticeMsg = getString(R.string.notice).split("@");
        Display mDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        tsNotice = (TextView) findViewById(R.id.tsNotice);
        tsNotice.setTextSize(16);
        tsNotice.setTextColor(0xffff6347);
        tsNotice.setGravity(Gravity.CENTER);
        noticeAnim = ObjectAnimator.ofFloat(tsNotice, "translationX", size.x, 0);
        noticeAnim.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                noticeIndex++;
                if (noticeIndex > NoticeMsg.length - 1) {
                    noticeIndex = 0;
                }
                animation.start();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {
                tsNotice.setText(NoticeMsg[noticeIndex]);
            }
        });
        noticeAnim.setDuration(2000);
        noticeAnim.setStartDelay(5000);
        noticeAnim.start();
    }

    private void initLogin() {
        levelTop = (LevelTop) findViewById(R.id.welcome_user);
        levelTop.setUploadListener(this);
        levelTop.reset();
    }

    private void initClickListener() {
        findViewById(R.id.mode0).setOnClickListener(this);
        findViewById(R.id.mode1).setOnClickListener(this);
        findViewById(R.id.mode2).setOnClickListener(this);
        findViewById(R.id.mode3).setOnClickListener(this);

        findViewById(R.id.music).setOnClickListener(this);
        findViewById(R.id.sound).setOnClickListener(this);
        findViewById(R.id.top).setOnClickListener(this);
        findViewById(R.id.friend).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
    }

    @Override
    protected void playMusic() {
        if (musicMgr != null) {
            musicMgr.setBgMusicRes(R.raw.welcomebg);
            musicMgr.play();
        }
    }

    /**
     * 初始化背景音乐设置
     */
    private void initMusicSetting() {
        ivMusic = (TextView) findViewById(R.id.music);
        setGameMusic();
        if (musicMgr != null) {
            musicMgr.setBgMisicEnabled(LevelCfg.globalCfg.isGameBgMusic());
        }
    }

    /**
     * 初始化音效设置
     */
    private void initSoundSetting() {
        ivSound = (TextView) findViewById(R.id.sound);
        setGameSound();
        if (soundMgr != null) {
            soundMgr.setSoundEnabled(LevelCfg.globalCfg.isGameSound());
        }
    }

    /**
     * 设置游戏背景音乐
     */
    private void setGameMusic() {
        if (LevelCfg.globalCfg.isGameBgMusic()) {
            Drawable drawable = getResources().getDrawable(R.drawable.music);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ivMusic.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.music_d);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ivMusic.setCompoundDrawables(null, drawable, null, null);
        }
    }

    /**
     * 设置游戏音效
     */
    private void setGameSound() {
        if (LevelCfg.globalCfg.isGameSound()) {
            Drawable drawable = getResources().getDrawable(R.drawable.sound);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ivSound.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.sound_d);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ivSound.setCompoundDrawables(null, drawable, null, null);
        }
    }

    /**
     * 初始化标题动画
     */
    private void startAnimation() {
        GameTitle gameTitle = (GameTitle) findViewById(R.id.gameTitle);
        gameTitle.startAnimation();

        // if (noticeAnim != null && !noticeAnim.isRunning()) {
        // noticeAnim.start();
        // }
    }

    /**
     * 反初始化标题动画
     */
    private void stopAnimation() {
        GameTitle gameTitle = (GameTitle) findViewById(R.id.gameTitle);
        gameTitle.stopAnimation();

        // if (noticeAnim != null && noticeAnim.isRunning()) {
        // noticeAnim.cancel();
        // }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();
        if (userInfo != null && levelTop != null) {
            levelTop.updateUserInfo();
        }
    }

    /**
     * 处理home或者back键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            if ((System.currentTimeMillis() - exitTime) > ViewSettings.TwoBackExitInterval) {
                ToastUtil.getToast(this, R.string.back_again).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.mode0:
        case R.id.mode1:
        case R.id.mode2:
        case R.id.mode3:
            soundMgr.select();
            int modeIndex = Integer.parseInt((String) v.getTag());
            if (modeIndex >= 0 && modeIndex < 4) {
                Intent intent = new Intent(this, RankActivity.class);
                intent.putExtra("modeIndex", modeIndex);
                startActivity(intent);
            }
            break;
        case R.id.music: {
            if (musicMgr != null) {
                musicMgr.setBgMisicEnabled(!musicMgr.isBgMisicEnabled());
                // 保存全局设置--背景音乐
                setGlobalCfg();
                setGameMusic();
            }
        }
            break;
        case R.id.sound: {
            if (soundMgr != null) {
                soundMgr.setSoundEnabled(!soundMgr.isSoundEnabled());
                // 保存全局设置--音效
                setGlobalCfg();
                setGameSound();
            }
        }
            break;
        case R.id.top: {
            Intent intent = new Intent(this, TopActivity.class);
            startActivity(intent);
        }
            break;
        case R.id.about: {
            HelpDialog helper = new HelpDialog(this);
            helper.setTitle(getString(R.string.about));
            helper.setMessage(getString(R.string.about_info));
            helper.show();
        }
            break;
        case R.id.friend: {
            // 打开通信录好友列表页面
            ContactsPage contactsPage = new ContactsPage();
            contactsPage.show(this);
        }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            ShareSDK.stopSDK(this);
        } catch (Exception ex) {
        }

        super.onDestroy();
    }

    @Override
    public void onLoginSuccess(Message msg) {
        if (userInfo != null) {
            UserScore.getUserImage(userInfo.getUserIcon(), levelTop.netMsgHandler);
        }
    }

    @Override
    public void onLevelResultAdd(Message msg) {
    }

    /**
     * 初始化第三方sdk
     */
    private void initSDK() {
        try {
            ShareSDK.initSDK(this);
        } catch (Exception ex) {
            Log.d("ShareSDK", ex.getMessage());
        }

        try {
            // 初始化短信SDK
            SMSSDK.initSDK(this, ViewSettings.APPKEY, ViewSettings.APPSECRET);
        } catch (Exception ex) {
            Log.d("SMSSDK", ex.getMessage());
        }

        try {
            XiaomiUpdateAgent.setCheckUpdateOnlyWifi(true);
            XiaomiUpdateAgent.update(this);
        } catch (Exception ex) {
            Log.d("XiaomiUpdate", ex.getMessage());
        }
    }

    /**
     * 初始化提示框
     */
    private void initInfoDialog() {
        if (!CacheUtil.hasBind(this, "info_login")) {
            InfoDialog info = new InfoDialog(this);
            info.setWeiboClick(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    levelTop.weiboClick();
                }
            });
            info.setQQClick(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    levelTop.qqClick();
                }
            });
            info.show();
        }
    }

    /**
     * 加载配置的异步任务
     * 
     * @author yzb
     * 
     */
    private class LoadCfgTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 加载配置信息
            loadCfgs();

            // 加载关卡适配器
            loadRankAdapters();

            // 加载图片信息
            loadSkinImages();

            return null;
        }
    }
}
