package com.znv.linkup.core.status;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 游戏时间处理
 * 
 * @author yzb
 * 
 */
class GameTime {

    public GameTime(int totalTime) {
        this(totalTime, null);
    }

    public GameTime(int totalTime, IGameStatus listener) {
        this.totalTime = totalTime;
        this.gameTime = totalTime + 1;
        this.listener = listener;
    }

    /**
     * 计时开始
     */
    public void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                timerTick();
            }
        }, 0, 1000);
    }

    private void timerTick() {
        if (gameTime < 1) {
            gameTime = 0;
            stop();
            // 时间用完时游戏失败
            if (listener != null) {
                listener.onGameFail();
            }
        } else {
            gameTime -= 1;
            if (listener != null) {
                listener.onTimeChanged(gameTime);
            }
        }
    }

    /**
     * 计时停止
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 增加游戏时间
     * 
     * @param seconds
     *            增加的时间数，单位秒
     */
    public void addTime(int seconds) {
        gameTime += seconds;
        if (gameTime > totalTime) {
            gameTime = totalTime;
        }
        if (listener != null) {
            listener.onTimeChanged(gameTime);
        }
    }

    /**
     * 获取游戏时间
     * 
     * @return 游戏时间
     */
    public int getGameTime() {
        return gameTime;
    }

    private Timer timer = null;
    private int gameTime;
    private int totalTime;
    private IGameStatus listener;
}
