package de.ferienakademie.neverrest.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import de.ferienakademie.neverrest.model.Activity;
import de.ferienakademie.neverrest.model.Challenge;
import de.ferienakademie.neverrest.model.LocationData;
import de.ferienakademie.neverrest.model.User;

/**
 * Created by explicat on 9/28/14.
 */
public class DatabaseHandler extends OrmLiteSqliteOpenHelper {

    public final String TAG = DatabaseHandler.class.getSimpleName();

	private final Context context;

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "de.ferienakademie.neverrest.android.database";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 8;

    // the DAO object we use to access the SimpleData table
    private Dao<LocationData, Long> locationDataDao = null;
    private Dao<Activity, String> activityDao = null;
    private Dao<User, String> userDao = null;
    private Dao<Challenge, String> challengeDao = null;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
    }
    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate");
            TableUtils.createTable(connectionSource, Activity.class);
            TableUtils.createTable(connectionSource, LocationData.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Challenge.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }

        // here we try inserting data in the on-create as a test
        /*
        try {
            Dao<LocationData2, Long> dao = getLocationDataDao();
            // create some entries in the onCreate
            LocationData2 data = new LocationData2(new Location("sample location 1"));
            dao.create(data);
            data = new LocationData2(new Location("sample location 2"));
            dao.create(data);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        */
    }


    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade");

            TableUtils.dropTable(connectionSource, LocationData.class, true);
            TableUtils.dropTable(connectionSource, Activity.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Challenge.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns the Database Access Object (DAO) for our LocationData class. It will create it or just give the cached
     * value.
     */
    public Dao<LocationData, Long> getLocationDataDao() throws SQLException {
        if (locationDataDao == null) {
            locationDataDao = getDao(LocationData.class);
        }
        return locationDataDao;
    }


    /**
     * Returns the Database Access Object (DAO) for our LocationData class. It will create it or just give the cached
     * value.
     */
    public Dao<Activity, String> getActivityDao() throws SQLException {
        if (activityDao == null) {
            activityDao = getDao(Activity.class);
        }
        return activityDao;
    }


    /**
     * Returns the Database Access Object (DAO) for our LocationData class. It will create it or just give the cached
     * value.
     */
    public Dao<User, String> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }


    /**
     * Returns the Database Access Object (DAO) for our Challenge class. It will create it or just give the cached
     * value.
     */
    public Dao<Challenge, String> getChallengeDao() throws SQLException {
        if (challengeDao == null) {
            challengeDao = getDao(Challenge.class);
        }
        return challengeDao;
    }


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        locationDataDao = null;
        activityDao = null;
        userDao = null;
        challengeDao = null;
    }
}