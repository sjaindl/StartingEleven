Pod::Spec.new do |spec|
    spec.name                     = 'auth'
    spec.version                  = '1.0'
    spec.homepage                 = 'https://starting-eleven-2019.firebaseapp.com/home'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'S11 iOS auth dependencies'
    spec.vendored_frameworks      = 'build/cocoapods/framework/auth.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '15.5'
    spec.dependency 'FBSDKCoreKit', '18.0.0'
    spec.dependency 'FBSDKLoginKit', '18.0.0'
    spec.dependency 'GoogleSignIn'
                
    if !Dir.exist?('build/cocoapods/framework/auth.framework') || Dir.empty?('build/cocoapods/framework/auth.framework')
        raise "

        Kotlin framework 'auth' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :auth:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.xcconfig = {
        'ENABLE_USER_SCRIPT_SANDBOXING' => 'NO',
    }
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':auth',
        'PRODUCT_MODULE_NAME' => 'auth',
    }
                
    spec.script_phases = [
        {
            :name => 'Build auth',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
    spec.resources = ['build/compose/cocoapods/compose-resources']
end