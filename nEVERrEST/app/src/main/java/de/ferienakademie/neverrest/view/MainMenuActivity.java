package de.ferienakademie.neverrest.view;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
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

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.controller.DatabaseHandler;
import de.ferienakademie.neverrest.controller.DatabaseUtil;
import de.ferienakademie.neverrest.controller.GPSService;
import de.ferienakademie.neverrest.controller.MetricCalculator;
import de.ferienakademie.neverrest.model.Activity;
import de.ferienakademie.neverrest.model.LocationData;
import de.ferienakademie.neverrest.model.SportsType;

import static android.view.View.OnClickListener;

public class MainMenuActivity extends FragmentActivity
        implements NeverrestInterface, ServiceConnection, OnClickListener, SensorEventListener {

    public static final int NUMBER_RECENT_POINTS = 5; // for outlier detection and smoothing
    public static final String TAG = MainMenuActivity.class.getSimpleName();
    public static final String SPORTS_TYPE = "sportsType";


    ///////// DATABASE ELEMENTS /////////
    private de.ferienakademie.neverrest.model.Activity mActivity;
    private SportsType mSportsType;


    ///////// UI ELEMENTS /////////

    private ToggleButton mBtnGPSTracking;

    ///////// MAP AND LOCATION STUFF /////////
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private LinkedList<LocationData> mLocationDataList = new LinkedList<LocationData>();
    private String mProvider;
    private Marker mMarker;
    private float mDistance = 0.f;
    long timeWhenStoppedChronometer = 0;



    ///////// NAVIGATION DRAWER STUFF /////////
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int mDrawerPosition;
    private boolean mIsCreated;

    ///// SENSOR DATA /////
    private SensorManager sensorManager;
    private Sensor accelor;
    private int mSamplingRate = 80;
    private float mSamplingTimeMillis = 1000 / mSamplingRate;
    private float mSamplingTimeMicro = 1000000 / mSamplingRate;
    private float[] mMovingAvgX = new float[4];
    private float[] mMovingAvgY = new float[4];
    private float[] mMovingAvgZ = new float[4];

    private int counterMovingAvg;
    private float[] mSamplesFilteredX = new float[30 * mSamplingRate / 4];
    private float[] mSamplesFilteredY = new float[30 * mSamplingRate / 4];
    private float[] mSamplesFilteredZ = new float[30 * mSamplingRate / 4];

    private float mSumX = 0;
    private float mSumY = 0;
    private float mSumZ = 0;

    private float burnedKcal = 0f;
    private int counterSamplesFiltered = 0;

    private TextView firstText;
    private TextView secondText;
    private TextView thirdTime;

    private Chronometer time;
    private TextView heading;
    DecimalFormat df;

   /*
    private int mRingbufferSize = mSamplingRate * 30;
    private int mCopySize = mSamplingRate * 5;


    int mBufferPosition = 0;
    int counter = 0;
    private float[] mAccXRingBuffer = new float[mRingbufferSize];
    private float[] mAccYRingBuffer = new float[mRingbufferSize];
    private float[] mAccZRingBuffer = new float[mRingbufferSize];

    private float[] mAccXProcessingBuffer = new float[mCopySize];
    private float[] mAccYProcessingBuffer = new float[mCopySize];
    private float[] mAccZProcessingBuffer = new float[mCopySize];
*/

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    private DatabaseHandler mDatabaseHandler = DatabaseUtil.INSTANCE.getDatabaseHandler();
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
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mSportsType = (SportsType) getIntent().getSerializableExtra(MainMenuActivity.SPORTS_TYPE);
        mIsCreated = true;


        mBtnGPSTracking = (ToggleButton) findViewById(R.id.btnStartGPSTracking);
        mBtnGPSTracking.setOnClickListener(this);
        firstText = (TextView) findViewById(R.id.first_textview);
        firstText.setTextSize(25f);
        time = (Chronometer) findViewById(R.id.chronometer);
        time.setTextSize(60f);
        heading = (TextView) findViewById(R.id.heading);
        secondText = (TextView) findViewById(R.id.second_textview);
        secondText.setText("0 km/h");
        secondText.setTextSize(25f);
        thirdTime = (TextView) findViewById(R.id.third_textview);
        thirdTime.setText(String.format("%d kcal",(int) burnedKcal));
        thirdTime.setTextSize(25f);

     //   heading.setText(mActivity.);

        setUpNavigationDrawer();
        setUpMapIfNeeded();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
         df = new DecimalFormat("#0.00",symbols);
        float dis = mDistance/1000;
        df.format(dis);
        firstText.setText(df.format(dis)+ " km");

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        initLocationHandler();

        if (accelor != null) {

            sensorManager.registerListener(this, accelor, 25000);

            Log.e(TAG, "requested");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpMapIfNeeded();

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
                    updateLocation(currentPosition);
                }
            } else {
                updateLocation(currentPosition);
            }
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

    public void updateLocation(LocationData currentPosition) {
        List<LocationData> recentPoints;
        if (mLocationDataList.size() >= NUMBER_RECENT_POINTS ) {
            recentPoints = mLocationDataList.subList(mLocationDataList.size() - (NUMBER_RECENT_POINTS), mLocationDataList.size() - 1);
        } else {
            recentPoints = mLocationDataList;
        }



        if (!MetricCalculator.isValid(currentPosition, recentPoints)) {
            Log.d(TAG, "Ignoring current location. Looks like an outlier");
            return;
        }

        // Smoothing
  //    currentPosition = MetricCalculator.smoothLocationData(currentPosition, recentPoints);
        if (null == currentPosition.getActivity()) {
            currentPosition.setActivity(mActivity);
        }

        // compute total distance
        int size = mLocationDataList.size();
        if (size > 1) {
            float[] tmp = new float[1];

            LocationData previous = mLocationDataList.get(size - 2);
            Location.distanceBetween(previous.getLatitude(),
                    previous.getLongitude(),
                    currentPosition.getLatitude(),
                    currentPosition.getLongitude(), tmp);

            mDistance += tmp[0];
            float dis = mDistance/1000;
            df.format(dis);
            firstText.setText(df.format(dis)+ " km");
            secondText.setText(String.format("%d km/h", (int) mLocationDataList.getLast().getSpeed()));

        }

        // save new location in database
        try {
            mDatabaseHandler.getLocationDataDao().create(currentPosition);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        mLocationDataList.add(currentPosition);

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
                // TODO this won't draw the track
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


        if (mLocationDataList != null && mLocationDataList.size() > 0) {
            LocationData current = mLocationDataList.get(mLocationDataList.size() - 1);
            LatLng currentLatLng = new LatLng(current.getLatitude(), current.getLongitude());



            if (mMap != null) {
                // draw route in map
                if (mLocationDataList.size() > 1) {
                    LocationData previous = mLocationDataList.get(mLocationDataList.size() - 2);
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
                    // Start new activity
                    time.setBase(SystemClock.elapsedRealtime() + timeWhenStoppedChronometer);
                    time.start();
                    mActivity = new Activity(UUID.randomUUID().toString(), System.currentTimeMillis(),
                            0L, "Some user id", mSportsType, null);
                    try {
                        mDatabaseHandler.getActivityDao().create(mActivity);
                    } catch (SQLException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    Intent serviceIntent = new Intent(this, GPSService.class);
                    bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
                } else {
                    // Stop tracking and finish activity
                    timeWhenStoppedChronometer = time.getBase() - SystemClock.elapsedRealtime();
                    time.stop();
                     mActivity.setDuration(System.currentTimeMillis() - mActivity.getTimestamp());
                    try {
                        mDatabaseHandler.getActivityDao().update(mActivity);
                    } catch (SQLException e) {
                        Log.e(TAG, e.getMessage());
                    }
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

    float alpha = 0.9f;
    float beta = 0.1f;
    float gx;
    float gy;
    float gz;
    float ax;
    float ay;
    float az;
    float xOld;
    float yOld;
    float zOld;

    long timeOld;
    long timeNew;
    long timeDiff;
    int timeDiffSec;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values.clone();

        if (timeOld == 0) {
            timeOld = System.nanoTime();
        }

        // LP filter
        gz = gx + beta * values[0];
        gy = gy + beta * values[1];
        gz = gz + beta * values[2];

        // raw Acceleration without g
        float xNew = values[0] - gx;
        float yNew = values[1] - gy;
        float zNew = values[2] - gz;

        ax = alpha * (ax + xNew - xOld);
        ay = alpha * (ay + yNew - yOld);
        az = alpha * (az + zNew - zOld);

        xOld = xNew;
        yOld = yNew;
        zOld = zNew;

        mMovingAvgX[counterMovingAvg] = ax;
        mMovingAvgY[counterMovingAvg] = ay;
        mMovingAvgZ[counterMovingAvg] = az;


        counterMovingAvg++;

        if (counterMovingAvg == 3) {
            counterMovingAvg = 0;

            mSamplesFilteredX[counterSamplesFiltered]
                    = 0.25f * (mMovingAvgX[0] + mMovingAvgX[1] + mMovingAvgX[2] + mMovingAvgX[3]);
            if (mSamplesFilteredX[counterSamplesFiltered] < 0.15f) {
                mSamplesFilteredX[counterSamplesFiltered] = 0.0f;
            }
            mSamplesFilteredY[counterSamplesFiltered]
                    = 0.25f * (mMovingAvgY[0] + mMovingAvgY[1] + mMovingAvgY[2] + mMovingAvgY[3]);
            if (mSamplesFilteredY[counterSamplesFiltered] < 0.15f) {
                mSamplesFilteredY[counterSamplesFiltered] = 0.0f;
            }
            mSamplesFilteredZ[counterSamplesFiltered]
                    = 0.25f * (mMovingAvgZ[0] + mMovingAvgZ[1] + mMovingAvgZ[2] + mMovingAvgZ[3]);
            if (mSamplesFilteredZ[counterSamplesFiltered] < 0.15f) {
                mSamplesFilteredZ[counterSamplesFiltered] = 0.0f;
            }
            Log.d(TAG, String.valueOf(mSamplesFilteredX[counterSamplesFiltered]) + ", "
                    + String.valueOf(mSamplesFilteredY[counterSamplesFiltered]) + ", "
                    + String.valueOf(mSamplesFilteredZ[counterSamplesFiltered]));
            counterSamplesFiltered++;
        }

        if (counterSamplesFiltered == mSamplesFilteredX.length - 1) {

            if (timeOld != 0) {
                timeNew = System.nanoTime();
                timeDiff = (int) ((timeNew - timeOld) / 1000000000);
                Log.d(TAG, String.valueOf(timeDiff));
                timeOld = timeNew;

            }

            for (int i = 0; i < mSamplesFilteredX.length; i++) {
                mSumX += Math.abs(mSamplesFilteredX[i]);
                mSumY += Math.abs(mSamplesFilteredY[i]);
                mSumZ += Math.abs(mSamplesFilteredZ[i]);
            }
            float tmp = mSumX + mSumY + mSumZ;

            // W * kg^-1

            double energyExpEstVal = 0.104 + 0.023 * tmp;
//78 male; statistisches Bundesamt, 62,6 kg female
            // user.getMass();
            // user.getMale();

            burnedKcal += ((70.7 * tmp/timeDiff) / 4.1868) / 1000;

            mSumX = 0;
            mSumY = 0;
            mSumZ = 0;
            counterSamplesFiltered = 0;

            thirdTime.setText(String.format("%d kcal",(int) burnedKcal));



        }

     /*   mAccXRingBuffer[mBufferPosition] = values[0];
        mAccYRingBuffer[mBufferPosition] = values[1];
        mAccZRingBuffer[mBufferPosition] = values[2];
        mBufferPosition = (mBufferPosition + 1) % mRingbufferSize;
        counter++;

        if (counter == mCopySize) {
            counter = 0;
            if ((mBufferPosition - mCopySize) >= 0) {
                System.arraycopy(mAccXRingBuffer, (mBufferPosition - mCopySize), mAccXProcessingBuffer, 0, mCopySize);
                System.arraycopy(mAccYRingBuffer, (mBufferPosition - mCopySize), mAccYProcessingBuffer, 0, mCopySize);
                System.arraycopy(mAccZRingBuffer, (mBufferPosition - mCopySize), mAccZProcessingBuffer, 0, mCopySize);

            }else{
                System.arraycopy(mAccXRingBuffer,mRingbufferSize-(mCopySize-mBufferPosition),mAccXProcessingBuffer,0,(mCopySize-mBufferPosition));
                System.arraycopy(mAccXRingBuffer,0,mAccXProcessingBuffer,(mCopySize-mBufferPosition),mBufferPosition);

                System.arraycopy(mAccYRingBuffer,mRingbufferSize-(mCopySize-mBufferPosition),mAccYProcessingBuffer,0,(mCopySize-mBufferPosition));
                System.arraycopy(mAccYRingBuffer,0,mAccYProcessingBuffer,(mCopySize-mBufferPosition),mBufferPosition);

                System.arraycopy(mAccZRingBuffer,mRingbufferSize-(mCopySize-mBufferPosition),mAccZProcessingBuffer,0,(mCopySize-mBufferPosition));
                System.arraycopy(mAccZRingBuffer,0,mAccZProcessingBuffer,(mCopySize-mBufferPosition),mBufferPosition);
            }

        }
*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeWhenStoppedChronometer = 0;
        if (accelor != null) {
            sensorManager.unregisterListener(this);
        }
    }

}