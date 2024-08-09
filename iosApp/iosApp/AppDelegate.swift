//
//  AppDelegate.swift
//  iosApp
//
//  Created by Stefan Jaindl on 03.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import FBSDKCoreKit
import FirebaseCore
import Foundation
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        ApplicationDelegate.shared.application(
            application,
            didFinishLaunchingWithOptions: launchOptions
        )
        
        FirebaseApp.configure()
        return true
    }
}
