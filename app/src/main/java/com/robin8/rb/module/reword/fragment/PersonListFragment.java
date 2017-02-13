package com.robin8.rb.module.reword.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.HeaderSpanSizeLookup;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.robin8.rb.R;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.model.CampaignInviteBean.InviteesBean;
import com.robin8.rb.module.first.activity.KolDetailContentActivity;
import com.robin8.rb.module.reword.adapter.ListBaseAdapter;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DensityUtils;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 人员列表
 */
public class PersonListFragment extends BaseFragment {

    /**
     * 服务器端一共多少条数据
     */
    private static int TOTAL_COUNTER = 0;
    /**
     * 每一页展示多少条数据
     */
    private static int REQUEST_COUNT = 0;
    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;
    @Bind(R.id.list)
    LRecyclerView mRecyclerView;
    private ViewPagerAdapter.SelectItem mData;
    private DataAdapter mDataAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private boolean isRefresh;
    private List<InviteesBean> mInviteBeanList = new ArrayList<>();
    private int currentPage = 0;

    private static class PreviewHandler extends Handler {

        private WeakReference<PersonListFragment> ref;

        PreviewHandler(PersonListFragment fragment) {
            ref = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final PersonListFragment fragment = ref.get();
            if (fragment == null || fragment.getActivity().isFinishing()) {
                return;
            }

            switch (msg.what) {
                case -1:
                    if (fragment.isRefresh) {
                        fragment.mDataAdapter.clear();
                        mCurrentCounter = 0;
                        fragment.currentPage = 0;
                    }

                    int currentSize = fragment.mDataAdapter.getItemCount();

                    //模拟组装20个数据
                    ArrayList<InviteesBean> newList = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        int position = fragment.currentPage * 20 + i;
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }
                        if (position >= fragment.mInviteBeanList.size()) {
                            return;
                        }
                        InviteesBean inviteesBean = fragment.mInviteBeanList.get(position);
                        newList.add(inviteesBean);
                    }
                    fragment.addItems(newList);

                    if (fragment.isRefresh) {
                        fragment.isRefresh = false;
                        fragment.mRecyclerView.refreshComplete();
                        fragment.notifyDataSetChanged();
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(fragment.mRecyclerView, LoadingFooter.State.Normal);
                    }
                    break;
                case -2:
                    fragment.notifyDataSetChanged();
                    break;
                case -3:
                    if (fragment.isRefresh) {
                        fragment.currentPage++;
                        fragment.isRefresh = false;
                        fragment.mRecyclerView.refreshComplete();
                        fragment.notifyDataSetChanged();
                    } else {
                        RecyclerViewStateUtils.setFooterViewState(fragment.getActivity(), fragment.mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, fragment.mFooterClick);
                    }
                    break;
            }
        }
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<InviteesBean> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Serializable serializable = getActivity().getIntent().getSerializableExtra("invitees");
        if (serializable instanceof List) {
            mInviteBeanList = (List<InviteesBean>) serializable;
            TOTAL_COUNTER = mInviteBeanList.size();
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_person_list, null);
        ButterKnife.bind(this, view);

        mDataAdapter = new DataAdapter(mActivity);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mActivity, mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        //setLayoutManager
        GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((LRecyclerViewAdapter) mRecyclerView.getAdapter(), manager.getSpanCount()));
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                requestData();
            }

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onBottom() {
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }

                if (mCurrentCounter < TOTAL_COUNTER) {
                    // loading more
                    RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
                    requestData();
                } else {
                    //the end
                    RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

        });
        mRecyclerView.setRefreshing(true);

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, KolDetailContentActivity.class);
                intent.putExtra("id", mDataAdapter.getDataList().get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class DataAdapter extends ListBaseAdapter<InviteesBean> {

        private LayoutInflater mLayoutInflater;

        public DataAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.fragment_person_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            InviteesBean inviteesBean = mDataList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(inviteesBean.getName());
            setImageView(viewHolder.imageView, viewHolder.textView, inviteesBean.getAvatar_url());
        }

        private void setImageView(final ImageView ivBg,final TextView textView, String url) {
            if (ivBg == null) {
                return;
            }

            ViewGroup.LayoutParams lp = ivBg.getLayoutParams();
            lp.width = (int) ((DensityUtils.getScreenWidth(mActivity) - 18 * BaseApplication.mPixelDensityF) / 3);
            lp.height = (int) ((DensityUtils.getScreenWidth(mActivity) - 18 * BaseApplication.mPixelDensityF) * 2 / 5);
            ivBg.setLayoutParams(lp);

            ViewGroup.LayoutParams lp2 = textView.getLayoutParams();
            lp2.width = (int) ((DensityUtils.getScreenWidth(mActivity) - 18 * BaseApplication.mPixelDensityF) / 3);
            textView.setLayoutParams(lp2);

            BitmapUtil.loadImage(ivBg.getContext(), url, ivBg, BitmapUtil.getBg());
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_name);
                imageView = (ImageView) itemView.findViewById(R.id.iv_bg);
            }
        }
    }

    /**
     * 模拟请求网络
     */
    private void requestData() {
        mHandler.sendEmptyMessage(-1);
    }
}
