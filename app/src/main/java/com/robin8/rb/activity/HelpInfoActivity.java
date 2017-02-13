package com.robin8.rb.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.HelpInfoModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包页面
 */
public class HelpInfoActivity extends BaseActivity {

    private ListView mListView;
    private HelpInfoAdapter mHelpInfoAdapter;
    private boolean mNeedProgress;
    private WProgressDialog mWProgressDialog;
    private List<HelpInfoModel.QuestionEntity> mNoticesList= new ArrayList<HelpInfoModel.QuestionEntity>();

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.normal_issue));
    }

    @Override
    public void initView() {
        mLLContent.setBackgroundResource(R.color.white_custom);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_help_info, mLLContent, true);
        mListView = (ListView) view.findViewById(R.id.lv_help);
        mHelpInfoAdapter = new HelpInfoAdapter();
        mListView.setAdapter(mHelpInfoAdapter);

        initData();
    }

    private void initData() {
        String helpInfoJson = CacheUtils.getString(this.getApplicationContext(), SPConstants.HELP_INFO, null);
        if (!TextUtils.isEmpty(helpInfoJson)) {
            mNeedProgress = false;
            parseJson(helpInfoJson);
        } else {
            mNeedProgress = true;
            mWProgressDialog = WProgressDialog.createDialog(this);
            mWProgressDialog.show();
        }
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.HELP_INFO_URL), new RequestParams(), new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                CacheUtils.putString(HelpInfoActivity.this.getApplicationContext(), SPConstants.HELP_INFO, response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (mNeedProgress) {
                    parseJson(response);
                }
            }
        });
    }

    private void parseJson(String json) {
        HelpInfoModel helpInfoModel = GsonTools.jsonToBean(json, HelpInfoModel.class);
        if(helpInfoModel == null){
            CustomToast.showShort(HelpInfoActivity.this, getString(R.string.please_data_wrong));
            return;
        }
        if (helpInfoModel.getError() == 0) {
            mNoticesList = helpInfoModel.getNotices();
            mHelpInfoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    class HelpInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(mNoticesList ==null){
                return 0;
            }
            return mNoticesList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNoticesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(HelpInfoActivity.this).inflate(R.layout.help_info_list_item, null);
                viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question);
                viewHolder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (mNoticesList == null || position < 0 || position >= mNoticesList.size()) {
                return null;
            }
            viewHolder.tvQuestion.setText(mNoticesList.get(position).getQuestion());
            viewHolder.tvAnswer.setText(mNoticesList.get(position).getAnswer());
            return convertView;
        }
    }

    public static class ViewHolder {
        public static TextView tvQuestion;
        public static TextView tvAnswer;
    }
}
