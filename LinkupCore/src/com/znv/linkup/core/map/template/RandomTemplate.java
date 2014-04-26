package com.znv.linkup.core.map.template;

import com.znv.linkup.core.GameSettings;

public class RandomTemplate extends MapTemplate {
	private static final long serialVersionUID = -4260503758055093714L;

	public RandomTemplate(int ySize, int xSize) {
		super(ySize, xSize);
	}

	public RandomTemplate(int ySize, int xSize, int emptyCount, int obstacleCount) {
		super(ySize, xSize, emptyCount, obstacleCount);
	}

	@Override
	protected void initTemplate() {
		int empty = EmptyCount;
		int obstacle = ObstacleCount;
		// 必须保证 Rows * Colomns - EmptyCount - ObstacleCount 为偶数
		if (empty + obstacle < YSize * XSize) {
			for (int i = 0; i < YSize; i++) {
				for (int j = 0; j < XSize; j++) {
					if (i == 0 || i == YSize - 1 || j == 0 || j == XSize - 1) {
						Data[i][j] = GameSettings.GroundCardValue;
					} else {
						if (empty > 0) {
							Data[i][j] = GameSettings.EmptyCardValue;
							empty--;
						} else if (obstacle > 0) {
							Data[i][j] = GameSettings.ObstacleCardValue;
							obstacle--;
						} else {
							Data[i][j] = GameSettings.GameCardValue;
						}
					}
				}
			}
		}

		// 随机排列
		randomRefresh();
	}
}
