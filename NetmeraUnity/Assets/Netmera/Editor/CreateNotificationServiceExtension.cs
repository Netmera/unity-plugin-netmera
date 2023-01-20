using UnityEngine;
using UnityEditor;
using UnityEditor.Callbacks;
using System.Collections;
using UnityEditor.iOS.Xcode;
using System.IO;
using UnityEditor.iOS.Xcode.Extensions;
using System.Text.RegularExpressions;


namespace Netmera {

    public class BuildPostProcessor {

        private const string serviceExtensionName = "NetmeraUnityNotificationServiceExtension";
        private const string netmeraFolderPath = "./Assets/Plugins/iOS/Netmera/";
        private const string editorFolderPath = "./Assets/Netmera/Editor/";
        private const string notificationServicePath = netmeraFolderPath + serviceExtensionName;
        private const string infoPlist = "/Info.plist";
        private const string notificationServicePlistPath = serviceExtensionName + infoPlist;
        private const string notificationServiceFilePath = serviceExtensionName + "/NotificationService.swift";
        private const string notificationServiceBridgingHeaderPath = serviceExtensionName + "/NetmeraUnityNotificationServiceExtension-Bridging-Header.h";
        private const string dependenciesFilePath = editorFolderPath + "NetmeraDependencies.xml";
        private static string appGroupName = "group.com." + PlayerSettings.applicationIdentifier;
        private static string targetName = "Unity-iPhone";

        
        [PostProcessBuildAttribute(45)]
        public static void OnPostProcessBuildAttribute(BuildTarget target, string pathToBuiltProject) {
            string podfilePath = pathToBuiltProject + "/" + "Podfile";
            AddTargetsToPodFile(podfilePath);
            // AddProjectCapabilities(pathToBuiltProject);
        }

        [PostProcessBuild]
        public static void OnPostProcessBuild(BuildTarget target, string pathToBuiltProject)
        {
            if(target == BuildTarget.iOS)
            {
                string projPath = PBXProject.GetPBXProjectPath(pathToBuiltProject);
                string serviceExtensionNameDestFolder = pathToBuiltProject + "/" + serviceExtensionName;
                
                string plistPath = pathToBuiltProject + infoPlist;
                
                PBXProject proj = new PBXProject();
                proj.ReadFromFile (projPath);
                string mainTarget = proj.GetUnityMainTargetGuid();
                
                if (!File.Exists(serviceExtensionNameDestFolder)) {
                    FileUtil.CopyFileOrDirectory(notificationServicePath,  serviceExtensionNameDestFolder);
                }

                ExtensionCreatePlist(plistPath, serviceExtensionNameDestFolder + infoPlist);

                var notificationServiceTarget = PBXProjectExtensions.AddAppExtension (proj, mainTarget, serviceExtensionName, PlayerSettings.GetApplicationIdentifier (BuildTargetGroup.iOS) + "." + serviceExtensionName, serviceExtensionName + infoPlist);            

                proj.AddFileToBuild (notificationServiceTarget, proj.AddFile (notificationServiceFilePath, notificationServiceFilePath));
                proj.AddFile (notificationServicePlistPath, notificationServicePlistPath);
                proj.AddFile (notificationServiceBridgingHeaderPath, notificationServiceBridgingHeaderPath);

                
                SetBuildProperties(proj, notificationServiceTarget);
                                
                proj.WriteToFile (projPath);
            }  
        }

        private static void AddTargetsToPodFile(string podfilePath) {
            var dependenciesFile = File.ReadAllText(dependenciesFilePath);
            var dependenciesRegex = new Regex("(?<=<iosPod name=\"Netmera/NetmeraWithoutAdId\" version=\").+(?=\" minTargetSdk=\"10.0\" addToAllTargets=\"true\"></iosPod>)");

            if (!dependenciesRegex.IsMatch(dependenciesFile)) {
                Debug.Log($"Could not read current iOS framework dependency version");
                return;
            }
            var requiredVersion = dependenciesRegex.Match(dependenciesFile).ToString();

            var target = $"target '{serviceExtensionName}' do\n  pod 'Netmera/NetmeraWithoutAdId', '{requiredVersion}'\n  pod 'Netmera/NotificationContentExtension', '{requiredVersion}'\n  pod 'Netmera/NotificationServiceExtension', '{requiredVersion}'\nend\n";
            
            var podfile = File.ReadAllText(podfilePath);
            File.WriteAllText(podfilePath, podfile + target);            
        }

        private static void ExtensionCreatePlist(string mainPlistPath, string destPath) {
            PlistDocument plist = new PlistDocument();
            plist.ReadFromString(File.ReadAllText(destPath));
            plist.root.SetString("CFBundleShortVersionString", PlayerSettings.bundleVersion);
            plist.root.SetString("CFBundleVersion", PlayerSettings.iOS.buildNumber);
            plist.root.SetString("CFBundleIdentifier", "$(PRODUCT_BUNDLE_IDENTIFIER)");
            plist.root.SetString("CFBundleName", "$(PRODUCT_NAME)");
            plist.root.SetString("CFBundleExecutable", "$(EXECUTABLE_NAME)");
            plist.root.SetString("CFBundleDisplayName", serviceExtensionName);
            plist.root.SetString("CFBundleInfoDictionaryVersion", "6.0");
            File.WriteAllText(destPath, plist.WriteToString());
        }

        private static void SetBuildProperties(PBXProject proj, string target) {
            proj.SetBuildProperty(target, "TARGETED_DEVICE_FAMILY", "1,2");
            proj.SetBuildProperty(target, "IPHONEOS_DEPLOYMENT_TARGET", "10.0");
            proj.SetBuildProperty(target, "SWIFT_VERSION", "5.0");
            proj.SetBuildProperty(target, "ARCHS", "arm64");
            proj.SetBuildProperty(target, "DEVELOPMENT_TEAM", PlayerSettings.iOS.appleDeveloperTeamID);
        }
        
        private static void AddProjectCapabilities(string projectPath) {
            string pbxPath = PBXProject.GetPBXProjectPath(projectPath);
            PBXProject proj = new PBXProject();
            proj.ReadFromFile (projectPath);
            var targetGuid = proj.GetUnityMainTargetGuid();

            string entitlementsFileName = targetName + ".entitlements";
            string entitlementsPath = targetName + "/" + entitlementsFileName;

            Debug.Log($"Deneme");

            var entitlementsPlist = new PlistDocument();
            entitlementsPlist.root.SetString("aps-environment", "development");
            PlistElementArray appGroupsArray = entitlementsPlist.root.CreateArray("com.apple.security.application-groups");
            appGroupsArray.AddString(appGroupName);
            File.WriteAllText(projectPath + "/" + entitlementsPath, entitlementsPlist.WriteToString());
            
            proj.AddFile(entitlementsPath, entitlementsPath);
            proj.SetBuildProperty(targetGuid, "CODE_SIGN_ENTITLEMENTS", entitlementsPath);
            
            Debug.Log($"pbxPath {pbxPath}");

            var projCapability = new ProjectCapabilityManager(pbxPath, entitlementsFileName, targetName);

            Debug.Log($"projCapability {projCapability}");

            projCapability.AddBackgroundModes(BackgroundModesOptions.RemoteNotifications);
            projCapability.AddPushNotifications(true);
            projCapability.AddAppGroups(new[] { appGroupName });
            
            projCapability.WriteToFile();
        }
    }
}
 