package com.znv.linkup.base.status;

import com.znv.linkup.base.GameSettings;

public class GameCombo {

	public GameCombo() {
		this(null);
	}

	public GameCombo(IGameStatus listener) {
		combo = 0;
		preRemoveTime = System.currentTimeMillis();
		this.listener = listener;
	}

	public void clearCombo() {
		combo = 0;
	}

	public void addCombo() {
		combo++;
	}

	public int getComboScore() {
		long curTime = System.currentTimeMillis();
		long span = curTime - preRemoveTime;
		// 毫秒数
		int diff = (int) span - GameSettings.ComboDelay;
		if (diff <= 0) {
			combo++;
		} else {
			combo = 1;
		}
		preRemoveTime = curTime;
		if (combo != 0 && combo % GameSettings.ComboMod == 0) {
			if (listener != null) {
				listener.onCombo();
			}
			return getComboScore(combo);
		}
		return 0;
	}
	
	public static int getComboScore(int combo) {
		return combo * GameSettings.ComboScore;
	}

	public int getGameCombo() {
		return combo;
	}

	private long preRemoveTime;
	private int combo;
	private IGameStatus listener;
}
