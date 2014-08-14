package com.znv.linkup.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.znv.linkup.core.config.GameAlign;
import com.znv.linkup.core.config.GlobalCfg;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.ModeCfg;
import com.znv.linkup.core.config.RankCfg;

/**
 * Json配置和原有的游戏配置相互转化
 * 
 * @author yzb
 * 
 */
public class JsonCfgConverter {
    private static int modeIndex = 0;
    private static int rankIndex = 0;
    private static int levelIndex = 0;

    /**
     * 将游戏配置转化为Json配置
     * 
     * @param modeCfgs
     *            游戏模式配置
     * @return Json配置
     */
    public static JsonGameCfg toJsonCfg(List<ModeCfg> modeCfgs) {
        JsonGameCfg jsonGame = new JsonGameCfg();
        jsonGame.setGlobalcfg(toJsonGlobalCfg());

        for (ModeCfg modeCfg : modeCfgs) {
            jsonGame.getModeCfgs().add(toJsonModeCfg(modeCfg));
        }

        return jsonGame;
    }

    private static JsonGlobalCfg toJsonGlobalCfg() {
        JsonGlobalCfg jsonGlobal = new JsonGlobalCfg();
        jsonGlobal.setGsound(LevelCfg.globalCfg.isGameSound() ? 1 : 0);
        jsonGlobal.setBgmusic(LevelCfg.globalCfg.isGameBgMusic() ? 1 : 0);
        jsonGlobal.setPrompt(LevelCfg.globalCfg.getPromptNum());
        jsonGlobal.setRefresh(LevelCfg.globalCfg.getRefreshNum());
        jsonGlobal.setAddtime(LevelCfg.globalCfg.getAddTimeNum());
        return jsonGlobal;
    }

    private static JsonModeCfg toJsonModeCfg(ModeCfg modeCfg) {
        JsonModeCfg jsonMode = new JsonModeCfg();
        jsonMode.setName(modeCfg.getModeName());
        for (RankCfg rankCfg : modeCfg.getRankInfos()) {
            jsonMode.getRankCfgs().add(toJsonRankCfg(rankCfg));
        }

        return jsonMode;
    }

    private static JsonRankCfg toJsonRankCfg(RankCfg rankCfg) {
        JsonRankCfg jsonRank = new JsonRankCfg();
        jsonRank.setName(rankCfg.getRankName());
        jsonRank.setBg(rankCfg.getRankBackground());
        jsonRank.setGskin(rankCfg.getGameSkin());

        for (LevelCfg levelCfg : rankCfg.getLevelInfos()) {
            jsonRank.getLevelCfgs().add(toJsonLevelCfg(levelCfg));
        }

        return jsonRank;
    }

    private static JsonLevelCfg toJsonLevelCfg(LevelCfg levelCfg) {
        JsonLevelCfg jsonLevel = new JsonLevelCfg();
        jsonLevel.setName(levelCfg.getLevelName());
        jsonLevel.setYsize(levelCfg.getYSize());
        jsonLevel.setXsize(levelCfg.getXSize());
        jsonLevel.setGtime(levelCfg.getLevelTime());
        jsonLevel.setGalign(levelCfg.getLevelAlign().value());
        jsonLevel.setEmpty(levelCfg.getEmptyNum());
        jsonLevel.setObstacle(levelCfg.getObstacleNum());
        jsonLevel.setMaptpl(levelCfg.getMaptplStr());
        return jsonLevel;
    }

    /**
     * 将Json配置转化为游戏配置
     * 
     * @param jsonCfg
     *            Json配置
     * @return 游戏模式配置
     */
    public static List<ModeCfg> toCfg(JSONObject jsonCfg) {
        modeIndex = 0;
        rankIndex = 0;
        levelIndex = 0;
        List<ModeCfg> modeCfgs = new ArrayList<ModeCfg>();
        try {
            toGlobalCfg(jsonCfg.getJSONObject("globalcfg"));
            JSONArray jsonModes = jsonCfg.getJSONArray("modeCfgs");
            for (int i = 0; i < jsonModes.length(); i++) {
                ModeCfg modeCfg = toModeCfg(jsonModes.getJSONObject(i));
                if (modeCfg != null) {
                    modeCfgs.add(modeCfg);
                }
            }
            return modeCfgs;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static GlobalCfg toGlobalCfg(JSONObject jsonGlobal) {
        try {
            GlobalCfg globalCfg = new GlobalCfg();
            globalCfg.setGameSound(jsonGlobal.getInt("gsound") == 1);
            globalCfg.setGameBgMusic(jsonGlobal.getInt("bgmusic") == 1);
            globalCfg.setPromptNum(jsonGlobal.getInt("prompt"));
            globalCfg.setRefreshNum(jsonGlobal.getInt("refresh"));
            globalCfg.setAddTimeNum(jsonGlobal.getInt("addtime"));

            return globalCfg;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ModeCfg toModeCfg(JSONObject jsonMode) {
        try {
            String name = jsonMode.getString("name");
            ModeCfg modeCfg = new ModeCfg(name);
            modeCfg.setModeId(String.valueOf(modeIndex++));
            JSONArray jsonRankCfgs = jsonMode.getJSONArray("rankCfgs");
            for (int i = 0; i < jsonRankCfgs.length(); i++) {
                RankCfg rankCfg = toRankCfg(jsonRankCfgs.getJSONObject(i));
                if (rankCfg != null) {
                    modeCfg.getRankInfos().add(rankCfg);
                }
            }
            return modeCfg;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static RankCfg toRankCfg(JSONObject jsonRank) {
        try {
            String name = jsonRank.getString("name");
            RankCfg rankCfg = new RankCfg(name);
            rankCfg.setRankId(String.valueOf(rankIndex++));
            rankCfg.setGameSkin(jsonRank.getString("gskin"));
            rankCfg.setRankBackground(jsonRank.getInt("bg"));
            JSONArray jsonLevelCfgs = jsonRank.getJSONArray("levelCfgs");
            for (int i = 0; i < jsonLevelCfgs.length(); i++) {
                LevelCfg levelCfg = toLevelCfg(jsonLevelCfgs.getJSONObject(i));
                if (levelCfg != null) {
                    rankCfg.getLevelInfos().add(levelCfg);
                }
            }
            return rankCfg;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LevelCfg toLevelCfg(JSONObject jsonLevel) {
        try {
            String name = jsonLevel.getString("name");
            LevelCfg levelCfg = new LevelCfg(name);
            levelCfg.setLevelId(levelIndex++);
            levelCfg.setYSize(jsonLevel.getInt("ysize"));
            levelCfg.setXSize(jsonLevel.getInt("xsize"));
            levelCfg.setLevelTime(jsonLevel.getInt("gtime"));
            levelCfg.setLevelAlign(GameAlign.valueOf(jsonLevel.getInt("galign")));
            levelCfg.setEmptyNum(jsonLevel.getInt("empty"));
            levelCfg.setObstacleNum(jsonLevel.getInt("obstacle"));
            levelCfg.setMaptplStr(jsonLevel.getString("maptpl"));
            levelCfg.initStarScores();
            return levelCfg;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
