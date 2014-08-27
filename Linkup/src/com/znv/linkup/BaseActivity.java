package com.znv.linkup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Window;
import android.view.WindowManager;

import com.znv.linkup.core.config.GameCfg;
import com.znv.linkup.core.config.GlobalCfg;
import com.znv.linkup.core.config.LevelCfg;
import com.znv.linkup.core.config.ModeCfg;
import com.znv.linkup.core.config.RankCfg;
import com.znv.linkup.core.util.ImageUtil;
import com.znv.linkup.db.DbScore;
import com.znv.linkup.db.LevelScore;
import com.znv.linkup.rest.UserInfo;
import com.znv.linkup.sound.MusicManager;
import com.znv.linkup.sound.SoundManager;
import com.znv.linkup.util.CacheUtil;
import com.znv.linkup.util.ShortcutUtil;
import com.znv.linkup.util.Stopwatch;
import com.znv.linkup.view.indicator.RankAdapter;

/**
 * 游戏Activity的基类
 * 
 * @author yzb
 * 
 */
public class BaseActivity extends Activity {

    // 游戏背景音乐
    public static MusicManager musicMgr = null;
    // 游戏音效
    public static SoundManager soundMgr = null;
    // 游戏关卡配置
    public static List<ModeCfg> modeCfgs = null;
    // 游戏配置Map
    public static SparseArray<LevelCfg> levelCfgs = null;
    // 第三方登录用户信息
    public static UserInfo userInfo = null;
    // rankAdapter缓存
    public static RankAdapter[] rankAdapters = null;
    // 根据游戏皮肤缓存图片，不用每次加载
    public static List<List<Bitmap>> skinImages = new ArrayList<List<Bitmap>>(ViewSettings.SkinNames.length);
    // 缓存特定比例的卡片
    public static Map<String, List<Bitmap>> scaleImages = new HashMap<String, List<Bitmap>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFullScreen();

        initShortcut();

        initMusic();

        initSound();
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
     * 初始化快捷键
     */
    private void initShortcut() {
        if (!CacheUtil.hasBind(this, "short_cut")) {
            ShortcutUtil util = new ShortcutUtil(this);
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
        playMusic();
    }

    /**
     * 播放背景音乐
     */
    protected void playMusic() {
        if (musicMgr != null) {
            musicMgr.play();
        }
    }

    /**
     * 停止播放背景音乐
     */
    protected void stopMusic() {
        if (musicMgr != null) {
            musicMgr.stop();
        }
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
    protected void loadCfgs() {
        if (modeCfgs == null) {
            Stopwatch sw = new Stopwatch();
            sw.start();
            XmlResourceParser xrp = getResources().getXml(R.xml.gamecfg);
            GameCfg gameCfg = new GameCfg(xrp);
            modeCfgs = gameCfg.getModeInfos();
            levelCfgs = new SparseArray<LevelCfg>();
            for (ModeCfg modeCfg : modeCfgs) {
                for (RankCfg rankCfg : modeCfg.getRankInfos()) {
                    for (LevelCfg levelCfg : rankCfg.getLevelInfos()) {
                        levelCfgs.append(levelCfg.getLevelId(), levelCfg);
                    }
                }
            }
            sw.stop();
            Log.e("loadXmlCfgs-time", String.valueOf(sw.getElapsedTime()));
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
        DbScore.init(this, modeCfgs);

        List<LevelScore> levelScores = DbScore.selectAll();
        for (LevelScore score : levelScores) {
            int levelId = Integer.parseInt(score.getLevel());
            LevelCfg cfg = levelCfgs.get(levelId);
            if (cfg != null) {
                cfg.setMaxScore(score.getMaxScore());
                cfg.setMinTime(score.getMinTime());
                cfg.setActive(score.getIsActive() == 1);
                cfg.setLevelStar(score.getStar());
                cfg.setUpload(score.getIsUpload() == 1);
            }
        }
    }

    /**
     * 加载rankAdapter
     */
    protected void loadRankAdapters() {
        rankAdapters = new RankAdapter[modeCfgs.size()];
        for (int i = 0; i < modeCfgs.size(); i++) {
            rankAdapters[i] = new RankAdapter(this, modeCfgs.get(i).getRankInfos());
        }
    }

    /**
     * 加载游戏皮肤
     */
    protected void loadSkinImages() {
        InputStream is = null;
        for (String skinName : ViewSettings.SkinNames) {
            String imageFile = String.format("%ss.dat", skinName);
            try {
                is = getResources().getAssets().open(imageFile);
                Bitmap bm = BitmapFactory.decodeStream(is);
                List<Bitmap> images = ImageUtil.cutImage(bm, ViewSettings.ImageXCount, ViewSettings.ImageYCount);
                if (images != null) {
                    // 加入缓存
                    skinImages.add(images);
                }
            } catch (Exception e) {
                Log.d("loadSkinImages", e.getMessage());
            }
        }

        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取全局配置字符串
     * 
     * @return 全局配置字符串
     */
    protected String getGlobalCfg() {
        return CacheUtil.getBindStr(getApplicationContext(), ViewSettings.GlobalCfgStr);
    }

    /**
     * 设置全局配置
     */
    public void setGlobalCfg() {
        CacheUtil.setBindStr(getApplicationContext(), ViewSettings.GlobalCfgStr, LevelCfg.globalCfg.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 停止背景音乐
        stopMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 开启背景音乐
        playMusic();
    }

}
