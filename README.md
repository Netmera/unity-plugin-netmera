
  
# unity-netmera  
  
  **Step 1- Importing Unity Jar Resolver**  
Download external-dependency-manager-1.2.164.unitypackage from NetmeraPackages folder and import it into your project.  
  
**Step 2- Importing Firebase Messaging**  
Download FirebaseMessaging.unitypackage from NetmeraPackages folder and import it into your project. (Please do not uncheck any file while importing)  
  
**Step 3 - Importing Netmera**  
Download Netmera_1.0.0.unitypackage from NetmeraPackages folder and import it into your project.  
  
**Step 4.1 For Android - Adding a google-services.json file**  
Download google-services.json file from Firebase console panel( More info at: https://firebase.google.com/docs/cloud-messaging/android/client#add_a_firebase_configuration_file )  
Put this file into your Unity application's **Assets** folder. (Note: Path is important. Do not move it anywhere.)  

**Step 4.2  For Ios - Adding a GoogleService-Info.plist file**  
Download GoogleService-Info.plist file from Firebase console panel( More info at: https://firebase.google.com/docs/cloud-messaging/ios/client#add-config-file )  
Put this file into your Unity application's **Assets** folder. (Note: Path is important. Do not move it anywhere.)  

Note: It is better to use custom data feature instead of internal app deeplinking in Unity for forwarding user to a spesific page since there are many steps to implement it outside of Unity Editor. If you want to get data or url after clicking to a notification, you can send it via custom parameters by setting keys and values through Netmera dashboard.

Following steps are divided into two as Android and Ios :

**IOS ONLY**

Build your project via Unity3D editor. Then open the generated Unity-iPhone.xcworkspace file. Xcode editor will be opened.
From Signing&Capabilities section inside Xcode, enable Push Notifications and Background Modes(Background Fetch, Remote Notifications, Background Processing,and Location Updates(this is optional))

Open your main Info.plist file inside Xcode Editor and your Netmera SDK key and configurations. Please visit Netmera dashboard for your sdk key:

    <key>netmera_logging_disabled</key>
	<false/>
	<key>netmera_mobile_sdkkey</key>
	<string>YOUR NETMERA SDK KEY</string>

    <!-- OPTIONALS-->
    <key>netmera_popup_presentation_disabled</key>
	<false/>
    <key>netmera_optional_baseurl</key>
	<string></string>

Then you are ready to run your application by clicking run inside Xcode.

Optional:

If you want to show Carousel push or Media push, you need to perform additional steps inside Xcode. 
Related documentation can be found in https://developer.netmera.com/en/IOS/Push-Notifications .



**ANDROID ONLY**

**Step 5 - Usage of a custom mainTemplate.gradle file.**  
Unity Jar Resolver will be used for integration. You already included it by importing FirebaseMessaging.unitypackage.  
  
Click Project Settings > Publishing Settings > Enable **Custom Main Gradle Template** , **Custom Main Manifest** and **Custom Gradle Properties Template**.  
  
There should be two gradle related files **gradleTemplate.properties** and  **mainTemplate.gradle** inside the folder Assets>Plugins>Android.  
  
Open gradleTemplate.properties file, and append this line **android.useAndroidX=true**  
  
**Step 6 - Jar Resolver Settings**  
Inside Unity Editor's top navigation panel, Click Assets > External Dependency Manager > Android Resolver > Settings.  
  
The below settings are recommended but not a must. The remaining settings are up to you.  
  
Uncheck 'Enable Auto Resolution'  
Uncheck 'Enable Resolution on Build'  
Check 'Install Android Packages'  
Check 'Explode AARs'  
Check 'Patch AndroidManifest.xml'  
Check 'Patch mainTemplate.gradle'  
Check 'Patch gradleTemplate.properties'  
Check 'Use Jetifier'
  
**Step 7 - Jar Resolver Resolve Process**  
  
Then, Inside Unity Editor's top navigation panel, Click Assets > External Dependency Manager > Android Resolver > Resolve.  
  
Then open mainTemplate.gradle file and check that whether libraries are imported correctly.  
  
Inside **dependencies{}** section of this file, you should at least see the following libraries.(version numbers would change).  
  
If they are not included, you can try with 'Force Resolve' rather than 'Resolve'.   
Note: While building your app, If you get duplicate class error, please check the dependencies and remove the duplicate one.  

      
    implementation 'com.google.android.gms:play-services-base:17.6.0' // Assets/Firebase/Editor/AppDependencies.xml:17     implementation 'com.google.android.gms:play-services-location:18.0.0' // Assets/Netmera/Editor/NetmeraDependencies.xml:6  
    implementation 'com.google.firebase:firebase-analytics:18.0.2' // Assets/Firebase/Editor/MessagingDependencies.xml:15  
    implementation 'com.google.firebase:firebase-app-unity:7.1.0' // Assets/Firebase/Editor/AppDependencies.xml:22  
    implementation 'com.google.firebase:firebase-common:19.5.0' // Assets/Firebase/Editor/AppDependencies.xml:13  
    implementation 'com.google.firebase:firebase-messaging:21.0.1' // Assets/Firebase/Editor/MessagingDependencies.xml:13  
    implementation 'com.google.firebase:firebase-messaging-unity:7.1.0' // Assets/Firebase/Editor/MessagingDependencies.xml:20  
    implementation 'com.netmera:netmera:3.8.7' // Assets/Netmera/Editor/NetmeraDependencies.xml:5  

  
**Step 8 - Enable Multidex**  
Open mainTemplate.gradle file, then go to android > defaultConfig section.  
  
Add a new item as **multiDexEnabled true** .  
  
If your minSDK version is below 21, you may need additional steps to perform -> https://appmediation.com/unity-enable-multidex/ .  
  
**Step 9- Editing your AndroidManifest.xml file**  
Open your Unity project's MAIN AndroidManifest.xml file (located at Assets>Plugins>Android>AndroidManifest.xml) and add the following ones inside application tag.  
Note: It is important to edit your Main AndroidManifest.xml file since there may be multiple AndroidManifest files. Please edit this file: Assets>Plugins>Android>AndroidManifest.xml
    
Use Netmera SDK's application class and add required meta-data tags:  
      
     <application android:name="com.netmera.unity.sdk.core.NetmeraCustomApp"> 
	     <meta-data android:name="netmera_mobile_sdkkey" android:value="Netmera Modile SDK key string value. Copy It From Netmera Dashboard."/>    
        <meta-data android:name="netmera_firebase_senderid" android:value="Firebase Sender ID as number. Copy it from Netmera Dashboard."/>    
         <meta-data  android:name="netmera_optional_baseurl" android:value=""/>     
    	 <meta-data android:name="netmera_logging_disabled" android:value="false"/>  
         <meta-data android:name="netmera_popup_presentation_disabled" android:value="false"/>     
     </application>  

Note: If using Netmera's Application class is not possible for you, you need to add this line to your own Application class' onCreate method rather than referencing to NetmeraCustomApp.  

     NetmeraCustomApp.initNetmera(this); // this should be an instance of Application class   

**Step 10 -  Demo Scene** 
We prepared a sample Unity Scene to show the capabilities.   Open **Assets>Netmera>Demo>NetmeraDemoScene**.   
You can include this scene  to your app in order to test your integration.   
  
**Step 11 -  Adding Prefab to your first Unity Scene** In your first Unity Scene, import the prefab at Assets>Netmera>Demo>Prefabs> **NetmeraManager**.  
This prefab contains the **NetmeraGameObject** script which shows the usage of all methods of the SDK.  
Please do not destroy this object while swapping your Unity Scenes. It should not be destroyed.   
  
You can create a similar implementation or use Netmera sdk by editing this file based on your use-cases.
Please call **NetmeraCore.Instance.Init(this);**  method before calling any other methods. Ideal place is **Awake()** method

NetmeraGameObject content:

    using System;  
    using System.Collections;  
    using System.Collections.Generic;  
    using Netmera;  
    using UnityEngine;  
      
    public class NetmeraGameObject : MonoBehaviour, Netmera.Callback  
    {  
      [SerializeField] private bool loggingEnabled = true;  
      
	    private void Awake()  
        {  
              Application.SetStackTraceLogType(LogType.Log, StackTraceLogType.None);  
		      DontDestroyOnLoad(gameObject);  
		      Test_Init();  
        }  
      
      
        public void Test_Init()  
        {  
            if (NetmeraCore.Instance == null)  
            {  
                return;  
             }  
            NetmeraCore.Instance.LoggingEnabled = loggingEnabled;  
	        NetmeraCore.Instance.Init(this);  
        }  
      
        /* Sample method calls */  
        public void Test_SendEvent()  
        {  
              JSONObject obj = new JSONObject();  
		      obj["ea"] = "valuesample";  
		      obj["ec"] = 10;  
		      NetmeraCore.Instance.SendEvent("zzl", obj);  
       }  
      
        public void Test_UpdateUser()  
        {  
	          NetmeraUser user = new NetmeraUser();  
		      user.SetBirthday("1980", "06", "26");  
		      user.SetCity("Istanbul");  
		      NetmeraCore.Instance.UpdateUser(user);  
        }  
      
        public void Test_FetchInbox()  
        {  
            List<string> categories = new List<string>() {};  
	        NetmeraCore.Instance.FetchInbox(20, NetmeraEnum.PushStatus.ReadOrUnread, categories, true);  
        }  
      
        public void Test_FetchNextPage()  
        {  
            NetmeraCore.Instance.FetchNextPage();  
        }  
      
        public void Test_Destroy()  
        {  
            NetmeraCore.Instance.Destroy();  
        }  
      
        public void Test_EnablePopupPresentation(bool isEnabled)  
        {  
            NetmeraCore.Instance.EnablePopupPresentation(isEnabled);  
        }  
      
        public void Test_GetStatusCount()  
        {  
            NetmeraCore.Instance.GetStatusCount(NetmeraEnum.PushStatus.ReadOrUnread);  
        }  
      
        public void Test_ChangeInboxItemStatuses()  
        {  
            NetmeraCore.Instance.ChangeInboxItemStatuses(1, 2, NetmeraEnum.PushStatus.Deleted);  
        }  
      
        public void Test_RequestPermissionsForLocation()  
        {  
            NetmeraCore.Instance.RequestPermissionsForLocation();  
        }  
      
        public void Test_ChangeAllInboxItemStatuses()  
        {  
            NetmeraCore.Instance.ChangeAllInboxItemStatuses(NetmeraEnum.PushStatus.Deleted);  
        }  
      
      
        // Callbacks  
        public void OnPushRegister(string gcmSenderId, string pushToken)  
        {  
        }  
      
        public void OnPushReceive(JSONNode rawJson, NetmeraPushObject pushObject)  
        {  
        }  
      
        public void OnPushOpen(JSONNode rawJson, NetmeraPushObject pushObject)  
        {  
        }  
      
        public void OnPushDismiss(JSONNode rawJson, NetmeraPushObject pushObject)  
        {  
        }  
      
        public void OnPushButtonClicked(JSONNode rawJson, NetmeraPushObject pushObject)  
        {  
        }  
      
        public void OnInboxFetchSuccess(JSONNode netmeraPushInboxJSON)  
        {  
        }  
      
        public void OnInboxFetchFail(int errorCode, string errorMessage)  
        {  
        }  
      
        public void OnInboxNextPageFetchSuccess(JSONNode netmeraPushInboxJSON)  
        {  
        }  
      
        public void OnInboxNextPageFetchFail(int errorCode, string errorMessage)  
        {  
        }  
      
        public void OnInboxStatusChangeSuccess()  
        {  
        }  
      
        public void OnInboxStatusChangeFail(int errorCode, string errorMessage)  
        {  
        }  
      
        public void OnInboxStatusCount(int countWithThatStatus)  
        {  
        }  
    }
