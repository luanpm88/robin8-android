package com.robin8.rb.base;

import android.view.KeyEvent;

import com.robin8.rb.R;
import com.robin8.rb.util.ActivityManagerUtils;
import com.robin8.rb.util.CustomToast;

public abstract class BaseBackHomeActivity extends BaseDataActivity {

    protected long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            CustomToast.showShort(getApplicationContext(), getString(R.string.click_again_go_back));
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityManagerUtils.getInstance().exit();
         //  finish();
        }
    }
}
