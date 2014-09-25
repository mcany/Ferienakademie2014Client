package ferienakademie.de.fitfritz.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import ferienakademie.de.fitfritz.model.LocationData;

/**
 * Created by explicat on 9/25/14.
 */
public class GPSService extends Service implements LocationListener {

    public final String TAG = GPSService.class.getSimpleName();


    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private LocationManager mLocationManager;
    private String provider = LocationManager.GPS_PROVIDER;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean enabled = mLocationManager.isProviderEnabled(provider);
        if (!enabled) {
            Intent aux = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            aux.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(aux);
        }


        mLocationManager.requestLocationUpdates(provider, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
        return START_NOT_STICKY;
    }


    @Override
    public void onLocationChanged(Location location) {
        LocationData locationData = new LocationData(location);
        Log.e(TAG, String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));

        // TODO saveData
    }


    @Override
    public void onProviderEnabled(String s) {

    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please enable GPS " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {  }
}
