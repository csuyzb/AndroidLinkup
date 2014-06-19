package com.znv.linkup.db;

/**
 * 关卡数据信息，属性与数据库表字段对应
 * 
 * @author yzb
 * 
 */
public class LevelScore {
    private String level;
    private String rank;
    private int maxScore;
    private int isActive;
    private int star;
    private int isUpload;

    public LevelScore(int level) {
        this.level = String.valueOf(level);
    }

    public LevelScore(String level) {
        this.level = level;
    }

    public LevelScore(String level, String rank, int maxScore, int isActive, int star, int isUpload) {
        this.level = level;
        this.rank = rank;
        this.maxScore = maxScore;
        this.isActive = isActive;
        this.star = star;
        this.isUpload = isUpload;
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
}
