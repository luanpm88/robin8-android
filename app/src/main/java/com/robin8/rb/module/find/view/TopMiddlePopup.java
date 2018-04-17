package com.robin8.rb.module.find.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.robin8.rb.R;
import com.robin8.rb.adapter.MainFindTypeAdapter;
import com.robin8.rb.module.social.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

/**
 Created by zc on 2018/4/10. */

public class TopMiddlePopup extends PopupWindow {

    private Context myContext;
    private HorizontalListView myLv;
    private OnItemClickListener myOnItemClickListener;
    private List<List<String>> mDataList;
    private int myWidth;
    private int myHeight;

    // 判断是否需要添加或更新列表子类项
    private boolean myIsDirty = true;

    private LayoutInflater inflater = null;
    private View myMenuView;

    private LinearLayout popupLL;

    private MainFindTypeAdapter adapter;

    public TopMiddlePopup(Context context) {
        // TODO Auto-generated constructor stub
    }

    public TopMiddlePopup(Context context, int width, int height, MainFindTypeAdapter adapter, List<List<String>> mDataList) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myMenuView = inflater.inflate(R.layout.dialog_find_top_type, null);

        this.myContext = context;
        this.adapter = adapter;
        this.mDataList = mDataList;
        this.myWidth = width;
        this.myHeight = height;

        initWidget();
        setPopup();
    }

    /**
     初始化控件
     */
    private void initWidget() {
        popupLL = ((LinearLayout) myMenuView.findViewById(R.id.ll_title_type));
        myLv = ((HorizontalListView) myMenuView.findViewById(R.id.list_type));
        mDataList = new ArrayList<>();

    }

    /**
     设置popup的样式
     */
    private void setPopup() {
        // 设置AccessoryPopup的view
        this.setContentView(myMenuView);
        // 设置AccessoryPopup弹出窗体的宽度
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置AccessoryPopup弹出窗体的高度
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置AccessoryPopup弹出窗体可点击
        this.setFocusable(true);
        // 设置AccessoryPopup弹出窗体的动画效果
        this.setAnimationStyle(R.style.AnimTop);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x33000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        myMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = popupLL.getBottom();
                int left = popupLL.getLeft();
                int right = popupLL.getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height || x < left || x > right) {
                        dismiss();
                        if (click!=null){
                            click.onItemClick(true);
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (click!=null){
            click.onItemClick(true);
        }
    }

    public static interface OnItemViewListener {
        void onItemClick(boolean is);
    }
    /**
     显示弹窗界面
     @param view
     */
    public void show(View view) {
        if (myIsDirty) {
            myIsDirty = false;
            myLv.setAdapter(adapter);
        }
        showAsDropDown(view, 0, 0);
    }

    private OnItemViewListener click;
    public void setOnRecyclerViewListener(OnItemViewListener onItemViewListener) {
        this.click = onItemViewListener;
    }
}

