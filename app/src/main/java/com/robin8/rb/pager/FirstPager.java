package com.robin8.rb.pager;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.robin8.rb.R;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.module.mine.activity.BeKolFirstActivity;

/**
 * @author Figo
 */
public class FirstPager extends BasePager implements View.OnClickListener{

    public FirstPager(FragmentActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public View initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
        View view = layoutInflater.inflate(R.layout.pager_home,mLLContent,true);
        Button beKOLBtn = (Button) view.findViewById(R.id.btn_be_kol);
        beKOLBtn.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_be_kol:
                startBeKolFirstActivity();
                break;

        }
    }

    private void startBeKolFirstActivity() {
        if (mActivity != null) {
            Intent intent = new Intent(mActivity, BeKolFirstActivity.class);
            mActivity.startActivity(intent);
        }
    }
 }
