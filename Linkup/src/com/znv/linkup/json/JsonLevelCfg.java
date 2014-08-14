package com.znv.linkup.json;

public class JsonLevelCfg {
    private String name;
    private int ysize;
    private int xsize;
    private int gtime;
    private int galign;
    private int empty;
    private int obstacle;
    private String maptpl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYsize() {
        return ysize;
    }

    public void setYsize(int ysize) {
        this.ysize = ysize;
    }

    public int getXsize() {
        return xsize;
    }

    public void setXsize(int xsize) {
        this.xsize = xsize;
    }

    public int getGtime() {
        return gtime;
    }

    public void setGtime(int gtime) {
        this.gtime = gtime;
    }

    public int getGalign() {
        return galign;
    }

    public void setGalign(int galign) {
        this.galign = galign;
    }

    public int getEmpty() {
        return empty;
    }

    public void setEmpty(int empty) {
        this.empty = empty;
    }

    public int getObstacle() {
        return obstacle;
    }

    public void setObstacle(int obstacle) {
        this.obstacle = obstacle;
    }

    public String getMaptpl() {
        return maptpl;
    }

    public void setMaptpl(String maptpl) {
        this.maptpl = maptpl;
    }
}
