package com.znv.linkup.view.dialog;

/**
 * 游戏结果信息
 * 
 * @author yzb
 * 
 */
public class ResultInfo {
    private String userId = "";
    private int level;
    private int score;
    private int time;
    private int stars;
    private int maxScore;
    private int minTime;
    private boolean isNewRecord;
    private boolean isUpload;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
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

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}
