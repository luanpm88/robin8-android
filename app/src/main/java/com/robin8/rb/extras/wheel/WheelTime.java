package com.robin8.rb.extras.wheel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.util.DateUtil;

import java.util.List;

public class WheelTime {
    private List<String> consumeWayList;
    private List<String> dateList;
    private View view;
    private WheelView consume_way;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;

    private TimePickerView.Type type;
    public WheelTime(View view) {
        super();
        this.view = view;
        type = TimePickerView.Type.CONSUME_WAY;
        setView(view);
    }

    public WheelTime(View view, TimePickerView.Type type, List<String> consumeWayList, List<String> dateList) {
        super();
        this.view = view;
        this.type = type;
        this.consumeWayList = consumeWayList;
        this.dateList = dateList;
        setView(view);
    }

   /* public void setPicker(int year, int month, int day) {
        this.setPicker(month, day, h, m);
    }*/

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void setPicker(int way, int day, int h, int m) {

        Context context = view.getContext();
        consume_way = (WheelView) view.findViewById(R.id.consume_way);
        consume_way.setAdapter(new NumericWheelAdapterMine(consumeWayList));
        consume_way.setCurrentItem(way);

        wv_day = (WheelView) view.findViewById(R.id.day);
        wv_day.setAdapter(new NumericWheelAdapterMine(dateList));
        wv_day.setCurrentItem(day);

        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_hours.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
        wv_hours.setCurrentItem(h);

        wv_mins = (WheelView) view.findViewById(R.id.min);
        wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        wv_mins.setCurrentItem(m);

        OnItemSelectedListener wheelListener_consume_way = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                consume_way.setCurrentItem(index);
            }
        };
        OnItemSelectedListener wheelListener_day = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                wv_day.setCurrentItem(index);
            }
        };
        consume_way.setOnItemSelectedListener(wheelListener_consume_way);
        wv_day.setOnItemSelectedListener(wheelListener_day);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 6;
        switch (type) {
            case CONSUME_WAY:
                textSize = textSize * 3;
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                break;
            case DATE_HOUR_MONTH:
                textSize = textSize * 3;
                consume_way.setVisibility(View.GONE);
                break;
        }
        consume_way.setTextSize(textSize);
        wv_day.setTextSize(textSize);
        wv_hours.setTextSize(textSize);
        wv_mins.setTextSize(textSize);
    }

    public void setType(TimePickerView.Type type){
        this.type = type;
        switch (type) {
            case CONSUME_WAY:
                wv_day.setVisibility(View.GONE);
                wv_hours.setVisibility(View.GONE);
                wv_mins.setVisibility(View.GONE);
                consume_way.setVisibility(View.VISIBLE);
                break;
            case DATE_HOUR_MONTH:
                consume_way.setVisibility(View.GONE);

                wv_day.setVisibility(View.VISIBLE);
                wv_hours.setVisibility(View.VISIBLE);
                wv_mins.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 设置是否循环滚动
     */
    public void setCyclic(boolean dateCyclic, boolean consumeWayCyclic) {
        consume_way.setCyclic(consumeWayCyclic);
        wv_day.setCyclic(dateCyclic);
        wv_hours.setCyclic(dateCyclic);
        wv_mins.setCyclic(dateCyclic);
    }

    public String getTime() {
        String dateStr = "";
        String consumeWayStr = "";
        if(type == TimePickerView.Type.DATE_HOUR_MONTH){
            StringBuffer sb = new StringBuffer();
            String month;
            String day;

            if(dateList == null || dateList.size()==0 || dateList.size()<=wv_day.getCurrentItem()){
                return "";
            }

            String hour = String.valueOf(wv_hours.getCurrentItem());
            String minute = String.valueOf(wv_mins.getCurrentItem());

            if (hour.length() < 2) {
                hour = String.valueOf(0) + hour;
            }

            if (minute.length() < 2) {
                minute = String.valueOf(0) + minute;
            }

            if ("今天".equals(dateList.get(wv_day.getCurrentItem()))) {
                sb.append(DateUtil.getNowTime("yyyy-MM-dd ")).append(hour).append(":").append(minute);
            } else {
                String currentString = dateList.get(wv_day.getCurrentItem());
                month = currentString.substring(0, currentString.indexOf("月"));
                day = currentString.substring(currentString.indexOf("月") + 1, currentString.indexOf("日"));
                if (month.length() < 2) {
                    month = String.valueOf(0) + month;
                }

                if (day.length() < 2) {
                    day = String.valueOf(0) + day;
                }
                sb.append(DateUtil.getNowTime("yyyy")).append("-").append(month).append("-").append(day).append(" ").
                        append(hour).append(":").append(minute);
            }

            dateStr = sb.toString();
        }else {
            if(consumeWayList == null || consumeWayList.size()==0 || consumeWayList.size()<=consume_way.getCurrentItem()){
                return "";
            }
            consumeWayStr = consumeWayList.get(consume_way.getCurrentItem());
        }

        Log.e("xxfigo","date==="+dateStr+"/"+consumeWayStr);
        return dateStr+"/"+consumeWayStr;
    }

    public int geConsumeItem(){
        int consumeWay = consume_way.getCurrentItem();
        return consumeWay;
    }

    public int getDayItem(){
        int day = wv_day.getCurrentItem();
        return day;
    }

    public int getHourItem(){
        int day = wv_hours.getCurrentItem();
        return day;
    }

    public int getMinuteItem(){
        int day = wv_mins.getCurrentItem();
        return day;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

}
