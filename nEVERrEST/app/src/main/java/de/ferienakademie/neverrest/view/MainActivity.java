package de.ferienakademie.neverrest.view;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.GPSService;
import de.ferienakademie.neverrest.model.LocationData;

import static android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements ServiceConnection, OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private LocationManager mLocationManager;
    private Location mLocation;
    private LatLng mLatLng;
    private LinkedList<Location> mLocationList;
    private LinkedList<LatLng> mLatLngList;
    private String mProvider;
    private Marker mMarker;
    private float[] mDistance;
    private float mVelocity;
    private double mAltitude;

    private TextView mCoordinateView;
    private TextView mDistanceView;
    private TextView mSpeedView;
    private TextView mAltitudeView;
    private ToggleButton mBtnGPSTracking;
    private Button mNewButton;
    private float mSpeed;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinateView = (TextView) findViewById(R.id.coordinates);
        mAltitudeView = (TextView) findViewById(R.id.altitude);
        mDistanceView = (TextView) findViewById(R.id.distance);
        mSpeedView = (TextView) findViewById(R.id.speed);
        mBtnGPSTracking = (ToggleButton) findViewById(R.id.btnStartGPSTracking);
        mBtnGPSTracking.setOnClickListener(this);
        mNewButton = (Button) findViewById(R.id.newButton);
        mNewButton.setOnClickListener(this);

        mDatabaseHandler = new DatabaseHandler(this);

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
        Log.e(TAG, "removed");
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
            mVelocity = mLocation.getSpeed() * 3.6f;
            mLocationList.add(mLocation);
            mLatLngList.add(mLatLng);

            mAltitude = mLocation.getAltitude();
            mSpeed = mLocation.getSpeed();

            updateTextView();

        } else {
            updateTextView();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

        Log.e(TAG, String.valueOf(mLatLng.latitude) + ", " + String.valueOf(mLatLng.longitude));

        mVelocity = mLocation.getSpeed();
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
        mAltitude = mLocation.getAltitude();
        mSpeed = mLocation.getSpeed();

        // save new location in database
        mDatabaseHandler.insertGPSDate(new LocationData(mLocation));

        updateTextView();
        updateMap();
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
                if (mMarker != null) {
                    mMarker.setPosition(mLatLng);
                }

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                // update Camera
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(mLatLng).tilt(75).bearing(mLocation.getBearing())
                        .zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.addMarker(new MarkerOptions().position(
                        mLatLng));

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
                Log.d(TAG, "Toggle Button pressed.");
                if (mBtnGPSTracking.isChecked()) {
                    Intent serviceIntent = new Intent(this, GPSService.class);
                    bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
                } else {
                    unbindService(this);
                }
                break;
            case R.id.newButton:
                startActivity(new Intent(this, MyActivity.class));
        }
    }
}
