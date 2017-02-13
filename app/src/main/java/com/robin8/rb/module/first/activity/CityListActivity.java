package com.robin8.rb.module.first.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.widget.citylist.CityAdapter;
import com.robin8.rb.ui.widget.citylist.CityData;
import com.robin8.rb.ui.widget.citylist.ContactItemInterface;
import com.robin8.rb.ui.widget.citylist.ContactListViewImpl;
import com.robin8.rb.util.CustomToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends BaseActivity {

    private static final int BEIJING = 0;
    private static final int GUANGZHOU = 1;
    private static final int HANGZHOU = 2;
    private static final int SHANGHAI = 3;
    private static final int SHENZHENG = 4;
    private static final int SUZHOU = 5;

    private static final int BEIJING_NORMAL = 31;
    private static final int GUANGZHOU_NORMAL = 89;
    private static final int HANGZHOU_NORMAL = 103;
    private static final int SHANGHAI_NORMAL = 234;
    private static final int SHENZHENG_NORMAL = 246;
    private static final int SUZHOU_NORMAL = 259;

    private final int CLEAR_ALL = 0;//清空
    private final int SELECT_ALL = 1;//全选
    private int mCurrentState = CLEAR_ALL;
    private ContactListViewImpl listview;
    List<ContactItemInterface> contactList;
    List<ContactItemInterface> filterList;
    private CityAdapter mCityAdapter;


    @Override
    public void setTitleView() {
        getData();
        mTVCenter.setText(R.string.kol_city);
        mTVRight.setVisibility(View.VISIBLE);
        mTVRight.setText(R.string.clear);
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(R.string.submit);
        mBottomTv.setOnClickListener(this);
        mPageName = StatisticsAgency.CITY_LIST;
    }

    private void getData() {
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("citylist");
        if (serializable != null && serializable instanceof List) {
            contactList = (List<ContactItemInterface>) serializable;
        } else {
            contactList = CityData.getSampleContactList();
        }
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.citylist, mLLContent);
        filterList = new ArrayList<ContactItemInterface>();

        mCityAdapter = new CityAdapter(this, R.layout.city_item, contactList);
        listview = (ContactListViewImpl) view.findViewById(R.id.listview);
        listview.setFastScrollEnabled(true);
        listview.setAdapter(mCityAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                switch (position) {
                    case BEIJING:
                    case BEIJING_NORMAL:
                        setPositionState(BEIJING, BEIJING_NORMAL);
                        break;

                    case GUANGZHOU:
                    case GUANGZHOU_NORMAL:
                        setPositionState(GUANGZHOU, GUANGZHOU_NORMAL);
                        break;

                    case HANGZHOU:
                    case HANGZHOU_NORMAL:
                        setPositionState(HANGZHOU, HANGZHOU_NORMAL);
                        break;

                    case SHANGHAI:
                    case SHANGHAI_NORMAL:
                        setPositionState(SHANGHAI, SHANGHAI_NORMAL);
                        break;

                    case SHENZHENG:
                    case SHENZHENG_NORMAL:
                        setPositionState(SHENZHENG, SHENZHENG_NORMAL);
                        break;

                    case SUZHOU:
                    case SUZHOU_NORMAL:
                        setPositionState(SUZHOU, SUZHOU_NORMAL);
                        break;

                    default:
                        if (contactList.get(position).isSelected()) {
                            contactList.get(position).setSelected(false);
                        } else {
                            contactList.get(position).setSelected(true);
                        }
                        break;
                }

                mCityAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setPositionState(int hot, int normal) {
        if (contactList.get(hot).isSelected()) {
            contactList.get(hot).setSelected(false);
            contactList.get(normal).setSelected(false);
        } else {
            contactList.get(hot).setSelected(true);
            contactList.get(normal).setSelected(true);
        }
    }

    private StringBuffer sb = new StringBuffer();

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                submit();
                break;
        }
    }

    private void submit() {
        sb.delete(0, sb.length());
        for (int i = 0; i < contactList.size(); i++) {
            ContactItemInterface anInterface = contactList.get(i);
            if (anInterface.isSelected()) {
                String cityName = anInterface.getDisplayInfo();
                if (!TextUtils.isEmpty(cityName)) {
                    if (cityName.startsWith("#")) {
                        cityName = cityName.substring(1, cityName.length());
                    }
                    if (sb.length() == 0 || !sb.toString().contains(cityName)) {
                        sb.append(cityName).append(",");
                    }
                }
            }
        }
        String citylistStr = sb.toString();
        if (TextUtils.isEmpty(citylistStr)) {
            CustomToast.showShort(CityListActivity.this, getString(R.string.please_select_one_city));
            return;
        }
        citylistStr = citylistStr.substring(0, citylistStr.length() - 1);
        Intent intent = getIntent();
        intent.putExtra("city_list_str", citylistStr);
        intent.putExtra("city_list", (Serializable) contactList);
        setResult(SPConstants.CITY_LIST, intent);
        finish();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
        if (mCurrentState == CLEAR_ALL) {
            mCurrentState = SELECT_ALL;
            mTVRight.setText(R.string.select_all);
            for (int i = 0; i < contactList.size(); i++) {
                ContactItemInterface anInterface = contactList.get(i);
                anInterface.setSelected(false);
            }
        } else {
            mCurrentState = CLEAR_ALL;
            mTVRight.setText(R.string.clear_all);
            for (int i = 0; i < contactList.size(); i++) {
                contactList.get(i).setSelected(true);
            }
        }
        mCityAdapter.notifyDataSetChanged();
    }
}
