package com.znv.linkup.db;

public class LevelScore {
	private String level;
	private String rank;
	private int maxScore;
	private int isActive;
	private int star;

    public LevelScore(int level) {
        this.level = String.valueOf(level);
    }
    
	public LevelScore(String level) {
		this.level = level;
	}
	
	public LevelScore(String level, String rank, int maxScore, int isActive, int star) {
		this.level = level;
		this.rank = rank;
		this.maxScore = maxScore;
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
}
