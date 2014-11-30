package libraries.GooglePlus;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import libraries.GooglePlus.Enums.GooglePlusStates;

/**
 * Article: https://developers.google.com/+/quickstart/android
 */

public class GooglePlusHelper {

    public Activity activity;
    public GooglePlusStates status;

    public GooglePlusHelper(Activity context) {
        this.activity = context;
        this.status = GooglePlusStates.STATE_DEFAULT;
    }

    /**
     * When we build the GoogleApiClient we specify where connected and
     * connection failed callbacks should be returned, which Google APIs our
     * app uses and which OAuth 2.0 scopes our app requests.
     * @return GoogleApiClient
     */
    public GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(activity)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }
}
