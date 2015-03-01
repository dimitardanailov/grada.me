package grada.me;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;

import grada.me.dialogs.ApplicationDialogFragment;
import grada.me.dialogs.enums.DialogType;
import grada.me.googlemaps.ApplicationMapFragment;
import libraries.GooglePlus.Enums.GooglePlusStates;
import libraries.GooglePlus.GooglePlusHelper;

public class GradaMeMainActivity extends DefaultActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    OnMapReadyCallback,
    View.OnClickListener {

    // Get Class Name
    private static String TAG = GradaMeMainActivity.class.getName();

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /**
     * GoogleApiClient wraps our service connection to Google Play services and
     * provides access to the users sign in state and Google's APIs.
     */
    private GoogleApiClient mGoogleApiClient;

    // Layout Elements
    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;
    private ListView mCirclesListView;
    private ArrayAdapter<String> mCirclesAdapter;
    private ArrayList<String> mCirclesList;

    // Class References and Internal Libraries
    private GooglePlusApiClient googlePlusApiClient = new GooglePlusApiClient();
    
    // Google Plus Helper
    private GooglePlusHelper googlePlusHelper;

    private GoogleMapClient googleMapClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grada_me_main);

        initializeLayoutElements();

        googlePlusHelper = new GooglePlusHelper(this);

        // mGoogleApiClient = googlePlusHelper.buildGoogleApiClient();

        Activity activity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        Log.d(TAG, "App is started ... ");
        
        // Try to load a new Google Map
        try {
            // googleMapClient = new GoogleMapClient(this);
            // googleMapClient.initializeMapFragment();
        } catch (Exception e) {
            Log.e(TAG, "Google Maps can't be loaded", e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grada_me_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        initializeClickListeners(view);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Load Configurations and Setup for our Google Map.
        googleMapClient.onMapReady(map);
    }

    /**
     * Google Play services establish connection
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        googlePlusApiClient.onConnected(connectionHint);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        googlePlusApiClient.onConnectionFailed(result);
    }

    /**
     * The connection to Google Play services was lost for some reason.
     * We call connect() to attempt to re-establish the connection or get a
     * ConnectionResult that we can attempt to resolve.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, 
     * * and any additional data from it. The resultCode will be RESULT_CANCELED if the activity explicitly returned that, 
     * * didn't return any result, or crashed during its operation.
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                googlePlusApiClient.OnActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * Make initialize of all layout elements and assign click listeners.
     */
    private void initializeLayoutElements() {
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
        mStatus = (TextView) findViewById(R.id.sign_in_status);
        mCirclesListView = (ListView) findViewById(R.id.circles_list);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);
    }

    /**
     * Initialize application click listeners
     * @param view
     */
    private void initializeClickListeners(View view) {

        // Get view identifier.
        Integer identifierId = view.getId();
        
        // Google Play Click Listeners
        googlePlusApiClient.initializeClickListeners(identifierId);
    }

    /**
     * A fragment that displays a dialog window, floating on top of its activity's window.
     * This fragment contains a Dialog object, which it displays as appropriate based on the fragment's state.
     * Control of the dialog (deciding when to show, hide, dismiss it) should be done through the API here, not with direct calls on the dialog.
     */
    public static class ActivityDialogFragment extends ApplicationDialogFragment {
        Activity activity;
        int title;
        DialogType type;
        GooglePlusApiClient googlePlusApiClient = null;

        /**
         * Create Activity Dialog Fragment
         * @param activity
         * @param title
         * @param dialogType
         */
        public ActivityDialogFragment(Activity activity, int title, DialogType dialogType) {
            super(activity, title, dialogType);
        }

        /**
         * Set googlePlusApiClient for current instance
         * @param googlePlusApiClient
         */
        public void setGooglePlusApiClient(GooglePlusApiClient googlePlusApiClient) {
            this.googlePlusApiClient = googlePlusApiClient;
        }

        public static ActivityDialogFragment newInstance(Activity activity, int title, DialogType dialogType) {
            ActivityDialogFragment fragment = new ActivityDialogFragment(activity, title, dialogType);
            
            // A mapping from String values to various Parcelable types.
            Bundle arguments = new Bundle();
            arguments.putInt("title", title);
            fragment.setArguments(arguments);

            return fragment;
        }
        
        @Override
        public Dialog onCrete(Bundle savedInstanceState) {

            // Load a new Google Play Services Error Dialog
            if (this.type == DialogType.DIALOG_PLAY_SERVICES_ERROR) {

                Dialog googlePlayErrorDialog = null;
                
                // Try to display
                if (googlePlusApiClient != null) {
                    googlePlayErrorDialog = googlePlusApiClient.createGooglePlayServicesErrorDialog(this.activity);
                }
                
                if (googlePlayErrorDialog != null) {
                    return googlePlayErrorDialog;
                } else {
                    Dialog defaultAlertDialog = this.createDefaultApplicationError();

                    return defaultAlertDialog;
                }
            } else {
                Dialog defaultAlertDialog = this.createDefaultApplicationError();

                return defaultAlertDialog;
            }
        }

    } // END ApplicationDialogFragment

    /**
     * Operation with all Google Plus iterations.
     */
    private class GooglePlusApiClient {

        // Used to store the PendingIntent most recently returned by Google Play
        // services until the user clicks 'sign in'.
        private PendingIntent mSignInIntent;

        // Used to store the error code most recently returned by Google Play services
        // until the user clicks 'sign in'.
        private int mSignInError;
        
        // The requestCode given when calling startActivityForResult.
        private static final int REQUEST_CODE = 0;

        /**
         * onConnected is called when our Activity successfully connects to Google
         * Play services.  onConnected indicates that an account was selected on the
         * device, that the selected account has granted any requested permissions to
         * our app and that we were able to establish a service connection to Google Play services.
         */
        private void onConnected(Bundle connectionHint) {
            Log.d(TAG, "onConnected");

            // Update the user interface to reflect that the user is signed in
            mSignInButton.setEnabled(false);
            mSignOutButton.setEnabled(true);
            mRevokeButton.setEnabled(true);

            // Indicate that the sign in process is complete.
            googlePlusHelper.status = GooglePlusStates.STATE_DEFAULT;
        }

        /**
         * onConnectionFailed is called when our Activity could not connect to Google
         * Play services.  onConnectionFailed indicates that the user needs to select
         * an account, grant permissions or resolve an error in order to sign in.
         * @param result
         */
        private void onConnectionFailed(ConnectionResult result) {
            // Refer to the javadoc for ConnectionResult to see what error codes might
            // be returned in onConnectionFailed.
            Log.d(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

            if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
                // An API requested for GoogleApiClient is not available. The device's current
                // configuration might not be supported with the requested API or a required component
                // may not be installed, such as the Android Wear application. You may need to use a
                // second GoogleApiClient to manage the application's optional APIs.
            } else if (googlePlusHelper.status != GooglePlusStates.STATE_IN_PROGRESS) {
                // We do not have an intent in progress so we should store the latest
                // error resolution intent for use when the sign in button is clicked.

                if (googlePlusHelper.status == GooglePlusStates.STATE_DEFAULT.STATE_IN_PROGRESS) {
                    // STATE_SIGN_IN indicates the user already clicked the sign in button
                    // so we should continue processing errors until the user is signed in
                    // or they click cancel.
                }
            }
        }

        /**
         * Check information from previous Sign in Activity.
         * @param requestCode
         * @param resultCode
         * @param data
         */
        private void OnActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                // If the error resolution was successful we should continue
                // processing errors.
                googlePlusHelper.status = GooglePlusStates.STATE_SIGN_IN;
            } else {
                // If the error resolution was not successful or the user canceled,
                // we should stop processing errors.
                googlePlusHelper.status = GooglePlusStates.STATE_DEFAULT;
            }

            if (!mGoogleApiClient.isConnected()) {
                // If Google Play services resolved the issue with a dialog then
                // onStart is not called so we need to re-attempt connection here.
                mGoogleApiClient.connect();
            }
        }

        /**
         * Initialize logic behind Google Plus Click Listeners
         * @param identifierId
         */
        private void initializeClickListeners(Integer identifierId) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
            if (!mGoogleApiClient.isConnected()) {
                switch (identifierId) {
                    // User try to sign in
                    case R.id.sign_in_button:
                        mStatus.setText(R.string.status_signing_in);
                        resolveSignInError();
                        break;
                    case R.id.sign_out_button:
                        // We clear the default account on sign out so that Google Play
                        // services will not return an onConnected callback without user
                        // interaction.
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();
                        break;
                }
            }
        }

        /* Starts an appropriate intent or dialog for user interaction to resolve
         * the current error preventing the user from being signed in.  This could
         * be a dialog allowing the user to select an account, an activity allowing
         * the user to consent to the permissions being requested by your app, a
         * setting to enable device networking, etc.
         */
        private void resolveSignInError() {
            if (mSignInIntent != null) {
                // We have an intent which will allow our user to sign in or
                // resolve an error.  For example if the user needs to
                // select an account to sign in with, or if they need to consent
                // to the permissions your app is requesting.
                
                try {
                    // Send the pending intent that we stored on the most recent
                    // OnConnectionFailed callback.  This will allow the user to
                    // resolve the error currently preventing our connection to
                    // Google Play services.
                    googlePlusHelper.status = GooglePlusStates.STATE_IN_PROGRESS;
                    startIntentSenderForResult(mSignInIntent.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
                    
                } catch (IntentSender.SendIntentException sendIntentException) {
                    Log.e(TAG, "Sign in intent could not be sent: ", sendIntentException);

                    // The intent was canceled before it was sent.  Attempt to connect to
                    // get an updated ConnectionResult.
                    googlePlusHelper.status = GooglePlusStates.STATE_SIGN_IN;
                    mGoogleApiClient.connect();
                }
            } else {

            }
        }

        /**
         * Start if Google Play Services Error Dialog if you have any error
         * @param activity
         * @return if you
         */
        private Dialog createGooglePlayServicesErrorDialog(Activity activity) {
            if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                /**
                 * Class GooglePlayServicesUtil:
                 * Utility class for verifying that the Google Play services APK is available and up-to-date on this device.
                 * http://developer.android.com/reference/com/google/android/gms/common/GooglePlayServicesUtil.html
                 *
                 * Method public static Dialog getErrorDialog (int errorCode, Activity activity, int requestCode, DialogInterface.OnCancelListener cancelListener)
                 * Returns a dialog to address the provided errorCode. 
                 * * The returned dialog displays a localized message about the error and upon user confirmation 
                 * * (by tapping on dialog) will direct them to the Play Store if Google Play services is out of date or missing, 
                 * * or to system settings if Google Play services is disabled on the device.
                 */
                Dialog googlePlayErrorDialog = GooglePlayServicesUtil.getErrorDialog(
                        // errorCode - error code returned by isGooglePlayServicesAvailable(Context) call. If errorCode is SUCCESS then null is returned.
                        mSignInError,
                        // activity - parent activity for creating the dialog, also used for identifying language to display dialog in.
                        activity,
                        // requestCode - The requestCode given when calling startActivityForResult.
                        REQUEST_CODE,
                        // cancelListener - the DialogInterface.OnCancelListener to invoke if the dialog is canceled.
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Log.e(TAG, "Google Play services resolution cancelled");
                                googlePlusHelper.status = GooglePlusStates.STATE_DEFAULT;
                                mStatus.setText(R.string.status_signed_out);
                            }
                        }
                );

                return googlePlayErrorDialog;
            } else {
                return null;
            }
        }
    } // END GooglePlusApiClient

    /**
     * Inner class to operate with all Google Maps iterations.
     */
    private class GoogleMapClient {

        // Instance to current activity
        private Activity activity;

        // Create a new Map fragment
        private ApplicationMapFragment mapFragment;

        // Instance of our Google Map
        private GoogleMap googleMap;
        
        // Google Map Fragment Name
        private static final String MAP_FRAGMENT_TAG = "google_maps_fragment";

        // Google Map Zoom default zoom level
        private static final int DEFAULT_ZOOM_LEVEL = 2;

        /**
         * User should to have large zoom value to get values from server.
         * User will get information after zoom level >= {MINIMUM_ZOOM_LEVEL_SERVER_REQUEST}
         */
        private static final int MINIMUM_ZOOM_LEVEL_SERVER_REQUEST = 7;

        // Initialize to a non-valid zoom value
        private float previousZoomLevel = -1.0f;

        public GoogleMapClient(Activity activity) {
            this.activity = activity;
        }

        /**
         * Initialize a new Map Fragment, if you have map fragment call
         */
        protected void initializeMapFragment() {
            
            if (this.activity != null) {
                // Try to get Map Fragment
                mapFragment = (ApplicationMapFragment) getFragmentManager()
                        .findFragmentByTag(MAP_FRAGMENT_TAG);

                // We only create a fragment if it doesn't already exist.
                if (mapFragment == null) {
                    mapFragment = new ApplicationMapFragment(this.activity);
                    mapFragment.initializeGoogleMapsOptions();

                    // This activity will receive the Map object once the map fragment
                    // is fully loaded
                    mapFragment.getMapAsync((OnMapReadyCallback) this.activity);

                    //mapFragment.initializeFragmentTransaction();
                    // Then we add it using a FragmentTransaction.
                    FragmentTransaction fragmentTransaction = getFragmentManager()
                            .beginTransaction();
                    fragmentTransaction.add(R.id.google_map_container, mapFragment,
                            MAP_FRAGMENT_TAG);
                    fragmentTransaction.commit();


                } else {
                    // This activity will receive the Map object once the map fragment
                    // is fully loaded
                    mapFragment.getMapAsync((OnMapReadyCallback) this.activity);
                }
            }
        }
        
        protected void onMapReady(GoogleMap map) {
            Log.d(TAG, "Google Map is loaded");
            
            // Previous Zoom level will be equal to our Default Zoom Level
            previousZoomLevel = DEFAULT_ZOOM_LEVEL;

            // googleMap our application will use to store all information
            googleMap = map;

            // Move Google Map Camera to Default Zoom Level
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_LEVEL));

            // The My Location button will be visible on the top right of the map.
            googleMap.setMyLocationEnabled(true);

            // Disable Zoom UI Controls
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            /*** Listeners ***/
            
            // Add Listener, when user change camera
            // googleMap.setOnCameraChangeListener(getCameraChangeListener());

            /*** Listeners ***/
        }

        /**
         * Attach listener when user change camera position (drag or zoom/zoom out)
         *
         * Android Documentation for GoogleMap.OnCameraChangeListener: Called after
         * the camera position has changed. During an animation, this listener may
         * not be notified of intermediate camera positions. It is always called for
         * the final position in the animation.
         *
         * Source: http://stackoverflow.com/questions/13973886/zoom-level-listener-in-
         * google-maps-v2-in-android
         *  
         * @return OnCameraChangeListener
         */
        private GoogleMap.OnCameraChangeListener getCameraChangeListener() {
            return new OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition position) {
                    Log.d(TAG, "Zoom: " + position.zoom);
                }
            };
        }

    } // END GoogleMapClient
}
