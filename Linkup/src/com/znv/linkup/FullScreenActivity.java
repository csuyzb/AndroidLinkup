package com.znv.linkup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.znv.linkup.core.config.GameCfg;
import com.znv.linkup.core.config.GlobalCfg;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.sound.MusicManager;
import com.znv.linkup.sound.SoundManager;
import com.znv.linkup.util.AppMetaUtil;
import com.znv.linkup.util.CacheUtil;

public class FullScreenActivity extends Activity {

    protected static MusicManager musicMgr = null;
    protected static SoundManager soundMgr = null;
    protected static List<RankCfg> rankCfgs = null;
    protected static Map<String, LevelCfg> levelCfgs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFullScreen();

        initBaiduPush();

        new Thread(new Runnable() {

            @Override
            public void run() {
                loadCfgs();
            }
        }).start();

        if (soundMgr == null) {
            soundMgr = new SoundManager(FullScreenActivity.this);
        }

        if (musicMgr == null) {
            musicMgr = new MusicManager(this);
        }
        musicMgr.play();
    }

    private void initFullScreen() {
        // set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // 以apikey的方式绑定
    private void initBaiduPush() {
        if (!CacheUtil.hasBind(getApplicationContext())) {
            // Push: 无账号初始化，用api key绑定
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, AppMetaUtil.getMetaValue(this, "api_key"));
        }
    }

    private void loadCfgs() {
        if (rankCfgs == null) {
            XmlResourceParser xrp = getResources().getXml(R.xml.gamecfg);
            GameCfg gameCfg = new GameCfg(xrp);
            rankCfgs = gameCfg.getRankInfos();
            levelCfgs = new HashMap<String, LevelCfg>();
            for (RankCfg rankCfg : rankCfgs) {
                for (LevelCfg levelCfg : rankCfg.getLevelInfos()) {
                    levelCfgs.put(levelCfg.getLevelId(), levelCfg);
                }
            }

            int menuWidth = BitmapFactory.decodeResource(getResources(), R.drawable.menu).getWidth();
            LevelCfg.globalCfg.setMenuWidth(menuWidth);
        }

        loadGlobalCfg();

        loadLevelScores();
    }

    protected void loadGlobalCfg() {
        String globalCfgStr = getGlobalCfg();
        if (globalCfgStr.equals("")) {
            setGlobalCfg();
        } else {
            LevelCfg.globalCfg = GlobalCfg.parse(globalCfgStr);
        }
    }

    protected String getGlobalCfg() {
        return CacheUtil.getBindStr(getApplicationContext(), "globalcfg");
    }

    protected void setGlobalCfg() {
        CacheUtil.setBindStr(getApplicationContext(), "globalcfg", LevelCfg.globalCfg.toString());
    }

    protected void loadLevelScores() {
        // 初始化数据库
        DbScore.init(this, rankCfgs);

        List<LevelScore> levelScores = DbScore.selectAll();
        for (LevelScore score : levelScores) {
            String levelId = score.getLevel();
            if (levelCfgs.containsKey(levelId)) {
                LevelCfg cfg = levelCfgs.get(levelId);
                if (cfg != null) {
                    cfg.setMaxScore(score.getMaxScore());
                    cfg.setActive(score.getIsActive() == 1);
                    cfg.setLevelStar(score.getStar());
                }
            }
        }
    }

    @Override
    protected void onPause() {
        musicMgr.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        musicMgr.play();
        super.onResume();
    }

    // @Override
    // protected void onStop() {
    // musicMgr.stop();
    // super.onStop();
    // }

}
