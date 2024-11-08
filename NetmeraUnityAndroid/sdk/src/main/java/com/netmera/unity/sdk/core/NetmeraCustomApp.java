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
        String firebaseSenderId = getMetaData(context, "netmera_firebase_senderid");
        String huaweiSenderId = getMetaData(context, "netmera_huawei_senderid");
        String netmeraSdkKey = getMetaData(context, "netmera_mobile_sdkkey");
        String baseUrl = getMetaData(context, "netmera_optional_baseurl");
        if (firebaseSenderId == null || firebaseSenderId.isEmpty() || netmeraSdkKey == null) {
            Log.d(Functions.LOGTAG, "ERROR: netmera_firebase_senderid or netmera_mobile_sdkkey can not be empty.\nCheck your AndroidManifest.xml file.");
            return;
        }
        boolean mPopupDisabled = Boolean.parseBoolean(getMetaData(context, "netmera_popup_presentation_disabled"));
        boolean mIsLoggingEnabled = !Boolean.parseBoolean(getMetaData(context, "netmera_logging_disabled"));

        NetmeraConfiguration.Builder netmeraConfiguration = new NetmeraConfiguration.Builder()
                .firebaseSenderId(firebaseSenderId)
                .apiKey(netmeraSdkKey)
                .nmPushActionCallbacks(new NetmeraPluginPushReceiver())
                .logging(mIsLoggingEnabled);

        if (huaweiSenderId != null && !huaweiSenderId.isEmpty()) {
            netmeraConfiguration.huaweiSenderId(huaweiSenderId);
        }

        NetmeraPlugin.mIsInitialized = true;
        Netmera.init(netmeraConfiguration.build(context));
        if (baseUrl != null) {
            Netmera.setBaseUrl(baseUrl);
        }

        if (mPopupDisabled) {
            Netmera.disablePopupPresentation();
        } else {
            Netmera.enablePopupPresentation();
        }

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        Functions.log("init called: fcmSenderId: " + firebaseSenderId + " netmeraSdkKey: " + netmeraSdkKey + " popupPresentationDisabled: " + mPopupDisabled + " baseUrl: " + baseUrl, NetmeraPlugin.LogLevel.INFO);
    }

    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                // Get the value, trim it, and return null if it's empty
                Object value = appInfo.metaData.get(name);
                if (value != null) {
                    String trimmed = value.toString().trim();
                    return trimmed.isEmpty() ? null : trimmed;
                } else {
                    Log.i("Netmera", "Meta data not found for " + name);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Log or handle exception if necessary
            Log.e("Netmera", "Application info not found", e);
        }
        return null;
    }

}
