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
import java.util.Date;

/**
 * 検索条件
 * 
 * @author ichy <ichylinux@gmail.com>
 */
public class TransitQuery implements Serializable {

	private String from;
	private String to;
    private TimeType timeType;
	private Date date;
	private boolean useExpress;
	private boolean useAirline;
	private String sort;

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
    public TimeType getTimeType() {
        return timeType;
    }
    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
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
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    
}
