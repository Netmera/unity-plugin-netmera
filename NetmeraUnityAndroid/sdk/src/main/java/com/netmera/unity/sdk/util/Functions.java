package com.netmera.unity.sdk.util;

import android.util.Log;

import com.netmera.NetmeraInbox;
import com.netmera.NetmeraPushObject;
import com.netmera.unity.sdk.core.NetmeraCustomApp;
import com.netmera.unity.sdk.core.NetmeraPlugin;

import org.json.JSONArray;
import org.json.JSONObject;

public class Functions {
    public static final String LOGTAG = "NetmeraUnity";

    private Functions() {
    }


    public static JSONObject convertToJSON(NetmeraPushObject push) {
        try {
            JSONObject responsePush = new JSONObject();
            if (push.getCustomJson() != null) {
                responsePush.put("customJson", push.getCustomJson().toString());
            }
            if (push.getDeepLink() != null) {
                responsePush.put("deeplinkUrl", push.getDeepLink().toString());
            }
            responsePush.put("title", push.getPushStyle().getContentTitle());
            responsePush.put("subtitle", push.getPushStyle().getSubText());
            responsePush.put("body", push.getPushStyle().getContentText());
            responsePush.put("pushId", push.getPushId());
            responsePush.put("pushInstanceId", push.getPushInstanceId());
            responsePush.put("pushType", push.getPushType());
            responsePush.put("inboxStatus", push.getInboxStatus());
            responsePush.put("sendDate", push.getSendDate());
            return responsePush;
        } catch (Exception e) {
            Functions.logError(NetmeraPlugin.Error.JSON_PARSE, e.getMessage());
            return null;
        }
    }

    public static JSONObject convertToJSON(NetmeraInbox inbox) {
        try {
            JSONObject responsePush = new JSONObject();
            if (inbox == null) {
                return null;
            }
            JSONArray pushObjects = new JSONArray();
            for (NetmeraPushObject pushObject : inbox.pushObjects()) {
                JSONObject pushJSON = convertToJSON(pushObject);
                if (pushJSON != null) {
                    pushObjects.put(pushJSON);
                }
            }
            responsePush.put("inbox", pushObjects);
            responsePush.put("hasNextPage", inbox.hasNextPage());
            return responsePush;
        } catch (Exception e) {
            Functions.logError(NetmeraPlugin.Error.JSON_PARSE, e.getMessage());
            return null;
        }
    }


    public static void logError(NetmeraPlugin.Error error, String extraMessage) {
        log(error.toString() + " : " + extraMessage, NetmeraPlugin.LogLevel.ERROR);
    }

    public static void logError(NetmeraPlugin.Error error) {
        logError(error, "");
    }


    public static void log(String message, NetmeraPlugin.LogLevel logLevel) {
        if(NetmeraCustomApp.getInstance() == null || NetmeraCustomApp.getBoolMetadata(NetmeraCustomApp.getInstance(), "netmera_logging_disabled")) {
            return;
        }
        message = "NETMERA Android log: " + message;
        switch (logLevel) {
            case INFO:
                Log.i(LOGTAG, message);
                break;
            case ERROR:
                Log.e(LOGTAG, message);
                break;
            case WARNING:
                Log.w(LOGTAG, message);
                break;
        }
    }
}
