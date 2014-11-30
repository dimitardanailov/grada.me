package grada.me;

import android.app.Activity;
import android.content.Intent;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.util.ArrayList;

import libraries.GooglePlus.Enums.GooglePlusStates;
import libraries.GooglePlus.GooglePlusHelper;

public class GradaMeMainActivity extends Activity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
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
    // Google Plus
    private GooglePlusApiClient googlePlusApiClient = new GooglePlusApiClient();
    private GooglePlusHelper googlePlusHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grada_me_main);

        initializeLayoutElements();

        googlePlusHelper = new GooglePlusHelper(this);

        mGoogleApiClient = googlePlusHelper.buildGoogleApiClient();

        Activity activity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(activity)
                .addOnConnectionFailedListener(activity)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                googlePlusApiClient.OnActivityResult(requestCode, resultCode, data);
                break;
        }
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

    private void initializeClickListeners(View view) {

        // Get view identifier.
        Integer identifierId = view.getId();
    }

    /**
     * Operation with all Google Plus iterations.
     */
    public class GooglePlusApiClient {

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
    }
}
