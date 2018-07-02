package com.robin8.rb.module.find.FloatAction;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.module.find.model.ArticleReadTimeModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

/**
 Created by zc on 2018/6/25. */

public class MyClick implements View.OnClickListener {
    private Context context;

    public MyClick(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.REDNUM))) {
            FloatActionController.getInstance().stopMonkServer(context);
        } else {
            if (Float.valueOf(HelpTools.getCommonXml(HelpTools.REDNUM))==0){
                FloatActionController.getInstance().stopMonkServer(context);
            }else {
                showDialog(context);
            }
        }

    }

    private void showDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_red_click_layout, null);
        final ImageView imgClose = (ImageView) view.findViewById(R.id.img_red_close);
        final RelativeLayout llRedOpen = (RelativeLayout) view.findViewById(R.id.ll_red_open);
        TextView tvRedNum = (TextView) view.findViewById(R.id.tv_red_num);
        TextView tvRedDetail = (TextView) view.findViewById(R.id.tv_red_detail);
        final CustomDialogManager cdm = new CustomDialogManager(context, view);
        if (! TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.REDNUM))) {
            tvRedNum.setText(HelpTools.getCommonXml(HelpTools.REDNUM));
            tvRedDetail.setText(HelpTools.getCommonXml(HelpTools.REDNUM)+"元奖励已放入钱包");
        }
        imgClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                imgClose.setVisibility(View.GONE);
                red(llRedOpen);
            }
        });
        llRedOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        Window window = cdm.dg.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        window.setGravity(Gravity.CENTER);
       window.setWindowAnimations(R.style.AnimCenter);
        cdm.dg.show();
    }

    private void red(final RelativeLayout llRedOpen) {
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("red_money", Float.valueOf(HelpTools.getCommonXml(HelpTools.REDNUM)));
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.SPLIT_RED_URL), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("拆红包结果+", response);
                ArticleReadTimeModel model = GsonTools.jsonToBean(response, ArticleReadTimeModel.class);
                if (model!=null){
                    if (model.getError()==0){
                        llRedOpen.setVisibility(View.VISIBLE);
                        FloatActionController.getInstance().stopMonkServer(context);
                        HelpTools.insertCommonXml(HelpTools.REDNUM,"");
                    }
                }

            }
        });
    }
}
