package com.netmera.unity.sdk.core;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.netmera.Netmera;
import com.netmera.NetmeraError;
import com.netmera.NetmeraInbox;
import com.netmera.NetmeraInboxFilter;
import com.netmera.NetmeraPushObject;
import com.netmera.NetmeraUser;
import com.netmera.unity.sdk.entity.NetmeraEventImpl;
import com.netmera.unity.sdk.util.Constants;
import com.netmera.unity.sdk.util.Functions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NetmeraPlugin {

    protected static boolean mIsInitialized;
    protected Activity mActivity;
    protected NetmeraPlugin mInstance;
    protected NetmeraPluginUnityBridge mUnityBridge;
    protected NetmeraInbox mCurrentInbox;

    public void sendEvent(String code, String jsonObject) {
        Runnable runnable = () -> {
            try {
                Functions.log("Event json: " + jsonObject, LogLevel.INFO);
                NetmeraEventImpl event;
                if (jsonObject != null) {
                    event = new NetmeraEventImpl(code, new Gson().fromJson(jsonObject, HashMap.class));
                } else {
                    event = new NetmeraEventImpl(code);
                }
                Netmera.sendEvent(event);
                Functions.log("Event sent: " + event.toString(), LogLevel.INFO);
            } catch (Exception e) {
                Functions.log("Exception while sending event", LogLevel.ERROR);
            }
        };
        mActivity.runOnUiThread(runnable);
    }

    public void updateUser(String jsonObject) {
        Functions.log("updateUser json: " + jsonObject, LogLevel.INFO);
        Runnable runnable = () -> {
            try {
                NetmeraUser user = new NetmeraUser();
                Map<String, Object> map = new Gson().fromJson(jsonObject, HashMap.class);
                for (Map.Entry<String, Object> item : map.entrySet()) {
                    Object value = item.getValue();
                    switch (item.getKey()) {
                        case Constants.USER_ID:
                            user.setUserId(value == null ? null : (String) value);
                            break;
                        case Constants.CHILD_COUNT:
                            user.setChildCount(value == null ? null : (int) value);
                            break;
                        case Constants.CITY:
                            user.setCity(value == null ? null : (String) value);
                            break;
                        case Constants.COUNTRY:
                            user.setCountry(value == null ? null : (String) value);
                            break;
                        case Constants.DATE_OF_BIRTH:
                            if (value == null) {
                                user.setDateOfBirth(null);
                                break;
                            }
                            String[] integers = ((String) value).split("-");
                            user.setDateOfBirth(new GregorianCalendar(Integer.parseInt(integers[0]), Integer.parseInt(integers[1]), Integer.parseInt(integers[2])).getTime());
                            break;
                        case Constants.DISTRICT:
                            user.setDistrict(value == null ? null : (String) value);
                            break;
                        case Constants.EMAIL:
                            user.setEmail(value == null ? null : (String) value);
                            break;
                        case Constants.EXTERNAL_SEGMENTS:
                            if (value == null) {
                                user.setExternalSegments(null);
                                break;
                            }
                            String[] externalSegments = ((String) value).split(",");
                            user.setExternalSegments(Arrays.asList(externalSegments));
                            break;
                        case Constants.FAVORITE_TEAM:
                            user.setFavoriteTeam(value == null ? null : (String) value);
                            break;
                        case Constants.GENDER:
                            user.setGender(value == null ? null : (int) value);
                            break;
                        case Constants.INDUSTRY:
                            user.setIndustry(value == null ? null : (String) value);
                            break;
                        case Constants.LANGUAGE:
                            user.setLanguage(value == null ? null : (String) value);
                            break;
                        case Constants.MARITAL_STATUS:
                            user.setMaritalStatus(value == null ? null : (int) value);
                            break;
                        case Constants.MSISDS:
                            user.setMsisdn(value == null ? null : (String) value);
                            break;
                        case Constants.NAME:
                            user.setName(value == null ? null : (String) value);
                            break;
                        case Constants.OCCUPATION:
                            user.setOccupation(value == null ? null : (String) value);
                            break;
                        case Constants.STATE:
                            user.setState(value == null ? null : (String) value);
                            break;
                        case Constants.SURNAME:
                            user.setSurname(value == null ? null : (String) value);
                            break;
                    }
                }
                Netmera.updateUser(user);
                Functions.log("updateUser called successfully: " + user, LogLevel.INFO);
            } catch (Exception e) {
                Functions.log("Exception while setting user properties" + e.toString(), LogLevel.ERROR);
            }
        };
        mActivity.runOnUiThread(runnable);
    }

    public void enablePopupPresentation(boolean isEnabled) {
        Runnable runnable = () -> {
            if (isEnabled) {
                Netmera.enablePopupPresentation();
            } else {
                Netmera.disablePopupPresentation();
            }
            Functions.log("enablePopupPresentation called successfully: " + isEnabled, LogLevel.INFO);
        };
        mActivity.runOnUiThread(runnable);

    }

    public void fetchInbox(int pageSize, int status, String categories, boolean includeExpiredObjects) {
        Runnable runnable = () -> {
            List<String> categoryList = new ArrayList<>();
            if (categories != null && !"".equals(categories)) {
                String[] temp = categories.split(",");
                categoryList.addAll(Arrays.asList(temp));
            }
            NetmeraInboxFilter filter = new NetmeraInboxFilter.Builder()
                    .pageSize(pageSize)
                    .status(status)
                    .categories(categoryList)
                    .includeExpiredObjects(includeExpiredObjects)
                    .build();
            Netmera.fetchInbox(filter, new NetmeraInbox.NetmeraInboxFetchCallback() {
                @Override
                public void onFetchInbox(NetmeraInbox netmeraInbox, NetmeraError netmeraError) {
                    mCurrentInbox = netmeraInbox;
                    JSONObject response = Functions.convertToJSON(netmeraInbox);
                    if (netmeraError != null)
                        mUnityBridge.OnInboxFetchFail(netmeraError.getErrorCode(), netmeraError.getMessage());
                    else if (response == null)
                        mUnityBridge.OnInboxFetchFail(505, "fetch inbox response is null");
                    else
                        mUnityBridge.OnInboxFetchSuccess(response.toString());
                    Functions.log("fetchInbox returned successfully: pageSize: " + pageSize + " status: " + status + " categories: " + categories + " includeExpiredObjects: " + includeExpiredObjects, LogLevel.INFO);
                }
            });
        };
        mActivity.runOnUiThread(runnable);

    }

    public void fetchNextPage() {
        Runnable runnable = () -> {
            if (mCurrentInbox == null) {
                mUnityBridge.OnInboxNextPageFetchFail(Error.SDK_INBOX_INIT.errorCode, Error.SDK_INBOX_INIT.errorMessage);
                Functions.logError(Error.SDK_INBOX_INIT);
                return;
            } else if (!mCurrentInbox.hasNextPage()) {
                mUnityBridge.OnInboxNextPageFetchFail(Error.SDK_INBOX_NO_MORE.errorCode, Error.SDK_INBOX_NO_MORE.errorMessage);
                Functions.logError(Error.SDK_INBOX_NO_MORE);
                return;
            }
            mCurrentInbox.fetchNextPage(new NetmeraInbox.NetmeraInboxFetchCallback() {
                @Override
                public void onFetchInbox(NetmeraInbox netmeraInbox, NetmeraError netmeraError) {
                    mCurrentInbox = netmeraInbox;
                    JSONObject response = Functions.convertToJSON(netmeraInbox);
                    if (netmeraError != null)
                        mUnityBridge.OnInboxNextPageFetchFail(netmeraError.getErrorCode(), netmeraError.getMessage());
                    else if (response == null)
                        mUnityBridge.OnInboxNextPageFetchFail(505, "fetch next page response is null");
                    else
                        mUnityBridge.OnInboxNextPageFetchSuccess(response.toString());
                    Functions.log("fetchNextPage returned successfully.", LogLevel.INFO);

                }
            });
        };
        mActivity.runOnUiThread(runnable);
    }

    public void changeInboxItemStatuses(int startIndex, int endIndex, int status) {
        Runnable runnable = () -> {
            if (mCurrentInbox == null) {
                mUnityBridge.OnInboxStatusChangeFail(Error.SDK_INBOX_INIT.errorCode, Error.SDK_INBOX_INIT.errorMessage);
                Functions.logError(Error.SDK_INBOX_INIT);
                return;
            } else if (startIndex < 0 || startIndex > endIndex || endIndex > mCurrentInbox.pushObjects().size()) {
                mUnityBridge.OnInboxStatusChangeFail(Error.SDK_INBOX_INDEX.errorCode, Error.SDK_INBOX_INDEX.errorMessage);
                Functions.logError(Error.SDK_INBOX_INDEX);
                return;
            }
            List<NetmeraPushObject> objectsToDelete = mCurrentInbox.pushObjects().subList(startIndex, endIndex);
            mCurrentInbox.updateStatus(objectsToDelete, status,
                    new NetmeraInbox.NetmeraInboxStatusCallback() {
                        @Override
                        public void onSetStatusInbox(NetmeraError netmeraError) {
                            if (netmeraError != null)
                                mUnityBridge.OnInboxStatusChangeFail(netmeraError.getErrorCode(), netmeraError.getMessage());
                            else
                                mUnityBridge.OnInboxStatusChangeSuccess();
                            Functions.log("changeInboxItemStatuses returned successfully: startIndex: " + startIndex + " endIndex: " + endIndex + " status: " + status, LogLevel.INFO);
                        }
                    });
        };
        mActivity.runOnUiThread(runnable);
    }

    public void changeAllInboxItemStatuses(int status) {
        Runnable runnable = () -> {
            Netmera.updateAll(status, new NetmeraInbox.NetmeraInboxStatusCallback() {
                @Override
                public void onSetStatusInbox(NetmeraError netmeraError) {
                    if (netmeraError != null)
                        mUnityBridge.OnInboxStatusChangeFail(netmeraError.getErrorCode(), netmeraError.getMessage());
                    else
                        mUnityBridge.OnInboxStatusChangeSuccess();
                    Functions.log("changeAllInboxItemStatuses returned successfully: status: " + status, LogLevel.INFO);
                }
            });
        };
        mActivity.runOnUiThread(runnable);
    }

    public void getStatusCount(int status) {
        Runnable runnable = () -> {
            if (mCurrentInbox == null) {
                Functions.logError(Error.SDK_INBOX_INIT);
                return;
            }
            int statusCount = mCurrentInbox.countForStatus(status);
            mUnityBridge.OnInboxStatusCount(statusCount);
            Functions.log("getStatusCount called successfully: status: " + status, LogLevel.INFO);
        };
        mActivity.runOnUiThread(runnable);
    }


    public void requestPermissionsForLocation() {
        Runnable runnable = () -> {
            Netmera.requestPermissionsForLocation();
            Functions.log("requestPermissionsForLocation called successfully.", LogLevel.INFO);
        };
        mActivity.runOnUiThread(runnable);
    }

    public void destroy() {
        Runnable runnable = () -> {
            mIsInitialized = false;
            mActivity = null;
            mInstance = null;
            NetmeraPluginPushReceiver.clearCallback();
            Functions.log("destroy called successfully.", LogLevel.INFO);
        };
        mActivity.runOnUiThread(runnable);
    }

    public boolean IsInitialized() {
        return mIsInitialized;
    }


    /*void init(string fcmSenderId, string netmeraSdkKey, bool loggingEnabled, bool popupPresentationEnabled, string baseUrl = null);
    void onNewToken(string token);
    void onMessageReceived(object remoteMessage, bool isNetmeraMessage); // TODO first parameter should be custom model.


    void onPushRegister(string gcmSenderId, string pushToken);
    void onPushReceive(object netmeraPushObject);
    void onPushOpen(object netmeraPushObject);
    void onPushDismiss(object netmeraPushObject);
    void onPushButtonClicked(object netmeraPushObject);

    void onWebContentShow(object bundle);
    void onWebContentClose(object bundle);

    void handleWebContent(object webview);
    */

    public enum LogLevel {
        INFO,
        WARNING,
        ERROR
    }

    public enum Error {
        SDK_INIT("SDK init error, please check init parameters", 5000),
        JSON_PARSE("Json parse error", 5001),
        SDK_INBOX_INIT("Inbox next page error! You need to fetch inbox first", 5002),
        SDK_INBOX_INDEX("Check your indexes. Index not valid for status change.", 5003),
        SDK_INBOX_NO_MORE("There is no more page to fetch", 5004);

        private int errorCode;
        private String errorMessage;

        private Error(String message, int code) {
            errorCode = code;
            errorMessage = message;
        }

        public int getCode() {
            return errorCode;
        }

        public String getMessage() {
            return errorMessage;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "errorCode=" + errorCode +
                    ", errorMessage='" + errorMessage + '\'' +
                    '}';
        }
    }

}
