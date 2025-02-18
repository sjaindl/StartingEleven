//
//  CryptoUtils.swift
//  iosApp
//
//  Created by Stefan Jaindl on 18.02.25.
//  Copyright © 2025 orgName. All rights reserved.
//

import CryptoKit
import Foundation

@objc public class CryptoUtils: NSObject {
    
    @objc public static let shared = CryptoUtils()
    
    private override init() {
    }
    
    @objc(sha256Hash:)
    public func sha256Hash(inputString: String) -> String {
        let inputData = Data(inputString.utf8)
        let hashedData = SHA256.hash(data: inputData)
        return hashedData.map { String(format: "%02hhx", $0) }.joined()
    }
}
