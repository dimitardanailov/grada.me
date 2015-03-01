package grada.me;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Class will store methods valid for another activities. 
 */
public class DefaultActivity extends FragmentActivity {

    // Get class name
    public static String TAG = DefaultActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
