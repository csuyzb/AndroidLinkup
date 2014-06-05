package com.znv.linkup.view.dialog;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.znv.linkup.R;

/**
 * 自定义确认框
 * 
 * @author yzb
 * 
 */
public class ConfirmDialog extends HelpDialog {

    public ConfirmDialog(Context context) {
        super(context);
        setContentView(R.layout.confirm_dialog);
        setNegativeButton(context.getResources().getString(R.string.cancel), null);
    }

    /**
     * 设置取消按钮
     * 
     * @param text
     *            按钮文字
     * @param listener
     *            取消操作
     * @return 确认框实例
     */
    public ConfirmDialog setNegativeButton(String text, final View.OnClickListener listener) {
        Button btn = (Button) findViewById(R.id.dialog_button_cancel);
        btn.setText(text);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancel();
                if (listener != null) {
                    listener.onClick(null);
                }
            }
        });
        return this;
    }

    /**
     * 处理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Button btn = (Button) findViewById(R.id.dialog_button_cancel);
            btn.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
