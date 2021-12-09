package com.netmera.unity.sdk.core;

import static com.netmera.unity.sdk.util.SharedPrefUtil.ON_PUSH_BUTTON_CLICKED_KEY;
import static com.netmera.unity.sdk.util.SharedPrefUtil.ON_PUSH_DISMISS_KEY;
import static com.netmera.unity.sdk.util.SharedPrefUtil.ON_PUSH_OPEN_KEY;
import static com.netmera.unity.sdk.util.SharedPrefUtil.ON_PUSH_RECEIVE_KEY;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.netmera.unity.sdk.util.SharedPrefUtil;

import java.util.ArrayList;

public class NetmeraPluginImpl extends NetmeraPlugin implements NetmeraPluginPushReceiver.Callback {

    public NetmeraPluginImpl(Activity activity, NetmeraPluginUnityBridge unityBridge) {
        mActivity = activity;
        mInstance = this;
        mUnityBridge = unityBridge;
        NetmeraPluginPushReceiver.setCallback(this);
        checkPushMessages(activity.getApplicationContext());
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
    public void onPushReceive(Context context, Bundle bundle, String message) {
        if (mUnityBridge == null) {
            return;
        }
        mUnityBridge.OnPushReceive(message);
    }

    @Override
    public void onPushOpen(Context context, Bundle bundle, String message) {
        if (mUnityBridge == null) {
            return;
        }
        mUnityBridge.OnPushOpen(message);
    }

    @Override
    public void onPushDismiss(Context context, Bundle bundle, String message) {
        if (mUnityBridge == null) {
            return;
        }
        mUnityBridge.OnPushDismiss(message);
    }

    @Override
    public void onPushButtonClicked(Context context, Bundle bundle, String message) {
        if (mUnityBridge == null) {
            return;
        }
        mUnityBridge.OnPushButtonClicked(message);
    }

    private void checkPushMessages(Context context) {
        if (mUnityBridge == null) {
            return;
        }
        ArrayList<String> events = (ArrayList<String>) SharedPrefUtil.getMessages(context);
        for (String event : events) {
            JsonObject object = new Gson().fromJson(event, JsonObject.class);
            String key = object.get(SharedPrefUtil.EVENT_KEY).getAsString();
            String message = object.get(SharedPrefUtil.MESSAGE_KEY).getAsString();
            switch (key) {
                case ON_PUSH_RECEIVE_KEY:
                    mUnityBridge.OnPushReceive(message);
                    break;
                case ON_PUSH_OPEN_KEY:
                    mUnityBridge.OnPushOpen(message);
                    break;
                case ON_PUSH_DISMISS_KEY:
                    mUnityBridge.OnPushDismiss(message);
                    break;
                case ON_PUSH_BUTTON_CLICKED_KEY:
                    mUnityBridge.OnPushButtonClicked(message);
                    break;
            }
        }
        SharedPrefUtil.deleteMessages(context);
    }
}
