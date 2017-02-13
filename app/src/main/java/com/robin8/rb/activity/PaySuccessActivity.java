package com.robin8.rb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.create.activity.FragmentsActivity;
import com.robin8.rb.util.HelpTools;

/**
 * 提现页面
 */
public class PaySuccessActivity extends BaseActivity {

    private int from;

    @Override
    public void setTitleView() {
        getData();

        if (from == SPConstants.EDIT_CREATE_ACTIVITY) {
            mTVCenter.setText(this.getText(R.string.article_success));
        } else {
            mTVCenter.setText(this.getText(R.string.order_pay_success));
        }
    }

    private void getData() {
        Intent intent = getIntent();
        from = intent.getIntExtra("from", -1);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_order_pay_success, mLLContent, true);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        TextView tv2 = (TextView) view.findViewById(R.id.tv2);
        TextView lookActivity = (TextView)view.findViewById(R.id.look_activity);

        if (from == SPConstants.EDIT_CREATE_ACTIVITY) {
            tv.setText(R.string.release_success);
            tv2.setText(R.string.we_will_check_your_create_in_one_day);
            lookActivity.setText(R.string.look_my_create);
        }

        lookActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至广告主页面
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.ADVERTISER_ADD_PAY_SUCCESSED;
        super.onResume();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public void finish() {

        if (from == SPConstants.EDIT_CREATE_ACTIVITY) {
            String nameArr[] = {"我的分享", "我的创作", "待审核", "审核拒绝"};//待审核、审核通过、审核拒绝, 我的分享
            String campaignTypeArr[] = {"shares", "passed", "pending", "rejected"};//'pending' , 'passed','rejected', 'shares'
            Intent intent = new Intent(this, FragmentsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("name", nameArr);
            bundle.putStringArray("type", campaignTypeArr);
            bundle.putString("page_name", StatisticsAgency.MY_CREATE);
            bundle.putString("title_name", getString(R.string.my_create));
            bundle.putString("url", HelpTools.getUrl(CommonConfig.MY_CREATE_URL));
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            super.finish();
            return;
        }

        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_PAY_SUCCESSFUL);
        Intent intent = new Intent(PaySuccessActivity.this, ADHostActivity.class);
        PaySuccessActivity.this.startActivity(intent);
        super.finish();
    }


}
