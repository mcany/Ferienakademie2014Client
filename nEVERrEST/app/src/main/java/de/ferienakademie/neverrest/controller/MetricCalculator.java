package de.ferienakademie.neverrest.controller;

import java.util.List;

import de.ferienakademie.neverrest.model.LocationData;

/**
 * Created by explicat on 9/29/14.
 */
public class MetricCalculator {

    // Sum of weight is 1
    private static final float[] weights = { 0.0625f, 0.0625f, 0.125f, 0.25f, 0.5f };
    private static final float factorStdDevToThreshold = 5.f;


    /**
     * Determine whether a location is an outlier or not. The current position should be within a certain range calculated from the standard deviation of the recent points
     * @param current location data
     * @param recentPoints list of recent points of which the standard deviation is calculated
     * @return true if the current location looks valid, false if the current location looks like an outlier
     */
    public static boolean isValid(LocationData current, List<LocationData> recentPoints) {

        if (recentPoints.size() < 2) {
            return true;
        }

        // Calculate std dev
        double[] stdDev = calcStdDev(recentPoints);

        // Determine min and max threshold
        LocationData mostRecent = recentPoints.get(recentPoints.size() - 1);
        double minLatitude = mostRecent.getLatitude() - stdDev[0] * factorStdDevToThreshold;
        double maxLatitude = mostRecent.getLatitude() + stdDev[0] * factorStdDevToThreshold;
        double minLongitude = mostRecent.getLongitude() - stdDev[1] * factorStdDevToThreshold;
        double maxLongitude = mostRecent.getLongitude() - stdDev[1] * factorStdDevToThreshold;

        // Decide whether current point is an outlier
        if (current.getLatitude() < minLatitude ||
                current.getLatitude() > maxLatitude ||
                current.getLongitude() < minLongitude ||
                current.getLongitude() > maxLongitude) {
            return false;
        }

        return true;
    }


    /**
     * Calculates the standard deviation from a list of points. Returns the maximum value for the std dev of latitude and longitude
     * @param points list of points to calculate from which the standard deviation is calculated
     * @return [stdDev for latitude, stdDev for longitude]
     */
    private static double[] calcStdDev(List<LocationData> points) {
        double[] squaredSum = {0.d, 0.d, 0.d, 0.d};
        double[] sum = {0.d, 0.d, 0.d, 0.d};

        int size = points.size();
        for (int i=0; i<size; i++) {
            LocationData cur = points.get(i);
            squaredSum[0] += cur.getLatitude() * cur.getLatitude();
            squaredSum[1] += cur.getLongitude() * cur.getLongitude();
            squaredSum[2] += cur.getAltitude() * cur.getAltitude();
            squaredSum[3] += cur.getSpeed() * cur.getSpeed();

            sum[0] += cur.getLatitude();
            sum[1] += cur.getLongitude();
            sum[2] += cur.getAltitude();
            sum[3] += cur.getSpeed();
        }

        // altitude and speed are not considered
        double stdDevLatitude = Math.sqrt((squaredSum[0] - (sum[0] * sum[0]) / (double) size) / (double) size);
        double stdDevLongitude = Math.sqrt((squaredSum[1] - (sum[1] * sum[1]) / (double) size) / (double) size);

        return new double[] { stdDevLatitude, stdDevLongitude };
    }


    /**
     * Smoothes the attributes of the current location with the recent points and static weights
     * @param current location
     * @param recentPoints list of size [weights.length - 1] recent points
     * @return
     */
    public static LocationData smoothLocationData(LocationData current, List<LocationData> recentPoints) {

        if (recentPoints.size() < weights.length - 1) {
            return current;
        }

        assert(recentPoints.size() == weights.length - 1);


        double latitude = current.getLatitude() * weights[weights.length - 1];
        double longitude = current.getLongitude() * weights[weights.length - 1];
        double altitude = current.getAltitude() * weights[weights.length - 1];
        float speed = current.getSpeed() * weights[weights.length - 1];

        for (int i=0; i<weights.length - 1; i++) {
            latitude += recentPoints.get(i).getLatitude() * weights[i];
            longitude += recentPoints.get(i).getLongitude() * weights[i];
            altitude += recentPoints.get(i).getAltitude() * weights[i];
            speed += recentPoints.get(i).getSpeed() * weights[i];
        }

        LocationData ret = new LocationData(current.getCreationDate(), latitude, longitude, altitude, speed);
        ret.setActivity(current.getActivity());
        return ret;
    }
}
