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
     * 本地数据库文件名
     */
    public static final String DbFileName = "data2.db";

    /**
     * 本地数据库的版本
     */
    public static final int DbVersion = 1;

    /**
     * 初始激活的关卡数
     */
    public final static int DefaultActiveNum = 45;

    /**
     * 两次back退出间的间隔
     */
    public final static int TwoBackExitInterval = 2000;

    /*
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

    /**
     * 游戏卡片
     */
    public static int[] CardImageIds = new int[] { R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7,
            R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14, R.drawable.p15, R.drawable.p16 };

}
