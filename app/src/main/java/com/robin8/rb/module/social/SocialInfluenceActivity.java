package com.robin8.rb.module.social;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.module.mine.activity.InviteFriendsActivity;
import com.robin8.rb.module.mine.activity.UserSignActivity;
import com.robin8.rb.module.social.view.LinearLayoutForListView;
import com.robin8.rb.ui.widget.myprogress.RoundIndicatorView;
import com.robin8.rb.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

public class SocialInfluenceActivity extends BaseActivity {

    private RoundIndicatorView viewResult;
    private LinearLayoutForListView myList;
    private List<ProveSocialItem> mDataList;
    private RelativeLayout llTitleLayout;
    private ImageView imgPhoto;

    @Override
    public void setTitleView() {
        mTVCenter.setText("提升影响力");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_social_influence, mLLContent);
        llTitleLayout = ((RelativeLayout) view.findViewById(R.id.ll_all));
        viewResult = ((RoundIndicatorView) view.findViewById(R.id.view_score_result));
        imgPhoto = ((ImageView) view.findViewById(R.id.img_user_photo));
        myList = ((LinearLayoutForListView) findViewById(R.id.lv_list));
        mDataList = new ArrayList<>();
        mDataList.add(new ProveSocialItem(R.mipmap.icon_share_campain,"分享活动","有质量有选择的参与感兴趣的品牌活动，用心的转发语可以显著提升社交媒体中的互动","去分享"));
        mDataList.add(new ProveSocialItem(R.mipmap.icon_invite_friend,"邀请好友","动员好友加入Robin8，影响力快速升级","去邀请"));
        mDataList.add(new ProveSocialItem(R.mipmap.icon_sign_social,"签到","时刻查看影响力的动态变化，有助于提高影响力","去签到"));
        myList.setAdapter(new MySocialAdapter());
        int v = (int)(Math.random() * (10000 - 10) + 10);
        viewResult.setCurrentValues(50);
        viewResult.setAllText("影响力结果","超过19%的用户","2017/2/2");
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String user_url = intent.getStringExtra("user_url");
        if (TextUtils.isEmpty(user_url)){
            llTitleLayout.setVisibility(View.GONE);
        }else {
            llTitleLayout.setVisibility(View.VISIBLE);
            BitmapUtil.loadImage(getApplicationContext(),user_url,imgPhoto);
            viewResult.setCurrentValues(Float.valueOf(intent.getStringExtra("user_score")));
            viewResult.setAllText(intent.getStringExtra("user_level"),intent.getStringExtra("user_percentile"),intent.getStringExtra("user_data"));
        }
    }

    //    private static final int ITEM_HEADER = 0;//头部
//    private static final int ITEM_MIDDLE = 1;//头部
//    private static final int ITEM_END = 2;//头部

    class MySocialAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mDataList.size();
        }

        @Override
        public ProveSocialItem getItem(int position) {

            return mDataList.get(position);
        }


        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(SocialInfluenceActivity.this).inflate(R.layout.prove_social_score_item, null);
                convertView.setTag(new SocialInfluenceActivity.Holder(convertView));
            }
            final SocialInfluenceActivity.Holder holder = (SocialInfluenceActivity.Holder) convertView.getTag();

            ProveSocialItem item = getItem(position);
            holder.imgIcon.setImageResource(item.icon);
            holder.tvTitle.setText(item.title);
            holder.tvContent.setText(item.detail);
            holder.btnAction.setText(item.btn);
            if (position==0){
                holder.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //区区哦活动
                        Intent intent = new Intent(SocialInfluenceActivity.this, MainActivity.class);
                        intent.putExtra("register_main", "zhu");
                        startActivity(intent);
                        finish();
                    }
                });
            }else if (position==1){
                holder.btnAction.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //邀请好友
                        startActivity(new Intent(SocialInfluenceActivity.this, InviteFriendsActivity.class));
                        finish();
                    }
                });
            }else {
                holder.btnAction.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //签到
                        startActivity(new Intent(SocialInfluenceActivity.this, UserSignActivity.class));
                        finish();
                    }
                });
            }
            return convertView;
        }
    }
    class Holder {


        private final ImageView imgIcon;
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView btnAction;

        Holder(View view) {
            imgIcon = ((ImageView) view.findViewById(R.id.img_icon));
            tvTitle = ((TextView) view.findViewById(R.id.tv_title));
            tvContent = ((TextView) view.findViewById(R.id.tv_content));
            btnAction = ((TextView) view.findViewById(R.id.btn_action));

        }
    }
    private class ProveSocialItem {

        public int icon;
        public String title;
        public String detail;
        public String btn;
        public String url;

        public ProveSocialItem(int icon, String title, String detail, String btn) {
            this.icon = icon;
            this.title = title;
            this.detail = detail;
            this.btn = btn;
        }
    }



    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

}
