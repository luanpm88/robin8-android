package com.robin8.rb.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.robin8.rb.R;

/**
 * 对dialog的一个封装
 */
public class RewordFilterDialog {
    protected Context mContext;
    protected View view;
    public Dialog dg;

    public RewordFilterDialog(Context mContext, int layoutId) {
        this(mContext, LayoutInflater.from(mContext).inflate(layoutId, null));
    }

    public RewordFilterDialog(Context mContext, View view) {
        this.mContext = mContext;
        dg = new Dialog(mContext, R.style.Theme_Dialog);
        this.view = view;
        dg.setContentView(getContentView(view));
        dg.setCanceledOnTouchOutside(true);
    }

    protected View getContentView(View view) {
        return view;
    }

    /**
     * 展示
     */
    public void showDialog() {
        if (dg != null) {
            dg.show();
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
        v.setOnClickListener(new OnClickListener() {

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
        dg.findViewById(id).setOnClickListener(new OnClickListener() {

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
            dg.dismiss();
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


}
