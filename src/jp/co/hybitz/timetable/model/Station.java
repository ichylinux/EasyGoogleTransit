package jp.co.hybitz.timetable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Station implements Serializable {
    private String name;
    private String url;
    private List<TimeTable> timeTables = new ArrayList<TimeTable>();
    
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
    
    public List<TimeTable> getTimeTables() {
        return timeTables;
    }
    
    public void addTimeTable(TimeTable timeTable) {
        timeTables.add(timeTable);
    }
    
    public void setTimeTables(List<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
