package com.zaixiaoqu.umeng;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.zaixiaoqu.umeng.sdk.AnalyticsModule;
import com.zaixiaoqu.umeng.sdk.PushModule;
import com.zaixiaoqu.umeng.sdk.ShareModule;

public class RnUmengModulePackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new RnUmengModuleModule(reactContext),

                new ShareModule(reactContext),
                new PushModule(reactContext),
                new AnalyticsModule(reactContext)
        );
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
