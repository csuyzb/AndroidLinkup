package com.znv.linkup.json;

import java.util.ArrayList;
import java.util.List;

public class JsonRankCfg {
    private String name;
    private int bg;
    private String gskin;
    private List<JsonLevelCfg> levelCfgs = new ArrayList<JsonLevelCfg>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public String getGskin() {
        return gskin;
    }

    public void setGskin(String gskin) {
        this.gskin = gskin;
    }

    public List<JsonLevelCfg> getLevelCfgs() {
        return levelCfgs;
    }

    public void setLevelCfgs(List<JsonLevelCfg> levelCfgs) {
        this.levelCfgs = levelCfgs;
    }
}
