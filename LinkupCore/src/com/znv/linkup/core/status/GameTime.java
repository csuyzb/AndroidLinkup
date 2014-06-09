package com.znv.linkup.core.status;

import java.util.Timer;
import java.util.TimerTask;

import com.znv.linkup.core.config.GameMode;

/**
 * 游戏时间处理
 * 
 * @author yzb
 * 
 */
class GameTime {

    public GameTime(GameMode mode) {
        this(mode, 0, null);
    }

    public GameTime(int totalTime) {
        this(GameMode.Level, totalTime, null);
    }

    public GameTime(GameMode mode, int totalTime, IGameStatus listener) {
        this.mode = mode;
        this.totalTime = totalTime;
        if (mode == GameMode.Level) {
            this.gameTime = totalTime;
        } else if (mode == GameMode.Time || mode == GameMode.Task) {
            this.gameTime = 0;
        }
        this.listener = listener;
    }

    /**
     * 计时开始
     */
    public void start() {
        if (mode == GameMode.Level || mode == GameMode.Time) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    timerTick();
                }
            }, 0, 1000);
        } else {
            timer = null;
        }
    }

    private void timerTick() {
        if (mode == GameMode.Level) {
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
        } else if (mode == GameMode.Time) {
            gameTime += 1;
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
        if (mode == GameMode.Level) {
            gameTime += seconds;
            if (gameTime > totalTime) {
                gameTime = totalTime;
            }
            if (listener != null) {
                listener.onTimeChanged(gameTime);
            }
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
    private GameMode mode = null;
    private IGameStatus listener;
}
