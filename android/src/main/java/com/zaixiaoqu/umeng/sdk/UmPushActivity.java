package com.zaixiaoqu.umeng.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.umeng.message.UmengNotifyClickActivity;
import org.android.agoo.common.AgooConstants;

public class UmPushActivity extends UmengNotifyClickActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        try {
            String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            PushModule.setLastPushWaitMessage(body);
        } catch (Exception e) {
        }
        this.onRun(intent, this);

    }

    protected void onRun(Intent intent, Context context) {
    }
}
