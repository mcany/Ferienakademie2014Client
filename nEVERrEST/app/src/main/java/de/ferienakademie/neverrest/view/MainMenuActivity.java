package de.ferienakademie.neverrest.view;

import android.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.GPSService;
import de.ferienakademie.neverrest.controller.MetricCalculator;
import de.ferienakademie.neverrest.model.LocationData;

import static android.view.View.OnClickListener;

public class MainMenuActivity extends FragmentActivity
        implements NeverrestInterface, ServiceConnection, OnClickListener {

    public static final int NUMBER_RECENT_POINTS = 5; // for outlier detection and smoothing
    public static final String TAG = MainMenuActivity.class.getSimpleName();


    ///////// DATABASE ELEMENTS /////////
    private de.ferienakademie.neverrest.model.Activity mActivity;


    ///////// UI ELEMENTS /////////
    private TextView mCoordinateView;
    private TextView mDistanceView;
    private TextView mSpeedView;
    private TextView mAltitudeView;
    private ToggleButton mBtnGPSTracking;

    ///////// MAP AND LOCATION STUFF /////////
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private LinkedList<LocationData> mLocationDataList = new LinkedList<LocationData>();
    private long lastShown = -1;
    private String mProvider;
    private Marker mMarker;
    private float[] mDistance = new float[1];


    ///////// NAVIGATION DRAWER STUFF /////////
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int mDrawerPosition;
    private boolean mIsCreated;

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
                    Location location = (Location) msg.obj;
                    LocationData currentPosition = new LocationData(new Date(), location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed());
                    updateLocation(currentPosition);
                    updateTextView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mIsCreated = true;

        mCoordinateView = (TextView) findViewById(R.id.coordinates);
        mAltitudeView = (TextView) findViewById(R.id.altitude);
        mDistanceView = (TextView) findViewById(R.id.distance);
        mSpeedView = (TextView) findViewById(R.id.speed);
        mBtnGPSTracking = (ToggleButton) findViewById(R.id.btnStartGPSTracking);
        mBtnGPSTracking.setOnClickListener(this);

        setUpNavigationDrawer();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initLocationHandler();

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

    private void initLocationHandler() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mProvider = LocationManager.GPS_PROVIDER;
        } else {
            mProvider = LocationManager.NETWORK_PROVIDER;
        }

        // initialize LocationManager => last Location
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(mProvider);
        if (lastKnownLocation != null) {
            LocationData currentPosition = new LocationData(new Date(), lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAltitude(), lastKnownLocation.getSpeed());
            if (mLocationDataList.size() > 0) {
                LocationData previous = mLocationDataList.getLast();
                if (previous.getLatitude() != currentPosition.getLatitude()
                        || previous.getLongitude() != currentPosition.getLongitude()) {
                    mLocationDataList.add(currentPosition);

                }
            } else {
                mLocationDataList.add(currentPosition);
            }
        }
        updateTextView();
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


    public void updateLocation(LocationData currentPosition) {

        List<LocationData> recentPoints;
        if (mLocationDataList.size() >= 5) {
            recentPoints = mLocationDataList.subList(mLocationDataList.size() - (NUMBER_RECENT_POINTS + 1), mLocationDataList.size() - 1);
        } else {
            recentPoints = mLocationDataList;
        }

        if (!MetricCalculator.isValid(currentPosition, recentPoints)) {
            Log.d(TAG, "Ignoring current location");
            return;
        }

        // Smoothing
        currentPosition = MetricCalculator.smoothLocationData(currentPosition, recentPoints);

        // compute total distance
        int size = mLocationDataList.size();
        if (size > 1) {
            float[] tmp = new float[1];

            LocationData previous = mLocationDataList.get(size - 2);
            Location.distanceBetween(previous.getLatitude(),
                    previous.getLongitude(),
                    currentPosition.getLatitude(),
                    currentPosition.getLongitude(), tmp);

            mDistance[0] += tmp[0];
        }

        // save new location in database
        try {
            mDatabaseHandler.getLocationDataDao().create(currentPosition);
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
        if (mLocationDataList != null && mLocationDataList.size() > 1) {
            LocationData previous = mLocationDataList.get(mLocationDataList.size() - 2);
            LocationData current = mLocationDataList.get(mLocationDataList.size() - 1);
            LatLng currentLatLng = new LatLng(current.getLatitude(), current.getLongitude());

            Toast.makeText(this, String.valueOf(current.getLatitude()) +
                            ", " + String.valueOf(current.getLongitude()),
                    Toast.LENGTH_SHORT).show();

            if (mMap != null) {
                // draw route in map
                if (mLocationDataList.size() > 1) {
                    mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(previous.getLatitude(), previous.getLongitude()),
                                    currentLatLng)
                            .color(Color.parseColor("#3f51b5")).width(20));
                }

                // update Marker position
                if (mMarker == null) {
                    mMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
                } else {
                    mMarker.setPosition(currentLatLng);
                }

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                // update Camera
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLatLng).tilt(75)
                        .zoom(16).build();
                // .bearing(mLocation.getBearing())
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }

    }

    private void updateTextView() {
        if (null != mLocationDataList && mLocationDataList.size() > 0) {
            LocationData currentPosition = mLocationDataList.getLast();

            mAltitudeView.setText("Altitude: " + currentPosition.getAltitude());
            mCoordinateView.setText("Latitude: " + currentPosition.getLatitude() + ", Longitude: " + currentPosition.getLongitude());
            mSpeedView.setText("Speed: " + currentPosition.getSpeed());
            mDistanceView.setText("Distance: " + mDistance[0]);
        }
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
                Log.d(TAG, "Toggle Button pressed.");
                if (mBtnGPSTracking.isChecked()) {
                    Intent serviceIntent = new Intent(this, GPSService.class);
                    bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
                } else {
                    unbindService(this);
                }
                break;
        }
    }

    @Override
    public void setUpNavigationDrawer() {
        mDrawerPosition = getIntent().getIntExtra(Constants.EXTRA_POSITION, -1);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer_main);
        mNavigationDrawerFragment.setPosition(mDrawerPosition);

        // Set up the drawer
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer_main,
                (DrawerLayout) findViewById(R.id.drawer_layout_main));
        onNavigationDrawerItemSelected(mDrawerPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mDrawerPosition = position;

        if (mIsCreated) {
            // update the main content by replacing fragments
            switch (position) {
                case Constants.ACTIVITY_MAIN_MENU:
                    Intent mainIntent = new Intent(this, FindChallengesActivity.class);
                    mainIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(mainIntent);
                    this.finish();
                    break;
                case Constants.ACTIVITY_PROFILE:
                    mTitle = getString(R.string.title_navigation_profile);
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    profileIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(profileIntent);
                    this.finish();
                    break;
                case Constants.ACTIVITY_CHALLENGE_OVERVIEW:
                    Intent challengeIntent = new Intent(this, ActiveChallengesActivity.class);
                    challengeIntent.putExtra(Constants.EXTRA_POSITION, position);
                    startActivity(challengeIntent);
                    this.finish();
                    break;
            }
        }
    }

    @Override
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

}
