package com.znv.linkup.core;

/**
 * 游戏相关的设置
 * 
 * @author yzb
 * 
 */
public class GameSettings {

    /**
     * 地板卡片值
     */
    public static byte GroundCardValue = 0;

    /**
     * 游戏卡片值，用于地图模版
     */
    public static byte GameCardValue = 1;

    /**
     * 障碍卡片值
     */
    public static byte ObstacleCardValue = -1;

    /**
     * 空卡片值
     */
    public static byte EmptyCardValue = -2;

    /**
     * 消除卡片获得的积分，消除一对卡片得10分
     */
    public static int CardScore = 5;

    /**
     * 连击延时时间，2000ms
     */
    public static int ComboDelay = 2000;

    /**
     * 连击间隔，3连击，6连击，9连击。。。
     */
    public static int ComboMod = 3;

    /**
     * 连击分数，3连击则3*10=30
     */
    public static int ComboScore = 10;

    /**
     * 剩余时间奖励分数，剩余10s，则奖励40分
     */
    public static int TimeScore = 4;

    /**
     * 路径转弯奖励分数，直线不奖励，一个弯奖励5分，2个弯奖励10分
     */
    public static int CornerScore = 5;

    /**
     * 消除一对卡片奖励的时间，0s
     */
    public static int RewardTime = 0;

    /**
     * 减少时间奖励，补偿分数
     */
    public static int RewardScoreMax = 5;

    /**
     * 任务系数
     */
    public static int TaskFactor = 10;

    /**
     * 重排时尝试次数，默认5次
     */
    public static int RefreshTryCount = 5;

    /**
     * 星星的比例
     */
    public static float StarRatio = 0.4f;
}
