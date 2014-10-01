package de.ferienakademie.neverrest.controller;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import de.ferienakademie.neverrest.R;
import de.ferienakademie.neverrest.model.Challenge;
import de.ferienakademie.neverrest.model.MetricType;

/**
 * Created by explicat on 9/28/14.
 */
public enum DatabaseUtil {
    INSTANCE, DatabaseUtil;

	private static final String TAG = DatabaseUtil.class.getSimpleName();

    private DatabaseHandler databaseHandler;

    public void initialize(Context context) {
        if (null == databaseHandler) {
            this.databaseHandler = new DatabaseHandler(context);
            // Will create the database if there it has not been created yet
            this.databaseHandler.getWritableDatabase();
			addDistanceChallenges(context);
			addHeightChallenges(context);
        }
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public void setDatabaseHandler(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
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
				.build());
		challenges.add(new Challenge.Builder()
				.title("Great Wall of China")
				.description("We are not planning to build a wall")
				.totalEffort(8850)
				.continentName(context.getString(R.string.continent_asia))
				.lat(40.431908)
				.lon(116.570375)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Jacob's Trail")
				.description("Don't cry, walk!")
				.totalEffort(6950)
				.continentName(context.getString(R.string.continent_europe))
				.lat(-33.391868)
				.lon(-70.6059235)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Sahara-Crossing")
				.description("Sweat like a camel")
				.totalEffort(4800)
				.continentName(context.getString(R.string.continent_africa))
				.lat(22.2243761)
				.lon(22.1154785)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Route 66")
				.description("= approx 8.12404")
				.totalEffort(3939)
				.continentName(context.getString(R.string.continent_north_america))
				.lat(39.0576271)
				.lon(-89.7508269)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Tour de France")
				.description("Without doping to the peak")
				.totalEffort(3663)
				.continentName(context.getString(R.string.continent_europe))
				.lat(48.8588589)
				.lon(2.3470599)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Bike tour along Donau")
				.description("Einst fuhr ich am Ufer der Donau entlang, ...")
				.totalEffort(2850)
				.continentName(context.getString(R.string.continent_europe))
				.lat(48.99763)
				.lon(2.476667)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Trans-Australian Railway")
				.description("Watch out for the kangaroos")
				.totalEffort(1693)
				.continentName(context.getString(R.string.continent_australia))
				.lat(-41.7399978)
				.lon(147.67)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("South Pole Traverse")
				.description("Win the race against Amundsen and Scott")
				.totalEffort(1450)
				.continentName(context.getString(R.string.continent_antarctica))
				.lon(89.9899009)
				.lon(0)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Carlifornia State Route")
				.description("Enjoy the ocean view")
				.totalEffort(1055)
				.continentName(context.getString(R.string.continent_north_america))
				.lat(36.7552565)
				.lon(-121.7652093)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Germany North-South")
				.description("From the North Sea to the Alps")
				.totalEffort(876)
				.continentName(context.getString(R.string.continent_europe))
				.lat(51.165691)
				.lon(10.451526)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("St. Petersburg - Moscow")
				.description("Russia is a beautiful country")
				.totalEffort(634)
				.continentName(context.getString(R.string.continent_europe))
				.lat(55.749792)
				.lon(37.632495)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("A8")
				.description("Hopefully congestion free to the target")
				.totalEffort(505)
				.continentName(context.getString(R.string.continent_europe))
				.lat(48.6070949)
				.lon(9.6330233)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Sao Paulo - Rio de Janeiro")
				.description("Samba at the Copacabana")
				.totalEffort(432)
				.continentName(context.getString(R.string.continent_south_america))
				.lat(-22.9156912)
				.lon(-43.449703)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		challenges.add(new Challenge.Builder()
				.title("Washington - New York")
				.description("Big city life")
				.totalEffort(328)
				.continentName(context.getString(R.string.continent_north_america))
				.lat(40.7056308)
				.lon(-73.9780035)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
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
		challenges.add(new Challenge.Builder()
				.title("Eurotunnel")
				.description("Bring the queen a baguette")
				.totalEffort(50.45)
				.continentName(context.getString(R.string.continent_europe))
				.lat(51.017884)
				.lon(1.4805104)
				.metricType(MetricType.HORIZONTALDISTANCE)
				.build());
		try {
			for (Challenge challenge : challenges) {
				databaseHandler.getChallengeDao().create(challenge);
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
				.description("")
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
				.build());
		challenges.add(new Challenge.Builder()
				.title("Mount Vinson")
				.description("")
				.totalEffort(4892)
				.continentName(context.getString(R.string.continent_antarctica))
				.lat(-78.516667)
				.lon(-85.616667)
				.metricType(MetricType.VERTICALDISTANCE)
				.build());
		try {
			for (Challenge challenge : challenges) {
				databaseHandler.getChallengeDao().create(challenge);
			}
		}
		catch (SQLException exception) {
			Log.d(TAG, exception.getMessage());
		}
	}

}
