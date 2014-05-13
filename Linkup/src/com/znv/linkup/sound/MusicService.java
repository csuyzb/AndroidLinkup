package com.znv.linkup.sound;

import com.znv.linkup.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * 游戏背景音乐服务
 * 
 * @author yzb
 * 
 */
public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int bgMusicRes = intent.getIntExtra("bgmusic", R.raw.bgmusic3);
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, bgMusicRes);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onDestroy();
    }
}
