package com.robin8.rb.indiana.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.indiana.model.CountBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

/**
 * 计算详情
 */
public class IndianaCountActivity extends BaseActivity {


    private TextView tv_lucky_number;
    private TextView tv_order_sum;
    private TextView tv_lottery;
    private TextView tv_tips;

    @Override
    public void setTitleView() {
        mTVCenter.setText(getString(R.string.count_detail));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_indiana_count,mLLContent);
         tv_lucky_number = (TextView) view.findViewById(R.id.tv_lucky_number);
         tv_order_sum = (TextView) view.findViewById(R.id.tv_order_sum);
         tv_lottery = (TextView) view.findViewById(R.id.tv_lottery);
         tv_tips = (TextView) view.findViewById(R.id.tv_tips);

        Intent intent = getIntent();
        String code = intent.getStringExtra("code");

        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("code",code);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET,
                HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL) + "/" + code + "/formula", params, new RequestCallback() {
                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("xxfigo","response="+response);
                        CountBean bean = GsonTools.jsonToBean(response,CountBean.class);
                        if (bean == null) {
                            CustomToast.showShort(IndianaCountActivity.this, getString(R.string.please_data_wrong));
                            return;
                        }
                        if(bean.getError()==0){
                            tv_lucky_number.setText(bean.getLucky_number());
                            tv_order_sum.setText("="+bean.getOrder_sum());
                            tv_lottery.setText("="+bean.getLottery_number()+"(第"+bean.getLottery_issue()+"期)");
                            tv_tips.setText(IndianaCountActivity.this.getString(R.string.lucky_number)+bean.getLucky_number());
                        }else {
                            CustomToast.showShort(IndianaCountActivity.this, bean.getDetail());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INDIANA_DETAIL_CALCULATION;
        super.onResume();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }
}
