package com.robin8.rb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.robin8.rb.ui.activity.SplashActivity;

public class GeTuiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast
        //  throw new UnsupportedOperationException("Not yet implemented");
        Bundle bundle = new Bundle();
        bundle.putInt("unread", 2);
        context.startActivity(new Intent(context, SplashActivity.class).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
