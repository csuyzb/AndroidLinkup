package com.znv.linkup.core.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏模式,闯关模式，计时模式，任务模式等
 * 
 * @author yzb
 * 
 */
public class ModeCfg {

    public ModeCfg(String modeName) {
        this.modeName = modeName;
    }

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public List<RankCfg> getRankInfos() {
        return rankInfos;
    }

    private String modeId;
    private String modeName;
    private List<RankCfg> rankInfos = new ArrayList<RankCfg>();
}
