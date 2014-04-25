package com.znv.linkup.base.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.znv.linkup.R;
import com.znv.linkup.base.config.LevelCfg;

public class GameMusic {
    private SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 8);
    private int[] soundIds = new int[5];

    private GameMusic() {
    }

    public void init(Context context) {
        // soundIds[0] = soundPool.load(context, R.raw.bg1, 1);
        soundIds[1] = soundPool.load(context, R.raw.select, 1);
        soundIds[2] = soundPool.load(context, R.raw.erase, 1);
        soundIds[3] = soundPool.load(context, R.raw.refresh, 1);
        soundIds[4] = soundPool.load(context, R.raw.translate, 1);
    }

    private static GameMusic gameMusic = new GameMusic();

    public synchronized static GameMusic getInstance() {
        return gameMusic;
    }

    // public void play() {
    // if (LevelCfg.globalCfg.isGameBgMusic()) {
    // // soundPool.play(soundIds[0], 1, 1, 0, 1, 1);
    // if (mplayer != null) {
    // mplayer.start();
    // }
    // }
    // }
    //
    // public void stop() {
    // if (mplayer != null) {
    // mplayer.stop();
    // }
    // }
    //
    // public void pause() {
    // if (mplayer != null) {
    // mplayer.pause();
    // }
    // }
    //
    // public void resume() {
    // if (mplayer != null) {
    // mplayer.start();
    // }
    // }

    public void select() {
        if (LevelCfg.globalCfg.isGameMusic()) {
            soundPool.play(soundIds[1], 1, 1, 0, 0, 1);
        }
    }

    public void erase() {
        if (LevelCfg.globalCfg.isGameMusic()) {
            soundPool.play(soundIds[2], 1, 1, 0, 0, 1);
        }
    }

    public void refresh() {
        if (LevelCfg.globalCfg.isGameMusic()) {
            soundPool.play(soundIds[3], 1, 1, 0, 0, 1);
        }
    }

    public void translate() {
        if (LevelCfg.globalCfg.isGameMusic()) {
            soundPool.play(soundIds[4], 1, 1, 0, 0, 1);
        }
    }

    // public boolean isBgMisicEnabled() {
    // return bgMisicEnabled;
    // }
    //
    // public void setBgMisicEnabled(boolean bgMisicEnabled) {
    // this.bgMisicEnabled = bgMisicEnabled;
    // if (bgMisicEnabled) {
    // play();
    // } else {
    // stop();
    // }
    // }
}
