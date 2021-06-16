using System;
using System.Runtime.InteropServices;
using UnityEngine;

public class NetmeraPlugin : MonoBehaviour
{



    [DllImport("__Internal")]
    private static extern void _RequestPushNotificationAuthorization();

    [DllImport("__Internal")]
    private static extern void _SendEvent(string key, string parameters);

    [DllImport("__Internal")]
    private static extern void _UpdateUser(string parameters);

    [DllImport("__Internal")]
    private static extern void _EnablePopupPresentation(bool isEnabled);

    [DllImport("__Internal")]
    private static extern void _RequestLocationAuthorization();

    [DllImport("__Internal")]
    private static extern void _SetListener(string listenerName);

    [DllImport("__Internal")]
    private static extern void _Log(string message);

    [DllImport("__Internal")]
    public static extern void _FetchInbox(int pageSize, int status, string categories, bool includeExpiredObjects);

    [DllImport("__Internal")]
    public static extern void _FetchNextPage();

    [DllImport("__Internal")]
    public static extern void _ChangeInboxItemStatuses(int startIndex, int endIndex, int status);

    [DllImport("__Internal")]
    public static extern void _GetStatusCount(int status);

    [DllImport("__Internal")]
    public static extern void _ChangeAllInboxItemStatuses(int status);

    public static void RequestPushNotificationAuthorization()
    {
        _RequestPushNotificationAuthorization();
    }

    public static void SetListener(string listenerName)
    {
        _SetListener(listenerName);
    }

    public static void SendEvent(string key, string parameters)
    {
        _SendEvent(key, parameters);
    }

    public static void UpdateUser(string parameters)
    {
        _UpdateUser(parameters);
    }

    public static void Log(string message)
    {
        _Log(message);
    }

    public static void EnablePopupPresentation(bool isEnabled)
    {
        _EnablePopupPresentation(isEnabled);
    }

    public static void RequestLocationAuthorization()
    {
        _RequestLocationAuthorization();
    }

    public static void FetchInbox(int pageSize, int status, string categories, bool includeExpiredObjects)
    {
        _FetchInbox(pageSize, status, categories, includeExpiredObjects);
    }

    public static void FetchNextPage()
    {
        _FetchNextPage();
    }

    public static void ChangeInboxItemStatuses(int startIndex, int endIndex, int status)
    {
        _ChangeInboxItemStatuses(startIndex, endIndex, status);
    }

    public static void GetStatusCount(int status)
    {
        _GetStatusCount(status);
    }

    public static void ChangeAllInboxItemStatuses(int status)
    {
        _ChangeAllInboxItemStatuses(status);
    }

}