package com.zaixiaoqu.umeng.config;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zaixiaoqu.umeng.sdk.RNUMConfigure;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

public class UMRegister {
    /**
     * 是否启用友盟日志
     */
    private Boolean LogEnabledState = false;

    /**
     * 友盟账号
     */
    private String UMAppKey = "";
    private String UMAppSecret = "";

    /**
     * 小米push账号
     */
    private String PushXiaomiID = "";
    private String PushXiaomiKey = "";

    /**
     * 魅族账号
     */
    private String PushMeizuAppID = "";
    private String PushMeizuAppKey = "";

    /**
     * 设置oppo账号
     */
    private String PushOppoAppKey = "";
    private String PushOppoAppSecret = "";

    /**
     * 禁止无参实例化
     */
    private UMRegister() {

    }

    public UMRegister(String UMAppKey, String UMAppSecret) {
        this.setUMAccount(UMAppKey, UMAppSecret);
    }

    /**
     * 注册友盟
     *
     * @param UMAppKey         appkey
     * @param UMAppSecret      secret
     * @return
     */
    private void setUMAccount(String UMAppKey, String UMAppSecret) {
        this.UMAppKey = UMAppKey;
        this.UMAppSecret = UMAppSecret;
    }

    /**
     * 是否启用日志
     *
     * @return
     */
    public UMRegister setLogEnabledState(Boolean LogEnabledState) {
        this.LogEnabledState = LogEnabledState;
        return this;
    }

    /**
     * 设置小米push账号
     *
     * @return
     */
    public UMRegister setPushXiaoMiAccount(String xiaomiID, String xiaomiKey) {
        this.PushXiaomiID = xiaomiID;
        this.PushXiaomiKey = xiaomiKey;
        return this;
    }

    /**
     * 设置魅族push账号
     *
     * @return
     */
    public UMRegister setPushMeizuAccount(String meizuAppID, String meizuAppKey) {
        this.PushMeizuAppID = meizuAppID;
        this.PushMeizuAppKey = meizuAppKey;
        return this;
    }

    /**
     * 设置oppo push账号
     *
     * @return
     */
    public UMRegister setPushOppoAccount(String oppoAppKey, String oppoAppSecret) {
        this.PushOppoAppKey = oppoAppKey;
        this.PushOppoAppSecret = oppoAppSecret;
        return this;
    }

    /**
     * 注册初始化数据
     *
     * @param application
     */
    public void init(Application application) {
        /**
         * 初始化分享的key配置
         *
         * edit by yusheng at 2019-12-05 10:00
         */
        PlatformConfig.setWeixin("wx95db4f8cddc0687f", "ed9aa6a3aaf5a34c5f015afa239a4598");
        PlatformConfig.setQQZone("1106425853", "naAFVNz3SOQjFI8J");

        // 错误的key, 需要重新找到正确的key
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        /**
         * END
         */
        RNUMConfigure.setLogEnabled(this.LogEnabledState);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        RNUMConfigure.init(application, this.UMAppKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, this.UMAppSecret);
        if (!this.PushXiaomiID.equals("") && !this.PushXiaomiKey.equals("")) {
            MiPushRegistar.register(application.getApplicationContext(), this.PushXiaomiID, this.PushXiaomiKey);
        }
        //华为通道
        HuaWeiRegister.register(application);
        if (!this.PushMeizuAppID.equals("") && !this.PushMeizuAppKey.equals("")) {
            //魅族通道
            MeizuRegister.register(application.getApplicationContext(), this.PushMeizuAppID, this.PushMeizuAppKey);
        }
        if (!this.PushOppoAppKey.equals("") && !this.PushOppoAppSecret.equals("")) {
            //OPPO通道，参数1为app key，参数2为app secret
            OppoRegister.register(application, this.PushOppoAppKey, this.PushOppoAppSecret);
        }
        //vivo 通道
        VivoRegister.register(application);
    }
}
