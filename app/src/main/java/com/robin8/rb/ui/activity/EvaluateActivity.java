package com.robin8.rb.ui.activity;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.StarBar;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

public class EvaluateActivity extends BaseActivity {
    public static  String  EFFECT_SCORE = "effect_score";
    public int effectScore = 0;
    public static String REVIEW_CONTENT = "review_content";
    private String  reviewContent= "";

    public static String CAMPAIGN_ID = "campaign_id";
    private String campaign_id;

    private float mark;
    private EditText evaluateContent;
    private Button submitBtn;
    private TextView showContent;
    private StarBar seekBar;

    @Override
    public void setTitleView() {
        mTVCenter.setText(getString(R.string.effect_evaluate));
    }

    @Override
    protected void executeOnclickRightView() {

    }

    @Override
    protected void executeOnclickLeftView() {
//        Intent intent = new Intent();
//        intent.putExtra(EFFECT_SCORE,12);
//        intent.putExtra(REVIEW_CONTENT,"12er32ih");
//        setResult(Activity.RESULT_OK,intent);
        finish();

    }

    @Override
    public void initView() {
        reviewContent = getIntent().getStringExtra(REVIEW_CONTENT);
        effectScore = getIntent().getIntExtra(EFFECT_SCORE,0);
        int campaignId = getIntent().getIntExtra(CAMPAIGN_ID,0);
        campaign_id = String.valueOf(campaignId);
        mLLContent.setBackgroundResource(R.color.white_custom);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_evaluate, mLLContent, true);
        seekBar = (StarBar) view.findViewById(R.id.starBar);
        showContent = (TextView) findViewById(R.id.show_content);
        seekBar.setIntegerMark(true);
        seekBar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
            @Override
            public void onStarChange(float mark) {
                getMark(mark);
            }
        });
        submitBtn = (Button) findViewById(R.id.submit_btn);
        evaluateContent = (EditText) findViewById(R.id.evaluate_content);
        evaluateContent.setHint(R.string.effect_import);
        seekBar.setStarMark(effectScore);

        if(!TextUtils.isEmpty(reviewContent)){
            showContent.setText(reviewContent);
            showContent.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.GONE);
            evaluateContent.setVisibility(View.GONE);
        }
        if (effectScore != 0){
            submitBtn.setVisibility(View.GONE);
            seekBar.setOnDrawFlag(true);
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String  evaluateValue = evaluateContent.getText().toString();
                if (mark == 0){
                    CustomToast.showShort(EvaluateActivity.this, "请打分");
                }else if(TextUtils.isEmpty(evaluateValue)){
                    CustomToast.showShort(EvaluateActivity.this, "请输入请假内容");
                }else{
                    postEvaluate(evaluateValue,mark);
                }

            }
        });

    }
    private BasePresenter mBasePresenter;
    public void postEvaluate(final String evaluateValue,final float gradeMark){
        final WProgressDialog mWProgressDialog =  WProgressDialog.createDialog(this);
        mWProgressDialog.show();
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("campaign_id", campaign_id);
        requestParams.put("effect_score", String.valueOf(gradeMark));
        requestParams.put("review_content",evaluateValue);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.CAMPAIGN_EVALUATIONS), requestParams, new RequestCallback() {
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
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);

                if (baseBean == null) {
                    CustomToast.showShort(EvaluateActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (baseBean.getError() == 0) {
                    Intent intent = new Intent();
                    intent.putExtra(EFFECT_SCORE,(int)gradeMark);
                    intent.putExtra(REVIEW_CONTENT,evaluateValue);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                } else {
                    CustomToast.showShort(BaseApplication.getContext(), baseBean.getDetail());
                }
            }
        });

    }

    public void  getMark(float mark){
        this.mark = mark;
    }

}
