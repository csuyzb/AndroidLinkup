package com.znv.linkup.core.status;

import com.znv.linkup.core.GameSettings;
import com.znv.linkup.core.card.path.LinkInfo;

/**
 * 游戏得分处理
 * 
 * @author yzb
 * 
 */
public class GameScore {

    public GameScore() {
        this(null);
    }

    public GameScore(IGameStatus listener) {
        gameScore = 0;
        this.listener = listener;
        if (listener != null) {
            listener.onScoreChanged(gameScore);
        }
    }

    public void addScore(int score) {
        this.gameScore += score;
        if (listener != null) {
            listener.onScoreChanged(gameScore);
        }
    }

    /**
     * 根据链路获取奖励分数
     * 
     * @param linkInfo
     *            连接路径信息
     * @return 转弯奖励的分数
     */
    public int getCornerScore(LinkInfo linkInfo) {
        return (linkInfo.getLinkPieces().size() - 2) * GameSettings.CornerScore;
    }

    /**
     * 根据时间获取奖励分数
     * 
     * @param gameTime
     *            游戏时间
     * @return
     */
    public int getRewardScore(int gameTime) {
        return gameTime * GameSettings.TimeScore;
    }

    public int getGameScore() {
        return gameScore;
    }

    private int gameScore;
    private IGameStatus listener;
}
