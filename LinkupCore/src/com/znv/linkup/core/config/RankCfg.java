package com.znv.linkup.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RankCfg implements Serializable {

	private static final long serialVersionUID = -5068249504885791712L;

	public RankCfg(String rankName) {
		this.rankName = rankName;
	}

	public int getRankScore() {
		int total = 0;
		for (LevelCfg levelCfg : LevelInfos) {
			total += levelCfg.getMaxScore();
		}
		return total;
	}

	private String rankId;
	private String rankName;
	private int rankBackground;
	private List<LevelCfg> LevelInfos = new ArrayList<LevelCfg>();

	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public List<LevelCfg> getLevelInfos() {
		return LevelInfos;
	}

	public void setLevelInfos(List<LevelCfg> levelInfos) {
		LevelInfos = levelInfos;
	}

	public int getRankBackground() {
		return rankBackground;
	}

	public void setRankBackground(int rankBackground) {
		this.rankBackground = rankBackground;
	}
}
