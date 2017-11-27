package com.robin8.rb.module.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.robin8.rb.R;
import com.robin8.rb.activity.ChangeHttpActivity;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.activity.WebViewActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.widget.SwitchView;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;


/**
 成为KOL */
public class SettingActivity extends BaseActivity {

    private static final int TYPE_LOGIN = 0;
    private static final int TYPE_NORMAL = 1;

    private static final int POSITION_NOTIFY_TOGGLE = 0;
    private static final int POSITION_FEEDBACK = 1;
    private static final int POSITION_ABOUT_ROBIN8 = 2;
    private static final int POSITION_LOG_OUT = 3;

    private ListView mListView;
    private List<SettingItem> mDataList = new ArrayList<>();
    private SettingAdapter mSettingAdapter;
    private TextView mLogOutTv;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.setting);
        mTVCenter.setOnClickListener(new View.OnClickListener() {

            final static int COUNTS = 5;//点击次数
            final static long DURATION = 3 * 1000;//规定有效时间
            long[] mHits = new long[COUNTS];

            @Override
            public void onClick(View view) {
                /**
                 * 实现双击方法
                 * src 拷贝的源数组
                 * srcPos 从源数组的那个位置开始拷贝.
                 * dst 目标数组
                 * dstPos 从目标数组的那个位子开始写数据
                 * length 拷贝的元素的个数
                 */
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
                  //  String tips = "您已在[" + DURATION + "]ms内连续点击【" + mHits.length + "】次了！！！";
                  //  Toast.makeText(SettingActivity.this, tips, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingActivity.this, ChangeHttpActivity.class));
                }
            }
        });
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_setting, mLLContent, true);
        mListView = (ListView) findViewById(R.id.lv_list);
        mLogOutTv = (TextView) findViewById(R.id.tv_log_out);
        mLogOutTv.setOnClickListener(new MyOnclickListener(null, POSITION_LOG_OUT));
        initData();
        initListView();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_SETTING;
        super.onResume();
    }

    private void initListView() {
        mSettingAdapter = new SettingAdapter();
        mListView.setAdapter(mSettingAdapter);
    }

    private void initData() {
        String[] stringArray = getResources().getStringArray(R.array.setting_item_name);
        mDataList.clear();
        for (int i = 0; i < stringArray.length; i++) {
            SettingItem item = new SettingItem();
            item.name = stringArray[i];
            item.position = i;
            mDataList.add(item);
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    private class SettingItem {
        public String name;
        public int position;
    }

    private class SettingAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public SettingItem getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position + 1 == getCount()) {
                return TYPE_LOGIN;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            SettingItem item = getItem(position);
            ViewHolder holder;
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tvArrow = (TextView) convertView.findViewById(R.id.tv_arrow);
            holder.vSwitch = (SwitchView) convertView.findViewById(R.id.view_switch);
            convertView.setTag(holder);
            holder.tvName.setText(item.name);
            switch (position) {
                case POSITION_NOTIFY_TOGGLE:
                case POSITION_FEEDBACK:
                    holder.tvArrow.setVisibility(View.GONE);
                    holder.vSwitch.setVisibility(View.VISIBLE);
                    holder.vSwitch.setOnStateChangedListener(new MyOnStateChangedListener(position, holder.vSwitch));
                    if (POSITION_NOTIFY_TOGGLE == position) {
                        boolean notifyToggle = CacheUtils.getBoolean(SettingActivity.this, SPConstants.NOTIFY_TOGGLE, true);
                        holder.vSwitch.toggleSwitch(notifyToggle);
                    } else if (POSITION_FEEDBACK == position) {
                        boolean feedbackToggle = CacheUtils.getBoolean(SettingActivity.this, SPConstants.FEEDBACK_TOGGLE, true);
                        holder.vSwitch.toggleSwitch(feedbackToggle);
                    }
                    break;
                default:
                    holder.tvArrow.setVisibility(View.VISIBLE);
                    holder.vSwitch.setVisibility(View.GONE);
                    IconFontHelper.setTextIconFont(holder.tvArrow, R.string.arrow_right);
                    break;
            }

            if (position == POSITION_ABOUT_ROBIN8) {
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.tvContent.setText(AppUtils.getVersionName(SettingActivity.this.getApplicationContext()));
            } else {
                holder.tvContent.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new MyOnclickListener(item, position));
            return convertView;
        }
    }


    class ViewHolder {
        public TextView tvName;
        public TextView tvArrow;
        public SwitchView vSwitch;
        public TextView tvContent;
    }

    private class MyOnclickListener implements View.OnClickListener {

        private SettingItem item;
        private int position;

        MyOnclickListener(SettingItem item, int position) {
            this.position = position;
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (position) {
                case POSITION_ABOUT_ROBIN8:
                    intent = new Intent(SettingActivity.this, WebViewActivity.class);
                    intent.putExtra("title", item.name);
                    intent.putExtra("url", CommonConfig.SERVICE);
                    SettingActivity.this.startActivity(intent);
                    break;
                case POSITION_LOG_OUT:
                    if (BaseApplication.getInstance().hasLogined()) {
                        BaseApplication.getInstance().setLoginBean(null);
                        CacheUtils.putString(SettingActivity.this, SPConstants.MINE_DATA, null);
                        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN_OUT);
                        //HelpTools.insertCommonXml(HelpTools.NATIVE,"");
                        HelpTools.insertCommonXml(HelpTools.MyKolId, "");
                        HelpTools.insertCommonXml(HelpTools.PagerData, "");
                        HelpTools.insertCommonXml(HelpTools.isLeader, "");
                        HelpTools.insertCommonXml(HelpTools.CloudToken, "");
                        intent = new Intent(SettingActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("from", SPConstants.MAINACTIVITY);
                        intent.putExtras(bundle);
                        SettingActivity.this.startActivity(intent);
                        SettingActivity.this.finish();
                    }
                    break;
            }
        }
    }

    private class MyOnStateChangedListener implements SwitchView.OnStateChangedListener {

        private int position;
        private SwitchView vSwitch;

        public MyOnStateChangedListener(int position, SwitchView vSwitch) {
            this.vSwitch = vSwitch;
            this.position = position;
        }

        @Override
        public void toggleToOn(View view) {
            // 执行一些耗时的业务逻辑操作 implement some time-consuming logic operation
            vSwitch.toggleSwitch(true);
            if (POSITION_NOTIFY_TOGGLE == position) {
                CacheUtils.putBoolean(SettingActivity.this, SPConstants.NOTIFY_TOGGLE, true);
            } else {
                CacheUtils.putBoolean(SettingActivity.this, SPConstants.FEEDBACK_TOGGLE, true);
                PushManager.getInstance().turnOnPush(SettingActivity.this);//打开个推开关
            }
        }

        @Override
        public void toggleToOff(View view) {
            // 原本为打开的状态，被点击后 originally present the status of open after clicking
            vSwitch.toggleSwitch(false);
            if (POSITION_NOTIFY_TOGGLE == position) {
                CacheUtils.putBoolean(SettingActivity.this, SPConstants.NOTIFY_TOGGLE, false);
            } else {
                CacheUtils.putBoolean(SettingActivity.this, SPConstants.FEEDBACK_TOGGLE, false);
                PushManager.getInstance().turnOffPush(SettingActivity.this);
            }
        }
    }
}
