package com.robin8.rb.module.mine.activity;

import android.content.Intent;
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
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.mine.model.HelpCenterModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by IBM on 2016/8/14.
 */
public class HelpCenterActivity extends BaseActivity {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private ListView mListView;
    private HelpListAdapter mHelpListAdapter;
    private List<HelpCenterModel.NoticesBean> mDataList = new ArrayList<>();
    private int from;

    @Override
    public void setTitleView() {
        getData();
        if (from == SPConstants.CREATE_FIRST_LIST) {
            mTVCenter.setText(getString(R.string.text_create));
        } else {
            mTVCenter.setText(getString(R.string.help_center));
        }
    }

    private void getData() {
        Intent intent = getIntent();
        from = intent.getIntExtra("from", -1);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_help_center, mLLContent);

        if (from == SPConstants.CREATE_FIRST_LIST) {
            View svContent = view.findViewById(R.id.sv_content);
            svContent.setVisibility(View.VISIBLE);
            return;
        }

        mListView = (ListView) view.findViewById(R.id.lv_list);
        mHelpListAdapter = new HelpListAdapter();
        mListView.setAdapter(mHelpListAdapter);
        String helpCenterData = CacheUtils.getString(HelpCenterActivity.this, SPConstants.HELP_CENTER_DATA, null);
        if (!TextUtils.isEmpty(helpCenterData)) {
            parseJson(helpCenterData);
        }
        initData();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_HELP;
        super.onResume();
    }

    private void initData() {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.HELP_CENTER_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {
        HelpCenterModel helpCenterModel = GsonTools.jsonToBean(response, HelpCenterModel.class);
        if (helpCenterModel != null && helpCenterModel.getError() == 0) {
            List<HelpCenterModel.NoticesBean> temp = helpCenterModel.getNotices();
            mDataList.clear();
            mDataList.add(null);
            mDataList.addAll(temp);
            mHelpListAdapter.notifyDataSetChanged();
            CacheUtils.putString(HelpCenterActivity.this, SPConstants.HELP_CENTER_DATA, response);
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    private class HelpListAdapter extends BaseAdapter implements View.OnClickListener {
        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public HelpCenterModel.NoticesBean getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HelpCenterModel.NoticesBean item = getItem(position);
            if (item == null) {
                View view = LayoutInflater.from(HelpCenterActivity.this).inflate(R.layout.help_center_header_item, null);
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                TextView tvArrow = (TextView) view.findViewById(R.id.tv_arrow);
                tvName.setText(R.string.feed_back);
                IconFontHelper.setTextIconFont(tvArrow, R.string.arrow_right);
                view.setOnClickListener(this);
                return view;
            }
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(HelpCenterActivity.this).inflate(R.layout.help_center_list_item, null);
                holder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question);
                holder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvQuestion.setText(item.getQuestion());
            holder.tvAnswer.setText(item.getAnswer());
            return convertView;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HelpCenterActivity.this, FeedBackActivity.class);
            startActivity(intent);
        }
    }

    private class ViewHolder {
        public TextView tvQuestion;
        public TextView tvAnswer;
    }
}
