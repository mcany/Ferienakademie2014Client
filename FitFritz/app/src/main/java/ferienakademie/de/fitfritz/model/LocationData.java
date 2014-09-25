package ferienakademie.de.fitfritz.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by explicat on 9/25/14.
 */

public class LocationData extends SensorData {

    private LatLng location;

    public LatLng getLatLng() {
        return location;
    }

    public void setLatLng(LatLng latLng) {
        this.location = latLng;
    }

    public LocationData(LatLng latLng) {
        this.location = latLng;
    }

}
