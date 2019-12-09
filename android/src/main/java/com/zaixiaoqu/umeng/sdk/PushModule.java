package com.zaixiaoqu.umeng.sdk;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.UmengMessageDeviceConfig;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wangfei on 17/8/30
 */

public class PushModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private final int SUCCESS = 200;
    private final int ERROR = 0;
    private final int CANCEL = -1;

    public static final String TAG = PushModule.class.getSimpleName();
    public static final String DidReceiveMessage = "didReceiveMessage";
    public static final String DidOpenMessage = "didOpenMessage";

    /**
     * 设置得到响应的push信息
     */
    private static String LastPushWaitMessage = "";
    private static ReactApplicationContext StaticContext = null;


    private static Handler mSDKHandler = new Handler(Looper.getMainLooper());
    private ReactApplicationContext context;
    private boolean isGameInited = false;
    private static Activity ma;
    private PushAgent mPushAgent;
    private Handler handler;
    private static PushBehaviorEvent mPushApplication;

    public PushModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
        StaticContext = reactContext;
        //添加监听
        context.addLifecycleEventListener(this);
        mPushAgent = PushAgent.getInstance(context);
    }

    public static void initPushSDK(Activity activity) {
        ma = activity;
    }

    @Override
    public String getName() {
        return "UMPushModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DidReceiveMessage, DidReceiveMessage);
        constants.put(DidOpenMessage, DidOpenMessage);
        return constants;
    }

    private static void runOnMainThread(Runnable runnable) {
        mSDKHandler.postDelayed(runnable, 0);
    }

    @ReactMethod
    public void addTag(String tag, final Callback successCallback) {
        mPushAgent.getTagManager().addTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {


                        if (isSuccess) {
                            successCallback.invoke(SUCCESS,result.remain);
                        } else {
                            successCallback.invoke(ERROR,0);
                        }

            }
        }, tag);
    }

    @ReactMethod
    public void deleteTag(String tag, final Callback successCallback) {
        mPushAgent.getTagManager().deleteTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(boolean isSuccess, final ITagManager.Result result) {
                Log.i(TAG, "isSuccess:" + isSuccess);
                if (isSuccess) {
                    successCallback.invoke(SUCCESS,result.remain);
                } else {
                    successCallback.invoke(ERROR,0);
                }
            }
        }, tag);
    }

    @ReactMethod
    public void listTag(final Callback successCallback) {
        mPushAgent.getTagManager().getTags(new TagManager.TagListCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final List<String> result) {
                mSDKHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccess) {
                            if (result != null) {

                                successCallback.invoke(SUCCESS,resultToList(result));
                            } else {
                                successCallback.invoke(ERROR,resultToList(result));
                            }
                        } else {
                            successCallback.invoke(ERROR,resultToList(result));
                        }

                    }
                });

            }
        });
    }

    @ReactMethod
    public void addAlias(String alias, String aliasType, final Callback successCallback) {
        mPushAgent.addAlias(alias, aliasType, new UTrack.ICallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final String message) {
                Log.i(TAG, "isSuccess:" + isSuccess + "," + message);

                        Log.e("xxxxxx","isuccess"+isSuccess);
                        if (isSuccess) {
                            successCallback.invoke(SUCCESS);
                        } else {
                            successCallback.invoke(ERROR);
                        }


            }
        });
    }

    @ReactMethod
    public void addAliasType() {
        Toast.makeText(ma,"function will come soon",Toast.LENGTH_LONG);
    }

    @ReactMethod
    public void addExclusiveAlias(String exclusiveAlias, String aliasType, final Callback successCallback) {
        mPushAgent.setAlias(exclusiveAlias, aliasType, new UTrack.ICallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final String message) {

                        Log.i(TAG, "isSuccess:" + isSuccess + "," + message);
                        if (Boolean.TRUE.equals(isSuccess)) {
                            successCallback.invoke(SUCCESS);
                        }else {
                            successCallback.invoke(ERROR);
                        }



            }
        });
    }

    @ReactMethod
    public void deleteAlias(String alias, String aliasType, final Callback successCallback) {
        mPushAgent.deleteAlias(alias, aliasType, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String s) {
                if (Boolean.TRUE.equals(isSuccess)) {
                    successCallback.invoke(SUCCESS);
                }else {
                    successCallback.invoke(ERROR);
                }
            }
        });
    }

    @ReactMethod
    public void appInfo(final Callback successCallback) {
        String pkgName = context.getPackageName();
        String info = String.format("DeviceToken:%s\n" + "SdkVersion:%s\nAppVersionCode:%s\nAppVersionName:%s",
            mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
            UmengMessageDeviceConfig.getAppVersionCode(context), UmengMessageDeviceConfig.getAppVersionName(context));
        successCallback.invoke("应用包名:" + pkgName + "\n" + info);
    }

    /**
     * 转化list到RN可以接受的数据
     *
     * @param result
     * @return
     */
    private WritableArray resultToList(List<String> result){
        WritableArray list = Arguments.createArray();
        if (result!=null){
            for (String key:result){
                list.pushString(key);
            }
        }
        return list;
    }

    /**
     * 转化map到RN可以接受的数据
     *
     * @param msg
     * @return
     */
    private static WritableMap convertToWriteMap(UMessage msg) {
        WritableMap map = Arguments.createMap();
        //遍历Json
        JSONObject jsonObject = msg.getRaw();
        Iterator<String> keys = jsonObject.keys();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            try {
                map.putString(key, jsonObject.get(key).toString());
            }
            catch (Exception e) {
                Log.e(TAG, "putString fail");
            }
        }
        return map;
    }

    /**
     * 获取设备id
     */
    @ReactMethod
    public void getDeviceToken(Promise promise) {
        String registrationId = mPushAgent.getRegistrationId();
        promise.resolve(registrationId);
    }

    /**
     * 唤醒等待push
     */
    @ReactMethod
    public void awakenWaitPush(Promise promise) {
         if (null == StaticContext) {
             promise.resolve("0");
             return;
         }
         if ("" == LastPushWaitMessage || LastPushWaitMessage.isEmpty()) {
             promise.resolve("0");
             return;
         }
        if(!StaticContext.hasActiveCatalystInstance()) {
            promise.resolve("0");
            return;
        }

        try {
            JSONObject s = new JSONObject(LastPushWaitMessage);
            try {
                StaticContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit(DidOpenMessage, convertToWriteMap(
                                new UMessage(s)
                        ));
            } catch (Exception e) {
            }
            // 运行完成, 就清空
            LastPushWaitMessage = "";
            promise.resolve("1");
        } catch (Exception e) {
            promise.resolve("0");
        }
    }


    /**
     * 暴露发送方法给外部
     *
     * @param eventName
     * @param msg
     */
    public void sendEvent(String eventName, UMessage msg) {
        Log.i("MipushActivity", eventName+"====11==="+msg.getRaw().toString());
        sendEvent(eventName, convertToWriteMap(msg));
    }

    /**
     * 发送事件
     *
     * @param eventName
     * @param params
     */
    private void sendEvent(String eventName, @Nullable WritableMap params) {
        //此处需要添加hasActiveCatalystInstance，否则可能造成崩溃
        //问题解决参考: https://github.com/walmartreact/react-native-orientation-listener/issues/8
        try {
            if(context.hasActiveCatalystInstance()) {
                try {
                    context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit(eventName, params);
                } catch (Exception e) {
                }
            } else {
                Log.i("MipushActivity", eventName+"====错误的hasActiveCatalystInstance");
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onHostResume() {
        if (null == mPushApplication) {
            return;
        }
        try {
            mPushApplication.setmPushModule(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
        if (null == mPushApplication) {
            return;
        }
        try {
            mPushApplication.setmPushModule(null);
        } catch (Exception e) {

        }
    }

    /**
     * 设置得到响应的push信息
     */
    public static void setLastPushWaitMessage(String lastPushWaitMessage) {
        LastPushWaitMessage = lastPushWaitMessage;
    }

    /**
     * 设置PushBehaviorEvent
     */
    public static void setPushBehaviorEvent(PushBehaviorEvent pushBehaviorEvent) {
        mPushApplication = pushBehaviorEvent;
    }


}