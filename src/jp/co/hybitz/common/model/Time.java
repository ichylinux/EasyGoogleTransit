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
package jp.co.hybitz.common.model;

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
    
    public Time(String HH, String mm) {
        this.hour = Integer.parseInt(HH);
        this.minute = Integer.parseInt(mm);
    }
    
    public Time(String HHmm) {
        this(HHmm.substring(0, 2), HHmm.substring(2));
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
    
    public boolean before(Time time) {
        return compareTo(time) < 0;
    }
    
    public boolean after(Time time) {
        return compareTo(time) > 0;
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hour;
        result = prime * result + minute;
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Time other = (Time) obj;
        if (hour != other.hour)
            return false;
        if (minute != other.minute)
            return false;
        return true;
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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getTimeAsString(true);
    }
    
}
