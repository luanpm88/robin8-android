package com.robin8.rb.ui.module.bigv.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.module.bigv.adapter.AddPriceAdapter;
import com.robin8.rb.ui.module.bigv.model.BigvDetailModel;
import com.robin8.rb.ui.module.social.view.LinearLayoutForListView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.robin8.rb.ui.module.bigv.activity.AddPriceActivity.FIX_LIST;
import static com.robin8.rb.ui.module.bigv.activity.AddPriceActivity.PLATFORM_LIST;

public class UploadActivity extends BaseActivity {

    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_result_text)
    TextView tvResultText;
    @Bind(R.id.lv_list)
    LinearLayoutForListView lvList;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.card_submit)
    CardView cardSubmit;
    @Bind(R.id.tv_add_link_title)
    TextView tvAddLinkTitle;
    @Bind(R.id.card_add_link)
    CardView cardAddLink;
    @Bind(R.id.ll_over)
    LinearLayout llOver;

    private int campaignId;
    private WProgressDialog mWProgressDialog;
    private List<BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean> terraceInfo;
    private List<BigvDetailModel.CreationBean.MyTendersBean> myTenders;
    private AddPriceAdapter addPriceAdapter;
    private ArrayList<Object> hashMapList;
    private CustomDialogManager cdm;
    public static final String STATUS = "status";


    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
        mCardTitle.setVisibility(View.VISIBLE);
        mViewLine.setVisibility(View.GONE);
        mTitle.setText(R.string.robin305);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_upload, mLLContent, true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        campaignId = intent.getIntExtra(BigvCampaignDetailActivity.CAMPAIGN_ID, 0);
        if (intent.getExtras().getSerializable(PLATFORM_LIST) != null) {
            terraceInfo = (List<BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean>) intent.getExtras().getSerializable(PLATFORM_LIST);
        }else {
            terraceInfo = new ArrayList<>();
        }
        if (intent.getExtras().getSerializable(FIX_LIST) != null) {
            tvSubmit.setText(R.string.robin442);
            myTenders = (List<BigvDetailModel.CreationBean.MyTendersBean>) intent.getExtras().getSerializable(FIX_LIST);
        } else {
            tvSubmit.setText(R.string.robin305);
        }
        String extra = intent.getStringExtra(STATUS);
        if (!TextUtils.isEmpty(extra)){
            //合作完成
            if(extra.equals("finished")){
                llOver.setVisibility(View.VISIBLE);
                cardAddLink.setVisibility(View.GONE);
                cardSubmit.setVisibility(View.GONE);
                tvResult.setText(R.string.robin444);
                tvResultText.setText(R.string.robin445);
            }else if (extra.equals("STATE_UPLOADED")){
                llOver.setVisibility(View.GONE);
                cardAddLink.setVisibility(View.VISIBLE);
                tvResult.setText(R.string.robin446);
                tvResultText.setText(R.string.robin029);
            }else {
                llOver.setVisibility(View.GONE);
                cardAddLink.setVisibility(View.VISIBLE);
                tvResult.setText(R.string.robin447);
                tvResultText.setText(R.string.robin029);
            }
        }else {
            llOver.setVisibility(View.GONE);
            cardAddLink.setVisibility(View.VISIBLE);
        }
        hashMapList = new ArrayList<>();
        addPriceAdapter = new AddPriceAdapter(this, terraceInfo, 2);
        if (myTenders != null) {
            if (myTenders.size() != 0) {
                for (int i = 0; i < myTenders.size(); i++) {
                    addPriceAdapter.contents.put(i, String.valueOf(myTenders.get(i).getLink()));
                }
            }
        }
        lvList.setAdapter(addPriceAdapter);
        showMyDialog();
    }

    private void initData(int id, HashMap<Integer, String> contents) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        for (int i = 0; i < contents.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("from_terrace", terraceInfo.get(i).getName());
                jsonObject.put("link", contents.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hashMapList.add(jsonObject);
        }

        for (int i = 0; i < hashMapList.size(); i++) {
            LogUtil.LogShitou("看看数据", hashMapList.get(i).toString());
        }
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("links_ary", hashMapList);
        LogUtil.LogShitou("看看数据", "==>" + hashMapList.size());

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, (HelpTools.getUrl(CommonConfig.BIGV_LIST_DETAIL_URL) + id + CommonConfig.BIGV_LIST_DETAIL_LINK_URL), params, new RequestCallback() {

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
                parseJson(response);
            }
        });
    }

    private void parseJson(String result) {
        LogUtil.LogShitou("报价" + (HelpTools.getUrl(CommonConfig.BIGV_LIST_DETAIL_URL) + campaignId + CommonConfig.BIGV_LIST_DETAIL_TENDER_URL), result);
        BaseBean baseBean = GsonTools.jsonToBean(result, BaseBean.class);
        if (baseBean != null) {
            if (baseBean.getError() == 0) {
                show(2000);
            } else {
                if (! TextUtils.isEmpty(baseBean.getDetail())) {
                    CustomToast.showShort(this, baseBean.getDetail());
                }

            }
        }
    }

    public void show(int duration) {
        TimeCount timeCount = new TimeCount(duration, 1000);
        timeCount.start();
    }

    private void showMyDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.toast_layout, null);
        TextView infoTv = (TextView) view.findViewById(R.id.toast_msg);
        infoTv.setText(R.string.robin448);
        infoTv.setGravity(Gravity.CENTER);
        cdm = new CustomDialogManager(this, view);
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        // cdm.showDialog();
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            cdm.showDialog();
        }

        @Override
        public void onFinish() {
            cdm.dismiss();
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }


    @OnClick(R.id.card_submit)
    public void onViewClicked() {
        if (addPriceAdapter.contents.size() != 0) {
            for (int i = 0; i < addPriceAdapter.contents.size(); i++) {
                LogUtil.LogShitou("报价", addPriceAdapter.contents.get(i) + "<===>" + i);
            }
            initData(campaignId, addPriceAdapter.contents);
        } else {
            CustomToast.showShort(UploadActivity.this, R.string.robin396);
        }
    }
}
