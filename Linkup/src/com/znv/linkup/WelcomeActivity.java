package com.znv.linkup;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.util.ToastUtil;
import com.znv.linkup.view.GameTitle;
import com.znv.linkup.view.dialog.HelpDialog;

/**
 * 欢迎界面活动处理类
 * 
 * @author yzb
 * 
 */
public class WelcomeActivity extends BaseActivity {

    private long exitTime = 0;
    private ImageView ivMusic = null;
    private ImageView ivSound = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        initMusicSetting();

        initSoundSetting();

        initTitle();
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
        ivMusic = (ImageView) findViewById(R.id.music);
        setGameMusic();
        if (musicMgr != null) {
            musicMgr.setBgMisicEnabled(LevelCfg.globalCfg.isGameBgMusic());
        }
        ivMusic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (musicMgr != null) {
                    musicMgr.setBgMisicEnabled(!musicMgr.isBgMisicEnabled());
                    // 保存全局设置--背景音乐
                    setGlobalCfg();
                    setGameMusic();
                }
            }
        });
    }

    /**
     * 初始化音效设置
     */
    private void initSoundSetting() {
        ivSound = (ImageView) findViewById(R.id.sound);
        setGameSound();
        if (soundMgr != null) {
            soundMgr.setSoundEnabled(LevelCfg.globalCfg.isGameSound());
        }
        ivSound.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (soundMgr != null) {
                    soundMgr.setSoundEnabled(!soundMgr.isSoundEnabled());
                    // 保存全局设置--音效
                    setGlobalCfg();
                    setGameSound();
                }
            }
        });
    }

    /**
     * 设置游戏背景音乐
     */
    private void setGameMusic() {
        if (LevelCfg.globalCfg.isGameBgMusic()) {
            ivMusic.setImageResource(R.drawable.music);
        } else {
            ivMusic.setImageResource(R.drawable.music_d);
        }
    }

    /**
     * 设置游戏音效
     */
    private void setGameSound() {
        if (LevelCfg.globalCfg.isGameSound()) {
            ivSound.setImageResource(R.drawable.sound);
        } else {
            ivSound.setImageResource(R.drawable.sound_d);
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
     * 点击开始按钮时的处理
     * 
     * @param v
     */
    public void startGame(View v) {
        soundMgr.select();
        Intent intent = new Intent(WelcomeActivity.this, RankActivity.class);
        startActivity(intent);
    }

    /**
     * 点击帮助按钮时的处理，显示游戏帮助
     * 
     * @param v
     */
    public void startHelp(View v) {
        HelpDialog helper = new HelpDialog(this);
        helper.setTitle(getString(R.string.help));
        helper.setMessage(getString(R.string.help_info));
        helper.show();
    }
    

    /**
     * 点击帮助按钮时的处理，显示游戏帮助
     * 
     * @param v
     */
    public void startAbout(View v) {
        HelpDialog helper = new HelpDialog(this);
        helper.setTitle(getString(R.string.about));
        helper.setMessage(getString(R.string.about_info));
        helper.show();
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

}
