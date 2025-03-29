//
//  AppDelegate.swift
//  iosApp
//
//  Created by Stefan Jaindl on 03.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import FBSDKCoreKit
import FirebaseCore
import FirebaseMessaging
import Foundation

class AppDelegate: NSObject, UIApplicationDelegate {
    let topicName = "news"
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        ApplicationDelegate.shared.application(
            application,
            didFinishLaunchingWithOptions: launchOptions
        )

        FirebaseApp.configure()

        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self

        application.registerForRemoteNotifications()
        
        Messaging.messaging().subscribe(toTopic: topicName) { error in
            debugPrint("Subscribed to topic")
        }

        return true
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }
}

extension AppDelegate : UNUserNotificationCenterDelegate {
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                  willPresent notification: UNNotification) async
        -> UNNotificationPresentationOptions {
        let userInfo = notification.request.content.userInfo

        // With swizzling disabled we must let Messaging know about the message for Analytics
        Messaging.messaging().appDidReceiveMessage(userInfo)

        return [[.sound, .badge]]
      }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse
    ) async {
        let userInfo = response.notification.request.content.userInfo
        debugPrint("received userInfo: \(userInfo)")
      }
    
    // silent push notifications:
    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable : Any],
       fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void)
    {
      Messaging.messaging().appDidReceiveMessage(userInfo)
      completionHandler(.noData)
    }
}

extension AppDelegate : MessagingDelegate {
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        debugPrint("received fcm token: \(fcmToken ?? "")")
    }
}
