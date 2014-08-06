package com.znv.linkup.core.status;

import com.znv.linkup.core.GameSettings;
import com.znv.linkup.core.card.PiecePair;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.config.LevelCfg;

/**
 * 游戏状态管理
 * 
 * @author yzb
 * 
 */
public class GameStatus {

    /**
     * 游戏状态:空闲，游戏，提示，暂停
     * 
     * @author yzb
     * 
     */
    enum GameState {
        None, Playing, Prompt, Pause
    }

    public GameStatus(LevelCfg levelCfg) {
        this(levelCfg, null);
    }

    public GameStatus(LevelCfg levelCfg, IGameStatus listener) {
        this.listener = listener;
        gameTime = new GameTime(levelCfg.getLevelMode(), levelCfg.getLevelTime(), listener);
        gameScore = new GameScore(listener);
        gameCombo = new GameCombo(listener);
    }

    /**
     * 游戏开始
     */
    public void start() {
        gameTime.stop();
        gameState = GameState.Playing;
        gameTime.start();
    }

    /**
     * 游戏重排
     */
    public void refresh() {
        gameCombo.clearCombo();
        if (listener != null) {
            listener.onRefresh();
        }
    }

    /**
     * 游戏暂停
     */
    public void pause() {
        // 游戏结束时暂停和重启游戏无效
        if (gameState != GameState.None) {
            gameTime.stop();
            gameState = GameState.Pause;
            if (listener != null) {
                listener.onGamePause();
            }
        }
    }

    /**
     * 游戏重新开始
     */
    public void resume() {
        if (gameState == GameState.Pause) {
            if (gameTime.getGameTime() >= 0) {
                gameTime.start();
                if (listener != null) {
                    listener.onGameResume();
                }
            }
        }
    }

    /**
     * 游戏停止
     */
    public void stop() {
        gameState = GameState.None;
        gameTime.stop();
    }

    /**
     * 游戏失败
     */
    public void fail() {
        stop();
        if (listener != null) {
            listener.onGameFail();
        }
    }

    /**
     * 游戏胜利
     */
    public void win() {
        stop();

        if (listener != null) {
            listener.onGameWin();
        }
    }

    /**
     * 游戏提示
     * 
     * @param pair
     *            提示的卡片对
     */
    public void prompt(PiecePair pair) {
        gameState = GameState.Prompt;
        if (listener != null) {
            listener.onPrompt(pair);
        }
    }

    /**
     * 取消提示
     * 
     * @param pair
     *            取消提示的卡片对
     */
    public void unPrompt(PiecePair pair) {
        if (gameState == GameState.Prompt) {
            gameState = GameState.Playing;
            if (listener != null) {
                listener.onUnPrompt(pair);
            }
        }
    }

    /**
     * 配对成功
     * 
     * @param linkInfo
     *            路径信息
     */
    public void matchSuccess(LinkInfo linkInfo) {
        gameTime.addTime(GameSettings.RewardTime);
        gameScore.addScore(GameSettings.CardScore * 2 + gameScore.getCornerScore(linkInfo) + gameCombo.getComboScore()
                + (int) (Math.random() * GameSettings.RewardScoreMax));
    }

    public void addGameTime(int seconds) {
        gameTime.addTime(seconds);
    }

    /**
     * 获取游戏时间
     * 
     * @return 游戏时间
     */
    public int getGameTime() {
        return gameTime.getGameTime();
    }

    /**
     * 获取游戏积分
     * 
     * @return 游戏积分
     */
    public int getGameScore() {
        return gameScore.getGameScore();
    }

    /**
     * 获取游戏连击
     * 
     * @return 连击数
     */
    public int getGameCombo() {
        return gameCombo.getGameCombo();
    }

    /**
     * 获取连击奖励分数
     * 
     * @return 连击奖励分数
     */
    public int getComboScore() {
        return GameCombo.getComboScore(gameCombo.getGameCombo());
    }

    /**
     * 获取奖励分数
     * 
     * @return 奖励分数
     */
    public int getRewardScore() {
        return gameScore.getRewardScore(gameTime.getGameTime());
    }

    private GameTime gameTime;
    private GameCombo gameCombo;
    private GameScore gameScore;
    private IGameStatus listener;
    private GameState gameState;
}
