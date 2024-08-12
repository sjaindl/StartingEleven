//
//  AppDelegate.swift
//  iosApp
//
//  Created by Stefan Jaindl on 03.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import composeApp
import FBSDKCoreKit
import FirebaseCore
import FirebaseFirestore
import Foundation
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        ApplicationDelegate.shared.application(
            application,
            didFinishLaunchingWithOptions: launchOptions
        )
        
        FirebaseApp.configure()

        // TODO: Replace once method in Firebase iOS SDK is out of preview
        // It's not available in iOSMain right now.
        AppModule_iosKt.globalFireStore = Firestore.firestore(database: "s11-prod")
        
        return true
    }
}
