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
	// 提示数
	private int promptNum;
	// 重排数
	private int refreshNum;

	/**
	 * 从字符串解析出全局配置信息
	 * 
	 * @param globalCfgStr
	 *            配置信息字符串
	 * @return 全局配置信息
	 */
	public static GlobalCfg parse(String globalCfgStr) {
		String[] cfgs = globalCfgStr.split(";");
		if (cfgs.length == 4) {
			GlobalCfg globalCfg = new GlobalCfg();
			globalCfg.setGameSound(cfgs[0].equals("1"));
			globalCfg.setGameBgMusic(cfgs[1].equals("1"));
			globalCfg.setPromptNum(Integer.parseInt(cfgs[2]));
			globalCfg.setRefreshNum(Integer.parseInt(cfgs[3]));
			return globalCfg;
		}
		return null;
	}

	/**
	 * 转化为配置信息字符串
	 */
	@Override
	public String toString() {
		String soundStr = gameSound ? "1" : "0";
		String musicStr = gameBgMusic ? "1" : "0";
		return String.format("%s;%s;%s;%s;", soundStr, musicStr, promptNum,
				refreshNum);
	}

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
