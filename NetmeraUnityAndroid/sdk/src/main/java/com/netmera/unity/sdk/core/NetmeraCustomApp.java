package com.netmera.unity.sdk.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.netmera.Netmera;
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
        float firebaseSenderID = getFloatMetadata(context, "netmera_firebase_senderid");
        String netmeraSdkKey = getStringMetadata(context, "netmera_mobile_sdkkey");
        String baseUrl = getStringMetadata(context, "netmera_optional_baseurl");
        if (firebaseSenderID <= 0 || netmeraSdkKey == null) {
            Log.d(Functions.LOGTAG, "ERROR: netmera_firebase_senderid or netmera_mobile_sdkkey can not be empty.\nCheck your AndroidManifest.xml file.");
            return;
        }
        boolean mPopupDisabled = getBoolMetadata(context, "netmera_popup_presentation_disabled");
        NetmeraPlugin.mIsInitialized = true;
        Netmera.logging(!getBoolMetadata(context, "netmera_logging_disabled"));
        Netmera.init(context, String.valueOf(firebaseSenderID), netmeraSdkKey);
        if (baseUrl != null) {
            Netmera.setBaseUrl(baseUrl);
        }
        if (!mPopupDisabled) {
            Netmera.enablePopupPresentation();
        } else {
            Netmera.disablePopupPresentation();
        }
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(true);
        Functions.log("init called: fcmSenderId: " + firebaseSenderID + " netmeraSdkKey: " + netmeraSdkKey + " popupPresentationEnabled: " + mPopupDisabled + " baseUrl: " + baseUrl, NetmeraPlugin.LogLevel.INFO);
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
