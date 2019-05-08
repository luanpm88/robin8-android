package com.robin8.rb.ui.indiana.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.indiana.model.OrderBean;
import com.robin8.rb.ui.indiana.model.PayOrderModel;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;


/**
 * 商品详情
 * 一元夺宝支付
 */
public class IndianaOrderPayActivity extends BaseActivity {

    private String code;
    private int times;
    private String url;
    private String name;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.order_pay);
    }

    @Override
    public void initView() {

        View view = LayoutInflater.from(this).inflate(R.layout.activity_indiana_pay_order, mLLContent, true);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_issue = (TextView) view.findViewById(R.id.tv_issue);
        TextView tv_times = (TextView) view.findViewById(R.id.tv_times);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        TextView tv_pay_instantly = (TextView) view.findViewById(R.id.tv_pay_instantly);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        times = intent.getIntExtra("times", 5);
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");

        tv_name.setText(name);
        tv_issue.setText(getString(R.string.robin367,code));
        tv_times.setText(String.valueOf(times));
        tv_money.setText(String.valueOf(times));
        BitmapUtil.loadImage(this.getApplicationContext(), url, iv);
        tv_pay_instantly.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INDIANA_PAY;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_pay_instantly:
                payInstantly();
                break;
        }
    }

    public void payInstantly() {
        final BasePresenter presenter = new BasePresenter();
        final RequestParams params = new RequestParams();
        params.put("activity_code", code);
        params.put("num", times);

        presenter.getDataFromServer(true, HttpRequest.POST,
                HelpTools.getUrl(CommonConfig.LOTTERY_ORDERS_URL), params, new RequestCallback() {

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtil.LogShitou("一元夺宝111","=======>"+response);

                        PayOrderModel payOrderModel = GsonTools.jsonToBean(response, PayOrderModel.class);
                        if(payOrderModel == null){
                            CustomToast.showShort(IndianaOrderPayActivity.this, getString(R.string.please_data_wrong));
                            return;
                        }
                        if (payOrderModel.getError() != 0) {
                            CustomToast.showShort(IndianaOrderPayActivity.this, payOrderModel.getDetail());
                            return;
                        }
                        final OrderBean orderBean = payOrderModel.getOrder();
                        params.put("code", orderBean.getCode());

                        presenter.getDataFromServer(true, HttpRequest.PUT,
                                HelpTools.getUrl(CommonConfig.LOTTERY_ORDERS_URL + "/" + orderBean.getCode() + "/checkout"), params, new RequestCallback() {

                                    @Override
                                    public void onError(Exception e) {

                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        LogUtil.LogShitou("一元夺宝"+HelpTools.getUrl(CommonConfig.LOTTERY_ORDERS_URL + "/" + orderBean.getCode() + "/checkout"),"=======>"+response);
                                        BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                                        if (baseBean == null) {
                                            CustomToast.showShort(IndianaOrderPayActivity.this, getString(R.string.please_data_wrong));
                                            return;
                                        }
                                        if (baseBean.getError() == 0) {
                                            CustomToast.showShort(IndianaOrderPayActivity.this, getString(R.string.order_pay_success));
                                            NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_INDIANA_PAY_SUCCESSFUL);
                                            finish();
                                        } else {
                                            CustomToast.showShort(IndianaOrderPayActivity.this, baseBean.getDetail());
                                        }
                                    }
                                });
                    }
                });
    }


    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

}
