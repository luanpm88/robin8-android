package com.robin8.rb.activity.uesr_msg.choose_city;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 城市选择 */
public class CityChooseActivity extends BaseActivity {

    private List<CityBean> cityList;
    private ArrayList<String> cityNameList;
    private ArrayList<String> cityHotList;
    private RecyclerView mRecycleView;
    private String[] city = new String[]{"title1", "北京", "上海", "深圳", "广州", "杭州", "成都", "南京", "武汉", "西安", "厦门", "长沙", "天津", "苏州", "title2", "鞍山", "蚌埠", "保定", "北京", "长春", "成都", "重庆", "长沙", "常熟", "朝阳", "常州", "东莞", "大连", "东营", "德州", "佛山", "福州", "title3", "桂林", "贵阳", "广州", "哈尔滨", "合肥", "呼和浩特", "海口", "杭州", "惠州", "湖州", "金华", "江门", "济南", "济宁", "嘉兴", "江阴", "title4", "昆明", "昆山", "聊城", "廊坊", "丽水", "洛阳", "临沂", "龙岩", "连云港", "兰州", "柳州", "绵羊", "宁波", "南昌", "南京", "南宁", "南通", "title5", "青岛", "秦皇岛", "泉州", "日照", "title6", "上海", "石家庄", "汕头", "绍兴", "沈阳", "三亚", "深圳", "苏州", "天津", "唐山", "台州", "太原", "title7", "潍坊", "武汉", "芜湖", "威海", "乌鲁木齐", "无锡", "温州", "西安", "香港", "厦门", "西宁", "邢台", "徐州", "银川", "盐城", "烟台", "扬州", "珠海", "张家港", "肇庆", "中山", "郑州"};
    private CityBean cityBean;
    private CityBean cityHotBean;
    private List<Integer> chooseList;
    private Map<Integer, Integer> map;
    private int count = 0;
    public static final String CITY_CHOOSE = "city_choose";
    private String[] hotCity = new String[]{"title1", "北京", "上海", "深圳", "广州", "杭州", "成都", "南京", "武汉", "西安", "厦门", "长沙", "天津", "苏州"};

    @Override
    public void setTitleView() {
        mTVCenter.setText("更多城市");
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_city_choose, mLLContent, true);
        mRecycleView = ((RecyclerView) view.findViewById(R.id.recycler_view));
        cityList = new ArrayList<>();
        cityHotList = new ArrayList<>();
        chooseList = new ArrayList<>();
        map = new HashMap<>();
        Intent intent = getIntent();
        for (int i = 0; i < city.length; i++) {
            cityBean = new CityBean();
            cityBean.setStr(city[i]);
            cityList.add(cityBean);
        }

        for (int i = 0; i < hotCity.length; i++) {
            cityHotList.add(hotCity[i]);
        }

