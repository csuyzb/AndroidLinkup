package com.znv.linkup;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.xiaomi.mipush.sdk.MiPushClient;

public class MyApplication extends Application {
    // user your appid the key.
    public static final String APP_ID = "2882303761517169919";
    // user your appid the key.
    public static final String APP_KEY = "5451716979919";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep com.znv.linkup
    public static final String TAG = "com.znv.linkup";

    // 小米推送的注册ID
    public static String Push_Reg_ID = "";

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }

        // LoggerInterface newLogger = new LoggerInterface() {
        //
        // @Override
        // public void setTag(String tag) {
        // // ignore
        // }
        //
        // @Override
        // public void log(String content, Throwable t) {
        // Log.d(TAG, content, t);
        // }
        //
        // @Override
        // public void log(String content) {
        // Log.d(TAG, content);
        // }
        // };
        // Logger.setLogger(this, newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
