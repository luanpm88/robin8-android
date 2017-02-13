package com.robin8.rb.view.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.robin8.rb.R;

/**
 * 对dialog的一个封装
 */
public class LaunchRewordDialog extends Dialog implements View.OnClickListener {
    private ImageView mImageView;

    public LaunchRewordDialog(Activity activity) {
        super(activity, R.style.Theme_Dialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_launch_reword, null);
        initView(view);
        Window window = getWindow();
        View decorView = window.getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        setContentView(view);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        getWindow().setWindowAnimations(R.style.umeng_socialize_shareboard_animation);
    }

    private void initView(View view) {
        mImageView = (ImageView) view.findViewById(R.id.iv);
        mImageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv:
                dismiss();
                break;
        }
    }
}
