/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */

package com.robin8.rb.module.reword.banner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.activity.web.BannerWebActivity;
import com.robin8.rb.activity.web.PutWebActivity;
import com.robin8.rb.autoviewpager.RecyclingPagerAdapter;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.CampaignListBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.activity.BeKolFirstActivity;
import com.robin8.rb.module.mine.activity.CollectMoneyActivity;
import com.robin8.rb.module.mine.activity.UserSignActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.List;


/**
 ImagePagerAdapter */
public class ImageBannerAdapter extends RecyclingPagerAdapter {

    private static final String STATE_REJECTED = "rejected";//pending,applying,passed,rejected
    private Activity mActivity;
    private List<CampaignListBean.AnnouncementEntity> imageIdList;
    private int size;
    private boolean isInfiniteLoop;

    public ImageBannerAdapter(Activity activity, List<CampaignListBean.AnnouncementEntity> imageIdList) {
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
        int p = isInfiniteLoop ? position % size : position;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(mActivity);
            view.setTag(R.id.ll_load_more, holder);
        } else {
            holder = (ViewHolder) view.getTag(R.id.ll_load_more);
        }
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        BitmapUtil.loadImageCenter(mActivity, imageIdList.get(p).getBanner_url(), holder.imageView);
        //  holder.imageView.setBackgroundResource(imageIdList.get(position % size).resId);
        view.setOnClickListener(new MyOnClickListener(position % size, imageIdList.get(position % size).getDetail_type(),imageIdList.get(position % size).getUrl()));
        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    private class MyOnClickListener implements View.OnClickListener {

        private String type;
        private int position;
        private String urlJump;

        public MyOnClickListener(int position, String type, String urlJump) {
            this.type = type;
            this.position = position;
            this.urlJump = urlJump;
        }

        @Override
        public void onClick(View v) {
            if (BaseApplication.getInstance().hasLogined()) {
                BannerClick(position);
                Intent intent = null;
                if (!TextUtils.isEmpty(type)) {
                    switch (type) {
                        case "complete_info":
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
                        case "invite_friend":
                            intent = new Intent(v.getContext(), CollectMoneyActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                            break;
                        case "indiana":
                            intent = new Intent(v.getContext(), BaseRecyclerViewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("destination", SPConstants.INDIANA_ROBIN);
                            intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL));
                            intent.putExtra("title", v.getContext().getString(R.string.robin_indiana));
                            v.getContext().startActivity(intent);
                            break;
                        case "check_in":
                            intent = new Intent(v.getContext(), UserSignActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                            break;
                        case "open_puts_wallet":
                            intent = new Intent(v.getContext(), PutWebActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(PutWebActivity.PUT_TYPE, "0");
                            v.getContext().startActivity(intent);
                            break;
                        case "web":
                            intent = new Intent(v.getContext(), BannerWebActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(BannerWebActivity.BANNER, urlJump);
                            v.getContext().startActivity(intent);
                            break;
                    }
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

    private void BannerClick(final int position) {
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("announcement_id", imageIdList.get(position).getId());
        params.put("params_json", imageIdList.get(position).getDetail_type());

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.BANNER_CLICK_URL), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("这是请求", position + response);
            }
        });
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
     @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImageBannerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
