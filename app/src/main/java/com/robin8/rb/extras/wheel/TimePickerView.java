package com.robin8.rb.extras.wheel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 Created by Sai on 15/11/22. */
public class TimePickerView extends BasePickerView implements View.OnClickListener {
    public static final int ACTIVITY_START = 0;
    public static final int ACTIVITY_END = 1;
    public static final int ACTIVITY_TYPE = 2;
    public static final int ACTIVITY_TYPE_NO_CLICK = 3;
    private View tvCompelete;
    private View inflate;
    private List<String> list;
    private int belongItem;
    private boolean mEndInB;
    private boolean mStrarInB;

    public void setBelongItem(int belongItem) {
        this.belongItem = belongItem;
    }

    public ArrayList<String> getConsumeWayList() {
        return consumeWayList;
    }

    public void setConsumeWayList(ArrayList<String> consumeWayList) {
        this.consumeWayList = consumeWayList;
    }

    public enum Type {
        CONSUME_WAY, DATE_HOUR_MONTH, OTHER
    }// 两种选择模式，年月日时分，年月日，时分，月日时分

    private WheelTime wheelTime;
    //	private View btnSubmit, btnCancel;
    private TextView tvTitle;
    private static final String TAG_COMPELETE = "compelete";
    private OnTimeSelectListener timeSelectListener;
    private ArrayList<String> consumeWayList;

    public TimePickerView(Context context, ArrayList<String> consumeWayList, List<String> dateList, Type type, LinearLayout rootView, int itmeType) {
        super(context, rootView);
        this.consumeWayList = consumeWayList;
        LayoutInflater.from(context).inflate(R.layout.pickerview_time, decorView);
        tvCompelete = decorView.findViewById(R.id.tv_compelete);//完成
        tvCompelete.setTag(TAG_COMPELETE);
        tvCompelete.setOnClickListener(this);
        // -----确定和取消按钮
        //		btnSubmit = findViewById(R.id.btnSubmit);
        //		btnSubmit.setTag(TAG_SUBMIT);
        //		btnCancel = findViewById(R.id.btnCancel);
        //		btnCancel.setTag(TAG_CANCEL);
        //		btnSubmit.setOnClickListener(this);
        //		btnCancel.setOnClickListener(this);
        // 顶部标题
        tvTitle = (TextView) decorView.findViewById(R.id.tvTitle);
        // ----时间转轮
        final View timepickerview = decorView.findViewById(R.id.timepicker);
        wheelTime = new WheelTime(timepickerview, type, getConsumeWayList(), dateList);
        initData(itmeType);
    }

    public void clearView() {
        callBack(true);
        decorView.removeAllViews();
    }

    public void initData(int itmeType) {

        if (itmeType == ACTIVITY_END) {
            if (mEndInB) {
                return;
            }
            mEndInB = true;
        }

        if (itmeType == ACTIVITY_START) {
            if (mStrarInB) {
                return;
            }
            mStrarInB = true;
        }

        // 默认选中当前时间
        Calendar calendar = new GregorianCalendar();/*Calendar.getInstance();*/
        calendar.setTimeInMillis(System.currentTimeMillis());
        int way = 0;
        int day = calendar.get(Calendar.DAY_OF_YEAR) - 1;//当前日期是一年中的第几天
        if (itmeType == ACTIVITY_END) {
            day++;
        }

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(way, day, hours, minute);
    }

    /**
     设置选中条目
     */
    public void setSelectItem(int day, int hour, int minute, int itmeType) {

        if (day == - 1 || hour == - 1 || minute == - 1) {
            initData(itmeType);
        } else {
            wheelTime.setPicker(wheelTime.getDayItem(), day, hour, minute);
        }
    }

    /**
     设置是否循环滚动
     */
    public void setCyclic(boolean dateCyclic, boolean consumeWayCyclic) {
        wheelTime.setCyclic(dateCyclic, consumeWayCyclic);
    }

    /**
     切换显示类型
     */
    public void setType(Type type) {
        wheelTime.setType(type);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_COMPELETE)) {
            callBack(true);
        }
    }

    public void callBack(boolean isMust) {
        if (timeSelectListener != null) {
            try {
                timeSelectListener.onTimeSelect(wheelTime.getTime(), wheelTime.getDayItem(), wheelTime.getHourItem(), wheelTime.getMinuteItem(), isMust);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnTimeSelectListener {
        public void onTimeSelect(String str, int day, int hour, int minute, boolean isMust);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
