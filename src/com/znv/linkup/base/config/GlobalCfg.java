package com.znv.linkup.base.config;

/**
 * 游戏全局设置
 * 
 * @author yzb
 * 
 */
public class GlobalCfg {

    // private int gameWidth;
    // private int gameHeight;
    private boolean gameMusic;
    private boolean gameBgMusic;
    private String gameSkin;
    private int menuWidth;

    // public int getGameWidth() {
    // return gameWidth;
    // }
    //
    // public void setGameWidth(int gameWidth) {
    // this.gameWidth = gameWidth;
    // }
    //
    // public int getGameHeight() {
    // return gameHeight;
    // }
    //
    // public void setGameHeight(int gameHeight) {
    // this.gameHeight = gameHeight;
    // }

    public boolean isGameMusic() {
        return gameMusic;
    }

    public void setGameMusic(boolean gameMusic) {
        this.gameMusic = gameMusic;
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
}
