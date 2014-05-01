package com.znv.linkup.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.znv.linkup.R;
import com.znv.linkup.core.config.LevelCfg;

/**
 * 游戏音乐和音效
 * 
 * @author yzb
 * 
 */
public class MusicServer {
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 8);
    private int[] soundIds = new int[5];

    public MusicServer(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bg);
            mediaPlayer.setLooping(true);
        }

        // soundIds[0] = soundPool.load(context, R.raw.bg1, 1);
        soundIds[1] = soundPool.load(context, R.raw.select, 1);
        soundIds[2] = soundPool.load(context, R.raw.erase, 1);
        soundIds[3] = soundPool.load(context, R.raw.refresh, 1);
        soundIds[4] = soundPool.load(context, R.raw.translate, 1);
    }

    public void play() {
        if (LevelCfg.globalCfg.isGameBgMusic()) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    // 先停止
                    mediaPlayer.stop();
                }

                mediaPlayer.start();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (LevelCfg.globalCfg.isGameBgMusic()) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    private void playSound(int source) {
        if (LevelCfg.globalCfg.isGameSound()) {
            soundPool.play(source, 1, 1, 0, 0, 1);
        }
    }

    public void select() {
        playSound(soundIds[1]);
    }

    public void erase() {
        playSound(soundIds[2]);
    }

    public void refresh() {
        playSound(soundIds[3]);
    }

    public void translate() {
        playSound(soundIds[4]);
    }

    public boolean isBgMisicEnabled() {
        return LevelCfg.globalCfg.isGameBgMusic();
    }

    public void setBgMisicEnabled(boolean bgMisicEnabled) {
        LevelCfg.globalCfg.setGameBgMusic(bgMisicEnabled);
        if (bgMisicEnabled) {
            resume();
        } else {
            pause();
        }
    }

    public boolean isSoundEnabled() {
        return LevelCfg.globalCfg.isGameSound();
    }

    public void setSoundEnabled(boolean soundEnabled) {
        LevelCfg.globalCfg.setGameSound(soundEnabled);
    }
}
