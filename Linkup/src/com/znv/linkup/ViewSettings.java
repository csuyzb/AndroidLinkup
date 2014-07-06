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
     * 用户登录消息
     */
    public static final int MSG_LOGIN = 0x11;

    /**
     * 用户取消登录消息
     */
    public static final int MSG_AUTH_CANCEL = 0x12;

    /**
     * 用户登录错误消息
     */
    public static final int MSG_AUTH_ERROR = 0x13;

    /**
     * 用户上传分数消息
     */
    public static final int MSG_SCORE_ADD = 0x14;

    /**
     * 用户获取分数排行榜
     */
    public static final int MSG_SCORE_GET = 0x15;

    /**
     * 用户上传时间
     */
    public static final int MSG_TIME_ADD = 0x16;

    /**
     * 用户获取时间排行榜
     */
    public static final int MSG_TIME_GET = 0x17;

    /**
     * 网络问题
     */
    public static final int MSG_NETWORK_EXCEPTION = 0x18;

    /**
     * 获取用户图标
     */
    public static final int MSG_IMAGE_GET = 0x19;

    /**
     * 获取排行榜图标
     */
    public static final int MSG_TOPIMAGES_GET = 0x20;

    /**
     * 每种皮肤的图片数量
     */
    public static int SkinImageCount = 16;

    /**
     * 最大的提示数
     */
    public static int PromptMaxNum = 10;

    /**
     * 最大的重排数
     */
    public static int RefreshMaxNum = 10;

    /**
     * 最大的重排数
     */
    public static int AddTimeMaxNum = 10;

    /**
     * 增加时间的秒数
     */
    public static int AddTimeSeconds = 5;

    /**
     * 排行榜图像宽度
     */
    public static int TopImageWidth = 50;

    /**
     * 用户图像宽度
     */
    public static int UserImageWidth = 60;

    /**
     * 昵称显示长度
     */
    public static int ShowNameLength = 11;

    /**
     * 数据缓存版本字符串
     */
    public static String CacheVersion = "120";

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
    public static int DbVersion = 3;

    /**
     * 初始激活的关卡，按升序排列
     */
    public static int[] DefaultActiveLevels = new int[] { 0, 1, 2, 3, 24, 25, 26, 27, 48, 49, 50, 51, 120, 121, 122, 123, 192, 193, 194, 195 };

    /**
     * 两次back退出间的间隔
     */
    public static int TwoBackExitInterval = 2000;

    /**
     * 游戏背景音乐
     */
    public static int[] BgMusics = new int[] { R.raw.bgmusic1, R.raw.bgmusic2 };

    /**
     * 数字图片
     */
    public static int[] Numbers = new int[] { R.drawable.n0, R.drawable.n1, R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5, R.drawable.n6,
            R.drawable.n7, R.drawable.n8, R.drawable.n9, R.drawable.n10, R.drawable.n11, R.drawable.n12, R.drawable.n13, R.drawable.n14, R.drawable.n15,
            R.drawable.n16, R.drawable.n17, R.drawable.n18, R.drawable.n19, R.drawable.n20, R.drawable.n21, R.drawable.n22, R.drawable.n23, R.drawable.n24 };

    /**
     * 游戏背景图片
     */
    public static int[] GameBgImageIds = new int[] { R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5 };

    /**
     * 等级名称前缀
     */
    public static String[] RankPrefix = new String[] { "一.", "二.", "三.", "四.", "五.", "六.", "七.", "八.", "九.", "十." };

    /**
     * 游戏评级
     */
    public static int[] StarImages = new int[] { R.drawable.star1, R.drawable.star2, R.drawable.star3 };
}