        if (intent != null) {
            cityNameList = intent.getStringArrayListExtra(CITY_CHOOSE);
            if (cityNameList!=null){
                if (cityNameList.size() != 0) {
                    for (int i = 0; i < cityList.size(); i++) {
                        for (int j = 0; j < cityNameList.size(); j++) {
                            if (cityList.get(i).getStr().equals(cityNameList.get(j))) {
                                cityList.get(i).setSelect(true);
                            }
                        }
                    }
                }
            }else {
                cityNameList = new ArrayList<>();
            }
        }
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                int type = mRecycleView.getAdapter().getItemViewType(position);//获得返回值
                if (type == 0) {
                    return mLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecycleView.setLayoutManager(mLayoutManager);
        final MyGridAdapter linkLabelAdapter = new MyGridAdapter(this, cityList);
        mRecycleView.setAdapter(linkLabelAdapter);

        //===============
        linkLabelAdapter.setOnRecyclerViewListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View itemView, int position) {
                LogUtil.LogShitou("当前点击的view" + position, cityList.get(position).getStr());
                        if (StringUtil.removeDuplicate(cityNameList).size() >= 3) {
                            if (cityList.get(position).isSelect()) {
                                //                        if (cityList.get(position).getStr().equals("北京")) {
                                //                            for (int i = 0; i < cityList.size(); i++) {
                                //                                if (cityList.get(i).getStr().equals("北京")) {
                                //                                    cityList.get(i).setSelect(false);
                                //                                }
                                //                            }
                                //                        } else {
                                //                            cityList.get(position).setSelect(false);
                                //                        }
                                for (int i = 0; i <cityHotList.size(); i++) {
                                    if (cityList.get(position).getStr().equals(cityHotList.get(i))){
                                        String s = cityHotList.get(i);
                                        for (int j = 0; j < cityList.size(); j++) {
                                            if (cityList.get(j).getStr().equals(s)) {
                                                cityList.get(j).setSelect(false);
                                            }
                                        }
                                    }else {
                                        cityList.get(position).setSelect(false);
                                    }

                                }

                                //集合处理
                                for (int i = 0; i < cityNameList.size(); i++) {
                                    if (cityNameList.get(i).equals(cityList.get(position).getStr())) {
                                        cityNameList.remove(i);
                                    }
                                }
                            } else {
                                CustomToast.showShort(CityChooseActivity.this, "最多只能选择3个标签");
                                return;
                            }
                        } else {
                            if (cityList.get(position).isSelect()) {
                                for (int i = 0; i <cityHotList.size(); i++) {
                                    if (cityList.get(position).getStr().equals(cityHotList.get(i))){
                                        String s = cityHotList.get(i);
                                        for (int j = 0; j < cityList.size(); j++) {
                                            if (cityList.get(j).getStr().equals(s)) {
                                                cityList.get(j).setSelect(false);
                                            }
                                        }
                                    }else {
                                        cityList.get(position).setSelect(false);
                                    }
                                }

                                //                        if (cityList.get(position).getStr().equals("北京")) {
                                //                            for (int i = 0; i < cityList.size(); i++) {
                                //                                if (cityList.get(i).getStr().equals("北京")) {
                                //                                    cityList.get(i).setSelect(false);
                                //                                }
                                //                            }
                                //                        } else {
                                //                            cityList.get(position).setSelect(false);
                                //                        }

                                //集合处理
                                for (int i = 0; i < cityNameList.size(); i++) {
                                    if (cityNameList.get(i).equals(cityList.get(position).getStr())) {
                                        cityNameList.remove(i);
                                    }
                                }
                            } else {
                                for (int i = 0; i <cityHotList.size(); i++) {
                                    if (cityList.get(position).getStr().equals(cityHotList.get(i))){
                                        String s = cityHotList.get(i);
                                        for (int j = 0; j < cityList.size(); j++) {
                                            if (cityList.get(j).getStr().equals(s)) {
                                                cityList.get(j).setSelect(true);
                                            }
                                        }
                                    }else {
                                        cityList.get(position).setSelect(true);
                                    }
                                }

                                //                            if (cityList.get(position).getStr().equals("北京")) {
                                //                            for (int i = 0; i < cityList.size(); i++) {
                                //                                if (cityList.get(i).getStr().equals("北京")) {
                                //                                    cityList.get(i).setSelect(true);
                                //                                }
                                //                            }
                                //                        } else {
                                //                            cityList.get(position).setSelect(true);
                                //                        }

                                //集合处理
                                cityNameList.add(cityList.get(position).getStr());
                            }
                        }

                //=============
                //                if (cityList.get(position).isSelect()) {
                //                    cityList.get(position).setSelect(false);
                //                } else {
                //                    cityList.get(position).setSelect(true);
                //                }
                //==========
                //                if (cityList.get(position).isSelect() == false) {
                //                    cityList.get(position).setSelect(true);
                //                    map.put(position, 0);
                //                } else {
                //                    cityList.get(position).setSelect(false);
                //                    if (map.size() != 0) {
                //                        if (map.get(position) == 0) {
                //                            map.put(position, 1);
                //                        }
                //                    }
                //                }
                linkLabelAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                //                boolean zhishao = false;
                //                for (int i = 0; i < cityList.size(); i++) {
                //                    if (cityList.get(i).isSelect()) {
                //                        zhishao = true;
                //                    }
                //                }
                //                if (! zhishao) {
                //                    CustomToast.showShort(this, "至少选择一个标签");
                //                    return;
                //                }
                // =====================
                //                Set<Integer> set = map.keySet();
                //                Iterator<Integer> iterator = set.iterator();
                //                while (iterator.hasNext()) {
                //                    Integer next = iterator.next();
                //                    if (map.get(next) == 0) {
                //                        chooseList.add(next);
                //                    }
                //                }
                //
                //                if (chooseList.size() != 0) {
                //                    for (int i = 0; i < chooseList.size(); i++) {
                //                        LogUtil.LogShitou("最后选择", "===>" + cityList.get(chooseList.get(i)).getStr());
                //                    }
                //                }
                //==============
                LogUtil.LogShitou("最后选择了几个", "===>" + chooseList.size());

                if (cityNameList.size() == 0) {
                    CustomToast.showShort(this, "请选择一个城市");

                } else {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(CITY_CHOOSE, cityNameList);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                break;
        }
    }


