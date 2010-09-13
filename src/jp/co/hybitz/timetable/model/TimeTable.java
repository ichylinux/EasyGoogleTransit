/**
 * Copyright (C) 2010 Hybitz.co.ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * 
 */
package jp.co.hybitz.timetable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TimeTable implements Serializable {
    public enum Type {
        WEEKDAY,
        SATURDAY,
        SUNDAY,
    };
    
    public static String getTypeString(Type type) {
        switch (type) {
        case WEEKDAY :
            return "平日";
        case SATURDAY :
            return "土曜";
        case SUNDAY :
            return "日曜・祝日";
        default :
            return null;
        }
    }

    private String myGid;
    private List<String> gidList = new ArrayList<String>();
    private String direction;
    private Type type;
    private List<TimeLine> timeLines = new ArrayList<TimeLine>();

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Type getType() {
        return type;
    }

    public String getTypeString() {
        return getTypeString(getType());
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public List<TimeLine> getTimeLines() {
        return timeLines;
    }
    
    public void addTimeLine(TimeLine timeLine) {
        timeLines.add(timeLine);
    }
    
    public String getMyGid() {
        return myGid;
    }

    public void setMyGid(String myGid) {
        this.myGid = myGid;
    }

    public List<String> getGidList() {
        return gidList;
    }
    
    public void addGid(String gid) {
        gidList.add(gid);
    }
}
