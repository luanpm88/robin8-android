package com.robin8.rb.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.util.StringUtil;


/**
 * 对dialog的一个封装
 */
public class InstantlyIndianaDialog extends Dialog implements View.OnClickListener {
    private float kolAmount;
    protected Context mContext;
    private EditText et_times;
    private TextView tv_tips;
    private int mCurrentTimes = 5;
    private InstantlyIndianaListener instantlyIndianaListener;

    public InstantlyIndianaDialog(Activity activity, float kolAmount) {
        super(activity, R.style.Theme_Dialog);
        mContext = activity;
        this.kolAmount = kolAmount;
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_instantly_indiana, null);

        initView(view);

        Window window = getWindow();
        View decorView = window.getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        setContentView(view);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_animations);
    }

    private void initView(View view) {
        View tv_minus_sign = view.findViewById(R.id.tv_minus_sign);
        View tv_plus_sign = view.findViewById(R.id.tv_plus_sign);
        et_times = (EditText) view.findViewById(R.id.et_times);
        View tv_times5 = view.findViewById(R.id.tv_times5);
        View tv_times10 = view.findViewById(R.id.tv_times10);
        View tv_times20 = view.findViewById(R.id.tv_times20);
        View tv_times50 = view.findViewById(R.id.tv_times50);
        tv_tips = (TextView) view.findViewById(R.id.tv_tips);
        View tv_bottom = view.findViewById(R.id.tv_bottom);

        et_times.addTextChangedListener(new MyTextWatcher());

        tv_minus_sign.setOnClickListener(this);
        tv_plus_sign.setOnClickListener(this);
        tv_times5.setOnClickListener(this);
        tv_times10.setOnClickListener(this);
        tv_times20.setOnClickListener(this);
        tv_times50.setOnClickListener(this);
        tv_bottom.setOnClickListener(this);
        updateTextView(mCurrentTimes);
    }

    public void updateTextView(int number) {
        et_times.setText(String.valueOf(number));
        tv_tips.setText(mContext.getString(R.string.total_consume) + String.valueOf(number) +
                mContext.getString(R.string.current_amount) + StringUtil.deleteZero(kolAmount) + mContext.getString(R.string.right_));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_minus_sign:
                if (mCurrentTimes <= 1) {
                    return;
                }
                mCurrentTimes--;
                updateTextView(mCurrentTimes);
                break;
            case R.id.tv_plus_sign:
                mCurrentTimes++;
                updateTextView(mCurrentTimes);
                break;
            case R.id.tv_times5:
                mCurrentTimes = 5;
                updateTextView(mCurrentTimes);
                break;
            case R.id.tv_times10:
                mCurrentTimes = 10;
                updateTextView(mCurrentTimes);
                break;
            case R.id.tv_times20:
                mCurrentTimes = 20;
                updateTextView(mCurrentTimes);
                break;
            case R.id.tv_times50:
                mCurrentTimes = 50;
                updateTextView(mCurrentTimes);
                break;
            case R.id.tv_bottom:
                if (instantlyIndianaListener != null) {
                    instantlyIndianaListener.skipToPay(mCurrentTimes);
                }
                break;
        }
    }

    public void setInstantlyIndianaListener(InstantlyIndianaListener instantlyIndianaListener) {
        this.instantlyIndianaListener = instantlyIndianaListener;
    }

    public interface InstantlyIndianaListener {
        void skipToPay(int times);
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                mCurrentTimes = Integer.parseInt(s.toString());
            }
        }
    }
}
