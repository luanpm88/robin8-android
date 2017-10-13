package com.robin8.rb.module.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.util.CustomToast;

public class CampaignChoseTypeActivity extends BaseActivity {
    private String chose;
    private CheckBox cbWechat;
    private CheckBox cbWeibo;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_campaign_chose_type);
//    }

    @Override
    public void setTitleView() {
        mTVCenter.setText("推广平台选择");
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(R.string.submit);
        mBottomTv.setOnClickListener(this);
        mIVBack.setOnClickListener(this);

    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_campaign_chose_type, mLLContent);
        RelativeLayout llChoseWeChat = (RelativeLayout) view.findViewById(R.id.ll_chose_wechat);
        RelativeLayout llChoseWeibo = (RelativeLayout) view.findViewById(R.id.ll_chose_weibo);
        cbWechat = ((CheckBox) view.findViewById(R.id.cb_weixin));
        cbWeibo = ((CheckBox) view.findViewById(R.id.cb_weibo));
        llChoseWeChat.setOnClickListener(this);
        llChoseWeibo.setOnClickListener(this);
        initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_chose_wechat:
                if (cbWechat.isChecked()){
                    cbWechat.setChecked(false);
                }else {
                    cbWechat.setChecked(true);
                }
                break;
            case R.id.ll_chose_weibo:
                if (cbWeibo.isChecked()){
                    cbWeibo.setChecked(false);
                }else {
                    cbWeibo.setChecked(true);
                }
                break;
            case R.id.tv_bottom:
                Intent intent = getIntent();
                if (cbWechat.isChecked() && cbWeibo.isChecked()){
                   // intent.putExtra("chose", R.string.weixin+R.string.wechat+","+R.string.weibo);
                    chose =getString( R.string.weixin)+getString(R.string.wechat)+","+getString(R.string.weibo);
                }else if (cbWechat.isChecked() && cbWeibo.isChecked()==false){
                   // intent.putExtra("chose", R.string.weixin+R.string.wechat);
                    chose =getString( R.string.weixin)+getString(R.string.wechat);
                }else if (cbWechat.isChecked()==false &&cbWeibo.isChecked()){
                  //  intent.putExtra("chose",R.string.weibo);
                    chose =getString(R.string.weibo);
                }else {
                    CustomToast.showShort(CampaignChoseTypeActivity.this,"请选择发布平台");
                    return;
                }
                intent.putExtra("chose",chose);
                setResult(SPConstants.CHOSE_TYPE, intent);
                finish();
                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        chose = intent.getStringExtra("chose");
        if(TextUtils.isEmpty(chose)){
            cbWechat.setChecked(true);
            cbWeibo.setChecked(false);
        }else {
            if (chose.equals(getString( R.string.weixin)+getString(R.string.wechat)+","+getString(R.string.weibo))){
                cbWechat.setChecked(true);
                cbWeibo.setChecked(true);
            }else if (chose.equals((getString( R.string.weixin)+getString(R.string.wechat)))){
                cbWechat.setChecked(true);
                cbWeibo.setChecked(false);
            }else {
                cbWechat.setChecked(false);
                cbWeibo.setChecked(true);
            }
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
