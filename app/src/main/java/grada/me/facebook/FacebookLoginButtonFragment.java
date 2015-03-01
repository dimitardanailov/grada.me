package grada.me.facebook;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import grada.me.R;

/**
 * Source: https://developers.facebook.com/docs/facebook-login/android/v2.2
 * 
 * To set up the button in your UI, create a new class named 
 * {Fragment Class Name} that's a subclass of the Fragment class
 */
public class FacebookLoginButtonFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        // Get Layout Information and inflate view
        View view = inflater.inflate(R.layout.facebook_login_button_fragment, container, false);

        return view;
    }
}
