package jp.co.hybitz.common;

import java.io.Serializable;

public class GeoLocation implements Serializable {
    private double longitude;
    private double latitude;
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
