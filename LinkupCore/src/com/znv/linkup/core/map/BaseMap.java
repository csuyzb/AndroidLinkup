package com.znv.linkup.core.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 游戏地图基类，处理地图模版和地图
 * 
 * @author yzb
 * 
 */
public class BaseMap implements Serializable {
    private static final long serialVersionUID = 7379717910848419154L;

    protected BaseMap() {
    }

    protected BaseMap(int ySize, int xSize) {
        YSize = ySize;
        XSize = xSize;
        Data = new Byte[ySize][xSize];
    }

    /**
     * 随机重排
     */
    public void randomRefresh() {
        int index = 0;
        // 获取游戏块的值
        List<Byte> array = new ArrayList<Byte>();
        for (int i = 0; i < YSize; i++) {
            for (int j = 0; j < XSize; j++) {
                if (isRefresh(i, j)) {
                    array.add(Data[i][j]);
                }
            }
        }
        // 随机打乱
        Collections.shuffle(array);
        // 重新赋值
        index = 0;
        for (int i = 0; i < YSize; i++) {
            for (int j = 0; j < XSize; j++) {
                if (isRefresh(i, j)) {
                    Data[i][j] = array.get(index++);
                }
            }
        }
    }

    protected boolean isRefresh(int i, int j) {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder strMap = new StringBuilder(String.valueOf(YSize) + "*" + String.valueOf(XSize) + "$");
        for (int i = 0; i < YSize; i++) {
            for (int j = 0; j < XSize; j++) {
                strMap.append(String.valueOf(Data[i][j]) + ",");
            }
            strMap.replace(strMap.length() - 1, strMap.length(), ";");
        }
        strMap.deleteCharAt(strMap.length() - 1);
        return strMap.toString();
    }

    protected int YSize;
    protected int XSize;
    protected Byte[][] Data;
}
