package com.znv.linkup.core.config;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

import com.znv.linkup.core.card.align.GameAlign;

public class GameCfg {
    private XmlResourceParser xrp;

    public GameCfg(XmlPullParser xpp) {

        this.xrp = (XmlResourceParser) xpp;
        LoadConfig();
    }

    public void LoadConfig() {
        if (xrp == null) {
            return;
        }

        RankCfg rankInfo = null;
        LevelCfg levelInfo = null;
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("game")) {
                        LoadGlobalCfg(xrp);
                    } else if (tagName.equals("rank")) {
                        if (rankInfo != null) {
                            if (levelInfo != null) {
                                setlevelByRank(levelInfo, rankInfo);
                                rankInfo.getLevelInfos().add(levelInfo);
                                levelInfo = null;
                            }
                            rankInfos.add(rankInfo);
                            rankInfo = null;
                        }
                        rankInfo = LoadRankCfg(xrp);
                    } else if (tagName.equals("level")) {
                        if (rankInfo != null && levelInfo != null) {
                            setlevelByRank(levelInfo, rankInfo);
                            rankInfo.getLevelInfos().add(levelInfo);
                            levelInfo = null;
                        }
                        levelInfo = LoadLevelCfg(xrp);
                    }
                } else if (xrp.getEventType() == XmlResourceParser.END_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("rank")) {
                        if (rankInfo != null) {
                            if (levelInfo != null) {
                                setlevelByRank(levelInfo, rankInfo);
                                rankInfo.getLevelInfos().add(levelInfo);
                                levelInfo = null;
                            }
                            rankInfos.add(rankInfo);
                            rankInfo = null;
                        }
                    } else if (tagName.equals("level")) {
                        if (rankInfo != null && levelInfo != null) {
                            setlevelByRank(levelInfo, rankInfo);
                            rankInfo.getLevelInfos().add(levelInfo);
                            levelInfo = null;
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setlevelByRank(LevelCfg levelInfo, RankCfg rankInfo) {
        levelInfo.setLevelBackground(rankInfo.getRankBackground());
        levelInfo.setRankName(rankInfo.getRankName());
    }

    private void LoadGlobalCfg(XmlResourceParser xrp) {
        GlobalCfg gamecfg = new GlobalCfg();
        gamecfg.setGameSkin(xrp.getAttributeValue(null, "gskin"));
        gamecfg.setGameSound(xrp.getAttributeValue(null, "gsound").equals("1"));
        gamecfg.setGameBgMusic(xrp.getAttributeValue(null, "bgmusic").equals("1"));
        gamecfg.setPromptNum(Integer.parseInt(xrp.getAttributeValue(null, "prompt")));
        gamecfg.setPauseNum(Integer.parseInt(xrp.getAttributeValue(null, "pause")));

        // 设置所有关卡的全局配置
        LevelCfg.globalCfg = gamecfg;
    }

    private RankCfg LoadRankCfg(XmlResourceParser xrp) {
        String name = xrp.getAttributeValue(null, "name");
        RankCfg gRank = new RankCfg(name);
        gRank.setRankId(String.valueOf(rankCount++));
        gRank.setRankBackground(Integer.parseInt(xrp.getAttributeValue(null, "bg")));
        return gRank;
    }

    private LevelCfg LoadLevelCfg(XmlResourceParser xrp) {
        String name = xrp.getAttributeValue(null, "name");
        LevelCfg levelCfg = new LevelCfg(name);
        levelCfg.setLevelId(String.valueOf(levelCount++));
        levelCfg.setYSize(Integer.parseInt(xrp.getAttributeValue(null, "ysize")));
        levelCfg.setXSize(Integer.parseInt(xrp.getAttributeValue(null, "xsize")));
        levelCfg.setLevelTime(Integer.parseInt(xrp.getAttributeValue(null, "gtime")));
        levelCfg.setLevelAlign(GameAlign.valueOf(Integer.parseInt(xrp.getAttributeValue(null, "galign"))));
        levelCfg.setEmptyNum(Integer.parseInt(xrp.getAttributeValue(null, "empty")));
        levelCfg.setObstacleNum(Integer.parseInt(xrp.getAttributeValue(null, "obstacle")));
        levelCfg.setMaptplStr(xrp.getAttributeValue(null, "maptpl"));
        return levelCfg;
    }

    private int levelCount = 0;
    private int rankCount = 0;
    private List<RankCfg> rankInfos = new ArrayList<RankCfg>();

    public int getLevelCount() {
        return levelCount;
    }

    public int getRankCount() {
        return rankCount;
    }

    public List<RankCfg> getRankInfos() {
        return rankInfos;
    }
}
