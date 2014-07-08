package com.znv.linkup.rest;

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
    void netMsgHandle(T t);
}
