package com.robin8.rb.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.robin8.rb.R;


/**
 * 对dialog的一个封装
 */
public class CustomDialogManager {
    protected Context mContext;
    protected View view;
    public Dialog dg;

    public CustomDialogManager(Context mContext, View view) {
        this.view = view;
        dg = new CustomDialog(mContext);
    }

    /**
     * 展示
     */
    public void showDialog() {
        if (dg != null) {
            try {
                dg.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到该布局的view视图
     *
     * @return
     */
    public View getView() {
        return view;
    }

    /**
     * 设置取消按钮
     *
     * @param v
     */
    public void setCancelButton(View v) {
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

    /**
     * 设置取消按钮
     *
     * @param id
     */
    public void setCancelButton(int id) {
        dg.findViewById(id).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

    /**
     * 隐藏对话框
     */
    public void dismiss() {
        if (dg != null && dg.isShowing()) {
            try {
                dg.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 状态
     */
    public Boolean isShow() {
        if (dg != null && dg.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 对dialog的一个封装
     */
    public class CustomDialog extends Dialog {

        public CustomDialog(Context context) {
            super(context, R.style.Theme_Dialog);
            Window window = getWindow();
            View decorView = window.getDecorView();
            decorView.setPadding(0,0,0,0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            setContentView(view);
            setCanceledOnTouchOutside(true);
        }
    }
}
