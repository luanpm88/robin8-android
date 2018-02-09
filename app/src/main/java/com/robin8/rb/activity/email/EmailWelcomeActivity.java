package com.robin8.rb.activity.email;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.StatisticsAgency;

public class EmailWelcomeActivity extends BaseActivity {

    public static final String EXTRA_EMAIL_NAME = "extra_email_name";

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.EMAIL_LOGIN;
        super.onResume();
    }

    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_email_welcome, mLLContent);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        Button btnNext = (Button) view.findViewById(R.id.bt_next);
        initData(tvName);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //跳转到活动页面
                Intent intent = new Intent(EmailWelcomeActivity.this, MainActivity.class);
                intent.putExtra("register_main", "zhu");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void initData(TextView tvName) {
        Intent intent = getIntent();
        String extra = intent.getStringExtra(EXTRA_EMAIL_NAME);
        if (TextUtils.isEmpty(extra)) {
            tvName.setText("WELCOME");
        } else {
            tvName.setText("WELCOME " + extra);
        }
    }

    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }

}
