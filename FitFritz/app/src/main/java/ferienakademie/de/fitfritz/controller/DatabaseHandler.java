package ferienakademie.de.fitfritz.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ferienakademie.de.fitfritz.model.LocationData;

/**
 * Created by isabelmax on 26.09.14.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String LOGCAT = "logcat";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SensorData";
    private static final String[] TABLE_NAMES = {"GPSData", "HeartRateData"};

    //columns gps data table
    private static final String COL_ID = "id";
    private static final String COL_TIMESTAMP = "timeStamp";
    private static final String COL_LONGITUDE = "longitude";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_ALTITUDE = "altitude";
    private static final String COL_SPEED = "speed";
    private static final String COL_SYNC_SERVER = "synchronized";

    private static final String[] COLUMS_GPSDATA = {COL_ID, COL_TIMESTAMP, COL_LONGITUDE, COL_LATITUDE, COL_ALTITUDE, COL_SPEED, COL_SYNC_SERVER};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GPS_TABLE = "CREATE TABLE " + TABLE_NAMES[0] + " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TIMESTAMP + " INTEGER, " + COL_LONGITUDE + " REAL, " + COL_LATITUDE
                + " REAL, " + COL_ALTITUDE + " REAL, " + COL_SPEED + " REAL, " + COL_SYNC_SERVER + " INTEGER);";
        sqLiteDatabase.execSQL(CREATE_GPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // drop older database table if exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES[0]);

        // create new database table
        this.onCreate(sqLiteDatabase);
    }

    // insert a list of GPS Data to the database
    public void insertGPSDate(LocationData gpsData) {

        String insertStatement = "INSERT INTO " + TABLE_NAMES[0] + "(" ;
        for (int i = 1; i < COLUMS_GPSDATA.length; i++) {
           if(i > 1) {
               insertStatement += ", ";
           }
            insertStatement += COLUMS_GPSDATA[i];
        }
        insertStatement += ") VALUES (";

        // creation date in seconds (not ms!!) since 01.01.1970
        insertStatement += gpsData.getCreationDate().getTime()/1000 + ", ";
        insertStatement += gpsData.getLatLng().longitude + ", ";
        insertStatement += gpsData.getLatLng().latitude + ", ";
        insertStatement += gpsData.getmLocation().getAltitude() + ", ";
        insertStatement += gpsData.getmLocation().getSpeed() + ", ";
        insertStatement += "0);";
        
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(insertStatement);
    }


    public List<LocationData> getUnsyncedLocationData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAMES[0], COLUMS_GPSDATA, COL_SYNC_SERVER + " = 0", new String[]{}, null, null, null);
        cursor.moveToFirst();
        List<LocationData> returnedList = new LinkedList<LocationData>();
        try {
            while(!cursor.isAfterLast()) {
                int timeStamp = cursor.getInt(1);
                double longitude = cursor.getDouble(2);
                double latitude = cursor.getDouble(3);
                double altitude = cursor.getDouble(4);
                double speed = cursor.getDouble(5);
                int isSyncedInt = cursor.getInt(6);

                Location location = new Location("");
                location.setLongitude(longitude);
                location.setLatitude(latitude);
                location.setAltitude(altitude);
                location.setSpeed((float) speed);
                Date creationDate = new Date(timeStamp * 1000);

                LocationData locationData = new LocationData(location, creationDate);
                returnedList.add(locationData);

                Log.d(LOGCAT, "Time: " + timeStamp + " Long: " + longitude + " Lat: " + latitude + " Alt: " + altitude + " Speed: " + speed + " syncstatus: " + (isSyncedInt == 1));
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return returnedList;
    }

}
