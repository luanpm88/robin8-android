package com.robin8.rb.activity.uesr_msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.robin8.rb.R;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.UserCircleBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.FavoriteButton;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.ui.widget.taglayout.FlowLayout;
import com.robin8.rb.ui.widget.taglayout.TagAdapter;
import com.robin8.rb.ui.widget.taglayout.TagFlowLayout;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.view.SerializableMapS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择圈子
 */
public class ChooseFavoriteActivity extends AppCompatActivity {
    @Bind(R.id.tv_jump)
    TextView tvJump;
    @Bind(R.id.flow_layout)
    TagFlowLayout mFlowLayout;
    @Bind(R.id.text_next)
    TextView tvNext;
    private String[] mVals = new String[]{"二次元", "车友圈", "旅游圈", "吃货圈", "房产圈", "电影爱好者", "游戏圈", "宝妈圈", "网红圈", "女神圈", "企业家", "宠物圈", "校园圈", "精英俱乐部", "时尚圈", "数码发烧友", "理财圈", "白领圈", "健身圈", "投资圈", "网购达人", "体育圈", "读书爱好者", "单身圈"};
    private TagAdapter<UserCircleBean.CirclesListBean> mAdapter;
    public static final String BASE_INFO = "From_base_info";
    public static final String CHOOSE_CIRCLE = "choose_circle";
    public static final String CHOOSE_LIST = "choose_circle_list";
    private WProgressDialog mWProgressDialog;
    private List<UserCircleBean.CirclesListBean> circlesList;
    private ArrayList<Integer> circlesIdList;
    private ArrayList<String> circlesNameList;
    private String[] circlesLists;
    private String selectFinal;
    private Set<Integer> selectFinalSet;
    private int TYPE = 0; //1:注册，2：选择
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_favorite);
        ButterKnife.bind(this);
        circlesIdList = new ArrayList<>();
        circlesNameList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            TYPE = 1;
            tvJump.setVisibility(View.INVISIBLE);
            final SerializableMapS mapPaths = (SerializableMapS) bundle.get(BASE_INFO);
            if (mapPaths != null) {
                //取值
                map = mapPaths.getMap();
            }
            tvNext.setText("下一步");
        }
        Intent intent = getIntent();
        String extra = intent.getStringExtra(CHOOSE_CIRCLE);
        String base = intent.getStringExtra("base");
        if (! TextUtils.isEmpty(base)) {
            TYPE = 1;
            tvNext.setText("下一步");
            tvJump.setVisibility(View.INVISIBLE);
        }
        if (! TextUtils.isEmpty(extra)) {
            tvJump.setVisibility(View.INVISIBLE);
            if (extra.equals(CreatorMsgActivity.CREATOR)) {
                TYPE = 2;//内容选择者
            } else if (extra.equals(PublicUserMsgActivity.PUBLIC)) {
                TYPE = 3;//普通用户
            } else if (extra.equals(WeiBoMsgActivity.WEIBOMSG)) {
                TYPE = 4;//微博
            } else if (extra.equals(WechatMsgActivity.WECHAT)) {
                TYPE = 5;//微信
            }
            if (intent.getIntegerArrayListExtra(CHOOSE_LIST) != null) {
                circlesIdList = intent.getIntegerArrayListExtra(CHOOSE_LIST);
                for (int i = 0; i < circlesIdList.size(); i++) {
                    LogUtil.LogShitou("传过来的圈子id", "==>" + circlesIdList.get(i));
                }
            }
            tvNext.setText("完成");
        }
        initData();
    }

    private void initView() {
        mFlowLayout.setAdapter(mAdapter = new TagAdapter<UserCircleBean.CirclesListBean>(circlesList) {

            @Override
            public View getView(FlowLayout parent, int position, UserCircleBean.CirclesListBean t) {
                FavoriteButton textView = (FavoriteButton) LayoutInflater.from(ChooseFavoriteActivity.this).inflate(R.layout.item_choose_favorite_tv, null);
                textView.setText(t.getLabel());
                if (circlesIdList != null && circlesIdList.size() != 0) {
                    for (int i = 0; i < circlesIdList.size(); i++) {
                        if (position == (circlesIdList.get(i) - 1)) {
                            if (TextUtils.isEmpty(circlesList.get(position).getColor())) {
                                textView.setStrokeColor(R.color.green_custom);
                                textView.setNormalColor(R.color.green_custom);
                            } else {
                                textView.setStrokeStrColor(circlesList.get(position).getColor());
                                textView.setNormalStrColor(circlesList.get(position).getColor());
                            }
                            textView.setTextColor(getResources().getColor(R.color.white_custom));
                        }
                    }
                }

                return textView;
            }

        });

        if (circlesIdList != null && circlesIdList.size() != 0) {
            Set<Integer> integers = new HashSet<Integer>();
            for (int i = 0; i < circlesIdList.size(); i++) {
                integers.add((circlesIdList.get(i) - 1));
            }
            mAdapter.setSelectedList(integers);
        }
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                FavoriteButton textView = (FavoriteButton) view.findViewById(R.id.f_btn);

                if (textView.isActivated() == false) {
                    if (circlesList.get(position).getColor() == null) {
                        textView.setStrokeColor(R.color.gray_first);
                        textView.setNormalColor(R.color.white_custom);
                    }
                    textView.setStrokeColor(R.color.gray_first);
                    textView.setNormalColor(R.color.white_custom);
                    textView.setTextColor(getResources().getColor(R.color.black_000000));
                } else {
                    if (TextUtils.isEmpty(circlesList.get(position).getColor())) {
                        textView.setStrokeColor(R.color.green_custom);
                        textView.setNormalColor(R.color.green_custom);
                    } else {
                        textView.setStrokeStrColor(circlesList.get(position).getColor());
                        textView.setNormalStrColor(circlesList.get(position).getColor());
                    }
                    textView.setTextColor(getResources().getColor(R.color.white_custom));
                }
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {

            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (circlesIdList!=null){
                    if (circlesIdList.size()!=0){
                        circlesIdList.clear();
                    }
                }
                selectFinal = selectPosSet.toString();
                selectFinalSet = selectPosSet;
            }
        });

    }

    private ArrayList<Integer> selecId(Set<Integer> s) {
        ArrayList<Integer> list = new ArrayList<>();
        if (circlesIdList != null || circlesIdList.size() != 0) {
            circlesIdList.clear();
        }
        Iterator<Integer> iterator = s.iterator();
        if (s != null && s.size() != 0) {
            while (iterator.hasNext()) {
                Integer next = iterator.next();
                circlesIdList.add(next);
            }
        }
        for (int j = 0; j < circlesIdList.size(); j++) {
            LogUtil.LogShitou("最后选择的ID", "===>" + circlesIdList.get(j));
            list.add(circlesList.get(circlesIdList.get(j)).getId());
        }

        return list;
    }

    @OnClick({R.id.tv_jump, R.id.text_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                finish();
                break;
            case R.id.text_next:
                if ((TextUtils.isEmpty(selectFinal) || selectFinal.equals("[]")) && circlesIdList.size() == 0) {
                    CustomToast.showShort(ChooseFavoriteActivity.this, "请选择你感兴趣的圈子");
                    return;
                }
                if (TYPE == 1) {
                    saveData();
                } else if (TYPE == 2) {
                    Intent intent = new Intent(this, CreatorMsgActivity.class);
                    if (! TextUtils.isEmpty(selectFinal) && ! selectFinal.equals("[]")) {
                        intent.putIntegerArrayListExtra(CHOOSE_CIRCLE, selecId(selectFinalSet));
                        setResult(RESULT_OK, intent);
                    } else {
                        setResult(RESULT_CANCELED, intent);
                    }
                    finish();
                } else if (TYPE == 3) {
                    Intent intent = new Intent(this, PublicUserMsgActivity.class);
                    if (! TextUtils.isEmpty(selectFinal) && ! selectFinal.equals("[]")) {
                        intent.putIntegerArrayListExtra(CHOOSE_CIRCLE, selecId(selectFinalSet));
                        setResult(RESULT_OK, intent);
                    } else {
                        setResult(RESULT_CANCELED, intent);
                    }
                    finish();
                } else if (TYPE == 4) {
                    Intent intent = new Intent(this, WeiBoMsgActivity.class);
                    if (! TextUtils.isEmpty(selectFinal) && ! selectFinal.equals("[]")) {
                        intent.putIntegerArrayListExtra(CHOOSE_CIRCLE, selecId(selectFinalSet));
                        setResult(RESULT_OK, intent);
                    } else {
                        setResult(RESULT_CANCELED, intent);
                    }
                    finish();
                } else if (TYPE == 5) {
                    Intent intent = new Intent(this, WechatMsgActivity.class);
                    if (! TextUtils.isEmpty(selectFinal) && ! selectFinal.equals("[]")) {
                        intent.putIntegerArrayListExtra(CHOOSE_CIRCLE, selecId(selectFinalSet));
                        setResult(RESULT_OK, intent);
                    } else {
                        setResult(RESULT_CANCELED, intent);
                    }
                    finish();
                }
                break;
        }
    }

    private void saveData() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("gender", map.get("gender"));
        // mRequestParams.put("age", map.get("age"));
        mRequestParams.put("kol_role", map.get("kol_role"));
        if (! TextUtils.isEmpty(selectFinal)) {
            if (circlesIdList.size() == 0 && selectFinal.equals("[]")) {
                CustomToast.showShort(ChooseFavoriteActivity.this, "请选择你感兴趣的圈子");
                return;
            } else {
                LogUtil.LogShitou("选择的数据", "==>" + selecId(selectFinalSet).size());
                String join = Joiner.on(",").join(selecId(selectFinalSet));
                mRequestParams.put("circle_ids", join);
            }
        }
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFO_URL), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                LogUtil.LogShitou("选择基本信息", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        Intent intent = new Intent(ChooseFavoriteActivity.this, MainActivity.class);
                        intent.putExtra("register_main", "zhu");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }


    /**
     初始化数据
     */
    private void initData() {
        String data = CacheUtils.getString(ChooseFavoriteActivity.this, HelpTools.BASEINFO, null);
        if (TextUtils.isEmpty(data)) {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(this);
            }
            mWProgressDialog.show();
            final BasePresenter mBasePresenter = new BasePresenter();
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFOS_URL), null, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                    UserCircleBean bean = GsonTools.jsonToBean(response, UserCircleBean.class);
                    if (bean != null) {
                        if (bean.getError() == 0) {
                            CacheUtils.putString(ChooseFavoriteActivity.this, HelpTools.BASEINFO, response);
                            circlesList = bean.getCircles_list();
                            circlesLists = new String[circlesList.size()];
                            for (int i = 0; i < circlesList.size(); i++) {
                                circlesLists[i] = circlesList.get(i).getLabel();
                            }
                            if (circlesList.size() != 0) {
                                initView();
                            }
                        }
                    }

                }
            });
        } else {
            UserCircleBean bean = GsonTools.jsonToBean(data, UserCircleBean.class);
            if (bean != null) {
                if (bean.getError() == 0) {
                    circlesList = bean.getCircles_list();
                    circlesLists = new String[circlesList.size()];
                    for (int i = 0; i < circlesList.size(); i++) {
                        circlesLists[i] = circlesList.get(i).getLabel();
                    }
                    if (circlesList.size() != 0) {
                        initView();
                    }
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
