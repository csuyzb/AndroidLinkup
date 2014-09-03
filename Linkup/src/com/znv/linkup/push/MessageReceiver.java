package com.znv.linkup.push;

import java.util.List;

import android.content.Context;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.znv.linkup.MyApplication;

public class MessageReceiver extends PushMessageReceiver {

    @Override
    public void onCommandResult(Context arg0, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                MyApplication.Push_Reg_ID = cmdArg1;
            } else {
                MyApplication.Push_Reg_ID = "";
            }
        } else {
            MyApplication.Push_Reg_ID = "";
        }
    }

    @Override
    public void onReceiveMessage(Context arg0, MiPushMessage message) {
        MyApplication.Push_Reg_ID = "";
    }

}
