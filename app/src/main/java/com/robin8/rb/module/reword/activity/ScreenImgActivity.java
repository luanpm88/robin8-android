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
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.module.mine.activity.NewUserTaskActivity;
import com.robin8.rb.module.reword.chose_photo.GridAdapter;
import com.robin8.rb.module.reword.chose_photo.PhotoPickerActivity;
import com.robin8.rb.module.reword.chose_photo.SelectModel;
import com.robin8.rb.module.reword.chose_photo.SerializableMap;
import com.robin8.rb.module.reword.chose_photo.intent.PhotoPickerIntent;
import com.robin8.rb.module.reword.chose_photo.intent.PhotoPreviewIntent;
import com.robin8.rb.module.reword.helper.DetailContentHelper;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
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
    private WProgressDialog mWProgressDialog;
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
        screenList = getIntent().getStringArrayListExtra(EXTRA_SCREEN_LISTS);//审核中的截图/截图示例
        if (nameList == null || nameList.size() == 0) {
            for (int i = 0; i < screenList.size(); i++) {
                nameList.add("截图" + i);
            }
        }
        if (screenType.equals("0") || screenType.equals("1")) {
            //截图参考||//查看已上传截图
                for (int i = 0; i < screenList.size(); i++) {
                    imgMap.put(i, screenList.get(i));
                }
        } else if (screenType.equals("2")) {
            //上传截图
            LogUtil.LogShitou("这是2", "2");
        }else if (screenType.equals("3")){
            //新手查看截图示例
            for (int i = 0; i < screenList.size(); i++) {
                imgMap.put(i, screenList.get(i));
            }
        }else if (screenType.equals("4")){
            //新手上传图片

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
                chose_position = position;
                if (isHaveImg(imgMap, position)) {
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
                if (screenType.equals("2") || screenType.equals("1")) {
                    if (imgMap.size() != nameList.size()) {
                        CustomToast.showShort(ScreenImgActivity.this, "请上传全部图片！");
                    } else {
                        SerializableMap map = new SerializableMap();
                        map.setMap(imgMap);
                        Intent intent = new Intent(ScreenImgActivity.this, DetailContentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(EXTRA_SCREEN_MAP, map);
                        intent.putExtras(bundle);
                        setResult(DetailContentHelper.IMAGE_REQUEST_MORE_IMG_CODE, intent);
                        finish();
                    }
                } else if (screenType.equals("3")){
                    setResult(NewUserTaskActivity.NEW_USER_LOOK_IMG);
                    finish();
                }else if (screenType.equals("4")){
                    if (imgMap.size() != nameList.size()) {
                        CustomToast.showShort(ScreenImgActivity.this, "请上传图片！");
                    } else {
                        upTasks();
                    }
                }else {
                    finish();
                }
                break;
            case R.id.iv_back:
                if (screenType.equals("3")){
                    setResult(NewUserTaskActivity.NEW_USER_LOOK_IMG);
                }
                finish();
                break;
        }
    }

    private void upTasks() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(ScreenImgActivity.this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.NEW_TASKS_UP_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("新手任务提交" + HelpTools.getUrl(CommonConfig.NEW_TASKS_UP_URL), response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(ScreenImgActivity.this, getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    HelpTools.insertCommonXml(HelpTools.ISNEWUSER,"no");
                    setResult(NewUserTaskActivity.NEW_USER_UP_IMG);
                    finish();
                } else {
                    if (! TextUtils.isEmpty(bean.getDetail())) {
                        CustomToast.showShort(ScreenImgActivity.this, bean.getDetail());
                    }
                }
            }
        });
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
        if (screenType.equals("3")){
            setResult(NewUserTaskActivity.NEW_USER_LOOK_IMG);
        }
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
}
