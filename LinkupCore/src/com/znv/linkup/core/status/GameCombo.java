package com.znv.linkup.core.status;

import com.znv.linkup.core.GameSettings;

/**
 * 游戏连击处理
 * 
 * @author yzb
 * 
 */
class GameCombo {

    public GameCombo() {
        this(null);
    }

    public GameCombo(IGameStatus listener) {
        combo = 0;
        preRemoveTime = System.currentTimeMillis();
        this.listener = listener;
    }

    public void clearCombo() {
        combo = 0;
    }

    public void addCombo() {
        combo++;
    }

    /**
     * 获取连击得分
     * 
     * @return 连击奖励分数
     */
    public int getComboScore() {
        long curTime = System.currentTimeMillis();
        long span = curTime - preRemoveTime;
        // 毫秒数
        int diff = (int) span - GameSettings.ComboDelay;
        if (diff <= 0) {
            combo++;
        } else {
            combo = 1;
        }
        preRemoveTime = curTime;
        if (combo != 0 && combo % GameSettings.ComboMod == 0) {
            if (listener != null) {
                listener.onCombo();
            }
            return getComboScore(combo);
        }
        return 0;
    }

    /**
     * 根据连击数获取连击奖励分数
     * 
     * @param combo
     *            连击数
     * @return 连击奖励分数
     */
    public static int getComboScore(int combo) {
        return combo * GameSettings.ComboScore;
    }

    public int getGameCombo() {
        return combo;
    }

    private long preRemoveTime;
    private int combo;
    private IGameStatus listener;
}
