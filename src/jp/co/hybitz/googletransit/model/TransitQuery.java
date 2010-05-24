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
 */
public class TransitQuery implements Serializable {

	private String from;
	private String to;
	private String date;
	private String time;
	private TimeType timeType;
	private boolean useExpress;
	private boolean useAirline;

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public TimeType getTimeType() {
        return timeType;
    }
    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }
    public boolean isUseExpress() {
        return useExpress;
    }
    public void setUseExpress(boolean useExpress) {
        this.useExpress = useExpress;
    }
    public boolean isUseAirline() {
        return useAirline;
    }
    public void setUseAirline(boolean useAirline) {
        this.useAirline = useAirline;
    }
}
