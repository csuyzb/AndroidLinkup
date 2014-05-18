package com.znv.linkup;

import com.znv.linkup.R;

/**
 * 视图配置
 * 
 * @author yzb
 * 
 */
public class ViewSettings {

    /**
     * 游戏时间消息
     */
    public final static int TimeMessage = 0x1;

    /**
     * 游戏分数消息
     */
    public final static int ScoreMessage = 0x2;

    /**
     * 游戏失败消息
     */
    public final static int FailMessage = 0x3;

    /**
     * 游戏胜利消息
     */
    public final static int WinMessage = 0x4;

    /**
     * 游戏提示消息
     */
    public final static int PromptMessage = 0x5;

    /**
     * 游戏重排消息
     */
    public final static int RefreshMessage = 0x6;

    /**
     * 每种皮肤的图片数量
     */
    public static int SkinImageCount = 16;

    /**
     * 增加提示的连击数
     */
    public static int ComboAddPrompt = 3;

    /**
     * 增加重排的连击数
     */
    public static int ComboAddRefresh = 6;

    /**
     * 最大的提示数
     */
    public static int PromptMaxNum = 10;

    /**
     * 最大的重排数
     */
    public static int RefreshMaxNum = 10;

    /**
     * 选择框的宽度
     */
    public static int CheckLineWidth = 6;

    /**
     * 消除路径的宽度
     */
    public static int PathWidth = 6;

    /**
     * 数据缓存版本字符串
     */
    public static String CacheVersion = "120_3";

    /**
     * 全局配置的 Shared Preference
     */
    public static String GlobalCfgStr = "globalcfg_" + CacheVersion;

    /**
     * 本地数据库文件名
     */
    public static String DbFileName = "data" + CacheVersion + ".db";

    /**
     * 本地数据库的版本
     */
    public static int DbVersion = 1;

    /**
     * 初始激活的关卡，按升序排列
     */
    public static int[] DefaultActiveLevels = new int[] { 0, 1, 2, 24, 25, 26 };

    /**
     * 两次back退出间的间隔
     */
    public static int TwoBackExitInterval = 2000;

    /**
     * 豌豆荚广告位ID
     */
    public static String ADS_ID = "6a7a78bf496696865c0815dabc1c4606";

    /**
     * 豌豆荚广告Appkey ID
     */
    public static String ADS_APP_ID = "100004467";

    /**
     * 豌豆荚广告Secret Key
     */
    public static String ADS_SECRET_KEY = "0efaaffde0edf35c5352985dcc15c20c";

    /**
     * Rank的标题背景颜色
     */
    public static int[] RankTitleBgColor = new int[] { R.color.rank_title_color_1, R.color.rank_title_color_2, R.color.rank_title_color_3 };

    /**
     * Rank的关卡背景图片
     */
    public static int[] RankLevelBgImages = new int[] { R.drawable.circle_green, R.drawable.circle_blue, R.drawable.circle_orange };

    /**
     * Rank背景图片
     */
    public static int[] RankBgImageIds = new int[] { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3 };
}
