package com.robin8.rb.ui.activity;

import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.util.BitmapUtil;


/**
 * 测试影响力 绑定社交账号页面
 */
public class RegistrationActivitiy extends BaseActivity {

//    {
//        Intent intent = new Intent(DetailContentActivity.this, RegistrationActivitiy.class);
//        intent.putExtra("image_url", mCampaignInviteEntity.getCampaign().getImg_url());
//        intent.putExtra("activity_name", mCampaignInviteEntity.getCampaign().getName());
//        intent.putExtra("activity_time", DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", mCampaignInviteEntity.getCampaign()
//                .getStart_time()) + " - " + DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", mCampaignInviteEntity.getCampaign().getDeadline()));
//        intent.putExtra("activity_address", mCampaignInviteEntity.getShare_url());
//        intent.putExtra("reword_money", StringUtil.deleteZero(String.valueOf(mCampaignInviteEntity.getCampaign().getPer_action_budget())));
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }
    private EditText mETNameItemInfo;
    private EditText mETTelphoneItemInfo;
    private EditText mETWechatAccountItemInfo;
    private EditText mETWechatFriendsItemInfo;
    private TextView mTVSinaItemInfo;
    private TextView mTVConsumeItemInfo;
    private TextView mTVRemarksItemInfo;
    private ImageView mImageView;
    private TextView mTVTitle;
    private TextView mTVRewordMoney;
    private TextView mTVActivityAddress;
    private TextView mTVActivityTime;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.activities_registration));
    }

    @Override
    public void initView() {
        mLLTitleBar.setBackgroundResource(android.R.color.transparent);
        mLLRoot.setBackgroundResource(R.mipmap.mine_bg);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_registration, mLLContent, true);

        mImageView = (ImageView) view.findViewById(R.id.iv);
        mTVTitle = (TextView) view.findViewById(R.id.tv_title);
        mTVRewordMoney = (TextView) view.findViewById(R.id.tv_reword_money);
        mTVActivityTime = (TextView) view.findViewById(R.id.tv_activity_time);
        mTVActivityAddress = (TextView) view.findViewById(R.id.tv_activity_address);
        initItem(view);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image_url");
        String activityName = intent.getStringExtra("activity_name");
        String activityTime = intent.getStringExtra("activity_time");
        String activityAddress = intent.getStringExtra("activity_address");
        String rewordMoney = intent.getStringExtra("reword_money");

        mTVTitle.setText(activityName);
        mTVActivityTime.setText(activityTime);
        mTVActivityAddress.setText(activityAddress);
        BitmapUtil.loadImage(this.getApplicationContext(),imageUrl,mImageView);
        mTVRewordMoney.setText("¥ " + rewordMoney );
    }

    private void initItem(View view) {
        // 初始化姓名
        View layoutName = view.findViewById(R.id.layout_name);
        TextView tvNameItemTitle = (TextView) layoutName.findViewById(R.id.tv_item_title);
        tvNameItemTitle.setText(getResources().getString(R.string.name));
        mETNameItemInfo = (EditText) layoutName.findViewById(R.id.et_item_info);
        mETNameItemInfo.setInputType(InputType.TYPE_CLASS_TEXT);
        // 初始化联系电话
        View layoutTelphone = view.findViewById(R.id.layout_telphone);
        TextView tvTelphoneItemTitle = (TextView) layoutTelphone.findViewById(R.id.tv_item_title);
        tvTelphoneItemTitle.setText(getResources().getString(R.string.contact_telphone));
        mETTelphoneItemInfo = (EditText) layoutTelphone.findViewById(R.id.et_item_info);
        mETTelphoneItemInfo.setInputType(InputType.TYPE_CLASS_PHONE);
        // 初始化微信号
        View layoutWechatAccount = view.findViewById(R.id.layout_wechat_account);
        TextView tvWechatAccountItemTitle = (TextView) layoutWechatAccount.findViewById(R.id.tv_item_title);
        tvWechatAccountItemTitle.setText(getResources().getString(R.string.wechat_account));
        mETWechatAccountItemInfo = (EditText) layoutWechatAccount.findViewById(R.id.et_item_info);
        mETWechatAccountItemInfo.setInputType(InputType.TYPE_CLASS_TEXT);
        // 初始化微信好友数
        View layoutWechatFriends = view.findViewById(R.id.layout_wechat_friends);
        TextView tvWechatFriendsItemTitle = (TextView) layoutWechatFriends.findViewById(R.id.tv_item_title);
        tvWechatFriendsItemTitle.setText(getResources().getString(R.string.wechat_friends_number));
        mETWechatFriendsItemInfo = (EditText) layoutWechatFriends.findViewById(R.id.et_item_info);
        mETWechatFriendsItemInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
        // 初始化微博
        View layoutSina = view.findViewById(R.id.layout_sina);
        TextView tvSinaItemTitle = (TextView) layoutSina.findViewById(R.id.tv_item_title);
        tvSinaItemTitle.setText(getResources().getString(R.string.sina));
        mTVSinaItemInfo = (TextView) layoutSina.findViewById(R.id.tv_item_info);
        // 初始化理想报价
        View layoutConsume = view.findViewById(R.id.layout_consume);
        TextView tvConsumeItemTitle = (TextView) layoutConsume.findViewById(R.id.tv_item_title);
        tvConsumeItemTitle.setText(getResources().getString(R.string.ideal_offer));
        mTVConsumeItemInfo = (TextView) layoutConsume.findViewById(R.id.tv_item_info);
        // 初始化备注信息
        View layoutRemarks = view.findViewById(R.id.layout_remarks);
        TextView tvRemarksItemTitle = (TextView) layoutRemarks.findViewById(R.id.tv_item_title);
        tvRemarksItemTitle.setText(getResources().getString(R.string.remark_info));
        mTVRemarksItemInfo = (TextView) layoutRemarks.findViewById(R.id.tv_item_info);
        mTVRemarksItemInfo.setTextColor(getResources().getColor(R.color.sub_gray_custom));
        mTVRemarksItemInfo.setText(getResources().getString(R.string.click_write));

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_start_test:
                break;
            case R.id.ib_weixin:
                break;
            case R.id.ib_weibo:
                break;
            case R.id.ib_qq:
                break;
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
