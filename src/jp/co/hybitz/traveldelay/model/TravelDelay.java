package jp.co.hybitz.traveldelay.model;

import java.io.Serializable;

public class TravelDelay implements Serializable {
    private String date;
    private String line;
    private String condition;
    
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
}
