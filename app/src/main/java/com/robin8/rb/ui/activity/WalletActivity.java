package com.robin8.rb.ui.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.presenter.WalletPresenter;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.IWalletView;

import java.text.DecimalFormat;

import lecho.lib.hellocharts.view.LineChartView;

/**
 我的钱包页面 */
public class WalletActivity extends BaseActivity implements IWalletView {

    private WalletPresenter mWalletPresenter;

    private View mLLTotalIncome;//点击总收益控件
    private View mLLbottom;//提现
    private TextView mTVTotalIncome;//  总收益金额
    private TextView mTVWasItemNumber;// 已提现金额
    private TextView mTVBeingItemNumber;// 提现中金额
    private TextView mTVBeableItemNumber;// 可提现金额
    private TextView mTVIncomes;//共 0 笔收入
    private TextView mTVTotal;//合计：¥ 0
    private LineChartView mLineChartView;
    private TextView mTVSubmit;
    private TextView mTVWasConsumeItemNumber;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.mine_wallet));
        mTVRight.setVisibility(View.VISIBLE);
        mTVRight.setText(R.string.bill);
        mTVRight.setTextColor(UIUtils.getColor(R.color.sub_gray_custom));
    }

    @Override
    public void initView() {
        mLLContent.setBackgroundResource(R.color.white_custom);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_wallet, mLLContent, true);

        mLLTotalIncome = view.findViewById(R.id.ll_total_income);
        mLLbottom = view.findViewById(R.id.ll_bottom);
        mLineChartView = (LineChartView) view.findViewById(R.id.lcv_chart);

        mTVSubmit = (TextView) view.findViewById(R.id.tv_submit);
        mTVTotalIncome = (TextView) view.findViewById(R.id.tv_total_income);
        mTVIncomes = (TextView) view.findViewById(R.id.tv_incomes);
        mTVTotal = (TextView) view.findViewById(R.id.tv_total);
        //已提现
        View layoutWasWithdraw = view.findViewById(R.id.layout_was_withdraw);
        TextView tvWasItemTitle = (TextView) layoutWasWithdraw.findViewById(R.id.tv_item_title);
        tvWasItemTitle.setText(R.string.was_withdraw);
        mTVWasItemNumber = (TextView) layoutWasWithdraw.findViewById(R.id.tv_item_number);

        //已提现
        View layoutWasConsume = view.findViewById(R.id.layout_was_consume);
        TextView tvWasConsumeItemTitle = (TextView) layoutWasConsume.findViewById(R.id.tv_item_title);
        tvWasConsumeItemTitle.setText(R.string.was_consume);
        mTVWasConsumeItemNumber = (TextView) layoutWasConsume.findViewById(R.id.tv_item_number);
        //提现中
        View layoutBeingWithdraw = view.findViewById(R.id.layout_being_withdraw);
        TextView tvBeingItemTitle = (TextView) layoutBeingWithdraw.findViewById(R.id.tv_item_title);
        tvBeingItemTitle.setText(R.string.being_withdraw);
        mTVBeingItemNumber = (TextView) layoutBeingWithdraw.findViewById(R.id.tv_item_number);
        //可提现
        View layoutBeableWithdraw = view.findViewById(R.id.layout_beable_withdraw);
        TextView tvBeableItemTitle = (TextView) layoutBeableWithdraw.findViewById(R.id.tv_item_title);
        tvBeableItemTitle.setText(R.string.beable_withdraw);
        mTVBeableItemNumber = (TextView) layoutBeableWithdraw.findViewById(R.id.tv_item_number);

        mLLTotalIncome.setOnClickListener(this);
        mLLbottom.setOnClickListener(this);

        mWalletPresenter = new WalletPresenter(this, this);
        mWalletPresenter.init();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INCOME;
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWalletPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_bottom:
                mWalletPresenter.withdrawCash();
                break;
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
        mWalletPresenter.checkIncomeDetail();
    }

    @Override
    public void setTVTotalIncome(CharSequence text) {
        mTVTotalIncome.setText(text);
    }

    @Override
    public void setTVWasItemNumber(CharSequence text) {
        mTVWasItemNumber.setText(text);
    }


    @Override
    public void setTVWasConsumeItemNumber(CharSequence text) {
        mTVWasConsumeItemNumber.setText(text);
    }

    @Override
    public void setTVBeingItemNumber(CharSequence text) {
        mTVBeingItemNumber.setText(text);
    }

    /**
     @param text 可提现金额
     */
    @Override
    public void setTVBeableItemNumber(CharSequence text) {
        mTVBeableItemNumber.setText(text);
    }

    @Override
    public void setTVIncomes(CharSequence text) {

        String timesIncome = getString(R.string.total) + "<font color=#ecb200>" + text + "</font>" + getString(R.string.income_times);

        mTVIncomes.setText(Html.fromHtml(timesIncome));
    }

    @Override
    public void setTVTotal(String text) {
        String todayTotalIncome = getString(R.string.total_count) + "<font color=#ffffff>¥ " + StringUtil.deleteZero(text) + "</font>";
        mTVTotal.setText(Html.fromHtml(todayTotalIncome));
    }

    /**
     @param clickEnable 底部按钮设置
     */
    @Override
    public void setLLbottom(boolean clickEnable) {
        if (clickEnable) {
            mTVSubmit.setText(getString(R.string.withdraw_now));
            mLLbottom.setBackgroundResource(R.color.blue_custom);
        } else {
            if (TextUtils.isEmpty(mTVBeableItemNumber.getText().toString().trim())) {
                mTVSubmit.setText(getString(R.string.income_info3));
            } else {
                try {
                    float value = 50 - Float.valueOf(StringUtil.deleteZero(mTVBeableItemNumber.getText().toString().trim()));
                    if (value > 0) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        String values = df.format(value);
                        if(values.indexOf(".") > 0){
                            //正则表达
                            values = values.replaceAll("0+?$", "");//去掉后面无用的零
                            values = values.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
                        }
                        mTVSubmit.setText(getString(R.string.robin313,values));
                    }
                } catch (Exception e) {
                    mTVSubmit.setText(getString(R.string.income_info3));
                    e.printStackTrace();
                }
            }
            mLLbottom.setBackgroundResource(R.color.gray_custom);
            mLLbottom.setEnabled(false);
            mLLbottom.setClickable(false);
        }
    }

    @Override
    public LineChartView getLineChartView() {
        return mLineChartView;
    }
}
