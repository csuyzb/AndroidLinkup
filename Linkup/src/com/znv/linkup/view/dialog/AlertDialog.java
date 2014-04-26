package com.znv.linkup.view.dialog;

import com.znv.linkup.R;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertDialog extends Dialog {

    public AlertDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        setContentView(R.layout.alert_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setPositiveButton(context.getResources().getString(R.string.submit), null);
    }

    public AlertDialog setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.dialog_title);
        tvTitle.setText(title);
        return this;
    }

    public AlertDialog setMessage(String msg) {
        TextView tvMessage = (TextView) findViewById(R.id.dialog_message);
        tvMessage.setText(msg);
        return this;
    }

    public AlertDialog setIcon(int resId) {
        ImageView ivImage = (ImageView) findViewById(R.id.dialog_title_image);
        ivImage.setBackgroundResource(resId);
        return this;
    }

    public AlertDialog setPositiveButton(String text, final View.OnClickListener listener) {
        Button btn = (Button) findViewById(R.id.dialog_button_ok);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Button btn = (Button) findViewById(R.id.dialog_button_ok);
            btn.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
