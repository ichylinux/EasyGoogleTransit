package jp.co.hybitz.stationapi.model;

import java.io.Serializable;

import jp.co.hybitz.common.GeoLocation;

public class StationApiQuery implements Serializable {
    private GeoLocation geoLocation;

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }
    
}
