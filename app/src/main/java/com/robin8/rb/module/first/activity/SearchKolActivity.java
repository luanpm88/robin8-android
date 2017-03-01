package com.robin8.rb.module.first.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.create.prenster.SearchArticlePresenter;
import com.robin8.rb.module.first.prenster.SearchResultPresenter;
import com.robin8.rb.module.first.view.ISearchKolView;
import com.robin8.rb.ui.widget.OtherListView;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 搜索页面
 */
public class SearchKolActivity extends BaseActivity implements ISearchKolView {

    private static final int ET_EMPTY = 0;
    private static final int ET_UNEMPTY = 1;
    private static final String SPLIT_SIGN = ",,,";
    @Bind(R.id.et)
    EditText mEditText;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    @Bind(R.id.ll_search)
    LinearLayout llSearch;
    @Bind(R.id.line_view)
    View lineView;
    @Bind(R.id.iv_back_search)
    ImageView ivBack;
    @Bind(R.id.lv_history_list)
    OtherListView lvHistoryList;
    @Bind(R.id.clear_history)
    TextView tvClearHistory;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.xrefreshview)
    XRefreshView mXRefreshView;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.ll_no_data)
    LinearLayout llNoData;
    private WProgressDialog mWProgressDialog;
    private String tagName;
    private SearchResultPresenter mSearchResultPresenter;//搜索KOL
    private SearchArticlePresenter mSearchArticlePresenter;//搜索文章
    private String kolName;
    private String from;
    private String tagNameCN;
    private List<String> mSearchHistoryList = new ArrayList<>();
    private MyBaseAdapter mMyBaseAdapter;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private String address;
    private Typeface mIconFont;
    private String title;

    @Override
    public void setTitleView() {
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        tagNameCN = intent.getStringExtra("tag_name_cn");
        tagName = intent.getStringExtra("tag_name");
        kolName = intent.getStringExtra("kol_name");
        address = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        initResume();
    }

    private void initResume() {
        if (SPConstants.FIRST_PAGE_ITEM_TAG.endsWith(from)) {
            mPageName = StatisticsAgency.KOL_LIST_CATEGORY;
        } else if (SPConstants.MY_CARE.endsWith(from)) {
            mPageName = StatisticsAgency.KOL_LIST_FOLLOWERS;
        } else if (SPConstants.FIRST_PAGE_SEARCH.endsWith(from)) {
            mPageName = StatisticsAgency.KOL_SEARCH;
        } else if (SPConstants.FIRST_PAGE_SEARCH.endsWith(from)) {
            mPageName = StatisticsAgency.KOL_SEARCH;
        } else if (SPConstants.ARTICLE_SEARCH.endsWith(from)) {
            mPageName = StatisticsAgency.ARTICLE_SEARCH;
        }
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_search_kol, mLLContent);
        ButterKnife.bind(this);
        mIconFont = Typeface.createFromAsset(getAssets(), SPConstants.ICON_FONT_TTF);
        tvDelete.setTypeface(mIconFont);
        mRefreshHeaderView = new RefreshHeaderView(this);
        mRefreshFooterView = new RefreshFooterView(this);

        if (SPConstants.ARTICLE_SEARCH.endsWith(from)) {
            mSearchArticlePresenter = new SearchArticlePresenter(this, HelpTools.getUrl(address));
        } else {
            mSearchResultPresenter = new SearchResultPresenter(this, tagName, kolName, false, HelpTools.getUrl(address));
        }
        initContent();
    }

    private void initContent() {
        if (SPConstants.FIRST_PAGE_SEARCH.endsWith(from) || SPConstants.ARTICLE_SEARCH.endsWith(from)) {
            mLLTitleBar.setVisibility(View.GONE);
            llSearch.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            tvDelete.setVisibility(View.GONE);
            updateView(ET_EMPTY);
            ivBack.setOnClickListener(this);
            tvDelete.setOnClickListener(this);
            tvSearch.setOnClickListener(this);
            tvClearHistory.setOnClickListener(this);
            EditTextWatcher editTextWatcher = new EditTextWatcher();
            mEditText.addTextChangedListener(editTextWatcher);

            getHistorySearchData();
            lvHistoryList.setVisibility(View.VISIBLE);
            mXRefreshView.setVisibility(View.GONE);
            mMyBaseAdapter = new MyBaseAdapter();
            lvHistoryList.setAdapter(mMyBaseAdapter);

            if (!TextUtils.isEmpty(title) && SPConstants.ARTICLE_SEARCH.endsWith(from)) {
                mEditText.setText(title);
                search(title);
            }
        } else if (SPConstants.FIRST_PAGE_ITEM_TAG.endsWith(from) || SPConstants.MY_CARE.endsWith(from)) {
            llSearch.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
            lvHistoryList.setVisibility(View.GONE);
            tvClearHistory.setVisibility(View.GONE);
            mLLTitleBar.setVisibility(View.VISIBLE);
            mXRefreshView.setVisibility(View.VISIBLE);
            mTVCenter.setText(tagNameCN);
            mSearchResultPresenter.init();
        }
    }

    private void getHistorySearchData() {
        String searchHistory = null;
        if (SPConstants.FIRST_PAGE_SEARCH.endsWith(from)) {
            searchHistory = CacheUtils.getString(this, SPConstants.SEARCH_HISTORY, "");
        } else if (SPConstants.ARTICLE_SEARCH.endsWith(from)) {
            searchHistory = CacheUtils.getString(this, SPConstants.ARTICLE_SEARCH + SPConstants.SEARCH_HISTORY, "");
        }

        mSearchHistoryList.clear();
        if (TextUtils.isEmpty(searchHistory)) {
            lvHistoryList.setVisibility(View.GONE);
            tvClearHistory.setVisibility(View.INVISIBLE);
        } else if (!searchHistory.contains(SPLIT_SIGN)) {
            mSearchHistoryList.add(searchHistory);
        } else {
            String[] searchHistoryArr = searchHistory.split(SPLIT_SIGN);
            List<String> searchHistoryList = Arrays.asList(searchHistoryArr);
            mSearchHistoryList.addAll(searchHistoryList);
        }
    }

    private void saveHistorySearchData(List<String> list) {
        if (list == null || list.size() <= 0) {
            if (SPConstants.FIRST_PAGE_SEARCH.endsWith(from)) {
                CacheUtils.putString(this, SPConstants.SEARCH_HISTORY, "");
            } else if (SPConstants.ARTICLE_SEARCH.endsWith(from)) {
                CacheUtils.putString(this, SPConstants.ARTICLE_SEARCH + SPConstants.SEARCH_HISTORY, "");
            }
            return;
        }

        int size = list.size();
        if (size > 5) {
            size = 5;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(list.get(i)).append(SPLIT_SIGN);
        }
        LogUtil.logXXfigo("putString " + sb.substring(0, sb.length() - SPLIT_SIGN.length()));
        String tagStr = null;
        if (SPConstants.FIRST_PAGE_SEARCH.endsWith(from)) {
            tagStr = SPConstants.SEARCH_HISTORY;
        } else if (SPConstants.ARTICLE_SEARCH.endsWith(from)) {
            tagStr = SPConstants.ARTICLE_SEARCH + SPConstants.SEARCH_HISTORY;
        }
        CacheUtils.putString(this, tagStr, sb.substring(0, sb.length() - SPLIT_SIGN.length()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                search(mEditText.getText().toString());
                break;
            case R.id.tv_delete:
                updateViewWhenETEmpty();
                if (SPConstants.ARTICLE_SEARCH.endsWith(from)) {
                    mSearchArticlePresenter.clearSearchResult();
                } else {
                    mSearchResultPresenter.clearSearchResult();
                }
                break;
            case R.id.iv_back_search:
                finish();
                break;
            case R.id.clear_history:
                mSearchHistoryList.clear();
                mMyBaseAdapter.notifyDataSetChanged();
                saveHistorySearchData(mSearchHistoryList);
                tvClearHistory.setVisibility(View.GONE);
                break;
        }
        super.onClick(v);
    }

    private void updateViewWhenETEmpty() {
        updateView(ET_EMPTY);
        getHistorySearchData();
        if (mSearchHistoryList.size() > 0) {
            lvHistoryList.setVisibility(View.VISIBLE);
            tvClearHistory.setVisibility(View.VISIBLE);
        } else {
            lvHistoryList.setVisibility(View.GONE);
            tvClearHistory.setVisibility(View.GONE);
        }
        mMyBaseAdapter.notifyDataSetChanged();
    }

    /**
     * 搜索
     */
    private void search(String searchContent) {

        if (TextUtils.isEmpty(searchContent)) {
            return;
        }
        mXRefreshView.setVisibility(View.VISIBLE);
        lvHistoryList.setVisibility(View.GONE);
        tvClearHistory.setVisibility(View.GONE);
        if (SPConstants.ARTICLE_SEARCH.endsWith(from)) {
            mSearchArticlePresenter.searchKol(searchContent);
        } else {
            mSearchResultPresenter.searchKol(searchContent);
        }
        ListUtils.deleteStrFromList(searchContent, mSearchHistoryList);
        mSearchHistoryList.add(0, searchContent);
        saveHistorySearchData(mSearchHistoryList);
    }

    private void updateView(int state) {
        switch (state) {
            case ET_EMPTY:
                mEditText.setText(null);
                tvSearch.setEnabled(false);
                tvDelete.setVisibility(View.GONE);
                mXRefreshView.setVisibility(View.GONE);
                break;
            case ET_UNEMPTY:
                tvDelete.setVisibility(View.VISIBLE);
                tvSearch.setEnabled(true);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (mWProgressDialog != null) {
            mWProgressDialog.dismiss();
            mWProgressDialog = null;
        }
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public XRefreshView getXRefreshView() {
        return mXRefreshView;
    }

    @Override
    public RefreshHeaderView getRefreshHeaderView() {
        return mRefreshHeaderView;
    }

    @Override
    public RefreshFooterView getRefreshFooterView() {
        return mRefreshFooterView;
    }

    @Override
    public WProgressDialog getWProgressDialog() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        return mWProgressDialog;
    }

    @Override
    public View getLLNoData() {
        return llNoData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    class EditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null) {
                updateView(ET_UNEMPTY);
            } else {
                updateView(ET_EMPTY);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSearchHistoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return mSearchHistoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SearchKolActivity.this).inflate(R.layout.item_clear_history, null);
                holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
                holder.tvDelete.setTypeface(mIconFont);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvContent.setText(mSearchHistoryList.get(position));
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ListUtils.deleteStrFromList(mSearchHistoryList.get(position), mSearchHistoryList);
                    if (mSearchHistoryList.size() == 0) {
                        tvClearHistory.setVisibility(View.GONE);
                    }
                    mMyBaseAdapter.notifyDataSetChanged();
                    saveHistorySearchData(mSearchHistoryList);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditText.setText(mSearchHistoryList.get(position));
                    search(mSearchHistoryList.get(position));
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        public TextView tvContent;
        public TextView tvDelete;
    }
}
