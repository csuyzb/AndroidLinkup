package com.znv.linkup.core.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.znv.linkup.core.GameSettings;
import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.map.template.MapTemplate;
import com.znv.linkup.core.map.template.RandomTemplate;

/**
 * 游戏地图类
 * 
 * @author yzb
 * 
 */
public class GameMap extends BaseMap {
    private static final long serialVersionUID = 8129010267609099217L;

    protected GameMap() {
        super();
    }

    public GameMap(int ySize, int xSize) {
        super(ySize, xSize);
        Template = new MapTemplate(ySize, xSize);
    }

    @Override
    protected boolean isRefresh(int i, int j) {
        return Data[i][j] != GameSettings.GroundCardValue && Data[i][j] != GameSettings.EmptyCardValue && Data[i][j] != GameSettings.ObstacleCardValue;
    }

    /**
     * 根据配置字符串解析出游戏地图信息
     * 
     * @param strMap
     *            地图配置字符串
     * @return 游戏地图信息
     */
    public static GameMap parse(String strMap) {
        try {
            GameMap gameMap = new GameMap();
            String map = strMap;
            String[] data = map.split("$");
            String[] strnm = data[0].split("*");
            gameMap.YSize = Integer.parseInt(strnm[0]);
            gameMap.XSize = Integer.parseInt(strnm[1]);
            gameMap.Data = new Byte[gameMap.YSize][gameMap.XSize];
            gameMap.Template = new MapTemplate(gameMap.YSize, gameMap.XSize);
            String[] rowData = data[1].split(";");
            String[] cellData;
            for (int i = 0; i < rowData.length; i++) {
                cellData = rowData[i].split(",");
                for (int j = 0; j < cellData.length; j++) {
                    gameMap.Data[i][j] = Byte.parseByte(cellData[j]);
                    gameMap.Template.Data[i][j] = MapTemplate.convertValue(gameMap.Data[i][j]);
                }
            }
            return gameMap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据关卡配置生成所有卡片信息
     * 
     * @param levelCfg
     *            游戏关卡配置
     * @return 所有卡片信息
     */
    public static Piece[][] createPieces(LevelCfg levelCfg) {
        int imageWidth = levelCfg.getPieceWidth();
        int imageHeight = levelCfg.getPieceHeight();
        String maptpl = levelCfg.getMaptplStr();
        MapTemplate mt = null;
        if (maptpl == null || maptpl.equals("")) {
            mt = new RandomTemplate(levelCfg.getYSize(), levelCfg.getXSize(), levelCfg.getEmptyNum(), levelCfg.getObstacleNum());
        } else {
            mt = MapTemplate.parse(maptpl);
        }
        GameMap map = new RandomMap(mt);
        Byte[][] data = map.Data;
        Piece[][] pieces = new Piece[data.length][data[0].length];
        Piece.YSize = data.length;
        Piece.XSize = data[0].length;
        List<Integer> pos = new ArrayList<Integer>(data.length * data[0].length);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                Piece piece = new Piece(i, j);
                piece.setWidth(imageWidth);
                piece.setHeight(imageHeight);
//                piece.setEmpty(data[i][j] == GameSettings.GroundCardValue);
                if (data[i][j] == GameSettings.GroundCardValue) {
                    piece.setImageId(GameSettings.GroundCardValue);
                } else if (data[i][j] == GameSettings.EmptyCardValue) {
                    piece.setImageId(GameSettings.GroundCardValue);
                } else if (data[i][j] == GameSettings.ObstacleCardValue) {
                    // 只记录图片Id，界面负责创建图片
                    piece.setImageId(GameSettings.ObstacleCardValue);
                } else {
                    // 只记录图片Id，界面负责创建图片
                    piece.setImageId(data[i][j]);
                    pos.add(i * data[0].length + j);
                }
                piece.setBeginY(i * imageHeight + levelCfg.getBeginImageY());
                piece.setBeginX(j * imageWidth + levelCfg.getBeginImageX());
                pieces[i][j] = piece;
            }
        }

        // 设置是否为星星卡片
        if (levelCfg.getStars() != 0) {
            Collections.shuffle(pos);
            if (levelCfg.getStars() < pos.size()) {
                for (int i = 0; i < levelCfg.getStars(); i++) {
                    pieces[pos.get(i) / data[0].length][pos.get(i) % data[0].length].setStar(true);
                }
            }
        }
        return pieces;
    }

    protected MapTemplate Template;
}
