package com.robin8.rb.activity.uesr_msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.util.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FirstKnowUserIdActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tv_jump)
    TextView tvJump;
    @Bind(R.id.layout_next)
    TextView layoutNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_know_user_id);
        ButterKnife.bind(this);
        layoutNext.setOnClickListener(this);
        tvJump.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("register_main", "zhu");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.layout_next:
              //  startActivity(new Intent(this, UserBaseMsgActivity.class));
                IntentUtils intentUtils = new IntentUtils(FirstKnowUserIdActivity.this, ChooseFavoriteActivity.class);
                intentUtils.putExtra("base","base");
                startActivity(intentUtils);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
        }
    }
}
