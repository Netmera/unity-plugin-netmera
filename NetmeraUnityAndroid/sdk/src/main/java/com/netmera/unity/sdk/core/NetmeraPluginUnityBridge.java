package com.netmera.unity.sdk.core;

public interface NetmeraPluginUnityBridge {

    // push
    void OnPushRegister(String gcmSenderId, String pushToken);

    void OnPushReceive(String netmeraPushObjectJSON);

    void OnPushOpen(String netmeraPushObjectJSON);

    void OnPushDismiss(String netmeraPushObjectJSON);

    void OnPushButtonClicked(String netmeraPushObjectJSON);


    // inbox
    void OnInboxFetchSuccess(String netmeraPushInboxJSON);

    void OnInboxFetchFail(int errorCode, String errorMessage);

    void OnInboxNextPageFetchSuccess(String netmeraPushInboxJSON);

    void OnInboxNextPageFetchFail(int errorCode, String errorMessage);

    void OnInboxStatusChangeSuccess();

    void OnInboxStatusChangeFail(int errorCode, String errorMessage);

    void OnInboxStatusCount(int countWithThatStatus);
}
