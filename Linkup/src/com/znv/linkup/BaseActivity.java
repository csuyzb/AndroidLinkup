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
import com.znv.linkup.util.ShortcutUtil;

/**
 * 游戏Activity的基类
 * 
 * @author yzb
 * 
 */
public class BaseActivity extends Activity {

    // 游戏背景音乐
    protected static MusicManager musicMgr = null;
    // 游戏音效
    protected static SoundManager soundMgr = null;
    // 游戏关卡配置
    protected static List<RankCfg> rankCfgs = null;
    // 游戏配置Map
    protected static Map<String, LevelCfg> levelCfgs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFullScreen();

        initBaiduPush();

        initShortcut();

        initMusic();

        initSound();

        // 单独开线程加载配置
        new Thread(new Runnable() {

            @Override
            public void run() {
                loadCfgs();
            }
        }).start();
    }

    /**
     * 全屏初始化
     */
    private void initFullScreen() {
        // set full screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化百度推送
     */
    private void initBaiduPush() {
        if (!CacheUtil.hasBind(getApplicationContext())) {
            // Push: 无账号初始化，用api key绑定
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, AppMetaUtil.getMetaValue(this, "api_key"));
        }
    }

    /**
     * 初始化快捷键
     */
    private void initShortcut() {
        ShortcutUtil util = new ShortcutUtil(this);
        if (!CacheUtil.hasBind(this, "short_cut")) {
            util.delShortcut();
            util.addShortcut();
            CacheUtil.setBind(this, "short_cut", true);
        }
    }

    /**
     * 初始化游戏音乐
     */
    private void initMusic() {
        if (musicMgr == null) {
            musicMgr = new MusicManager(this);
        }
        musicMgr.play();
    }

    /**
     * 初始化游戏音效
     */
    private void initSound() {
        if (soundMgr == null) {
            soundMgr = new SoundManager(BaseActivity.this);
        }

    }

    /**
     * 加载关卡配置
     */
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

        // 加载全局配置
        loadGlobalCfg();

        // 加载本地关卡分数
        loadLevelScores();
    }

    /**
     * 加载全局配置
     */
    private void loadGlobalCfg() {
        String globalCfgStr = getGlobalCfg();
        if (globalCfgStr.equals("")) {
            setGlobalCfg();
        } else {
            LevelCfg.globalCfg = GlobalCfg.parse(globalCfgStr);
        }
    }

    /**
     * 加载本地关卡分数
     */
    private void loadLevelScores() {
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

    /**
     * 获取全局配置字符串
     * 
     * @return 全局配置字符串
     */
    protected String getGlobalCfg() {
        return CacheUtil.getBindStr(getApplicationContext(), "globalcfg");
    }

    /**
     * 设置全局配置
     */
    protected void setGlobalCfg() {
        CacheUtil.setBindStr(getApplicationContext(), "globalcfg", LevelCfg.globalCfg.toString());
    }

    @Override
    protected void onPause() {
        // 停止背景音乐
        musicMgr.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // 开启背景音乐
        musicMgr.play();
        super.onResume();
    }

}
