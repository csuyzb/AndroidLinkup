package com.znv.linkup;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.rest.IUpload;
import com.znv.linkup.rest.UserInfo;
import com.znv.linkup.rest.UserScore;
import com.znv.linkup.util.ToastUtil;
import com.znv.linkup.view.GameTitle;
import com.znv.linkup.view.LevelTop;
import com.znv.linkup.view.dialog.HelpDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        initClickListener();

        initMusicSetting();

        initSoundSetting();

        initTitle();

        initLogin();
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

        findViewById(R.id.music).setOnClickListener(this);
        findViewById(R.id.sound).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
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
    private void initTitle() {
        GameTitle gameTitle = (GameTitle) findViewById(R.id.gameTitle);
        gameTitle.startAnimation();
    }

    /**
     * 反初始化标题动画
     */
    private void unInitTitle() {
        GameTitle gameTitle = (GameTitle) findViewById(R.id.gameTitle);
        gameTitle.stopAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unInitTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTitle();
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
        case R.id.mode2: {
            soundMgr.select();
            int modeIndex = Integer.parseInt((String) v.getTag());
            if (modeIndex >= 0 && modeIndex < 3) {
                Intent intent = new Intent(WelcomeActivity.this, RankActivity.class);
                intent.putExtra("modeIndex", modeIndex);
                startActivity(intent);
            }
            break;
        }
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
        case R.id.help: {
            HelpDialog helper = new HelpDialog(this);
            helper.setTitle(getString(R.string.help));
            helper.setMessage(getString(R.string.help_info));
            helper.show();
        }
            break;
        case R.id.about: {
            HelpDialog helper = new HelpDialog(this);
            helper.setTitle(getString(R.string.about));
            helper.setMessage(getString(R.string.about_info));
            helper.show();
        }
            break;
        }
    }

    @Override
    public void onLoginSuccess(Message msg) {
        userInfo = (UserInfo) msg.obj;
        if (userInfo != null) {
            UserScore.getUserImage(userInfo.getUserIcon(), levelTop.netMsgHandler);
        }
    }

    @Override
    public void onScoreAdd(Message msg) {
    }

    @Override
    public void onTimeAdd(Message msg) {
    }

    @Override
    public void onAuthorizeClick() {
    }

}
