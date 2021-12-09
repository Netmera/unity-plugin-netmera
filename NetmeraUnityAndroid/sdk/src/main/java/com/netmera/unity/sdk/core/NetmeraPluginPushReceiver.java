package com.netmera.unity.sdk.core;

import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;

import com.netmera.NetmeraPushBroadcastReceiver;
import com.netmera.NetmeraPushObject;
import com.netmera.unity.sdk.util.Functions;
import com.netmera.unity.sdk.util.SharedPrefUtil;

import org.json.JSONObject;

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
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (json == null) {
            return;
        }
        String message = json.toString();
        if (mCallback == null) {
            SharedPrefUtil.addMessage(context, SharedPrefUtil.ON_PUSH_RECEIVE_KEY, message);
        } else {
            mCallback.onPushReceive(context, bundle, message);
        }
    }

    @Override
    protected void onPushOpen(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (json == null) {
            return;
        }
        String message = json.toString();
        if (mCallback == null) {
            SharedPrefUtil.addMessage(context, SharedPrefUtil.ON_PUSH_OPEN_KEY, message);
        } else {
            mCallback.onPushOpen(context, bundle, message);
        }
    }

    @Override
    protected void onPushDismiss(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (json == null) {
            return;
        }
        String message = json.toString();
        if (mCallback == null) {
            SharedPrefUtil.addMessage(context, SharedPrefUtil.ON_PUSH_DISMISS_KEY, message);
        } else {
            mCallback.onPushDismiss(context, bundle, message);
        }
    }

    @Override
    protected void onPushButtonClicked(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (json == null) {
            return;
        }
        String message = json.toString();
        if (mCallback == null) {
            SharedPrefUtil.addMessage(context, SharedPrefUtil.ON_PUSH_BUTTON_CLICKED_KEY, message);
        } else {
            mCallback.onPushButtonClicked(context, bundle, message);
        }
    }

    public interface Callback {
        void onPushRegister(Context context, String gcmSenderId, String pushToken);

        void onPushReceive(Context context, Bundle bundle, String message);

        void onPushOpen(Context context, Bundle bundle, String message);

        void onPushDismiss(Context context, Bundle bundle, String message);

        void onPushButtonClicked(Context context, Bundle bundle, String message);
    }
}
