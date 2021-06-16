
@interface FNetmeraPlugin : NSObject
    +(void)willPresentNotification:(UNNotification*)notification;
    +(void)didReceiveRemoteNotification:(NSDictionary*)userInfo;
    +(void)didReceiveNotificationResponse:(UNNotificationResponse *)response;
    +(void)didRegisterForRemoteNotificationsWithDeviceToken:(NSString*)deviceToken;
    +(void) NMLog:(NSString*) message;
    +(void) init:(BOOL)printLogs;
    +(void) setListener:(NSString*)unityListenerName;
    +(void) log:(NSString*)message;
@end
