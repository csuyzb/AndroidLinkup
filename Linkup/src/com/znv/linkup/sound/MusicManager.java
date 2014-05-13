package com.znv.linkup.sound;

import com.znv.linkup.R;
import com.znv.linkup.core.config.LevelCfg;

import android.content.Context;
import android.content.Intent;

/**
 * 背景音乐管理类
 * @author yzb
 *
 */
public class MusicManager {

    private static Intent musicIntent = new Intent("com.znv.linkup.BGMUSIC");
    private int bgMusicRes = R.raw.bgmusic3;

    private Context ctx = null;

    public MusicManager(Context ctx) {
        this.ctx = ctx;
    }

    public int getBgMusicRes() {
        return bgMusicRes;
    }

    public void setBgMusicRes(int bgMusicRes) {
        this.bgMusicRes = bgMusicRes;
    }

    /**
     * 开启背景音乐
     */
    public void play() {
        if (isBgMisicEnabled()) {
            musicIntent.putExtra("bgmusic", bgMusicRes);
            ctx.startService(musicIntent);
        }
    }

    /**
     * 停止背景音乐
     */
    public void stop() {
        ctx.stopService(musicIntent);
    }

    /**
     * 获取是否启用背景音乐
     * 
     * @return 是否开启背景音乐
     */
    public boolean isBgMisicEnabled() {
        return LevelCfg.globalCfg.isGameBgMusic();
    }

    /**
     * 设置是否开启背景音乐
     * 
     * @param bgMisicEnabled
     *            是否开启背景音乐
     */
    public void setBgMisicEnabled(boolean bgMisicEnabled) {
        LevelCfg.globalCfg.setGameBgMusic(bgMisicEnabled);
        if (bgMisicEnabled) {
            play();
        } else {
            stop();
        }
    }
}
