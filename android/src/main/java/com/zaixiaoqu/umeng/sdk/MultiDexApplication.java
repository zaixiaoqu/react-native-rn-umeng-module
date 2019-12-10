package com.zaixiaoqu.umeng.sdk;

import com.zaixiaoqu.umeng.config.UMRegister;


public class MultiDexApplication extends androidx.multidex.MultiDexApplication {
    /**
     * 注册友盟
     *
     * @param UMAppKey         appkey
     * @param UMAppSecret      secret
     * @return
     */
    protected UMRegister registerUMAccount(String UMAppKey, String UMAppSecret) {
        return new UMRegister(UMAppKey, UMAppSecret);
    }
}