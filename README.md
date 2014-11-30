# Grada.me

Android Repo

## Google Play Services [Google Play]

Documentation:

- https://developers.google.com/+/mobile/android/getting-started
- http://developer.android.com/google/play-services/setup.html
- http://developer.android.com/sdk/installing/studio-build.html
- https://developers.google.com/+/quickstart/android

### Generation of SHA1 key

http://stackoverflow.com/questions/19207940/how-to-get-my-android-fingerprint-certifica-in-android-studio
keytool -list -v -keystore  ~/.android/debug.keystore

### Android studio gradle configuration

Please replace:
```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
}
```

with

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v{#versionNumber}'
    compile 'com.google.android.gms:play-services:{#versionNumber}'
}
```

### Android Manifest

In your AndroidManifest.xml, you must add a meta-data tag as a child of the application tag to declare the version of Google Play services you are using.

```xml
<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
```

You must also request the following permissions to use the noted features:

```xml
<!-- To access Google+ APIs: -->
<uses-permission android:name="android.permission.INTERNET" />
```

```xml
<!-- To retrieve the account name (email) as part of sign-in: -->
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
```

```xml
<!--
    To retrieve OAuth 2.0 tokens or invalidate tokens to disconnect a user.
    This disconnect option is required to comply with the Google+ Sign-In developer policies:
-->
<uses-permission android:name="android.permission.USE_CREDENTIALS" />
```

### MacOSX Emulators

Issue:
```bash
HAX is not working and emulator runs in emulation mode
```

Fix:
```bash
# Post http://stackoverflow.com/questions/21031903/how-to-fix-hax-is-not-working-and-emulator-runs-in-emulation-mode
brew install caskroom/cask/brew-cask && brew update
brew cask install intel-haxm
```