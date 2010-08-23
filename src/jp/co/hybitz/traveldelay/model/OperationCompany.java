package jp.co.hybitz.traveldelay.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationCompany implements Serializable {
    private String name;
    private List<TravelDelay> delays = new ArrayList<TravelDelay>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TravelDelay> getTravelDelays() {
        return delays;
    }
    
    
    public void addTravelDelay(TravelDelay td) {
        delays.add(td);
    }
}
