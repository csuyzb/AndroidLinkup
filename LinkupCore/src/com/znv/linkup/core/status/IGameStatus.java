package com.znv.linkup.core.status;

import com.znv.linkup.core.card.PiecePair;

/**
 * 游戏状态处理
 * 
 * @author yzb
 * 
 */
public interface IGameStatus {

    /**
     * 连击时的处理
     */
    void onCombo();

    /**
     * 提示时的处理
     * 
     * @param pair
     *            提示的卡片对
     */
    void onPrompt(PiecePair pair);

    /**
     * 取消提示的处理
     * @param pair
     *            取消提示的卡片对
     */
    void onUnPrompt(PiecePair pair);

    /**
     * 刷新时的处理
     */
    void onRefresh();

    /**
     * 积分改变时的处理
     */
    void onScoreChanged(int gameScore);

    /**
     * 时间改变时的处理
     */
    void onTimeChanged(int gameTime);

    /**
     * 游戏失败时的处理
     */
    void onGameFail();

    /**
     * 游戏暂停时的处理
     */
    void onGamePause();

    /**
     * 游戏重新开始时的处理
     */
    void onGameResume();

    /**
     * 游戏胜利时的处理
     */
    void onGameWin();
}
