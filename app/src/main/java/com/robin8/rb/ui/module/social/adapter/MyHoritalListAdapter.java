package com.robin8.rb.ui.module.social.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.module.first.activity.SearchKolActivity;
import com.robin8.rb.ui.module.social.SocialDetailActivity;
import com.robin8.rb.ui.module.social.bean.InfluenceScoreBean;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 Created by zc on 2017/08/09. */

public class MyHoritalListAdapter extends BaseAdapter {
    private List<InfluenceScoreBean.SimilarKolsBean> mDataList;
    private Context context;
    private static final String IMAGE_URL = CommonConfig.APP_ICON;
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";
    public MyHoritalListAdapter(Context context, List<InfluenceScoreBean.SimilarKolsBean> mList) {
        this.context = context;
        this.mDataList = mList;
    }

    @Override
    public int getCount() {
        return mDataList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.social_photo_item, null);
            convertView.setTag(new Holder(convertView));
        }
        final Holder holder = (Holder) convertView.getTag();
        if (position == 0) {
            holder.imgIcon.setImageResource(R.mipmap.icon_list_first);
            holder.imgIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    showMyDialog(context);
                }
            });
        } else if (position > 0) {
            if (TextUtils.isEmpty(mDataList.get(position-1).getAvatar_url())) {
                holder.imgIcon.setImageResource(R.mipmap.logo_circle);
            } else {
                BitmapUtil.loadImage(context.getApplicationContext(),mDataList.get(position-1).getAvatar_url(), holder.imgIcon);
            }
            holder.imgIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SocialDetailActivity.class);
                    intent.putExtra(SocialDetailActivity.OTHER_DETAIL_TAG,mDataList.get(position-1).getId());
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    class Holder {
        private final ImageView imgIcon;

        Holder(View view) {
            imgIcon = ((ImageView) view.findViewById(R.id.civ_image));
        }
    }

    public void showMyDialog(final Context activity) {
        // String rejectReason = campaignInviteEntity.getReject_reason();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_other_influence, null);
       TextView tvFirst = ((TextView) view.findViewById(R.id.tv_one));
       TextView tvSecond = ((TextView) view.findViewById(R.id.tv_two));
       TextView tvCancel = ((TextView) view.findViewById(R.id.tv_cancel));
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        tvFirst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchKolActivity.class);
                intent.putExtra("from", SPConstants.FIRST_PAGE_SEARCH);
                intent.putExtra("url",  CommonConfig.FIRST_KOL_LIST_URL);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                cdm.dismiss();
            }
        });
        tvSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               share(Wechat.NAME);
                cdm.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }
    private void share(String platName) {

        int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
        CustomToast.showShort(context, "正在前往分享...");
        //ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(context.getResources().getString(R.string.share_pk_text) + TITLE_URL + String.valueOf(id));
        } else {
            oks.setText(context.getResources().getString(R.string.share_pk_text));
        }
        oks.setTitle(context.getResources().getString(R.string.share_pk_title));

        oks.setTitleUrl(TITLE_URL + String.valueOf(id));
        oks.setImageUrl(IMAGE_URL);
        oks.setUrl(TITLE_URL + String.valueOf(id));
        oks.setSite(context.getResources().getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(context);
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(context, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(context, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(context, "分享取消");
        }
    }

}


