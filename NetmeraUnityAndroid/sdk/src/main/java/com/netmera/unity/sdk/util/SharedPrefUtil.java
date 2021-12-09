package com.netmera.unity.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SharedPrefUtil {

    public static final String SHARED_PREF_NAME = "NM_UN_SHARED_PREF";
    public static final String SHARED_PREF_KEY = "NM_UN_SHARED_KEY";

    public static final String EVENT_KEY = "EVENT_KEY";
    public static final String MESSAGE_KEY = "MESSAGE_KEY";

    public static final String ON_PUSH_RECEIVE_KEY = "ON_PUSH_RECEIVE";
    public static final String ON_PUSH_OPEN_KEY = "ON_PUSH_OPEN";
    public static final String ON_PUSH_DISMISS_KEY = "ON_PUSH_DISMISS";
    public static final String ON_PUSH_BUTTON_CLICKED_KEY = "ON_PUSH_BUTTON_CLICKED";


    public static String jsonToString(String key, String message) {
        JsonObject object = new JsonObject();
        object.addProperty(EVENT_KEY, key);
        object.addProperty(MESSAGE_KEY, message);
        return new Gson().toJson(object);
    }

    public static void addMessage(Context context, String key, String message) {
        List<String> currentMessages = getMessages(context);
        currentMessages.add(jsonToString(key, message));
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SHARED_PREF_KEY, new Gson().toJson(currentMessages)).apply();
    }

    public static List<String> getMessages(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(SHARED_PREF_KEY, "");
        List<String> messages = new Gson().fromJson(json, new TypeToken<List<String>>() {}.getType());
        if (messages == null) {
            return new ArrayList<>();
        } else {
            return messages;
        }
    }

    public static void deleteMessages(Context context) {
        List<String> emptyMessage = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SHARED_PREF_KEY, new Gson().toJson(emptyMessage)).apply();
    }
}
