package com.znv.linkup.core.config;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

/**
 * 游戏配置类，根据xml文件解析游戏关卡配置
 * 
 * @author yzb
 * 
 */
public class GameCfg {
    private XmlResourceParser xrp;

    public GameCfg(XmlPullParser xpp) {

        this.xrp = (XmlResourceParser) xpp;
        LoadConfig();
    }

    /**
     * 加载配置
     */
    public void LoadConfig() {
        if (xrp == null) {
            return;
        }

        ModeCfg modeInfo = null;
        RankCfg rankInfo = null;
        LevelCfg levelInfo = null;
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("game")) {
                        LoadGlobalCfg(xrp);
                    } else if (tagName.equals("mode")) {
                        if (modeInfo != null) {
                            if (rankInfo != null) {
                                if (levelInfo != null) {
                                    setlevelByMode(levelInfo, modeInfo);
                                    setlevelByRank(levelInfo, rankInfo);
                                    levelInfo.initStarScores();
                                    rankInfo.getLevelInfos().add(levelInfo);
                                    levelInfo = null;
                                }
                                modeInfo.getRankInfos().add(rankInfo);
                                rankInfo = null;
                            }
                            modeInfos.add(modeInfo);
                            modeInfo = null;
                        }
                        modeInfo = LoadModeCfg(xrp);
                    } else if (tagName.equals("rank")) {
                        if (rankInfo != null) {
                            if (levelInfo != null) {
                                setlevelByMode(levelInfo, modeInfo);
                                setlevelByRank(levelInfo, rankInfo);
                                levelInfo.initStarScores();
                                rankInfo.getLevelInfos().add(levelInfo);
                                levelInfo = null;
                            }
                            modeInfo.getRankInfos().add(rankInfo);
                            rankInfo = null;
                        }
                        rankInfo = LoadRankCfg(xrp);
                    } else if (tagName.equals("level")) {
                        if (rankInfo != null && levelInfo != null) {
                            setlevelByMode(levelInfo, modeInfo);
                            setlevelByRank(levelInfo, rankInfo);
                            levelInfo.initStarScores();
                            rankInfo.getLevelInfos().add(levelInfo);
                            levelInfo = null;
                        }
                        levelInfo = LoadLevelCfg(xrp);
                    }
                } else if (xrp.getEventType() == XmlResourceParser.END_TAG) {
                    String tagName = xrp.getName();
                    if (tagName.equals("mode")) {
                        if (modeInfo != null) {
                            if (rankInfo != null) {
                                if (levelInfo != null) {
                                    setlevelByMode(levelInfo, modeInfo);
                                    setlevelByRank(levelInfo, rankInfo);
                                    levelInfo.initStarScores();
                                    rankInfo.getLevelInfos().add(levelInfo);
                                    levelInfo = null;
                                }
                                modeInfo.getRankInfos().add(rankInfo);
                                rankInfo = null;
                            }
                            modeInfos.add(modeInfo);
                            modeInfo = null;
                        }
                    } else if (tagName.equals("rank")) {
                        if (rankInfo != null) {
                            if (levelInfo != null) {
                                setlevelByMode(levelInfo, modeInfo);
                                setlevelByRank(levelInfo, rankInfo);
                                levelInfo.initStarScores();
                                rankInfo.getLevelInfos().add(levelInfo);
                                levelInfo = null;
                            }
                            modeInfo.getRankInfos().add(rankInfo);
                            rankInfo = null;
                        }
                    } else if (tagName.equals("level")) {
                        if (rankInfo != null && levelInfo != null) {
                            setlevelByMode(levelInfo, modeInfo);
                            setlevelByRank(levelInfo, rankInfo);
                            levelInfo.initStarScores();
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

    private void setlevelByMode(LevelCfg levelInfo, ModeCfg modeInfo) {
        levelInfo.setLevelMode(GameMode.valueOf(Integer.parseInt(modeInfo.getModeId())));
    }

    private void setlevelByRank(LevelCfg levelInfo, RankCfg rankInfo) {
        levelInfo.setLevelBackground(rankInfo.getRankBackground());
        levelInfo.setRankName(rankInfo.getRankName());
        levelInfo.setGameSkin(rankInfo.getGameSkin());
    }

    /**
     * 加载全局配置
     * 
     * @param xrp
     *            xml节点
     */
    private void LoadGlobalCfg(XmlResourceParser xrp) {
        GlobalCfg gamecfg = new GlobalCfg();
        gamecfg.setGameSound(xrp.getAttributeValue(null, "gsound").equals("1"));
        gamecfg.setGameBgMusic(xrp.getAttributeValue(null, "bgmusic").equals("1"));
        gamecfg.setPromptNum(Integer.parseInt(xrp.getAttributeValue(null, "prompt")));
        gamecfg.setRefreshNum(Integer.parseInt(xrp.getAttributeValue(null, "refresh")));
        gamecfg.setAddTimeNum(Integer.parseInt(xrp.getAttributeValue(null, "addtime")));

        // 设置所有关卡的全局配置
        LevelCfg.globalCfg = gamecfg;
    }

    /**
     * 加载模式配置
     * 
     * @param xrp
     *            xml节点
     * @return 模式配置信息
     */
    private ModeCfg LoadModeCfg(XmlResourceParser xrp) {
        String name = xrp.getAttributeValue(null, "name");
        ModeCfg gMode = new ModeCfg(name);
        gMode.setModeId(String.valueOf(modeCount++));
        return gMode;
    }

    /**
     * 加载等级配置
     * 
     * @param xrp
     *            xml节点
     * @return 等级配置信息
     */
    private RankCfg LoadRankCfg(XmlResourceParser xrp) {
        String name = xrp.getAttributeValue(null, "name");
        RankCfg gRank = new RankCfg(name);
        gRank.setRankId(String.valueOf(rankCount++));
        gRank.setGameSkin(xrp.getAttributeValue(null, "gskin"));
        gRank.setRankBackground(Integer.parseInt(xrp.getAttributeValue(null, "bg")));
        return gRank;
    }

    /**
     * 加载关卡配置
     * 
     * @param xrp
     *            xml节点
     * @return 关卡配置信息
     */
    private LevelCfg LoadLevelCfg(XmlResourceParser xrp) {
        String name = xrp.getAttributeValue(null, "name");
        LevelCfg levelCfg = new LevelCfg(name);
        levelCfg.setLevelId(levelCount++);
        levelCfg.setYSize(Integer.parseInt(xrp.getAttributeValue(null, "ysize")));
        levelCfg.setXSize(Integer.parseInt(xrp.getAttributeValue(null, "xsize")));
        levelCfg.setLevelTime(Integer.parseInt(xrp.getAttributeValue(null, "gtime")));
        levelCfg.setLevelAlign(GameAlign.valueOf(Integer.parseInt(xrp.getAttributeValue(null, "galign"))));
        levelCfg.setEmptyNum(Integer.parseInt(xrp.getAttributeValue(null, "empty")));
        levelCfg.setObstacleNum(Integer.parseInt(xrp.getAttributeValue(null, "obstacle")));
        String starStr = xrp.getAttributeValue(null, "star");
        if (starStr != null) {
            levelCfg.setStars(Integer.parseInt(starStr));
        }
        levelCfg.setMaptplStr(xrp.getAttributeValue(null, "maptpl"));
        return levelCfg;
    }

    /**
     * 获取游戏等级信息列表
     * 
     * @return 游戏等级信息列表
     */
    public List<ModeCfg> getModeInfos() {
        return modeInfos;
    }

    private int levelCount = 0;
    private int rankCount = 0;
    private int modeCount = 0;
    private List<ModeCfg> modeInfos = new ArrayList<ModeCfg>();
}
