package com.znv.linkup.core.map.template;

import com.znv.linkup.core.GameSettings;
import com.znv.linkup.core.map.BaseMap;

/**
 * 地图模版类
 * 
 * @author yzb
 * 
 */
public class MapTemplate extends BaseMap {
    private static final long serialVersionUID = -8030816635103999967L;

    protected MapTemplate() {
    }

    public MapTemplate(int ySize, int xSize) {
        this(ySize, xSize, 0, 0);
    }

    public MapTemplate(int ySize, int xSize, int empty, int obstacle) {
        super(ySize, xSize);
        this.EmptyCount = empty;
        this.ObstacleCount = obstacle;
        this.Data = new Byte[ySize][xSize];

        initTemplate();
    }

    protected void initTemplate() {
        for (int i = 0; i < YSize; i++) {
            for (int j = 0; j < XSize; j++) {
                if (i == 0 || i == YSize - 1 || j == 0 || j == XSize - 1) {
                    Data[i][j] = GameSettings.GroundCardValue;
                } else {
                    Data[i][j] = GameSettings.GameCardValue;
                }
            }
        }
    }

    @Override
    protected boolean isRefresh(int i, int j) {
        return Data[i][j] != GameSettings.GroundCardValue;
    }

    /**
     * 根据地图模版配置字符串解析出地图模版
     * 
     * @param strMapTemp
     *            地图模版配置字符串
     * @return 地图模版
     */
    public static MapTemplate parse(String strMapTemp) {
        try {
            MapTemplate tpl = new MapTemplate();
            String map = strMapTemp;
            String[] data = map.split("\\$");
            String[] strnm = data[0].split("\\*");
            tpl.YSize = Integer.parseInt(strnm[0]);
            tpl.XSize = Integer.parseInt(strnm[1]);
            tpl.Data = new Byte[tpl.YSize][tpl.XSize];
            String[] rowData = data[1].split(";");
            String[] cellData = null;
            for (int i = 0; i < rowData.length; i++) {
                cellData = rowData[i].split(",");
                for (int j = 0; j < cellData.length; j++) {
                    tpl.Data[i][j] = convertValue(Byte.parseByte(cellData[j]));
                }
            }
            return tpl;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Byte convertValue(Byte bValue) {
        return (byte) (bValue > 0 ? 1 : (bValue < 0 ? bValue : 0));
    }

    protected int EmptyCount;
    protected int ObstacleCount;
}
