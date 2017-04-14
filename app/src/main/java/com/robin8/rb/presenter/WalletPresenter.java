package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.activity.IncomeDetailActivity;
import com.robin8.rb.activity.WithdrawCashActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.UserAccountBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.IWalletView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author Figo
 */
public class WalletPresenter extends BasePresenter implements PresenterI {

    private final IWalletView mIUserView;
    private Activity mActivity;
    private WProgressDialog mWProgressDialog;
    private UserAccountBean mUserAccountBean;
    private List<UserAccountBean.StatsEntity> mStatsList;
    private PointValue mLastSelect;
    private LineChartView mLineChartView;

    public WalletPresenter(Activity activity, IWalletView userView) {
        mActivity = activity;
        mIUserView = userView;
    }

    public void init() {
        mLineChartView = mIUserView.getLineChartView();
        mLineChartView.setVisibility(View.INVISIBLE);
        getData();
    }

    private void getData() {
        mWProgressDialog = WProgressDialog.createDialog(mActivity);
        mWProgressDialog.show();

        getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.KOLS_ACCOUNT_URL), new RequestParams(), new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                mUserAccountBean = GsonTools.jsonToBean(response, UserAccountBean.class);
                if (mUserAccountBean != null && mUserAccountBean.getError() == 0) {
                    updateView();
                }
            }
        });
    }

    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {
        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
        }
    }

    private void updateView() {
        if (mUserAccountBean == null || mUserAccountBean.getKol() == null) {
            return;
        }
        mStatsList = mUserAccountBean.getStats();
        if (mStatsList == null || mStatsList.size() == 0) {
            return;
        }

        mIUserView.setTVWasItemNumber(StringUtil.deleteZero(mUserAccountBean.getKol().getTotal_withdraw()));
        mIUserView.setTVWasConsumeItemNumber(StringUtil.deleteZero(mUserAccountBean.getKol().getTotal_expense()));
        mIUserView.setTVBeingItemNumber(StringUtil.deleteZero(mUserAccountBean.getKol().getFrozen_amount()));
        mIUserView.setTVBeableItemNumber(StringUtil.deleteZero(mUserAccountBean.getKol().getAvail_amount()));
        mIUserView.setTVIncomes(String.valueOf(mStatsList.get(mStatsList.size() - 1).getCount()));
        mIUserView.setTVTotal(String.valueOf(mStatsList.get(mStatsList.size() - 1).getTotal_amount()));
        mIUserView.setTVTotalIncome(StringUtil.deleteZero(String.valueOf(mUserAccountBean.getKol().getTotal_income())));

        if (Float.parseFloat(mUserAccountBean.getKol().getAvail_amount()) >= 50) {
            mIUserView.setLLbottom(true);
        } else {
            mIUserView.setLLbottom(false);
        }

        updateLineChartView();
    }

    /**
     * 加载折线图
     */
    public void updateLineChartView() {

        if (mStatsList == null || mStatsList.size() <= 0) {
            return;
        }

        //初始化数据
        final ArrayList<PointValue> mPointValues = new ArrayList<>();
        List<AxisValue> mAxisValues = new ArrayList<>();
        int size = mStatsList.size();
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += mStatsList.get(i).getTotal_amount();
            float totalAmount = (float) mStatsList.get(i).getTotal_amount();
            LogUtil.logXXfigo("totalAmount"+totalAmount);
            PointValue pointValue;
            if(totalAmount==0){
                pointValue = new PointValue(i, 0.04f);
            }else {
                pointValue = new PointValue(i, totalAmount);
            }

            if (i == size - 1) {
                mLastSelect = pointValue;
                pointValue.setMode(PointValue.MODE_HIGHLIGHT);
            } else {
                pointValue.setMode(PointValue.MODE_DRAW);
            }
            mPointValues.add(pointValue);
            mAxisValues.add(new AxisValue(i).setLabel(DateUtil.formatTime("yyyy-MM-dd", mStatsList.get(i).getDate(), "MM/dd")));
        }

        //初始化线条
        int color = mActivity.getResources().getColor(R.color.red_custom);
        List<Line> lines = new ArrayList<Line>();
        Line line = new Line(mPointValues).setColor(color).setCubic(false);
        line.setColor(Color.parseColor("#EDB001"));
        line.setPointRadius(4);
        line.setStrokeWidth(3);
        line.setFilled(false);
        line.setSquare(false);
        line.setHasLabels(true);
        line.setPointHilightColor(Color.BLACK);
        line.setHasLabelsOnlyForSelected(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setValues(mAxisValues);
        axisX.setTypeface(Typeface.DEFAULT);
        axisX.setDrawVirtualLine(false);
        axisX.setHasSeparationLine(true);
        axisX.setTextSize(12);
        data.setAxisXBottom(axisX);

        Axis axisY = new Axis();  //Y轴
        axisY.setAutoGenerated(true);
        axisY.setHasLines(true);
        axisY.setDrawVirtualLine(true);
        axisY.setHasTiltedLabels(false);//倾斜
        axisY.setHasSeparationLine(false);//是否画轴线
        data.setAxisYLeft(axisY);
        if (sum < 5) {
            ArrayList<PointValue> mPointValues2 = new ArrayList<>();
            List<AxisValue> mAxisValues2 = new ArrayList<>();
            for (int i = 0; i < mStatsList.size(); i++) {
                mPointValues2.add(new PointValue(i, i));
                mAxisValues2.add(new AxisValue(i));
            }
            Line line2 = new Line(mPointValues2).setColor(color).setCubic(false);
            line2.setColor(Color.parseColor("#00ffffff"));
            line2.setPointColor(Color.parseColor("#00ffffff"));
            lines.add(line2);
            LineChartData data2 = new LineChartData();
            data2.setLines(lines);

            Axis axisX2 = new Axis(); //X轴
            axisX2.setValues(mAxisValues2);
            axisX2.setTypeface(Typeface.DEFAULT);
            axisX2.setDrawVirtualLine(false);
            axisX2.setLineColor(Color.BLACK);
            axisX2.setHasTiltedLabels(false);
            data2.setAxisXBottom(axisX2);

            Axis axisY2 = new Axis();  //Y轴
            axisY2.setAutoGenerated(true);
            axisY2.setHasLines(true);
            axisY2.setDrawVirtualLine(true);
            axisY2.setHasTiltedLabels(false);
            axisY2.setHasSeparationLine(true);
            data2.setAxisYLeft(axisY2);
        }
        //设置行为属性，支持缩放、滑动以及平移
        mLineChartView.setInteractive(true);//点击动画
        mLineChartView.setZoomEnabled(false);
        mLineChartView.setScrollEnabled(true);
        mLineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mLineChartView.setLineChartData(data);
        mLineChartView.setVisibility(View.VISIBLE);
        mLineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {

                if (mStatsList == null || pointIndex < 0 || pointIndex >= mStatsList.size()) {
                    return;
                }
                UserAccountBean.StatsEntity statsEntity = mStatsList.get(pointIndex);
                mIUserView.setTVIncomes(String.valueOf(statsEntity.getCount()));
                mIUserView.setTVTotal(String.valueOf(statsEntity.getTotal_amount()));

                //点中时置为高亮 并把其他的置为普通
                if (mLastSelect != null) {
                    mLastSelect.setMode(PointValue.MODE_DRAW);
                }
                value.setMode(PointValue.MODE_HIGHLIGHT);
                mLastSelect = value;
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    /**
     * 点击总收益跳转
     */
    public void checkIncomeDetail() {
        Intent intent = new Intent(mActivity, IncomeDetailActivity.class);
        intent.putExtra("url", HelpTools.getUrl(CommonConfig.TRANSACTIONS_URL));
        intent.putExtra("title", mActivity.getString(R.string.account_menu));
        mActivity.startActivity(intent);
    }

    /**
     * 提现
     */
    public void withdrawCash() {
        Intent intent = new Intent(mActivity, WithdrawCashActivity.class);
        if(mUserAccountBean!=null){
            String availAmount = mUserAccountBean.getKol().getAvail_amount();
            intent.putExtra("avail_amount",availAmount);
        }
        mActivity.startActivityForResult(intent, SPConstants.WALLETACTIVIRY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SPConstants.WALLETACTIVIRY && data != null) {
            boolean withcashed = data.getBooleanExtra("withcashed", false);
            if (withcashed) {
                init();
            }
        }
    }

}
