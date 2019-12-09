package com.zaixiaoqu.umeng.sdk;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


/**
 * Created by Magic on 2018/6/13.
 */
public class PushBehaviorEvent {
    protected static final String TAG = PushModule.class.getSimpleName();
    protected Application application;
    protected PushModule mPushModule;
    protected String mRegistrationId;
    protected PushAgent mPushAgent;
    //应用退出时，打开推送通知时临时保存的消息
    private UMessage tmpMessage;
    //应用退出时，打开推送通知时临时保存的事件
    private String tmpEvent;

    public void setmPushModule(PushModule module) {
        mPushModule = module;
        if (tmpMessage != null && tmpEvent != null && mPushModule != null) {
            //execute the task
            clikHandlerSendEvent(tmpEvent, tmpMessage);
            //发送事件之后，清空临时内容
            tmpEvent = null;
            tmpMessage = null;
        }
    }

    //开启推送
    public void enablePush(Application application) {
        this.application = application;
        // 把当前的对象注入到PushModule
        PushModule.setPushBehaviorEvent(this);

        Log.i(TAG, "启用推送服务开始");
        mPushAgent = PushAgent.getInstance(application);
        mPushAgent.setResourcePackageName("com.szlanlingtong");
        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);

        //注册推送服务 每次调用register都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                mRegistrationId = deviceToken;
                Log.i(TAG, "umeng push device token: " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i(TAG, "umeng push register failed: " + s + " " + s1);
            }
        });
        //前台显示通知
        mPushAgent.setNotificaitonOnForeground(true);

        //统计应用启动数据
        mPushAgent.onAppStart();

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                // super.launchApp(context, msg);
                clikHandlerSendEvent(PushModule.DidOpenMessage, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                clikHandlerSendEvent(PushModule.DidOpenMessage, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
                clikHandlerSendEvent(PushModule.DidOpenMessage, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                super.dealWithCustomAction(context, msg);
                clikHandlerSendEvent(PushModule.DidOpenMessage, msg);
            }
        };

        //设置通知点击处理者
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //设置消息和通知的处理
        mPushAgent.setMessageHandler(new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                messageHandlerSendEvent(PushModule.DidReceiveMessage, msg);
                Log.i(TAG, msg.toString());
                Log.i(TAG, "推送消息监听");
                return super.getNotification(context, msg);
            }

            @Override
            public void dealWithCustomMessage(Context context, UMessage msg) {
                super.dealWithCustomMessage(context, msg);
                messageHandlerSendEvent(PushModule.DidReceiveMessage, msg);
            }
        });
    }

    /**
     * 点击推送通知触发的事件
     * @param event
     * @param msg
     */
    private void clikHandlerSendEvent(final String event, final UMessage msg) {
        if(mPushModule == null) {
            tmpEvent = event;
            tmpMessage = msg;
            return;
        }
        //延时500毫秒发送推送，否则可能收不到
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mPushModule.sendEvent(event, msg);
            }
        }, 500);
    }

    /**
     * 消息处理触发的事件
     * @param event
     * @param msg
     */
    private void messageHandlerSendEvent(String event, UMessage msg) {
        if(mPushModule == null) {
            return;
        }
        mPushModule.sendEvent(event, msg);
    }
}