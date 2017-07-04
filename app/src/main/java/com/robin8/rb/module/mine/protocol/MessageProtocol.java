package com.robin8.rb.module.mine.protocol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.activity.WebViewActivity;
import com.robin8.rb.base.BaseRecyclerViewPresenter;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.indiana.protocol.IProtocol;
import com.robin8.rb.module.mine.adapter.MessageAdapter;
import com.robin8.rb.module.mine.model.MessageModel;
import com.robin8.rb.module.reword.activity.DetailContentActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Figo on 2016/6/30.
 */
public class MessageProtocol implements IProtocol {

    private static final String TYPE_ANNOUNCEMENT = "announcement";
    private static final String TYPE_COMMON = "common";
    private Activity mActivity;
    private List<MessageModel.MessagesBean> mMessagesBeanList = new ArrayList<MessageModel.MessagesBean>();
    private MessageAdapter mMessageAdapter;
    private int mCurrentPage = 1;
    private RequestParams mRequestParams;
    private int mTotalPages;

    public MessageProtocol(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public RequestParams getRequestParams() {

        if (mTotalPages != 0 && mCurrentPage > mTotalPages) {
            return null;
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("status", "all");
        return mRequestParams;
    }

    @Override
    public RequestParams getHeaderRequestParams() {
        return null;
    }

    @Override
    public void parseHeaderJson(String json, int currentState) {
    }


    @Override
    public BaseRecyclerAdapter getAdapter() {
        mMessageAdapter = new MessageAdapter(mMessagesBeanList);
        mMessageAdapter.setOnRecyclerViewListener(new MessageAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                skipToDetail(position);
            }
        });
        return mMessageAdapter;
    }

    private void skipToDetail(int position) {

        if (mMessagesBeanList == null || position < 0 || position >= mMessagesBeanList.size()
                || mMessagesBeanList.get(position) == null) {
            return;
        }
        readThisMessage(mMessagesBeanList.get(position).getId());
        String messageType = mMessagesBeanList.get(position).getMessage_type();
        if (TextUtils.isEmpty(messageType)) {
            return;
        }
        Intent intent = null;
        switch (messageType) {
            case TYPE_ANNOUNCEMENT:
                intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("title", mMessagesBeanList.get(position).getUrl());
                intent.putExtra("url", mMessagesBeanList.get(position).getTitle());
                mActivity.startActivity(intent);
                return;
            case TYPE_COMMON:
                return;
            default:
                intent = new Intent(mActivity, DetailContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("from", SPConstants.MESSAGE_ACTIVITY);
                bundle.putSerializable("bean", mMessagesBeanList.get(position));
                mActivity.startActivity(intent.putExtras(bundle));
                return;
        }
    }

    private void readThisMessage(int id) {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.MESSAGES_URL + "/" + String.valueOf(id) + "/read"), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.logXXfigo(response);
            }
        });
    }

    @Override
    public void setCurrentPage(int i) {
        this.mCurrentPage = i;
    }

    @Override
    public void showNumberSeletor() {

    }

    @Override
    public void notifyData() {
        if(mMessagesBeanList == null || mMessagesBeanList.size()==0){
            return;
        }
        for(int i=0;i<mMessagesBeanList.size();i++){
            mMessagesBeanList.get(i).setIs_read(true);
        }
    }

    @Override
    public void parseJson(String json, int currentState) {
        MessageModel MessageModel = GsonTools.jsonToBean(json, MessageModel.class);
        if (MessageModel != null && MessageModel.getError() == 0) {
            mTotalPages = MessageModel.getTotal_pages();
            List<MessageModel.MessagesBean> tempList = MessageModel.getMessages();
            if (tempList != null && tempList.size() > 0) {
                if (currentState != BaseRecyclerViewPresenter.LOAD_MORE) {
                    mMessagesBeanList.clear();
                    mCurrentPage++;
                    mMessagesBeanList.addAll(tempList);
                    mMessageAdapter.setDataList(mMessagesBeanList);
                    mMessageAdapter.notifyDataSetChanged();
                }else {
                    if (mCurrentPage<=mTotalPages){
                        mCurrentPage++;
                        mMessagesBeanList.addAll(tempList);
                        mMessageAdapter.setDataList(mMessagesBeanList);
                        mMessageAdapter.notifyDataSetChanged();
                   }
                        // else {
//                        CustomToast.showShort(mActivity,"没有更多消息");
//                    }
                }

            }
        }
    }
}
