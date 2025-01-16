//
//  AppleSignIn.swift
//  Starting Eleven
//
//  Created by Stefan Jaindl on 12.01.25.
//  Copyright © 2025 Stefan Jaindl. All rights reserved.
//

import AuthenticationServices
import CryptoKit
import Foundation

private enum SignInWithAppleError: LocalizedError {
    case invalidCredential
    case unableToFetchToken
    case unableToSerializeToken
    case unableToFindNonce
    
    var errorDescription: String {
        switch self {
        case .invalidCredential:
            return "Invalid credential: ASAuthorization failure."
        case .unableToFetchToken:
            return "Unable to fetch identity token"
        case .unableToSerializeToken:
            return "Unable to serialize token string from data"
        case .unableToFindNonce:
            return "Unable to find current nonce."
        }
    }
}

@objc public class AppleSignInManager: NSObject {
    
    @objc public static let shared = AppleSignInManager()
    
    fileprivate var currentNonce: String?
    
    private var onSignInSuccess: ((String, String) -> Void)?
    private var onSignInFailure: ((String) -> Void)?
    
    private override init() {
    }
    
    @objc(signIn:onFailure:) public func signIn(
        onSuccess: @escaping (String, String) -> Void,
        onFailure: @escaping (String) -> Void
    ) {
        self.onSignInSuccess = onSuccess
        self.onSignInFailure = onFailure
        
        let nonce = randomNonceString()
        if nonce.isEmpty {
            return
        }
        currentNonce = nonce
        
        let appleIDProvider = ASAuthorizationAppleIDProvider()
        let request = appleIDProvider.createRequest()
        request.requestedScopes = [.fullName, .email]
        request.nonce = sha256(nonce)
        
        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        
        authorizationController.performRequests()
    }
    
    private func sha256(_ input: String) -> String {
      let inputData = Data(input.utf8)
      let hashedData = SHA256.hash(data: inputData)
      let hashString = hashedData.compactMap {
        String(format: "%02x", $0)
      }.joined()

      return hashString
    }
    
    private func randomNonceString(length: Int = 32) -> String {
      precondition(length > 0)
      var randomBytes = [UInt8](repeating: 0, count: length)
      let errorCode = SecRandomCopyBytes(kSecRandomDefault, randomBytes.count, &randomBytes)
      if errorCode != errSecSuccess {
          self.onSignInFailure?("Unable to generate nonce. SecRandomCopyBytes failed with OSStatus \(errorCode)")
          return ""
      }

      let charset: [Character] =
        Array("0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._")

      let nonce = randomBytes.map { byte in
        // Pick a random character from the set, wrapping around if needed.
        charset[Int(byte) % charset.count]
      }

      return String(nonce)
    }
}

// MARK: - ASAuthorizationControllerDelegate
extension AppleSignInManager: ASAuthorizationControllerDelegate {
    public func authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization authorization: ASAuthorization
    ) {
        do {
            let token = try getTokenFromAuthorization(authorization: authorization)
            let nonce = try getCurrentNonce()
            self.onSignInSuccess?(token, nonce)
        } catch {
            if let error = error as? SignInWithAppleError {
                self.onSignInFailure?(error.errorDescription)
            } else {
                self.onSignInFailure?(error.localizedDescription)
            }
            
            return
        }
    }
    
    public func authorizationController(controller: ASAuthorizationController,
                                 didCompleteWithError error: Error) {
        self.onSignInFailure?("Sign in with Apple failed: \(error.localizedDescription)")
    }
    
    private func getTokenFromAuthorization(authorization: ASAuthorization) throws -> String {
        guard let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential else {
            throw SignInWithAppleError.invalidCredential
        }

        guard let appleIDToken = appleIDCredential.identityToken else {
            throw SignInWithAppleError.unableToFetchToken
        }
        
        guard let idTokenString = String(data: appleIDToken, encoding: .utf8) else {
            throw SignInWithAppleError.unableToSerializeToken
        }
        
        return idTokenString
    }
    
    private func getCurrentNonce() throws -> String {
        guard let currentNonce else {
            throw SignInWithAppleError.unableToFindNonce
        }
        return currentNonce
    }
}
