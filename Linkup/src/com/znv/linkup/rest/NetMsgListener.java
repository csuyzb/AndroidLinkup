package com.znv.linkup.rest;

import com.android.volley.VolleyError;

/**
 * 处理网络消息的监听器
 * 
 * @author yzb
 * 
 * @param <T>
 *            类型
 */
public interface NetMsgListener<T> {
    /**
     * 处理网络消息
     * 
     * @param t
     *            消息
     */
    void onNetMsg(T t);

    /**
     * 出错时的处理
     * 
     * @param e
     *            错误
     */
    void onError(VolleyError e);
}
