package com.znv.linkup.core.config;

/**
 * 游戏全局设置
 * 
 * @author yzb
 * 
 */
public class GlobalCfg {
    private boolean gameSound = true;
    private boolean gameBgMusic = true;
    private String gameSkin;
    private int menuWidth;
    private int promptNum;
    private int pauseNum;

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

    public int getPauseNum() {
        return pauseNum;
    }

    public void setPauseNum(int pauseNum) {
        this.pauseNum = pauseNum;
    }
}
