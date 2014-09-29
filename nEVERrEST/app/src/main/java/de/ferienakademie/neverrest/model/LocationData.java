package de.ferienakademie.neverrest.model;

import android.location.Location;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by explicat on 9/25/14.
 */

@DatabaseTable(tableName = LocationData.TABLE_LOCATION_DATA)
public class LocationData extends SensorData {

    public static final String TABLE_LOCATION_DATA = "locationData";
    public static final String COL_ID = "id";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_SPEED = "speed";
    public static final String COL_ALTITUDE = "altitude";

    @DatabaseField(generatedId = true, columnName = COL_ID)
    private long id;

    @DatabaseField(columnName = COL_LATITUDE)
    private double latitude;

    @DatabaseField(columnName = COL_LONGITUDE)
    private double longitude;

    @DatabaseField(columnName = COL_SPEED)
    private float speed;

    @DatabaseField(columnName = COL_ALTITUDE)
    private double altitude;

    public LocationData() {
        // ORMLite needs a no-arg constructor
    }


    public LocationData(Location location) {
        setCreationDate(new Date());
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.speed = location.getSpeed();
        this.altitude = location.getAltitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
