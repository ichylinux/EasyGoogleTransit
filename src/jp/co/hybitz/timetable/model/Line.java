package jp.co.hybitz.timetable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Line implements Serializable {
    private String name;
    private String url;
    private String company;
    private List<Station> stations = new ArrayList<Station>();
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    
    public List<Station> getStations() {
        return stations;
    }
    
    public void addStation(Station station) {
        stations.add(station);
    }
    
    public void setStations(List<Station> stations) {
        this.stations = stations;
    }
    
    public Station getStation(String name) {
        for (Iterator<Station> it = stations.iterator(); it.hasNext();) {
            Station s = it.next();
            if (s.getName().equals(name)) {
                return s;
            }
        }
        
        return null;
    }
}
