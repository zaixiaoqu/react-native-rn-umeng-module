package com.zaixiaoqu.umeng.sdk;

import android.content.Context;
import androidx.multidex.MultiDex;

import com.zaixiaoqu.umeng.config.UMRegister;


public class MultiDexApplication extends androidx.multidex.MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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