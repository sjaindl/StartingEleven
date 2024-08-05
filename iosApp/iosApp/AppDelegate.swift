//
//  AppDelegate.swift
//  iosApp
//
//  Created by Stefan Jaindl on 03.08.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import FirebaseCore
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        FirebaseApp.configure()
        return true
    }
}
