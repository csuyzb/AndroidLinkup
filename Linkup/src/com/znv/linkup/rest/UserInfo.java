package com.znv.linkup.rest;

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

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public int getPlatVersion() {
        return platVersion;
    }

    public void setPlatVersion(int platVersion) {
        this.platVersion = platVersion;
    }

    private String userId;
    private String userName;
    private String userGender;
    private String userIcon;
    private String plat;
    private int platVersion;
}
