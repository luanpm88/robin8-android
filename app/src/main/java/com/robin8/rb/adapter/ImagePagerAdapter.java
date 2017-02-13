/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.robin8.rb.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.autoviewpager.RecyclingPagerAdapter;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.BannerBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.first.activity.LaunchRewordFirstActivity;
import com.robin8.rb.module.mine.activity.BeKolFirstActivity;
import com.robin8.rb.module.mine.activity.HelpCenterActivity;
import com.robin8.rb.module.mine.activity.InviteFriendsActivity;
import com.robin8.rb.module.mine.activity.UserSignActivity;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.List;


/**
 * ImagePagerAdapter
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private static final String STATE_REJECTED = "rejected";//pending,applying,passed,rejected
    private Activity mActivity;
    private List<BannerBean> imageIdList;
    private int size;
    private boolean isInfiniteLoop;

    public ImagePagerAdapter(Activity activity, List<BannerBean> imageIdList) {
        this.mActivity = activity;
        this.imageIdList = imageIdList;
        this.size = ListUtils.getSize(imageIdList);
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
    }

    @Override
    public View getView(final int position, View view, final ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(mActivity);
            view.setTag(R.id.ll_load_more, holder);
        } else {
            holder = (ViewHolder) view.getTag(R.id.ll_load_more);
        }

        holder.imageView.setBackgroundResource(imageIdList.get(position % size).resId);
        view.setOnClickListener(new MyOnClickListener(position % size, imageIdList.get(position % size).type));
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    private class MyOnClickListener implements View.OnClickListener {

        private int type;

        public MyOnClickListener(int position, int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if (BaseApplication.getInstance().hasLogined()) {
                Intent intent = null;
                switch (type) {
                    case BannerBean.BE_KOL:
                        LoginBean.KolEntity kol = BaseApplication.getInstance().getLoginBean().getKol();
                        int id = 0;
                        if (kol != null) {
                            id = kol.getId();
                        }
                        if (STATE_REJECTED.equals(kol.getRole_apply_status())) {
                            showRejectedDialog(kol);
                            return;
                        }
                        skipToBeKol(id);
                        break;
                    case BannerBean.LAUNCH_CAMPAIGN:
                        intent = new Intent(v.getContext(), LaunchRewordFirstActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        break;
                    case BannerBean.INVITE_FRIEND:
                        intent = new Intent(v.getContext(), InviteFriendsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        break;
                    case BannerBean.INDIANA:
                        intent = new Intent(v.getContext(), BaseRecyclerViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("destination", SPConstants.INDIANA_ROBIN);
                        intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL));
                        intent.putExtra("title", v.getContext().getString(R.string.robin_indiana));
                        v.getContext().startActivity(intent);
                        break;
                    case BannerBean.CHECK_IN:
                        intent = new Intent(v.getContext(), UserSignActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        break;
                    case BannerBean.CREATE_EARN_MONEY:
                    case BannerBean.HOW_CREATE:
                        intent = new Intent(v.getContext(), HelpCenterActivity.class);
                        intent.putExtra("from",SPConstants.CREATE_FIRST_LIST);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);
                        break;
                }
            } else {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("from", SPConstants.MAINACTIVITY);
                intent.putExtras(bundle);
                mActivity.startActivityForResult(intent, SPConstants.MAIN_TO_LOGIN);
            }
        }
    }

    private void showRejectedDialog(final LoginBean.KolEntity kol) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_reject_kol, null);
        TextView bekolTV = (TextView) view.findViewById(R.id.tv_be_kol);
        TextView reasonTV = (TextView) view.findViewById(R.id.tv_reason);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);
        reasonTV.setText(kol.getRole_check_remark());
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });

        bekolTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToBeKol(kol.getId());
                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void skipToBeKol(int id) {
        Intent intent = null;
        intent = new Intent(mActivity, BeKolFirstActivity.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
