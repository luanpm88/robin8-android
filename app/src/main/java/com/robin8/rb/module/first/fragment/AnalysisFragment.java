package com.robin8.rb.module.first.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagCloudView;
import com.robin8.rb.R;
import com.robin8.rb.activity.WebViewActivity;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.module.first.adapter.TextTagsAdapter;
import com.robin8.rb.module.first.model.AnalysisResultModel;
import com.robin8.rb.module.first.model.ColumnarDataBean;
import com.robin8.rb.module.first.model.PieDataBean;
import com.robin8.rb.ui.widget.ColumnarChartView;
import com.robin8.rb.ui.widget.PieChartView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 文章分析
 */
public class AnalysisFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.pv_text)
    PieChartView pvText;
    @Bind(R.id.tag_cloud)
    TagCloudView tagCloud;
    @Bind(R.id.pv_sentiment)
    PieChartView pvSentiment;
    @Bind(R.id.atv_text)
    TextView atvText;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    @Bind(R.id.ccv_product)
    ColumnarChartView ccvProduct;
    @Bind(R.id.ccv_person_brand)
    ColumnarChartView ccvPersonBrand;
    private ViewPagerAdapter.SelectItem mData;
    private TextTagsAdapter mTextTagsAdapter;
    private List<String> mTagsList = new ArrayList<>();
    private String mDetailUrl;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_analysis, null);
        ButterKnife.bind(this, view);
        tagCloud.setFocusable(false);
        mTextTagsAdapter = new TextTagsAdapter(mTagsList);
        tagCloud.setAdapter(mTextTagsAdapter);
        tvDetail.setOnClickListener(this);
        return view;
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
        if(!(obj instanceof AnalysisResultModel)){
            return;
        }
        AnalysisResultModel analysisResultModel = (AnalysisResultModel) obj;
        super.setAnalysisResultModel(analysisResultModel);
        AnalysisResultModel.AnalysisInfoBean analysisInfo = analysisResultModel.getAnalysis_info();
        mDetailUrl = analysisResultModel.getCampaign_input().getUrl();
        String text = analysisInfo.getText();
        List<PieDataBean> keywordsList = analysisInfo.getKeywords();
        List<PieDataBean> sentimentList = analysisInfo.getSentiment();
        List<String> citiesList = analysisInfo.getCities();
        List<PieDataBean> categoriesList = analysisInfo.getCategories();

        List<ColumnarDataBean> personsBrandsList = analysisInfo.getPersons_brands();
        List<ColumnarDataBean> productsList = analysisInfo.getProducts();

        pvText.setDataList(categoriesList);
        pvSentiment.setDataList(sentimentList);
        atvText.setText(text);
        ccvProduct.setDataList(productsList);
        ccvPersonBrand.setDataList(personsBrandsList);

        mTagsList.clear();
        for (int i = 0; i < categoriesList.size(); i++) {
            mTagsList.add(categoriesList.get(i).getLabel());
        }

        for (int i = 0; i < keywordsList.size(); i++) {
            mTagsList.add(keywordsList.get(i).getLabel());
        }

        mTextTagsAdapter.setDataSet(mTagsList);
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
                if (!TextUtils.isEmpty(mDetailUrl)) {
                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra("title", "文章详情");
                    intent.putExtra("url", mDetailUrl);
                    startActivity(intent);
                }
                break;
        }
    }
}