    class MyGridAdapter extends RecyclerView.Adapter {
        private Context context;
        private List<CityBean> list;
        private int selectItem = - 1;
        private OnItemClickListener onItemViewListener;
        private final int TITLE = 0;
        private final int CONTENT = 1;


        public void setOnRecyclerViewListener(OnItemClickListener onItemViewListener) {
            this.onItemViewListener = onItemViewListener;
        }

        public MyGridAdapter(Context context, List<CityBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        @Override
        public int getItemViewType(int position) {
            if (list.get(position).getStr().contains("title")) {
                return TITLE;
            } else {
                return CONTENT;
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            if (viewType == TITLE) {
                holder = new MyTitleHolder(LayoutInflater.from(context).inflate(R.layout.city_title_item, null));
            } else {
                holder = new MyItemHolder(LayoutInflater.from(context).inflate(R.layout.city_name_item, null));
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TITLE) {
                ((MyTitleHolder) holder).setData(position);
            } else {
                ((MyItemHolder) holder).setData(position);
            }
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyItemHolder extends RecyclerView.ViewHolder {
            private final TextView tvCityName;
            private final LinearLayout llContent;

            public MyItemHolder(View itemView) {
                super(itemView);
                tvCityName = ((TextView) itemView.findViewById(R.id.tv_city_name));
                llContent = (LinearLayout) itemView.findViewById(R.id.llContent);
                //                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //                layoutParams.topMargin=15;
                //                layoutParams.bottomMargin=15;
                //                llContent.setLayoutParams(layoutParams);
            }

            public void setData(final int position) {
                tvCityName.setText(list.get(position).getStr());
                if (list.get(position).isSelect() == true) {
                    tvCityName.setTextColor(getResources().getColor(R.color.white_custom));
                    tvCityName.setBackground(getDrawable(R.drawable.shape_blue_rectangular));
                } else {
                    tvCityName.setTextColor(getResources().getColor(R.color.gray_first));
                    tvCityName.setBackground(getDrawable(R.drawable.shape_gray_rectangular_pane));
                }
                tvCityName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onItemViewListener.onItemClick(v, getAdapterPosition());
                    }
                });
            }
        }

        public class MyTitleHolder extends RecyclerView.ViewHolder {
            private final TextView tvTitle;

            public MyTitleHolder(View itemView) {
                super(itemView);
                tvTitle = ((TextView) itemView.findViewById(R.id.tv_city_title));
            }

            @SuppressLint("WrongConstant")
            public void setData(int position) {
                switch (list.get(position).getStr()) {
                    case "title1":
                        tvTitle.setText("热门城市");
                        break;
                    case "title2":
                        tvTitle.setText("A-F");
                        break;
                    case "title3":
                        tvTitle.setText("GHIJ");
                        break;
                    case "title4":
                        tvTitle.setText("KLNNM");
                        break;
                    case "title5":
                        tvTitle.setText("OPQR");
                        break;
                    case "title6":
                        tvTitle.setText("STUV");
                        break;
                    case "title7":
                        tvTitle.setText("WXYZ");
                        break;

                }
            }
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    /**
     px转换成dp
     */
    private int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
