package com.robin8.rb.base;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.robin8.rb.util.UIUtils;

import com.robin8.rb.R;

/**
 * Created by Figo on 2016/6/30.
 */
public abstract class LoadingPage extends FrameLayout {

    public final static int STATE_UNLOAD = 0;//页面请求网络的初始状态
    public final static int STATE_LOADING = 1;//正在加载
    public final static int STATE_LOAD_ERROR = 2;//加载失败
    public final static int STATE_LOAD_EMPTY = 3;//加载数据为空
    public final static int STATE_LOAD_SUCCESS = 4;//加载数据成功

    private final LayoutParams layoutParams;

    private View loadingView;//正在加载view
    private View errorView;//加载失败view
    private View emptyView;//加载数据为空view
    private View successView;//加载数据成功view

    //当前(请求网络)状态,决定添加在帧布局中那个view对象去做展示
    private  int currentState = STATE_UNLOAD;

    public LoadingPage(Context context) {
        super(context);
        layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        //初始化4中类型界面
        initLoadingPage();
    }

    private void initLoadingPage() {
        //1,正在加载
        if(loadingView == null){
            loadingView = UIUtils.inflate(R.layout.layout_loading);
            //添加到帧布局内部
            addView(loadingView, layoutParams);
        }
        //2,加载失败
        if(errorView == null){
            errorView = UIUtils.inflate(R.layout.layout_error);
            View view = errorView.findViewById(R.id.ll_error);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("xxfigo","onClick");
                    onLoad();
                }
            });
            addView(errorView, layoutParams);
        }
        //3,加载为空
        if(emptyView == null){
            emptyView = UIUtils.inflate(R.layout.layout_empty);
            addView(emptyView, layoutParams);
        }
        //根据初始状态去决定哪个界面显示,哪个隐藏
        showSafePage(STATE_LOADING);
    }

    public void showSafePage(int state) {
        currentState = state;
        if(loadingView!=null){
            loadingView.setVisibility((currentState == STATE_UNLOAD
                    || currentState == STATE_LOADING)?View.VISIBLE:View.GONE);
        }

        if(errorView!=null){
            errorView.setVisibility(currentState == STATE_LOAD_ERROR?View.VISIBLE:View.GONE);
        }

        if(emptyView!=null){
            emptyView.setVisibility(currentState == STATE_LOAD_EMPTY?View.VISIBLE:View.GONE);
        }
        //添加获取数据成功的界面到帧布局中
        if(successView == null && currentState == STATE_LOAD_SUCCESS){
            //构建成功的View对象,因为每一个Fragment中成功获取数据放置的界面效果都不一样,也就是不知道如果去构建这个successView
            successView = onCreateSuccessedView();
            addView(successView,layoutParams);
        }

        if(successView!=null){
            if(currentState == STATE_LOAD_SUCCESS){
                successView.setVisibility(View.VISIBLE);
            }else {
                successView.setVisibility(View.GONE);
            }
        }
    }

    //请求网络的抽象方法
    public abstract void onLoad();
    //未知的构建成功界面的过程,方法留给具体的Fragment子类界面去实现
    public abstract View onCreateSuccessedView() ;

}
