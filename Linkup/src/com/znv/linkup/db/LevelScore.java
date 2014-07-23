package com.znv.linkup.db;

/**
 * 关卡数据信息，属性与数据库表字段对应
 * 
 * @author yzb
 * 
 */
public class LevelScore {
    public LevelScore(int level) {
        this.level = String.valueOf(level);
    }

    public LevelScore(String level) {
        this.level = level;
    }

    public LevelScore(String level, String rank, int maxScore, int minTime, int isActive, int star) {
        this.level = level;
        this.rank = rank;
        this.maxScore = maxScore;
        this.minTime = minTime;
        this.isActive = isActive;
        this.star = star;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(int isUpload) {
        this.isUpload = isUpload;
    }

    private String level;
    private String rank;
    private int maxScore;
    private int minTime;
    private int isActive;
    private int star;
    private int isUpload = 0;

}
