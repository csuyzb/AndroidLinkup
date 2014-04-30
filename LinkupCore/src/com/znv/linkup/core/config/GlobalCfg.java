package com.znv.linkup.core.config;

/**
 * 游戏全局设置
 * 
 * @author yzb
 * 
 */
public class GlobalCfg {
    // 是否播放游戏音效
    private boolean gameSound = true;
    // 是否播放背景音乐
    private boolean gameBgMusic = true;
    // 游戏皮肤，暂时未用
    private String gameSkin;
    // 菜单按钮宽度
    private int menuWidth;
    // 提示数
    private int promptNum;
    // 重排数
    private int refreshNum;

    public boolean isGameSound() {
        return gameSound;
    }

    public void setGameSound(boolean gameSound) {
        this.gameSound = gameSound;
    }

    public boolean isGameBgMusic() {
        return gameBgMusic;
    }

    public void setGameBgMusic(boolean gameBgMusic) {
        this.gameBgMusic = gameBgMusic;
    }

    public String getGameSkin() {
        return gameSkin;
    }

    public void setGameSkin(String gameSkin) {
        this.gameSkin = gameSkin;
    }

    public int getMenuWidth() {
        return menuWidth;
    }

    public void setMenuWidth(int menuWidth) {
        this.menuWidth = menuWidth;
    }

    public int getPromptNum() {
        return promptNum;
    }

    public void setPromptNum(int promptNum) {
        this.promptNum = promptNum;
    }

    public int getRefreshNum() {
        return refreshNum;
    }

    public void setRefreshNum(int refreshNum) {
        this.refreshNum = refreshNum;
    }
}
