package com.robin8.rb.util;

import android.content.Context;
import android.widget.TextView;

/**
 * 计时器 用于验证码时间显示
 * 用法:setShowButton(TextView),runTime()
 */
public class TimerUtil {
    private final int secondCount = 60;// 总得计时秒数
    private String lastRequestTime;
    public Boolean isRuning = false;// 读秒器是否在运行
    private TextView bt_code;
    private TextView inputView;
    private Context context;

    public TimerUtil(Context context){
        this.context = context;
    }

    public void setShowButton(TextView bt_code, TextView inputView) {
        this.bt_code = bt_code;
        this.inputView = inputView;
    }

    public void setShowButton(TextView bt_code) {
        this.bt_code = bt_code;
    }


}
