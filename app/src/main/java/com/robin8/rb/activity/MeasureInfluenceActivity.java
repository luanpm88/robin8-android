package com.robin8.rb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseBackHomeActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.OtherLoginListBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

/**
 * 社交影响力测试首页面
 */
public class MeasureInfluenceActivity extends BaseBackHomeActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_influence);
        TextView mTVMeasure = (TextView) findViewById(R.id.tv_measure);
        TextView mTVTourist = (TextView) findViewById(R.id.tv_tourist);
        mTVMeasure.setOnClickListener(this);
        mTVTourist.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_measure:
                goToBindSocial();
                break;

            case R.id.tv_tourist:
                goToMain();
                break;
        }
    }

    public void goToMain() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void goToBindSocial() {

        HttpRequest.getInstance().get(true,HelpTools.getUrl(CommonConfig.START_URL), new RequestCallback() {
            @Override
            public void onError(Exception e) {
                CustomToast.showShort(MeasureInfluenceActivity.this, MeasureInfluenceActivity.this.getString(R.string.no_net));
            }

            @Override
            public void onResponse(String response) {
                if(TextUtils.isEmpty(response)){
                     return;
                }
                OtherLoginListBean bean = GsonTools.jsonToBean(response, OtherLoginListBean.class);
                if (bean.getError() == 0) {
                    Intent intent = new Intent(MeasureInfluenceActivity.this, BindSocialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list",bean.getIdentities());
                    bundle.putString("kol_uuid", bean.getKol_uuid());
                    bundle.putBoolean("uploaded_contacts",bean.isUploaded_contacts());

                    MeasureInfluenceActivity.this.startActivity(intent.putExtras(bundle));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }
}
