package de.ferienakademie.neverrest.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by explicat on 9/25/14.
 */

public class LocationData extends SensorData {
;
    private Location mLocation;

    public LocationData(Location location) {
        setCreationDate(new Date());
        this.mLocation = location;
    }

    public LocationData(Location location, Date creationDate) {
        setCreationDate(creationDate);
        this.mLocation = location;
    }

    public LatLng getLatLng() {
        return new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
    }

    public void setLatLng(Location location) {
        this.mLocation = location;
    }

    public Location getmLocation() {
        return mLocation;
    }

}
