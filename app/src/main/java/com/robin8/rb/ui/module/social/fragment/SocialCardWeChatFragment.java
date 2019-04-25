package com.robin8.rb.ui.module.social.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.ui.module.social.view.CustomViewPager;

/**
 */
@SuppressLint("ValidFragment")
public class SocialCardWeChatFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM = "TYPE";
    private String[] name = {"微博", "微信"};
    private String type;
    private RelativeLayout layoutShowOne;
    private RelativeLayout layoutShowTwo;
    private RelativeLayout layoutShowThree;
    private ImageView imgShowOne;
    private ImageView imgShowTwo;
    private ImageView imgShowThree;
    private TextView tvCanSee;
    private LinearLayout llOne;
    private LinearLayout llTwo;
    private LinearLayout llThree;
    private LinearLayout llShow;
    private LinearLayout llHide;
    private LinearLayout llWechatHide;
    private TextView tvUserName;
    private TextView tvUserDec;
    private static CustomViewPager vp;

    @SuppressLint("ValidFragment")
    public SocialCardWeChatFragment(CustomViewPager vp) {
        this.vp = vp;
    }


//    public static SocialCardWeChatFragment newInstance(int param1) {
//
//        SocialCardWeChatFragment fragment = new SocialCardWeChatFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            int anInt = getArguments().getInt(ARG_PARAM);
//            type = name[anInt];
//        }
//    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_social_card, null);

//        tvUserName = ((TextView) view.findViewById(R.id.tv_user_name));
//        tvUserDec = ((TextView) view.findViewById(R.id.tv_user_dec));
//        imgShowOne = (ImageView) view.findViewById(R.id.img_show_one);
//        imgShowTwo = (ImageView) view.findViewById(R.id.img_show_two);
//        imgShowThree = (ImageView) view.findViewById(R.id.img_show_three);
//        layoutShowOne = ((RelativeLayout) view.findViewById(R.id.show_layout_one));
//        layoutShowTwo = ((RelativeLayout) view.findViewById(R.id.show_layout_two));
//        layoutShowThree = ((RelativeLayout) view.findViewById(R.id.show_layout_three));
//        llOne = ((LinearLayout) view.findViewById(R.id.ll_item_one));
//        llTwo = ((LinearLayout) view.findViewById(R.id.ll_item_two));
//        llThree = ((LinearLayout) view.findViewById(R.id.ll_item_three));
//        llThree = ((LinearLayout) view.findViewById(R.id.ll_item_three));
//        llShow = ((LinearLayout) view.findViewById(R.id.ll_can_see));
//        llHide = ((LinearLayout) view.findViewById(R.id.ll_no_see));//设置权限不能查看的界面
//        llWechatHide = ((LinearLayout) view.findViewById(R.id.ll_wechat_no_see));//微信敬请期待
//        tvCanSee = ((TextView) view.findViewById(R.id.tv_can_see));//按钮对他人可见
//
//        tvCanSee.setOnClickListener(this);
//
//        llOne.setOnClickListener(this);
//        llTwo.setOnClickListener(this);
//        llThree.setOnClickListener(this);




        //        Drawable drawable = mActivity.getResources().getDrawable(item.icon);
        //        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        //        holder.mTvName.setCompoundDrawables(drawable,null,null,null);
        llWechatHide.setVisibility(View.VISIBLE);
        tvCanSee.setVisibility(View.GONE);
        tvUserName.setText("我是Robin8");
        tvUserDec.setText("您的个人数据管理平台");
//        if (type.equals(mActivity.getString(R.string.weibo))) {
//            llWechatHide.setVisibility(View.GONE);
//            llShow.setVisibility(View.VISIBLE);
//            llHide.setVisibility(View.GONE);
//            tvCanSee.setVisibility(View.VISIBLE);
           // vp.setObjectForPosition(view,1);
//        } else {
//            llWechatHide.setVisibility(View.VISIBLE);
//            tvCanSee.setVisibility(View.GONE);
//            tvUserName.setText("我是Robin8");
//            tvUserDec.setText("您的个人数据管理平台");
//            //vp.setObjectForPosition(view,1);
//        }
//vp.resetHeight(0);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.ll_item_one:
//                upOrDown(imgShowOne, layoutShowOne);
//                break;
//            case R.id.ll_item_two:
//                upOrDown(imgShowTwo, layoutShowTwo);
//                break;
//            case R.id.ll_item_three:
//                upOrDown(imgShowThree, layoutShowThree);
//                break;
            case R.id.tv_can_see:
                if (llShow.getVisibility() == View.VISIBLE) {
                    llShow.setVisibility(View.GONE);
                    llHide.setVisibility(View.VISIBLE);
                    tvCanSee.setText("对他人不可见");
                } else {
                    llShow.setVisibility(View.VISIBLE);
                    llHide.setVisibility(View.GONE);
                    tvCanSee.setText("对他人可见");
                }
                break;
        }
    }

    private void upOrDown(ImageView img, RelativeLayout lyOne) {
        if (lyOne.getVisibility() == View.VISIBLE) {
            lyOne.setVisibility(View.GONE);
            lyOne.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_out));
            img.setImageResource(R.mipmap.icon_data_down);
        } else {
            lyOne.setVisibility(View.VISIBLE);
            lyOne.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_in));
            img.setImageResource(R.mipmap.icon_up);
        }
    }


    @Override
    public void setData(ViewPagerAdapter.SelectItem data, String url, String pageName) {

    }
}
