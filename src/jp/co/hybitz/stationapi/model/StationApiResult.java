package jp.co.hybitz.stationapi.model;

import java.util.ArrayList;
import java.util.List;

import jp.co.hybitz.common.GeoLocation;
import jp.co.hybitz.common.HttpResult;
import jp.co.hybitz.rgeocode.model.RGeocodeResult;

public class StationApiResult extends HttpResult {
    private GeoLocation geoLocation;
    private RGeocodeResult rGeocodeResult;
    private List<Station> stations = new ArrayList<Station>();
    
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public RGeocodeResult getRGeocodeResult() {
        return rGeocodeResult;
    }

    public void setRGeocodeResult(RGeocodeResult rGeocodeResult) {
        this.rGeocodeResult = rGeocodeResult;
    }

    public List<Station> getStations() {
        return stations;
    }
    
    public void addStation(Station station) {
        stations.add(station);
    }
}
