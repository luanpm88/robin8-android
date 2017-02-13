package com.robin8.rb.base;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;

/**
 * @author Figo
 */

public class BasePager {
    protected FragmentActivity mActivity;
    public View rootView;// 孩子的具体界面内容，是为了ViewPager中的instantiateItem返回View
    protected LinearLayout mLLContent;// 每个孩子的具体内容的容器
    protected TextView mTitleBarText;
    protected ImageView mRewordFilterIv;
    protected ImageView mRewordLaunchIv;
    protected ImageView firstLeft;
    protected ImageView firstRight;
    protected long lastTime;

    public BasePager() {
    }

    public BasePager(FragmentActivity activity) {
        this.mActivity = activity;
        rootView = initView();
    }

    /**
     * 因为孩子都有相同的布局 ，所以写到父类中 加载基本的布局，带标题和 内容
     *
     * @return
     */
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_base, null);
        mLLContent = (LinearLayout) view.findViewById(R.id.fl_basepager_content);
        mTitleBarText = (TextView) view.findViewById(R.id.titlebar_text);
        mRewordFilterIv = (ImageView) view.findViewById(R.id.reword_filter_iv);
        mRewordLaunchIv = (ImageView) view.findViewById(R.id.reword_launch_iv);
        firstLeft = (ImageView) view.findViewById(R.id.first_left);
        firstRight = (ImageView) view.findViewById(R.id.first_right);
        return view;
    }

    /**
     * 让子类覆盖 初始化数据
     */
    public void initData() {

    }

    /**
     * 让子类覆盖 初始化titlebar
     */
    public void initTitleBar() {

    }

    public boolean isDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if(currentTimeMillis - lastTime <800){
            return true;
        }
        lastTime =currentTimeMillis;
        return false;
    }

}
