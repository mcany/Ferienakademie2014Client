package de.ferienakademie.neverrest.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.controller.GPSService;
import de.ferienakademie.neverrest.controller.MetricCalculator;
import de.ferienakademie.neverrest.model.LocationData;
import de.ferienakademie.neverrest.model.SportsType;

import static android.view.View.OnClickListener;
import static de.ferienakademie.neverrest.view.NavigationDrawerFragment.NavigationDrawerCallbacks;

public class MainActivity extends FragmentActivity
        implements ServiceConnection, OnClickListener, NavigationDrawerCallbacks {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int NUMBER_RECENT_POINTS = 5; // for outlier detection and smoothing

    ///////// DATABASE ELEMENTS /////////
    private de.ferienakademie.neverrest.model.Activity mActivity;



    ///////// UI ELEMENTS /////////
    private TextView mCoordinateView;
    private TextView mDistanceView;
    private TextView mSpeedView;
    private TextView mAltitudeView;
    private ToggleButton mBtnGPSTracking;
    private Button mNewButton;

    ///////// MAP AND LOCATION STUFF /////////
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private Location mLocation;
    private LatLng mLatLng;
    private LinkedList<Location> mLocationList;
    private LinkedList<LatLng> mLatLngList;
    private LinkedList<LocationData> mLocationDataList = new LinkedList<LocationData>();
    private String mProvider;
    private Marker mMarker;
    private float[] mDistance;
    private double mAltitude;
    private float mSpeed;

    ///////// NAVIGATION DRAWER STUFF /////////
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    private DatabaseHandler mDatabaseHandler;
    private GPSService mLocationService;
    private Handler mUIHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GPSService.MSG_GPSDATA:
                    Log.d(TAG, "Got new GPS data!");
                    mLocation = (Location) msg.obj;
                    updateLocation();
                    updateTextView();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Initialize database
        DatabaseUtil.INSTANCE.initialize(getApplicationContext());

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        // Initialize database
        DatabaseUtil.INSTANCE.initialize(getApplicationContext());
        mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();


        mCoordinateView = (TextView) findViewById(R.id.coordinates);
        mAltitudeView = (TextView) findViewById(R.id.altitude);
        mDistanceView = (TextView) findViewById(R.id.distance);
        mSpeedView = (TextView) findViewById(R.id.speed);
        mBtnGPSTracking = (ToggleButton) findViewById(R.id.btnStartGPSTracking);
        mBtnGPSTracking.setOnClickListener(this);
        mNewButton = (Button) findViewById(R.id.newButton);
        mNewButton.setOnClickListener(this);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();

        handleLocation();

        Log.e(TAG, "requested");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpMapIfNeeded();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void handleLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mProvider = LocationManager.GPS_PROVIDER;
        } else {
            mProvider = LocationManager.NETWORK_PROVIDER;
        }

        mLocationList = new LinkedList<Location>();
        mLatLngList = new LinkedList<LatLng>();
        mDistance = new float[1];

        // initialize LocationManager => last Location
        mLocation = mLocationManager.getLastKnownLocation(mProvider);

        if (mLocation != null) {
            mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            mLocationList.add(mLocation);
            mLatLngList.add(mLatLng);

            mAltitude = mLocation.getAltitude();
            mSpeed = mLocation.getSpeed() * 3.6f;

            updateTextView();

        } else {
            updateTextView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateLocation() {
        LocationData current = new LocationData(mLocation);

        List<LocationData> recentPoints;
        if (mLocationDataList.size() >= 5) {
            recentPoints = mLocationDataList.subList(mLocationDataList.size() - (NUMBER_RECENT_POINTS + 1), mLocationDataList.size() - 1);
        } else {
            recentPoints = mLocationDataList;
        }

        if (! MetricCalculator.isValid(current, recentPoints)) {
            Log.d(TAG, "Ignoring current location");
            return;
        }

        // TODO smoothing

        mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

        Log.d(TAG, String.valueOf(mLatLng.latitude) + ", " + String.valueOf(mLatLng.longitude));

        mSpeed = mLocation.getSpeed();
        mAltitude = mLocation.getAltitude();

        mLocationList.add(mLocation);
        mLatLngList.add(mLatLng);

        // compute total distance
        int size = mLocationList.size();
        if (size > 1) {
            float[] tmp = new float[1];

            Location.distanceBetween(mLatLngList.get(size - 2).latitude,
                    mLatLngList.get(size - 2).longitude,
                    mLatLngList.get(size - 1).latitude,
                    mLatLngList.get(size - 1).longitude, tmp);
            mDistance[0] += tmp[0];
        }

        // save new location in database
        try {
            LocationData mLocationData = new LocationData(mLocation);
            mLocationData.setActivity(mActivity);
            mDatabaseHandler.getLocationDataDao().create(mLocationData);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }

        updateTextView();
        updateMap();
    }

    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                updateMap();

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void updateMap() {
        if (mLatLng != null) {
            Toast.makeText(this, String.valueOf(mLatLng.latitude) +
                            ", " + String.valueOf(mLatLng.longitude),
                    Toast.LENGTH_SHORT).show();

            if (mMap != null) {
                // draw route in map
                if (mLocationList.size() > 1) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(mLatLngList.get(mLatLngList.size() - 2), mLatLng)
                            .color(Color.parseColor("#3f51b5")).width(20));
                }

                // update Marker position
                if (mMarker == null) {
                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(mLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
                } else {
                    mMarker.setPosition(mLatLng);
                }

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                // update Camera
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(mLatLng).tilt(75).bearing(mLocation.getBearing())
                        .zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }

    }

    private void updateTextView() {
        mAltitudeView.setText("Altitude: " + mAltitude);
        mCoordinateView.setText("Latitude: " + mLocation.getLatitude() + ", Longitude: " + mLocation.getLongitude());
        mSpeedView.setText("Speed: " + mSpeed);
        mDistanceView.setText("Distance: " + mDistance[0]);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        GPSService.GPSServiceBinder binder = (GPSService.GPSServiceBinder) iBinder;
        mLocationService = binder.getService();
        mLocationService.connectLayoutHandler(mUIHandler);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mLocationService = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartGPSTracking:
                Log.d(
                        TAG, "Toggle Button pressed.");
                long startingTime = 0;
                if (mBtnGPSTracking.isChecked()) {
                    Intent serviceIntent = new Intent(this, GPSService.class);
                    bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
                    startingTime = System.currentTimeMillis();
                    mActivity = new de.ferienakademie.neverrest.model.Activity(UUID.randomUUID().toString(), startingTime, 0.d, 0.d, 0.d, "", SportsType.RUNNING);

                    try {
                        mDatabaseHandler.getActivityDao().create(mActivity);
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                    }

                    unbindService(this);
                    long duration = System.currentTimeMillis()-startingTime;
                    mActivity.setDuration(((double) duration));
                    try {
                        mDatabaseHandler.getActivityDao().update(mActivity);
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                    }
                }
                break;
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
