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

import jp.co.hybitz.common.model.Time;

public class TransitTime extends Time {
    private String transitClass;
    private String boundFor;

    public TransitTime(int hour, int minute) {
        super(hour, minute);
    }

    public TransitTime(int hour, int minute, String transitClass, String boundFor) {
        super(hour, minute);
        this.transitClass = transitClass;
        this.boundFor = boundFor;
    }

    public TransitTime(String HH, String mm) {
        super(HH, mm);
    }

    public TransitTime(String HHmm) {
        super(HHmm);
    }

    public String getTransitClass() {
        return transitClass;
    }

    public void setTransitClass(String transitClass) {
        this.transitClass = transitClass;
    }

    public String getBoundFor() {
        return boundFor;
    }

    public void setBoundFor(String boundFor) {
        this.boundFor = boundFor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((boundFor == null) ? 0 : boundFor.hashCode());
        result = prime * result
                + ((transitClass == null) ? 0 : transitClass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransitTime other = (TransitTime) obj;
        if (boundFor == null) {
            if (other.boundFor != null)
                return false;
        } else if (!boundFor.equals(other.boundFor))
            return false;
        if (transitClass == null) {
            if (other.transitClass != null)
                return false;
        } else if (!transitClass.equals(other.transitClass))
            return false;
        return true;
    }

}
