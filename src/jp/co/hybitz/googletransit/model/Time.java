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
package jp.co.hybitz.googletransit.model;

import java.io.Serializable;

/**
 * @author ichy <ichylinux@gmail.com>
 *
 */
public class Time implements Serializable, Comparable<Time> {
    private int hour;
    private int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
    
    public Time(String hour, String minute) {
        this.hour = Integer.parseInt(hour);
        this.minute = Integer.parseInt(minute);
    }
    
    public int getHour() {
        return hour;
    }
    
    public String getHourAsString() {
        return hour < 10 ? "0" + hour : String.valueOf(hour);
    }
    
    public int getMinute() {
        return minute;
    }
    
    public String getMinuteAsString() {
        return minute < 10 ? "0" + minute : String.valueOf(minute);
    }
    
    public String getTimeAsString() {
        return getTimeAsString(false);
    }
    
    public String getTimeAsString(boolean withColon) {
        if (withColon) {
            return getHourAsString() + ":" + getMinuteAsString();
        }
        else {
            return getHourAsString() + getMinuteAsString();
        }
    }
    
    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Time o) {
        int ret = hour - o.hour;
        if (ret != 0) {
            return ret;
        }
        
        return minute - o.minute;
    }
}
