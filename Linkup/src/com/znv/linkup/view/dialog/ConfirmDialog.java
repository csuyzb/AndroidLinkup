package com.znv.linkup.view.dialog;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.znv.linkup.R;

public class ConfirmDialog extends AlertDialog {

    protected View.OnClickListener cancelListener = null;

    public ConfirmDialog(Context context) {
        super(context);
        setContentView(R.layout.confirm_dialog);
        setNegativeButton(context.getResources().getString(R.string.cancel), null);
    }

    public ConfirmDialog setNegativeButton(String text, final View.OnClickListener listener) {
        Button btn = (Button) findViewById(R.id.dialog_button_cancel);
        btn.setText(text);
        cancelListener = listener;
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmCancel();
            }
        });
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            confirmCancel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void confirmCancel() {
        cancel();
        if (cancelListener != null) {
            cancelListener.onClick(null);
        }
    }

}
