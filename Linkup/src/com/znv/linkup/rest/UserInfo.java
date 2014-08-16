package com.znv.linkup.rest;

import android.content.Context;
import android.graphics.Bitmap;

import com.znv.linkup.util.CacheUtil;

/**
 * 第三方登录用户信息
 * 
 * @author yzb
 * 
 */
public class UserInfo {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getDiamond(Context context) {
        String d = CacheUtil.getBindStr(context, userId + "_diamond");
        if (d == null || d.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(d);
        }
    }

    public void setDiamond(Context context, int diamond) {
        CacheUtil.setBindStr(context, userId + "_diamond", String.valueOf(diamond));
    }

    public void addDiamond(Context context, int addDiamond) {
        CacheUtil.setBindStr(context, userId + "_diamond", String.valueOf(getDiamond(context) + (addDiamond)));
    }

    public int getGold(Context context) {
        String d = CacheUtil.getBindStr(context, userId + "_gold");
        if (d == null || d.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(d);
        }
    }

    public void setGold(Context context, int gold) {
        CacheUtil.setBindStr(context, userId + "_gold", String.valueOf(gold));
    }

    public void addGold(Context context, int addGold) {
        CacheUtil.setBindStr(context, userId + "_gold", String.valueOf(getGold(context) + (addGold)));
    }

    public boolean isAward() {
        return isAward;
    }

    public void setAward(boolean isAward) {
        this.isAward = isAward;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public int getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(int totalRank) {
        this.totalRank = totalRank;
    }

    public int getTop1Levels() {
        return top1Levels;
    }

    public void setTop1Levels(int top1Levels) {
        this.top1Levels = top1Levels;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    private String userId;
    private String userName;
    private String userGender;
    private String userIcon;
    private boolean isAward = false;
    private Bitmap userImage;
    private int totalRank;
    private int top1Levels;
    private int like;
}
