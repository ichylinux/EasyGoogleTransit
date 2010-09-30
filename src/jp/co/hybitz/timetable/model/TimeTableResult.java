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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.co.hybitz.common.HttpResponse;

public class TimeTableResult extends HttpResponse {
    private List<Area> areas = new ArrayList<Area>();
    
    public void addArea(Area area) {
        areas.add(area);
    }
    
    public List<Area> getAreas() {
        return areas;
    }

    public Area getArea(String name) {
        for (Iterator<Area> it = areas.iterator(); it.hasNext();) {
            Area a = it.next();
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }
}
