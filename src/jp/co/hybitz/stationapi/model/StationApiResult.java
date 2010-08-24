package jp.co.hybitz.stationapi.model;

import java.util.ArrayList;
import java.util.List;

import jp.co.hybitz.common.HttpResult;

public class StationApiResult extends HttpResult {
    private List<Station> stations = new ArrayList<Station>();
    
    public List<Station> getStations() {
        return stations;
    }
    
    public void addStation(Station station) {
        stations.add(station);
    }
}
