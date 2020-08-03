package in.asclepius.app.models;

import com.google.firebase.database.PropertyName;

public class LocationClass {

    private Double lat;
    private Double longitude;

    public LocationClass(Double lat, Double longitude) {
        this.lat = lat;
        this.longitude = longitude;
    }

    public LocationClass() {
    }

    @PropertyName("lat")
    public Double getLat() {
        return lat;
    }

    @PropertyName("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @PropertyName("long")
    public Double getLongitude() {
        return longitude;
    }

    @PropertyName("long")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
