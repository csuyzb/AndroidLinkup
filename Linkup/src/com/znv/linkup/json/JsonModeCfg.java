package com.znv.linkup.json;

import java.util.ArrayList;
import java.util.List;

public class JsonModeCfg {
    private String name;
    private List<JsonRankCfg> rankCfgs = new ArrayList<JsonRankCfg>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JsonRankCfg> getRankCfgs() {
        return rankCfgs;
    }

    public void setRankCfgs(List<JsonRankCfg> rankCfgs) {
        this.rankCfgs = rankCfgs;
    }
}
