package com.znv.linkup.json;

import java.util.ArrayList;
import java.util.List;

public class JsonGameCfg {
    private JsonGlobalCfg globalcfg;
    private List<JsonModeCfg> modeCfgs = new ArrayList<JsonModeCfg>();

    public JsonGlobalCfg getGlobalcfg() {
        return globalcfg;
    }

    public void setGlobalcfg(JsonGlobalCfg globalcfg) {
        this.globalcfg = globalcfg;
    }

    public List<JsonModeCfg> getModeCfgs() {
        return modeCfgs;
    }

    public void setModeCfgs(List<JsonModeCfg> modeCfgs) {
        this.modeCfgs = modeCfgs;
    }
}
