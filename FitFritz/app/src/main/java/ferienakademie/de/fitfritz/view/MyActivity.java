package ferienakademie.de.fitfritz.view;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

import ferienakademie.de.fitfritz.R;
import ferienakademie.de.fitfritz.controller.DatabaseHandler;
import ferienakademie.de.fitfritz.model.LocationData;

public class MyActivity extends Activity implements LocationListener {

    public static final String TAG = MyActivity.class.getSimpleName();

    private LocationManager mLocationManager;
    private Location mLocation;
    private LatLng mLatLng;
    private LinkedList<Location> mLocationList;
    private LinkedList<LatLng> mLatLngList;
    private String mProvider;
    private LatLng mMarker;
    private float[] mDistance;
    private float mVelocity;
    private double mAltitude;

    private TextView mCoordinateView;
    private TextView mDistanceView;
    private TextView mSpeedView;
    private TextView mAltitudeView;
    private float mSpeed;

    private DatabaseHandler mDatabaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mCoordinateView = (TextView) findViewById(R.id.coordinates);
        mAltitudeView = (TextView) findViewById(R.id.altitude);
        mDistanceView = (TextView) findViewById(R.id.distance);
        mSpeedView = (TextView) findViewById(R.id.speed);

        mDatabaseHandler = new DatabaseHandler(getBaseContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        handleLocation();

        mLocationManager.requestLocationUpdates(mProvider, 50, 0, this);
        Log.e(TAG, "requested");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
        Log.e(TAG, "removed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void handleLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

            updateTextView(String.valueOf(mAltitude), String.valueOf(mLocation.getLatitude()),
                    String.valueOf(mLocation.getLongitude()),
                    String.valueOf(mSpeed), String.valueOf(mDistance[0]));

        } else {
            updateTextView(String.valueOf(0), String.valueOf(0), String.valueOf(0),
                    String.valueOf(0), String.valueOf(0));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

        Log.e(TAG, String.valueOf(mLatLng.latitude) + ", " + String.valueOf(mLatLng.longitude));

        mVelocity = mLocation.getSpeed();
        mAltitude = mLocation.getAltitude();

        mLocationList.add(mLocation);
        mLatLngList.add(mLatLng);

        // compute total distance
        int size = mLocationList.size();
        if(size > 1) {
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

        updateTextView(String.valueOf(mAltitude), String.valueOf(mLocation.getLatitude()),
                String.valueOf(mLocation.getLongitude()),
                String.valueOf(mSpeed), String.valueOf(mDistance[0]));
    }

    private void updateTextView(String alt, String lat, String lon, String speed, String dist) {

        mAltitudeView.setText(String.format(mAltitudeView.getText().toString(), alt));
        mCoordinateView.setText(String.format(mCoordinateView.getText().toString(), lat, lon));
        mSpeedView.setText(String.format(mSpeedView.getText().toString(), speed));
        mDistanceView.setText(String.format(mDistanceView.getText().toString(), dist));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        if (s.equals(LocationManager.GPS_PROVIDER)) {
            mProvider = s;
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        if(s.equals(LocationManager.GPS_PROVIDER)) {
            mProvider = LocationManager.NETWORK_PROVIDER;
        }
    }
}
