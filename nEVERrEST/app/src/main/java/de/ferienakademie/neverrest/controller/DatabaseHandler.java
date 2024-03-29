package de.ferienakademie.neverrest.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.Activity;
import de.ferienakademie.neverrest.model.Challenge;
import de.ferienakademie.neverrest.model.LocationData;
import de.ferienakademie.neverrest.model.MetricType;
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
		addDistanceChallenges(context);
		addHeightChallenges(context);
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


	private void addDistanceChallenges(Context context) {
		List<Challenge> challenges = new LinkedList<Challenge>();
		challenges.add(new Challenge.Builder()
				.title("Trans-Siberian Railway")
				.timestampStarted(123456789)
				.completedEffort(5000)
				.description("Choo Choo")
				.totalEffort(9228)
				.continentName(context.getString(R.string.continent_asia))
				.lat(54.5638511)
				.lon(100.5932779)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.transsibirische_eisenbahn)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Great Wall of China")
				.description("We are not planning to build a wall")
				.finished(true)
				.totalEffort(8850)
				.continentName(context.getString(R.string.continent_asia))
				.timestampStarted(1413129)
				.timestampLastModified(1832119)
				.lat(40.431908)
				.lon(116.570375)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.chinesische_mauer)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Jacob's Trail")
				.description("Don't cry, walk!")
				.totalEffort(6950)
				.continentName(context.getString(R.string.continent_europe))
				.lat(-33.391868)
				.lon(-70.6059235)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.jakosweg)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Sahara-Crossing")
				.description("Sweat like a camel")
				.totalEffort(4800)
				.continentName(context.getString(R.string.continent_africa))
				.lat(22.2243761)
				.lon(22.1154785)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.sahara)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Route 66")
				.description("= approx 8.12404")
				.totalEffort(3939)
				.continentName(context.getString(R.string.continent_north_america))
				.lat(39.0576271)
				.lon(-89.7508269)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.route_66)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Tour de France")
				.description("Without doping to the peak")
				.totalEffort(3663)
				.continentName(context.getString(R.string.continent_europe))
				.lat(48.8588589)
				.lon(2.3470599)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.tour_de_france)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Bike tour along Donau")
				.description("Einst fuhr ich am Ufer der Donau entlang, ...")
				.totalEffort(2850)
				.continentName(context.getString(R.string.continent_europe))
				.lat(48.99763)
				.lon(2.476667)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.donauradweg)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Trans-Australian Railway")
				.description("Watch out for the kangaroos")
				.totalEffort(1693)
				.continentName(context.getString(R.string.continent_australia))
				.lat(-41.7399978)
				.lon(147.67)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.transaustralische_eisenbahn)
				.build());
		challenges.add(new Challenge.Builder()
				.title("South Pole Traverse")
				.description("Win the race against Amundsen and Scott")
				.totalEffort(1450)
				.continentName(context.getString(R.string.continent_antarctica))
				.lon(89.9899009)
				.lon(0)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.suedpol)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Carlifornia State Route")
				.description("Enjoy the ocean view")
				.totalEffort(1055)
				.continentName(context.getString(R.string.continent_north_america))
				.lat(36.7552565)
				.lon(-121.7652093)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.california_state_route)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Germany North-South")
				.description("From the North Sea to the Alps")
				.totalEffort(876)
				.continentName(context.getString(R.string.continent_europe))
				.lat(51.165691)
				.lon(10.451526)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.deutschland_nord_sued)
				.build());
		challenges.add(new Challenge.Builder()
				.title("St. Petersburg - Moscow")
				.description("Russia is a beautiful country")
				.totalEffort(634)
				.continentName(context.getString(R.string.continent_europe))
				.lat(55.749792)
				.lon(37.632495)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.moskau)
				.build());
		challenges.add(new Challenge.Builder()
				.title("A8")
				.description("Hopefully congestion free to the target")
				.totalEffort(505)
				.continentName(context.getString(R.string.continent_europe))
				.lat(48.6070949)
				.lon(9.6330233)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.a8)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Sao Paulo - Rio de Janeiro")
				.description("Samba at the Copacabana")
				.totalEffort(432)
				.continentName(context.getString(R.string.continent_south_america))
				.lat(-22.9156912)
				.lon(-43.449703)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.rio_de_janeiro)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Washington - New York")
				.finished(true)
				.description("No details available, internet too slow")
				.timestampStarted(141312)
				.timestampLastModified(183211)
				.totalEffort(328)
				.continentName(context.getString(R.string.continent_north_america))
				.lat(40.7056308)
				.lon(-73.9780035)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.new_york)
				.build());
		/*
		challenges.add(new Challenge.Builder()
				.title("Machu Picchu")
				.description("Hopefully you're not that ancient")
				.totalEffort(80)
				.continentName(context.getString(R.string.continent_south_america))
				.lat(13.163141)
				.lon(-72.544963)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Yungas Road")
				.description("Stay on the road")
				.totalEffort(65)
				.continentName(context.getString(R.string.continent_south_america))
				.lat(-16.3303225)
				.lon(-67.5960231)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		*/
		challenges.add(new Challenge.Builder()
				.title("Eurotunnel")
				.description("Bring the queen a baguette")
				.totalEffort(50.45)
				.continentName(context.getString(R.string.continent_europe))
				.lat(51.017884)
				.lon(1.4805104)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.iconResourceId(R.drawable.eurotunnel)
				.build());
		try {
			for (Challenge challenge : challenges) {
				getChallengeDao().create(challenge);
			}
		}
		catch (SQLException exception) {
			Log.d(TAG, exception.getMessage());
		}
	}


	private void addHeightChallenges(Context context) {
		List<Challenge> challenges = new LinkedList<Challenge>();
		challenges.add(new Challenge.Builder()
				.title("Mount Elbrus")
				.description("")
				.totalEffort(5642)
				.continentName(context.getString(R.string.continent_europe))
				.lat(51.1758057)
				.lon(10.4541194)
				.metricType(MetricType.VERTICALDISTANCE)
				.iconResourceId(R.drawable.mount_elbrus)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Mount McKinley")
				.totalEffort(6195)
				.continentName(context.getString(R.string.continent_north_america))
				.lat(63.0693955)
				.lon(-151.0074323)
				.metricType(MetricType.VERTICALDISTANCE)
				.iconResourceId(R.drawable.mount_mckinley)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Mount Aconcagna")
				.description("")
				.totalEffort(6962)
				.continentName(context.getString(R.string.continent_south_america))
				.lat(-32.653207)
				.lon(-70.0108333)
				.metricType(MetricType.VERTICALDISTANCE)
				.iconResourceId(R.drawable.mount_aconcagna)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Kilimanjaro")
				.description("")
				.totalEffort(5895)
				.continentName(context.getString(R.string.continent_africa))
				.lat(-3.0696375)
				.lon(37.3524428)
				.metricType(MetricType.VERTICALDISTANCE)
				.iconResourceId(R.drawable.mount_kilimanjaro)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Mount Everest")
				.description("")
				.completedEffort(4323)
				.timestampStarted(123112)
				.totalEffort(8848)
				.continentName(context.getString(R.string.continent_asia))
				.lat(27.98002)
				.lon(86.921543)
				.metricType(MetricType.VERTICALDISTANCE)
				.iconResourceId(R.drawable.mount_everest)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Mount Kosciuszko")
				.description("")
				.totalEffort(2228)
				.continentName(context.getString(R.string.continent_australia))
				.lat(-36.4559169)
				.lon(148.264588)
				.metricType(MetricType.VERTICALDISTANCE)
				.iconResourceId(R.drawable.mount_kilimanjaro)
				.build());
		/*
		challenges.add(new Challenge.Builder()
				.title("Mount Vinson")
				.description("")
				.totalEffort(4892)
				.continentName(context.getString(R.string.continent_antarctica))
				.lat(-78.516667)
				.lon(-85.616667)
				.metricType(MetricType.VERTICALDISTANCE)
				.build());
				*/
		try {
			for (Challenge challenge : challenges) {
				getChallengeDao().create(challenge);
			}
		}
		catch (SQLException exception) {
			Log.d(TAG, exception.getMessage());
		}
	}
}