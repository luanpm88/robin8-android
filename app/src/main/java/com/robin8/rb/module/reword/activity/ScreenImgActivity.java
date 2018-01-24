package com.robin8.rb.module.reword.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.module.reword.chose_photo.GridAdapter;
import com.robin8.rb.module.reword.chose_photo.PhotoPickerActivity;
import com.robin8.rb.module.reword.chose_photo.SelectModel;
import com.robin8.rb.module.reword.chose_photo.SerializableMap;
import com.robin8.rb.module.reword.chose_photo.intent.PhotoPickerIntent;
import com.robin8.rb.module.reword.chose_photo.intent.PhotoPreviewIntent;
import com.robin8.rb.module.reword.helper.DetailContentHelper;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 多图上传
 Created by zc on 2018/12/27. */
public class ScreenImgActivity extends BaseActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_NAME_LIST = "extra_name_list";
    public static final String EXTRA_SCREEN_LISTS = "extra_screen_list";
    public static final String EXTRA_SCREEN_MAP = "extra_screen_map";
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    public GridView imgGrid;
    //private List<ScreenImgBean> mList;
    private ArrayList<String> nameList;
    private ArrayList<String> screenList;
    private GridAdapter gridAdapter;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private int chose_position = - 1;
    private Map<Integer, String> imgMap;
    public String screenType;
    // private ArrayList<String> lookList;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.screenshot_reference);
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setOnClickListener(this);
        mBottomTv.setText(R.string.done);
    }

    /**
     不同的来源设置不同的数据
     */
    private void initData() {
        //title
        String extra = getIntent().getStringExtra(EXTRA_TITLE);
        if (! TextUtils.isEmpty(extra)) {
            mTVCenter.setText(extra);
        }
        screenType = getIntent().getStringExtra(EXTRA_TYPE);
        nameList = getIntent().getStringArrayListExtra(EXTRA_NAME_LIST);

        if (screenType.equals("0") ||screenType.equals("1")){
            //截图参考||//查看已上传截图
            LogUtil.LogShitou("---------","哈哈哈");
            screenList = getIntent().getStringArrayListExtra(EXTRA_SCREEN_LISTS);//审核中的截图/截图示例
            for (int i = 0; i < screenList.size(); i++) {
                imgMap.put(i, screenList.get(i));
            }
        }else if (screenType.equals("2")){
            //上传截图
            LogUtil.LogShitou("---------","哈哈哈");

        }
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_screen_img, mLLContent, true);
        imgGrid = ((GridView) view.findViewById(R.id.img_grid));
        // lookList = new ArrayList<>();
        nameList = new ArrayList<>();
        imgMap = new HashMap<>();
        initData();
        gridAdapter = new GridAdapter(this, nameList, getItemImageWidth(), imgMap, Integer.valueOf(screenType));
        imgGrid.setNumColumns(getNumColnums());
        imgGrid.setAdapter(gridAdapter);
        imgGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  String imgs = (String) parent.getItemAtPosition(position);
                chose_position = position;
                if (isHaveImg(imgMap, position)) {
                    //                    if (lookList != null) {
                    //                        lookList.clear();
                    //                    }
                    //                    for (int i = 0; i < imgMap.size(); i++) {
                    //                        lookList.add(imgMap.get(i));
                    //                    }
                    if (imgMap.size() != nameList.size()) {
                        if (imgMap.get(position) != null) {
                            PhotoPreviewIntent intent = new PhotoPreviewIntent(ScreenImgActivity.this);
                            intent.setCurrentItem(position);
                            intent.setPhotoPath(imgMap.get(position));
                            startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                        }
                    } else {
                        PhotoPreviewIntent intent = new PhotoPreviewIntent(ScreenImgActivity.this);
                        intent.setCurrentItem(position);
                        intent.setPhotoTitle(nameList);
                        intent.setPhotoMapPaths(imgMap);
                        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                    }
                    //缺张预览
                    //                    PhotoPreviewIntent intent = new PhotoPreviewIntent(ScreenImgActivity.this);
                    //                    intent.setCurrentItem(position);
                    //                    intent.setPhotoMapPaths(imgMap);
                    //                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                } else {
                    PhotoPickerIntent intent = new PhotoPickerIntent(ScreenImgActivity.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(false); // 是否显示拍照
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    intent.setImgPosition(position);// 已选中的照片的位置
                    intent.setMaxTotal(1);
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                }
            }
        });
        //        gridAdapter.setOnClearListener(new GridAdapter.OnClearListener() {
        //
        //            @Override
        //            public void onClick(int position) {
        //                imgMap.remove(position);
        //                gridAdapter.setNewData(imgMap);
        //            }
        //        });
    }

    /**
     检测当前位置是否有图
     @param position
     @return
     */
    public static boolean isHaveImg(Map<Integer, String> map, int position) {
        if (map != null) {
            Iterator<Integer> it = map.keySet().iterator();
            while (it.hasNext()) {
                Integer next = it.next();
                if (position == next) {
                    // Log.e("map中对应的value", "===>" + imgMap.get(position));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                //完成后返回
                SerializableMap map = new SerializableMap();
                map.setMap(imgMap);
                Intent intent = new Intent(ScreenImgActivity.this, DetailContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_SCREEN_MAP, map);
                intent.putExtras(bundle);
                setResult(DetailContentHelper.IMAGE_REQUEST_MORE_IMG_CODE, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    loadAdpater(list);
                    break;
                // 预览
                //                case REQUEST_PREVIEW_CODE:
                //                    // ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                //                    // loadAdpater(ListExtra);
                //                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        imagePaths.addAll(paths);
        imgMap.put(chose_position, imagePaths.get(0));
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    /**
     获取GridView Item宽度
     @return
     */
    private int getItemImageWidth() {
        int cols = getNumColnums();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    /**
     根据屏幕宽度与密度计算GridView显示的列数， 最少为三列
     @return
     */
    private int getNumColnums() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        return cols < 3 ? 3 : cols;
    }
    //    public class GridAdapter extends BaseAdapter {
    //        private List<ScreenImgBean> mList;
    //        private ArrayList<String> list;
    //        private Context context;
    //        private int mItemSize;
    //        private LayoutInflater mInflater;
    //
    //        private GridView.LayoutParams mItemLayoutParams;
    //
    //        public GridAdapter(List<ScreenImgBean> mList, int itemSize) {
    //            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //            this.mList = mList;
    //            this.mItemSize = itemSize;
    //            mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);
    //
    //        }
    //
    //        @Override
    //        public int getCount() {
    //            return mList.size();
    //        }
    //
    //        @Override
    //        public Object getItem(int position) {
    //            return mList.get(position);
    //        }
    //
    //        @Override
    //        public long getItemId(int position) {
    //            return position;
    //        }
    //
    //        @Override
    //        public View getView(final int position, View convertView, final ViewGroup parent) {
    //            if (convertView == null) {
    //                convertView = mInflater.inflate(R.layout.screen_img_item, parent, false);
    //                //convertView = LayoutInflater.from(ScreenImgActivity.this).inflate(R.layout.screen_img_item, parent, false);
    //                convertView.setTag(new ViewHolder(convertView));
    //            }
    //            final ViewHolder holder = (ViewHolder) convertView.getTag();
    //            holder.tvContent.setText(mList.get(position).getText());//当前上传图片描述
    //            holder.llBottom.setVisibility(View.GONE);
    //            holder.imgClear.setVisibility(View.GONE);//清除所选图片
    //            if (imagePaths != null) {
    //                int size = imagePaths.size();
    //                if (size != 0) {
    //                    if (chose_position != - 1) {
    //                        if (position == chose_position) {
    //                            Glide.with(ScreenImgActivity.this).load(imagePaths.get(0)).
    //                                    placeholder(R.mipmap.default_error).error(R.mipmap.default_error).centerCrop().crossFade().into(holder.img);
    //                            imgMap.put(position, imagePaths.get(0));
    //                        }
    //                    }
    //
    //                }
    //                if (isHaveImg(position)) {
    //                    holder.llBottom.setVisibility(View.VISIBLE);
    //                    holder.imgClear.setVisibility(View.VISIBLE);
    //                    holder.tvName.setText("哇哇哇哇");
    //                }
    //            }
    //            //删除所选择的图片
    //            holder.imgClear.setOnClickListener(new View.OnClickListener() {
    //
    //                @Override
    //                public void onClick(View view) {
    //                    LogUtil.LogShitou("这是第几个", "==>" + position + "在map中这是第几" + imgMap.get(position));
    //                    imgMap.remove(position);
    //                    LogUtil.LogShitou("移除后", "==>" + position + "在map中这是第几" + imgMap.size());
    //                    notifyDataSetChanged();
    //                }
    //            });
    //            return convertView;
    //        }
    //
    //        /**
    //         重置每个Column的Size
    //         @param columnWidth
    //         */
    //        public void setItemSize(int columnWidth) {
    //
    //            if (mItemSize == columnWidth) {
    //                return;
    //            }
    //
    //            mItemSize = columnWidth;
    //
    //            mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);
    //
    //            notifyDataSetChanged();
    //        }
    //
    //        class ViewHolder {
    //
    //            public final ImageView img;
    //            public final TextView tvContent;
    //            public final SquareLayout llScreen;
    //            public final ImageView imgClear;
    //            public final RelativeLayout llBottom;
    //            public final TextView tvName;
    //
    //            private ViewHolder(View v) {
    //                img = ((ImageView) v.findViewById(R.id.img_chose));
    //                tvContent = ((TextView) v.findViewById(R.id.tv_content));
    //                llScreen = ((SquareLayout) v.findViewById(R.id.ll_screen));
    //                imgClear = ((ImageView) v.findViewById(R.id.img_clear));
    //                llBottom = ((RelativeLayout) v.findViewById(R.id.ll_bottom));
    //                tvName = ((TextView) v.findViewById(R.id.tv_name));
    //            }
    //        }
    //    }
}
