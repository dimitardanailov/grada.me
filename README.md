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

```bash
keytool -list -v -keystore  ~/.android/debug.keystore
```

### Android studio gradle configuration

- Migrating Gradle Projects to version 1.0.0 (http://tools.android.com/tech-docs/new-build-system/migrating-to-1-0-0)

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
    compile 'com.google.android.gms:play-services-plus:{#versionNumber}'
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

## Google Play Services [Google Maps]

Documentation:

- http://developer.android.com/google/play-services/setup.html
- https://developers.google.com/maps/documentation/android/start

### Android studio gradle configuration

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v{#versionNumber}'
    compile 'com.google.android.gms:play-services-maps:{#versionNumber}'
}
```

### Android Manifest

In your AndroidManifest.xml, you must add a meta-data tag as a child of the application tag to declare the version of Google Play services you are using.

```xml
<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
```

- Sign in Google Console https://console.developers.google.com/ and add Google Maps Android API v2 in you service stack
- Apis & auth -> Credentials -> Create new Key -> Android Key

```bash
# Get information for your Developer profile
keytool -list -v -keystore  ~/.android/debug.keystore
```

```xml
<!--
    Goolge Maps API Key
    Developer Key : {Key}
-->
<meta-data
    android:name="com.google.android.maps.v2.API_KEY"
    android:value="{Key}" />
```

You must also request the following permissions to use the noted features:
- https://developers.google.com/maps/documentation/android/start#specify_app_settings_in_the_application_manifest

```xml
<!-- Used by the Google Maps API to download map tiles from Google Maps servers. -->
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
```

```xml
<!-- Allows the Google Maps API to check the connection status in order to determine whether data can be downloaded. -->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

```xml
<!-- Allows the Google Maps API to cache map tile data in the device's external storage area. -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

```xml
<!-- Allows the Google Maps API to use WiFi or mobile cell data (or both) to determine the device's location. -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

```xml
<!-- Allows the Google Maps API to use the Global Positioning System (GPS) to determine the device's location to within a very small area. -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Specify requirement for OpenGL ES version 2

```
The Google Maps Android API uses OpenGL ES version 2 to render the map. 
If OpenGL ES version 2 is not installed, your map will not appear. 
We recommend that you add the following <uses-feature> element as a child of the <manifest> element in AndroidManifest.xml:
```

```xml
<uses-feature
    android:glEsVersion="0x00020000"
    android:required="true" />
```

## Facebook SDK

### Android studio gradle configuration

```bash
dependencies {
    compile 'com.facebook.android:facebook-android-sdk:[facebook-sdk-version]'
}
```

#### Prerequisites

Before you implement Facebook Login you need:

- Facebook App - Configured and linked to your app, with Single Sign On enabled.

After that add this Facebook App Id in Android Manifest configuration:

```xml
<!-- Facebook APP ID -->
<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
```

### Add Facebook Login

Documentation: https://developers.facebook.com/docs/facebook-login/android/v2.2

#### Create Facebook Fragment

Create a new layout xml and place facebook button. Example `facebook_login_button_fragment`:

```xml

    <com.facebook.widget.LoginButton
        android:id="@+id/authButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="30dp"
        />
```

Create Facebook Fragment Class. Example `FacebookLoginButtonFragment` class

```java
    
    import android.support.v4.app.Fragment;

    public class FacebookLoginButtonFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // Get Layout Information and inflate view
            View view = inflater.inflate(R.layout.facebook_login_button_fragment, container, false);

            return view;
        }
    }
```

Next, define a private instance variable for your `MainFragment` class:

```java
private FacebookLoginButtonFragment FacebookLoginButtonFragment;
```

Change `onCreate()` to add an instance of the `FacebookLoginButtonFragment` class to the activity 
during your app's initial setup or after the saved state restores:

```java

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