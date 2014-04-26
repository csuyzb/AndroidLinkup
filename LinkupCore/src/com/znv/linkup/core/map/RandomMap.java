package com.znv.linkup.core.map;

import com.znv.linkup.core.map.template.MapTemplate;
import com.znv.linkup.core.map.template.RandomTemplate;

public class RandomMap extends GameMap {
	private static final long serialVersionUID = -3788653825318577597L;

	public RandomMap(int rows, int cols) {
		this(new RandomTemplate(rows, cols));
	}

	public RandomMap(MapTemplate template) {
		super(template.YSize, template.XSize);
		Template = template;

		// 根据模板初始化游戏
		initTemplate();

		// 刷新重排
		randomRefresh();
	}

	private void initTemplate() {
		int index = 0;
		boolean even = false;
		// cardno 必须为偶数
		int blockNum = 2 * (YSize > XSize ? XSize - 2 : YSize - 2);
		Data = new Byte[YSize][XSize];
		for (int i = 0; i < YSize; i++) {
			for (int j = 0; j < XSize; j++) {
				if (Template.Data[i][j] > 0) {
					if (even) {
						Data[i][j] = (byte) (index++ % blockNum + 1);
					} else {
						Data[i][j] = (byte) (index % blockNum + 1);
					}
					even = !even;
				} else {
					Data[i][j] = Template.Data[i][j];
				}
			}
		}
	}

}
