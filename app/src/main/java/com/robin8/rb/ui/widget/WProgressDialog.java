package com.robin8.rb.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;

public class WProgressDialog extends Dialog {

    private static WProgressDialog customProgressDialog = null;

    public WProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public WProgressDialog(Activity activity, int theme) {
        super(activity, theme);
        // TODO Auto-generated constructor stub
        // this.context = context;
    }

    public WProgressDialog(Context context) {
        super(context);
        // this.context = context;
    }

    public static WProgressDialog createDialog(Activity activity) {
        customProgressDialog = new WProgressDialog(activity, R.style.WeslyDialogNobg);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.widget_progressdialog, null);
        customProgressDialog.setContentView(layout);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }
		ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.start();
    }

    public WProgressDialog setTitile(String strTitle) {
        return customProgressDialog;
    }

    public WProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(strMessage);
        }

        return customProgressDialog;
    }

    public WProgressDialog setMessage(int strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(strMessage);
        }

        return customProgressDialog;
    }
}
