package com.robin8.rb.ui.module.social;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.activity.MainActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.model.OtherLoginListBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

/**
 社交影响力测试首页面 */
public class MeasureInfluenceActivity extends BaseActivity implements View.OnClickListener {

    private View view;
    private RelativeLayout llFirst;

    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        view = LayoutInflater.from(this).inflate(R.layout.activity_measure_influence, mLLContent);
        HelpTools.insertCommonXml(HelpTools.SecondIn,"Second");
        HelpTools.insertCommonXml(HelpTools.ThirdIn,"Third");
        llFirst = ((RelativeLayout) view.findViewById(R.id.layout_first_women));
            TextView mTVMeasureFirst = (TextView) findViewById(R.id.tv_measure_women);//women中测试社交价值
            TextView mTVCampaign = (TextView) findViewById(R.id.tv_campaign);//man中测试社交价值
            TextView mTVJump = (TextView) findViewById(R.id.tv_jump_this);//man中测试社交价值
           // TextView mTVMeasureSecond = (TextView) findViewById(R.id.tv_measure);//man中测试社交价值
            mTVMeasureFirst.setOnClickListener(this);
            mTVCampaign.setOnClickListener(this);
            mTVJump.setOnClickListener(this);
//            mTVMeasureSecond.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump_this:
                //跳过
              //  llFirst.setVisibility(View.GONE);
              //  llFirst.startAnimation(AnimationUtils.loadAnimation(MeasureInfluenceActivity.this,R.anim.slide_out_left));
                intentMan();
                break;
            case R.id.tv_campaign:
                //去做活动啦
                Intent intent = new Intent(MeasureInfluenceActivity.this, MainActivity.class);
                intent.putExtra("register_main", "zhu");
                startActivity(intent);
                finish();
                break;
            case R.id.tv_measure_women:
                Intent intents = new Intent(MeasureInfluenceActivity.this, MainActivity.class);
                intents.putExtra("register_main", "influence");
                startActivity(intents);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
               // llFirst.setVisibility(View.GONE);
               // llFirst.startAnimation(AnimationUtils.loadAnimation(MeasureInfluenceActivity.this,R.anim.slide_out_left));
                break;
        }
    }
private void intentMan(){
    //Intent intent = new Intent(MeasureInfluenceActivity.this, MeasureInfluenceManActivity.class);
   // startActivity(intent);
   // finish();
   // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    Intent intent = new Intent(MeasureInfluenceActivity.this, MainActivity.class);
    intent.putExtra("register_main", "zhu");
    startActivity(intent);
    finish();
}
    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void goToBindSocial() {

        HttpRequest.getInstance().get(true, HelpTools.getUrl(CommonConfig.START_URL), new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(MeasureInfluenceActivity.this, MeasureInfluenceActivity.this.getString(R.string.no_net));
            }

            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    LogUtil.LogShitou("影响力开始测试", response);
                    return;
                }

                OtherLoginListBean bean = GsonTools.jsonToBean(response, OtherLoginListBean.class);
                if (bean.getError() == 0) {
                    Intent intent = new Intent(MeasureInfluenceActivity.this, BindSocialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", bean.getIdentities());
                    bundle.putString("kol_uuid", bean.getKol_uuid());
                    bundle.putBoolean("uploaded_contacts", bean.isUploaded_contacts());
                    MeasureInfluenceActivity.this.startActivity(intent.putExtras(bundle));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intentMan();
        }
        return false;
    }
}
