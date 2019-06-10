package com.robin8.rb.ui.module.reword.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.ui.module.first.model.PieDataBean;
import com.robin8.rb.ui.module.reword.bean.PersonAnalysisResultModel;
import com.robin8.rb.ui.module.reword.bean.PersonAnalysisResultModel.AgeAnalysisBean;
import com.robin8.rb.ui.module.reword.bean.PersonAnalysisResultModel.GenderAnalysisBean;
import com.robin8.rb.ui.module.reword.bean.PersonAnalysisResultModel.RegionAnalysisBean;
import com.robin8.rb.ui.module.reword.bean.PersonAnalysisResultModel.TagAnalysisBean;
import com.robin8.rb.ui.widget.PieChartView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 人员分析
 */
public class PersonAnalysisFragment extends BaseFragment implements View.OnClickListener {

//    @Bind(R.id.pv_classify)
//    PieChartView pvClassify;
    @Bind(R.id.pv_sex)
    PieChartView pvSex;
    @Bind(R.id.pv_age)
    PieChartView pvAge;
    @Bind(R.id.webview)
    WebView mWebView;
    private ViewPagerAdapter.SelectItem mData;
    private List<PieDataBean> mTempList = new ArrayList();
    private String mDetailUrl;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_person_analysis, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initWebView(final String provinceOpt) {
       // Log.e("provinceOpt",provinceOpt);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
//        mWebView.addJavascriptInterface(new Object() {
//            @SuppressWarnings("unused")
//            public void setResult(String param) {
//               String result = param;
//                CustomToast.showShort(mActivity,"结果是：" + result);
//            }
//        }, "jsObj");
        // 加载本地assets下面的index.html文件
        mWebView.loadUrl("file:///android_asset/index.html");
        // 这个是为了，判断页面是否加载完成。加载完成才能调用js方法
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // 加载完成，调用js方法。runOnUiThread这样做是为了运行在主线程
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json;
                        String p = "{'result':[{'xizang':{'stateInitColor':'0'},'hunan':{'stateInitColor':'0'}}]}";
                        try {
                            JSONObject jsonObject = new JSONObject(provinceOpt);
                            JSONArray arr = jsonObject.getJSONArray("result");
                            json = arr.toString();
                            if(mWebView == null){
                                return;
                            }
                            mWebView.loadUrl("javascript:test(" + json.substring(1, json.length() - 1) + ")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public String getName() {
        return mData.name;
    }

    @Override
    public void setData(ViewPagerAdapter.SelectItem data, String mUrl, String name) {
        this.mData = data;
    }

    @Override
    public void setAnalysisResultModel(Object obj) {
        if (!(obj instanceof PersonAnalysisResultModel)) {
            return;
        }
        PersonAnalysisResultModel analysisResultModel = (PersonAnalysisResultModel) obj;

        List<TagAnalysisBean> tagAnalysisList = analysisResultModel.getTag_analysis();
        mTempList.clear();
        for (int i = 0; i < tagAnalysisList.size(); i++) {
            TagAnalysisBean tagAnalysisBean = tagAnalysisList.get(i);
            PieDataBean bean = new PieDataBean(tagAnalysisBean.getName(), (float) tagAnalysisBean.getRatio());
            mTempList.add(bean);
        }
//        pvClassify.setDataList(mTempList);

        List<AgeAnalysisBean> ageAnalysisBeanList = analysisResultModel.getAge_analysis();
        mTempList.clear();
        for (int i = 0; i < ageAnalysisBeanList.size(); i++) {
            AgeAnalysisBean ageAnalysisBean = ageAnalysisBeanList.get(i);
            PieDataBean bean = new PieDataBean(ageAnalysisBean.getName(), (float) ageAnalysisBean.getRatio());
            mTempList.add(bean);
        }
        pvAge.setDataList(mTempList);


        List<GenderAnalysisBean> genderAnalysisBeanList = analysisResultModel.getGender_analysis();
        mTempList.clear();
        for (int i = 0; i < genderAnalysisBeanList.size(); i++) {
            GenderAnalysisBean genderAnalysisBean = genderAnalysisBeanList.get(i);
            PieDataBean bean = new PieDataBean(genderAnalysisBean.getName(), (float) genderAnalysisBean.getRatio());
            mTempList.add(bean);
        }
        pvSex.setDataList(mTempList);

        List<RegionAnalysisBean> regionAnalysisBeanList = analysisResultModel.getRegion_analysis();
        String provinceOpt = "{'result':[{'province_code0':{'stateInitColor':'0'}," +
                "'province_code1':{'stateInitColor':'0'}," + "'province_code2':{'stateInitColor':'0'}," +
                "'province_code3':{'stateInitColor':'0'}, 'province_code4':{'stateInitColor':'0'}, 'province_code5':{'stateInitColor':'0'}," +
                "'province_code6':{'stateInitColor':'0'}, 'province_code7':{'stateInitColor':'0'}, 'province_code8':{'stateInitColor':'0'}," +
                "'province_code9':{'stateInitColor':'0'}, 'province_codeA0':{'stateInitColor':'0'}, 'province_codeA1':{'stateInitColor':'0'}," +
                "'province_codeA2':{'stateInitColor':'0'}, 'province_codeA3':{'stateInitColor':'0'}, 'province_codeA4':{'stateInitColor':'0'}," +
                "'province_codeA5':{'stateInitColor':'0'}, 'province_codeA6':{'stateInitColor':'0'}, 'province_codeA7':{'stateInitColor':'0'}," +
                "'province_codeA8':{'stateInitColor':'0'}, 'province_codeA9':{'stateInitColor':'0'}, 'province_codeB0':{'stateInitColor':'0'}," +
                "'province_codeB1':{'stateInitColor':'0'}, 'province_codeB2':{'stateInitColor':'0'}, 'province_codeB3':{'stateInitColor':'0'}," +
                "'province_codeB4':{'stateInitColor':'0'}, 'province_codeB5':{'stateInitColor':'0'}, 'province_codeB6':{'stateInitColor':'0'}," +
                "'province_codeB7':{'stateInitColor':'0'}, 'province_codeB8':{'stateInitColor':'0'}, 'province_codeB9':{'stateInitColor':'0'}," +
                "'province_codeC0':{'stateInitColor':'0'}, 'province_codeC1':{'stateInitColor':'0'}, 'province_codeC2':{'stateInitColor':'0'}, " +
                "'province_codeC3':{'stateInitColor':'0'}}]}";

        for (int i = 0; i < regionAnalysisBeanList.size(); i++) {
            int a = i / 10;
            int b = i % 10;
            RegionAnalysisBean regionAnalysisBean = regionAnalysisBeanList.get(i);
            switch (a){
                case 0:
                    provinceOpt = provinceOpt.replace("province_code" + String.valueOf(b), regionAnalysisBean.getProvince_code());
                    break;
                case 1:
                    provinceOpt = provinceOpt.replace("province_codeA" + String.valueOf(b), regionAnalysisBean.getProvince_code());
                    break;
                case 2:
                    provinceOpt = provinceOpt.replace("province_codeB" + String.valueOf(b), regionAnalysisBean.getProvince_code());
                    break;
                case 3:
                    provinceOpt = provinceOpt.replace("province_codeC" + String.valueOf(b), regionAnalysisBean.getProvince_code());
                    break;
            }

        }
        initWebView(provinceOpt);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_detail:
                break;
        }
    }

}
