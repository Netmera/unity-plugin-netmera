package com.netmera.unity.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.netmera.NetmeraPushObject;
import com.netmera.unity.sdk.util.Functions;

import org.json.JSONObject;

import java.util.List;

public class NetmeraPluginImpl extends NetmeraPlugin implements NetmeraPluginPushReceiver.Callback {

    public NetmeraPluginImpl(Activity activity, NetmeraPluginUnityBridge unityBridge) {
        mActivity = activity;
        mInstance = this;
        mUnityBridge = unityBridge;
        NetmeraPluginPushReceiver.setCallback(this);
    }

    @Override
    public void sendEvent(String code, String jsonObject) {
        super.sendEvent(code, jsonObject);
    }

    @Override
    public void enablePopupPresentation(boolean isEnabled) {
        super.enablePopupPresentation(isEnabled);
    }

    public void requestPermissionsForLocation(boolean dummy) {
        super.requestPermissionsForLocation();
    }

    @Override
    public void updateUser(String jsonObject) {
        super.updateUser(jsonObject);
    }

    public void destroy(boolean dummy) {
        super.destroy();
    }

    @Override
    public void fetchInbox(int pageSize, int status, String categories, boolean includeExpiredObjects) {
        super.fetchInbox(pageSize, status, categories, includeExpiredObjects);
    }

    public void fetchNextPage(boolean dummy) {
        super.fetchNextPage();
    }

    @Override
    public void changeInboxItemStatuses(int startIndex, int endIndex, int status) {
        super.changeInboxItemStatuses(startIndex, endIndex, status);

    }

    @Override
    public void getStatusCount(int status) {
        super.getStatusCount(status);

    }

    @Override
    public void changeAllInboxItemStatuses(int status) {
        super.changeAllInboxItemStatuses(status);
    }

    // Push notification callbacks
    @Override
    public void onPushRegister(Context context, String gcmSenderId, String pushToken) {
        mUnityBridge.OnPushRegister(gcmSenderId, pushToken);
    }

    @Override
    public void onPushReceive(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (mUnityBridge == null || json == null) {
            return;
        }
        mUnityBridge.OnPushReceive(json.toString());
    }

    @Override
    public void onPushOpen(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (mUnityBridge == null || json == null) {
            return;
        }
        mUnityBridge.OnPushOpen(json.toString());
    }

    @Override
    public void onPushDismiss(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (mUnityBridge == null || json == null) {
            return;
        }
        mUnityBridge.OnPushDismiss(json.toString());
    }

    @Override
    public void onPushButtonClicked(Context context, Bundle bundle, NetmeraPushObject netmeraPushObject) {
        JSONObject json = Functions.convertToJSON(netmeraPushObject);
        if (mUnityBridge == null || json == null) {
            return;
        }
        mUnityBridge.OnPushButtonClicked(json.toString());
    }
}
