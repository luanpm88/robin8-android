package com.robin8.rb.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.robin8.rb.base.BaseFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mList;

    public ViewPagerAdapter(ArrayList<BaseFragment> mFragmentList, FragmentManager fm) {
        super(fm);
        this.mList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getName();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    public static class SelectItem {
        public String name;
        public String campaignType;
    }
}