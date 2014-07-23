package com.znv.linkup.rest;

import android.os.Message;

/**
 * 上传处理接口
 * 
 * @author yzb
 * 
 */
public interface IUpload {
    /**
     * 点击验证时的处理
     */
//    void onAuthorizeClick();

    /**
     * 登录成功后的处理
     * 
     * @param msg
     *            登录信息
     */
    void onLoginSuccess(Message msg);

    /**
     * 上传关卡分数和时间后的处理
     * 
     * @param msg
     *            消息
     */
    void onLevelResultAdd(Message msg);
}