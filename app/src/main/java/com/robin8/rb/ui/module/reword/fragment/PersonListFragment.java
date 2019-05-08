package com.robin8.rb.ui.module.reword.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.BrandBillAdapter;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.ui.module.first.activity.KolDetailContentActivity;
import com.robin8.rb.ui.module.reword.bean.PersonListBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 人员列表 */
public class PersonListFragment extends BaseFragment {

    private ViewPagerAdapter.SelectItem mData;
    private DataAdapter mDataAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isRefresh;
    private List<PersonListBean.InviteesBean> mInviteBeanList;
    private final static int INIT_DATA = 0;
    private final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private int mCurrentPage = 1;

    private BasePresenter mBasePresenter;
    //  private WProgressDialog mWProgressDialog;
    private boolean mHasMore = true;
    private int mTotalPages;

    private void getInitMsg(String campaign_id) {
        if ((mTotalPages != 0 && mCurrentPage > mTotalPages) && mCurrentState == LOAD_MORE) {
            mXRefreshView.stopLoadMore(true);
            return;
        }
        //        if (mCurrentState == INIT_DATA) {
        //            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        //            mWProgressDialog.show();
        //        }

        if (mCurrentState != LOAD_MORE) {
            mCurrentPage = 1;
        }
        RequestParams requestParams = new RequestParams();
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        requestParams.put("id", campaign_id);
        requestParams.put("invitee_page", mCurrentPage);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl("api/v1/campaigns/" + campaign_id + "/invitees"), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                CustomToast.showShort(mActivity, R.string.robin345);
                //                if (mWProgressDialog != null) {
                //                    mWProgressDialog.dismiss();
                //                }
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                //                if (mWProgressDialog != null) {
                //                    mWProgressDialog.dismiss();
                //                }
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore();
                }
               // LogUtil.LogShitou("接单人员数据", response);
                PersonListBean invitePeopleModel1 = GsonTools.jsonToBean(response, PersonListBean.class);
                if (invitePeopleModel1 == null) {
                    CustomToast.showShort(mActivity, getString(R.string.please_data_wrong));
                    return;
                }
                if (invitePeopleModel1.getError() == 0) {
                    mTotalPages = invitePeopleModel1.getTotal_pages();
                    List<PersonListBean.InviteesBean> invitees = invitePeopleModel1.getInvitees();
                    if (invitees != null && invitees.size() > 0) {
                        if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                            mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                        }

                        if (mCurrentState != LOAD_MORE) {
                            mInviteBeanList.clear();
                        }
                        mCurrentPage++;
                        mInviteBeanList.addAll(invitees);
                        mDataAdapter.setDataList(mInviteBeanList);
                        mDataAdapter.notifyDataSetChanged();
                    }
                }
                if (mInviteBeanList.size() == 0) {
                    mNoDataLL.setVisibility(View.VISIBLE);
                    mXRefreshView.setVisibility(View.GONE);
                }
            }
        });
    }

    private String campaign_id;
    private RecyclerView mRecyclerView;
    private XRefreshView mXRefreshView;
    private View mNoDataLL;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_person_list, null);
        campaign_id = getActivity().getIntent().getStringExtra("campaign_id");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_income_detail);
        mRecyclerView.setHasFixedSize(true);
        mXRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);
        mNoDataLL = view.findViewById(R.id.ll_no_data);
        mInviteBeanList = new ArrayList<>();
        initXRefreshView();

        initRecyclerView();
        mDataAdapter.setOnBottomListener(new DataAdapter.OnBottomListener() {

            @Override
            public void isOnBottom(boolean isBottom) {
                mIsBottomB = isBottom;
            }
        });
        getInitMsg(campaign_id);
        mDataAdapter.setOnRecyclerViewListener(new DataAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if (mInviteBeanList.get(position) != null) {
                    if ( mInviteBeanList.get(position).getId()!=0){
                        Intent intent = new Intent(mActivity, KolDetailContentActivity.class);
                        intent.putExtra("id", mInviteBeanList.get(position).getId());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
        //setLayoutManager
        //        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        //        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((LRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        //        mRecyclerView.setLayoutManager(manager);
        //        Intent intent = new Intent(mActivity, KolDetailContentActivity.class);
        //        intent.putExtra("id", mDataAdapter.getDataList().get(position).getId());
        //        startActivity(intent);
        return view;
    }

    private void initXRefreshView() {

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(mActivity);
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.post(new Runnable() {

            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(mActivity);
            }
        });

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshFooterView = new RefreshFooterView(mActivity);
        mXRefreshView.setCustomFooterView(mRefreshFooterView);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentState = DRAG_REFRESH;
                getInitMsg(campaign_id);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                getInitMsg(campaign_id);
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastPosition = manager.findLastVisibleItemPosition();
                mIsBottomB = mDataAdapter.getItemCount() - 1 == lastPosition;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
        });
    }

    private boolean mIsBottomB;
    private GridLayoutManager manager;

    private void initRecyclerView() {
        mDataAdapter = new DataAdapter(mActivity, mInviteBeanList);
        manager = new GridLayoutManager(mActivity, 3);
        // manager.setSpanSizeLookup(new HeaderSpanSizeLookup((LRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mDataAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private static class DataAdapter extends BaseRecyclerAdapter {
        private ViewHolders mIncomeDetailViewHolder;
        private List<PersonListBean.InviteesBean> mList;
        private Context context;

        private interface OnRecyclerViewListener {
            void onItemClick(int position);

            boolean onItemLongClick(int position);
        }

        private OnRecyclerViewListener onRecyclerViewListener;

        public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
            this.onRecyclerViewListener = onRecyclerViewListener;
        }

        private final String TAG = BrandBillAdapter.class.getSimpleName();

        public void setDataList(List<PersonListBean.InviteesBean> list) {
            this.mList = list;

        }
        public List<PersonListBean.InviteesBean>  getDataList(){
            return mList;
        }


        public DataAdapter(Context context, List<PersonListBean.InviteesBean> list) {
            this.context = context;
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean isItem) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_person_list_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            mIncomeDetailViewHolder = new ViewHolders(view);
            return mIncomeDetailViewHolder;
        }

        @Override
        public RecyclerView.ViewHolder getViewHolder(View view) {
            return mIncomeDetailViewHolder;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position, boolean isItem) {
            ViewHolders holder = (ViewHolders) viewHolder;
            holder.position = position;
            if (mList.get(position) == null) {
                setImageView(holder.imageView, holder.textView, null);
                return;
            }
            if (TextUtils.isEmpty(mList.get(position).getName())) {
                holder.textView.setText("");
            } else {
                holder.textView.setText(mList.get(position).getName().toString());
            }
            setImageView(holder.imageView, holder.textView, mList.get(position).getAvatar_url());
            holder.imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (null != onRecyclerViewListener) {
                        onRecyclerViewListener.onItemClick(position);
                    }
                }
            });
        }

        @Override
        public int getAdapterItemCount() {
            return mList.size();
        }

        private class ViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private TextView textView;
            private ImageView imageView;
            private int position;

            public ViewHolders(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_name);
                imageView = (ImageView) itemView.findViewById(R.id.iv_bg);
            }

            @Override
            public void onClick(View v) {
                if (null != onRecyclerViewListener) {
                    onRecyclerViewListener.onItemClick(position);
                }
            }

            @Override
            public boolean onLongClick(View v) {
                if (null != onRecyclerViewListener) {
                    return onRecyclerViewListener.onItemLongClick(position);
                }
                return false;
            }
        }

        private void setImageView(final ImageView ivBg, final TextView textView, String url) {

            if (ivBg == null) {
                return;
            }
            ViewGroup.LayoutParams lp = ivBg.getLayoutParams();
            lp.width = (int) ((DensityUtils.getScreenWidth(context) - 18 * BaseApplication.mPixelDensityF) / 3);
            lp.height = (int) ((DensityUtils.getScreenWidth(context) - 18 * BaseApplication.mPixelDensityF) * 2 / 5);
            ivBg.setLayoutParams(lp);

            ViewGroup.LayoutParams lp2 = textView.getLayoutParams();
            lp2.width = (int) ((DensityUtils.getScreenWidth(context) - 18 * BaseApplication.mPixelDensityF) / 3);
            textView.setLayoutParams(lp2);
            if (url != null) {
                BitmapUtil.loadImage(ivBg.getContext(), url, ivBg, BitmapUtil.getBg());
            } else {
                ivBg.setImageResource(R.mipmap.icon_user_default);
            }
        }

        private OnBottomListener bottomListener;

        public void setOnBottomListener(OnBottomListener bottomListener) {
            this.bottomListener = bottomListener;
        }

        public interface OnBottomListener {
            void isOnBottom(boolean isBottom);
        }
        //        private LayoutInflater mLayoutInflater;
        //        private List<PersonListBean.InviteesBean> mList;
        //
        //        public DataAdapter(List<PersonListBean.InviteesBean> mList) {
        //            this.mList = mList;
        //            mLayoutInflater = LayoutInflater.from(mActivity);
        //        }
        //
        //        @Override
        //        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //
        //            return new ViewHolder(mLayoutInflater.inflate(R.layout.fragment_person_list_item, parent, false));
        //        }
        //
        //        @Override
        //        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //
        //            PersonListBean.InviteesBean inviteesBean = mList.get(position);
        //            ViewHolder viewHolder = (ViewHolder) holder;
        //            if (inviteesBean == null) {
        //                viewHolder.textView.setText("");
        //            } else {
        //                if (TextUtils.isEmpty(inviteesBean.getName())) {
        //                    viewHolder.textView.setText("");
        //                } else {
        //                    viewHolder.textView.setText(inviteesBean.getName());
        //                }
        //                setImageView(viewHolder.imageView, viewHolder.textView, inviteesBean.getAvatar_url());
        //            }
        //
        //        }
        //
        //        private void setImageView(final ImageView ivBg, final TextView textView, String url) {
        //
        //            if (ivBg == null) {
        //                return;
        //            }
        //
        //            ViewGroup.LayoutParams lp = ivBg.getLayoutParams();
        //            lp.width = (int) ((DensityUtils.getScreenWidth(mActivity) - 18 * BaseApplication.mPixelDensityF) / 3);
        //            lp.height = (int) ((DensityUtils.getScreenWidth(mActivity) - 18 * BaseApplication.mPixelDensityF) * 2 / 5);
        //            ivBg.setLayoutParams(lp);
        //
        //            ViewGroup.LayoutParams lp2 = textView.getLayoutParams();
        //            lp2.width = (int) ((DensityUtils.getScreenWidth(mActivity) - 18 * BaseApplication.mPixelDensityF) / 3);
        //            textView.setLayoutParams(lp2);
        //            if (url != null) {
        //                BitmapUtil.loadImage(ivBg.getContext(), url, ivBg, BitmapUtil.getBg());
        //            }
        //        }
        //
        //        @Override
        //        public int getItemCount() {
        //
        //            return mList.size();
        //        }
        //
        //        private class ViewHolder extends RecyclerView.ViewHolder {
        //
        //            private TextView textView;
        //            private ImageView imageView;
        //
        //            public ViewHolder(View itemView) {
        //
        //                super(itemView);
        //                textView = (TextView) itemView.findViewById(R.id.tv_name);
        //                imageView = (ImageView) itemView.findViewById(R.id.iv_bg);
        //            }
        //        }
    }

}
