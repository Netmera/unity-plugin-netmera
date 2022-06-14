package com.netmera.unity.sdk.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.netmera.Netmera;
import com.netmera.NetmeraConfiguration;
import com.netmera.unity.sdk.util.Functions;

public class NetmeraCustomApp extends Application {
    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        initNetmera(this);
    }

    public static Application getInstance() {
        return mInstance;
    }

    public static void initNetmera(Application app) {
        mInstance = app;
        Context context = app.getApplicationContext();
        float firebaseSenderId = getFloatMetadata(context, "netmera_firebase_senderid");
        float huaweiSenderId = getFloatMetadata(context, "netmera_huawei_senderid");
        String netmeraSdkKey = getStringMetadata(context, "netmera_mobile_sdkkey");
        String baseUrl = getStringMetadata(context, "netmera_optional_baseurl");
        if (firebaseSenderId <= 0 || netmeraSdkKey == null) {
            Log.d(Functions.LOGTAG, "ERROR: netmera_firebase_senderid or netmera_mobile_sdkkey can not be empty.\nCheck your AndroidManifest.xml file.");
            return;
        }
        boolean mPopupDisabled = getBoolMetadata(context, "netmera_popup_presentation_disabled");

        NetmeraConfiguration.Builder netmeraConfiguration = new NetmeraConfiguration.Builder()
                .firebaseSenderId(String.valueOf(firebaseSenderId))
                .apiKey(netmeraSdkKey)
                .logging(!getBoolMetadata(context, "netmera_logging_disabled"));

        if (huaweiSenderId > 0) {
            netmeraConfiguration.huaweiSenderId(String.valueOf(firebaseSenderId));
        }

        NetmeraPlugin.mIsInitialized = true;
        Netmera.init(netmeraConfiguration.build(context));
        if (baseUrl != null) {
            Netmera.setBaseUrl(baseUrl);
        }
        if (!mPopupDisabled) {
            Netmera.enablePopupPresentation();
        } else {
            Netmera.disablePopupPresentation();
        }
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        Functions.log("init called: fcmSenderId: " + firebaseSenderId + " netmeraSdkKey: " + netmeraSdkKey + " popupPresentationEnabled: " + mPopupDisabled + " baseUrl: " + baseUrl, NetmeraPlugin.LogLevel.INFO);
    }

    public static String getStringMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                String trimmed = appInfo.metaData.getString(name) != null ? appInfo.metaData.getString(name).trim() : null;
                return trimmed != null && trimmed.length() > 0 ? trimmed : null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        return null;
    }

    public static float getFloatMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getFloat(name,0) ;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
        return 0;
    }

    public static boolean getBoolMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getBoolean(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return false;
    }
}
