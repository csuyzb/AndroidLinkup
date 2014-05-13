package com.znv.linkup.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.znv.linkup.R;
import com.znv.linkup.core.config.LevelCfg;

/**
 * 游戏音效管理
 * 
 * @author yzb
 * 
 */
public class SoundManager {
    private SoundPool soundPool = new SoundPool(8, AudioManager.STREAM_SYSTEM, 8);
    private int[] soundIds = new int[8];

    public SoundManager(Context context) {
        soundIds[0] = soundPool.load(context, R.raw.readygo, 1);
        soundIds[1] = soundPool.load(context, R.raw.select, 1);
        soundIds[2] = soundPool.load(context, R.raw.erase, 1);
        soundIds[3] = soundPool.load(context, R.raw.refresh, 1);
        soundIds[4] = soundPool.load(context, R.raw.translate, 1);
        soundIds[5] = soundPool.load(context, R.raw.combo, 1);
        soundIds[6] = soundPool.load(context, R.raw.win, 1);
        soundIds[7] = soundPool.load(context, R.raw.fail, 1);
    }

    /**
     * 播放音效
     * 
     * @param source
     *            声音源id
     */
    private void playSound(int source) {
        if (LevelCfg.globalCfg.isGameSound()) {
            soundPool.play(source, 1, 1, 0, 0, 1);
        }
    }

    /**
     * 播放开始ready-go
     */
    public void readyGo() {
        playSound(soundIds[0]);
    }

    /**
     * 播放选中声音
     */
    public void select() {
        playSound(soundIds[1]);
    }

    /**
     * 播放消除声音
     */
    public void erase() {
        playSound(soundIds[2]);
    }

    /**
     * 播放重排声音
     */
    public void refresh() {
        playSound(soundIds[3]);
    }

    /**
     * 播放变换声音
     */
    public void translate() {
        playSound(soundIds[4]);
    }

    /**
     * 播放连击声音
     */
    public void combo() {
        playSound(soundIds[5]);
    }

    /**
     * 播放游戏胜利声音
     */
    public void win() {
        playSound(soundIds[6]);
    }

    /**
     * 播放游戏失败声音
     */
    public void fail() {
        playSound(soundIds[7]);
    }

    /**
     * 获取是否开启音效
     * 
     * @return 是否开启音效
     */
    public boolean isSoundEnabled() {
        return LevelCfg.globalCfg.isGameSound();
    }

    /**
     * 设置是否开启音效
     * 
     * @param soundEnabled
     *            是否开启音效
     */
    public void setSoundEnabled(boolean soundEnabled) {
        LevelCfg.globalCfg.setGameSound(soundEnabled);
    }
}
