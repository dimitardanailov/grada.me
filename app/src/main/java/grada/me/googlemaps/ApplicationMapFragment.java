package grada.me.googlemaps;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

import grada.me.dialogs.ApplicationDialogFragment;

/**
 * Class will be created follow:
 * - http://stackoverflow.com/questions/19108843/mapfragment-return-null
 * - https://developer.android.com/google/play-services/index.html
 * - http://developer.android.com/reference/com/google/android/gms/maps/MapFragment.html#getMapAsync(com.google.android.gms.maps.OnMapReadyCallback)
 * 
 * And will support applications with API version higher from 11 Version
 */
public class ApplicationMapFragment extends MapFragment {

    // Reference to activity calling ApplicationMapFragment
    private Activity activity;
    
    private MapWrapperLayout mMapWrapperLayout;
    private View mOriginalContentView;
    
    public ApplicationMapFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Create new Inflate view
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
        mMapWrapperLayout = new MapWrapperLayout(this.activity);
        mMapWrapperLayout.addView(mOriginalContentView);

        return mMapWrapperLayout;
    }

    public void setOnDragListener(MapWrapperLayout.OnDragListener onDragListener) {
        mMapWrapperLayout.setOnDragListener(onDragListener);
    }
    
    /**
     * Initialize default Google Maps Options for our Application
     * @return GoogleMapOptions
     */
    public GoogleMapOptions initializeGoogleMapsOptions() {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL);
        
        return googleMapOptions;
    }

    /**
     * Load MapFragment Configurations and add Map to our Activity
     * @param drawableId - Layout Drawable id resource
     * @param mapFragmentTag - name of Map Fragment*
     */
    public void initializeFragmentTransaction(Integer drawableId, String mapFragmentTag) {
        // Run a new FragmentTransaction.
        FragmentTransaction fragmentTransaction = this.activity.getFragmentManager().beginTransaction();
        fragmentTransaction.add(drawableId, this, mapFragmentTag);
        fragmentTransaction.commit();
    }
}
