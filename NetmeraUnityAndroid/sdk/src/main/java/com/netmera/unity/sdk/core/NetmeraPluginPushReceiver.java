package com.netmera.unity.sdk.core;

import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;

import com.netmera.NetmeraPushBroadcastReceiver;
import com.netmera.NetmeraPushObject;

public class NetmeraPluginPushReceiver extends NetmeraPushBroadcastReceiver {

    private static Callback mCallback;

    public static void setCallback(Callback callback) {
        mCallback = callback;
    }

    public static void clearCallback() {
        mCallback = null;
    }

    @Override
    protected void onPushRegister(Context context, String gcmSenderId, String pushToken) {
        if (mCallback == null)
            return;
        mCallback.onPushRegister(context, gcmSenderId, pushToken);
    }

    @Override
    protected void onPushReceive(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        if (mCallback == null)
            return;
        mCallback.onPushReceive(context, bundle, netmeraPushObject);
    }

    @Override
    protected void onPushOpen(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        if (mCallback == null)
            return;
        mCallback.onPushOpen(context, bundle, netmeraPushObject);
    }

    @Override
    protected void onPushDismiss(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        //if you want to know when a push is dismissed
        //NetmeraPlugin.sendPushNotification(netmeraPushObject);
        if (mCallback == null)
            return;
        mCallback.onPushDismiss(context, bundle, netmeraPushObject);
    }

    @Override
    protected void onPushButtonClicked(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        //if you want to know when a interactive push button is clicked
        if (mCallback == null)
            return;
        mCallback.onPushButtonClicked(context, bundle, netmeraPushObject);
    }

    public interface Callback {
        void onPushRegister(Context context, String gcmSenderId, String pushToken);

        void onPushReceive(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject);

        void onPushOpen(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject);

        void onPushDismiss(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject);

        void onPushButtonClicked(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject);
    }
}
