package com.znv.linkup.rest;

public interface NetMsgListener<T> {
    void netMsgHandle(T t);
}
