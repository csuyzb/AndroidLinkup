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
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.sound.MusicServer;
import com.znv.linkup.util.CacheUtil;

public class FullScreenActivity extends Activity {

    protected static MusicServer musicServer = null;
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

                if (musicServer == null) {
                    musicServer = new MusicServer(FullScreenActivity.this);
                }
            }
        }).start();
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
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, CacheUtil.getMetaValue(this, "api_key"));
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

        loadLevelScores();
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
        super.onPause();
        if (musicServer != null) {
            musicServer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicServer != null) {
            musicServer.resume();
        }
    }

}
