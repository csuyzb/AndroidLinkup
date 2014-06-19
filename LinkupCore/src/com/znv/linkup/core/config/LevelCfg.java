package com.znv.linkup.core.config;

import java.io.Serializable;

import com.znv.linkup.core.GameSettings;

/**
 * 关卡配置
 * 
 * @author yzb
 * 
 */
public class LevelCfg implements Serializable {

    private static final long serialVersionUID = 2145150870959932172L;

    /**
     * 静态全局配置
     */
    public static GlobalCfg globalCfg = new GlobalCfg();

    public LevelCfg(String levelName) {
        this.levelName = levelName;
    }

    private int levelId;
    private String levelName;
    private String rankName;
    private String gameSkin;
    private int xSize;
    private int ySize;
    private int levelTime;
    private GameMode levelMode;
    // 任务目标
    private int task;
    private GameAlign levelAlign;
    private int maxScore;
    private boolean isActive;
    private int levelStar;
    private boolean isUpload = false;
    private int[] starScores = null;
    private int emptyNum;
    private int obstacleNum;
    private int pieceWidth;
    private int pieceHeight;
    private int beginImageX;
    private int beginImageY;
    private int levelBackground;
    // 关卡地图信息字符串
    private String maptplStr;

    /**
     * 初始化游戏星级的临界分数
     */
    public void initStarScores() {
        if (starScores == null) {
            starScores = new int[3];
            int cardCount = (xSize - 2) * (ySize - 2) - emptyNum - obstacleNum;
            int min = cardCount * GameSettings.CardScore + levelTime * GameSettings.TimeScore / 2;
            starScores[1] = min + GameSettings.CornerScore * cardCount / 2;
            starScores[0] = (min + starScores[1]) / 2;
            starScores[2] = starScores[1] + levelTime * GameSettings.TimeScore / 2;
        }
        // 设置任务为2颗星的分数值
        task = starScores[1];
    }

    /**
     * 根据游戏得分判断游戏星级
     * 
     * @param score
     *            游戏得分
     * @return 游戏星级
     */
    public int getStar(int score) {
        if (score < starScores[0])
            return 0;
        else if (score < starScores[1])
            return 1;
        else if (score < starScores[2])
            return 2;
        else
            return 3;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public GameMode getLevelMode() {
        return levelMode;
    }

    public void setLevelMode(GameMode levelMode) {
        this.levelMode = levelMode;
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public String getGameSkin() {
        return gameSkin;
    }

    public void setGameSkin(String gameSkin) {
        this.gameSkin = gameSkin;
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getLevelTime() {
        return levelTime;
    }

    public void setLevelTime(int levelTime) {
        this.levelTime = levelTime;
    }

    public GameAlign getLevelAlign() {
        return levelAlign;
    }

    public void setLevelAlign(GameAlign levelAlign) {
        this.levelAlign = levelAlign;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getLevelStar() {
        return levelStar;
    }

    public void setLevelStar(int levelStar) {
        this.levelStar = levelStar;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public int[] getStarScores() {
        return starScores;
    }

    public void setStarScores(int[] starScores) {
        this.starScores = starScores;
    }

    public int getEmptyNum() {
        return emptyNum;
    }

    public void setEmptyNum(int emptyNum) {
        this.emptyNum = emptyNum;
    }

    public int getObstacleNum() {
        return obstacleNum;
    }

    public void setObstacleNum(int obstacleNum) {
        this.obstacleNum = obstacleNum;
    }

    public int getPieceWidth() {
        return pieceWidth;
    }

    public void setPieceWidth(int pieceWidth) {
        this.pieceWidth = pieceWidth;
    }

    public int getPieceHeight() {
        return pieceHeight;
    }

    public void setPieceHeight(int pieceHeight) {
        this.pieceHeight = pieceHeight;
    }

    public int getBeginImageX() {
        return beginImageX;
    }

    public void setBeginImageX(int beginImageX) {
        this.beginImageX = beginImageX;
    }

    public int getBeginImageY() {
        return beginImageY;
    }

    public void setBeginImageY(int beginImageY) {
        this.beginImageY = beginImageY;
    }

    public int getLevelBackground() {
        return levelBackground;
    }

    public void setLevelBackground(int levelBackground) {
        this.levelBackground = levelBackground;
    }

    public String getMaptplStr() {
        return maptplStr;
    }

    public void setMaptplStr(String maptplStr) {
        this.maptplStr = maptplStr;
    }
}
