package com.zaixiaoqu.umeng.sdk;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.zaixiaoqu.umeng.config.UMRegister;


public class MultiDexApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        (new PushBehaviorEvent()).enablePush(this);
    }

    /**
     * 注册友盟
     *
     * @param UMAppKey         appkey
     * @param UMAppSecret      secret
     * @return
     */
    public UMRegister registerUMAccount(String UMAppKey, String UMAppSecret) {
        return new UMRegister(UMAppKey, UMAppSecret);
    }
}