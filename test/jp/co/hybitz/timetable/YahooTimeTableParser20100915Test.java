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
package jp.co.hybitz.timetable;

import jp.co.hybitz.timetable.model.Area;
import jp.co.hybitz.timetable.model.Line;
import jp.co.hybitz.timetable.model.Prefecture;
import jp.co.hybitz.timetable.model.Station;
import jp.co.hybitz.timetable.model.TimeLine;
import jp.co.hybitz.timetable.model.TimeTable;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class YahooTimeTableParser20100915Test extends YahooTimeTableParser20100912Test {

    public void testParse20100915() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.getArea().setName("北海道");
        query.setPrefecture(new Prefecture());
        query.getPrefecture().setName("北海道");
        query.setLine(new Line());
        query.getLine().setCompany("JR");
        query.getLine().setName("札沼線");
        query.setStation(new Station());
        query.getStation().setName("札幌");
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_timetable_20100915.html"), query);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getAreas().size());
        Area a = result.getAreas().get(0);
        assertEquals("北海道", a.getName());
        Prefecture p = a.getPrefectures().get(0);
        assertEquals("北海道", p.getName());
        Line l = p.getLines().get(0);
        assertEquals("JR", l.getCompany());
        assertEquals("札沼線", l.getName());
        Station s = l.getStations().get(0);
        assertEquals("札幌", s.getName());
        assertEquals(1, s.getTimeTables().size());

        TimeTable tt = s.getTimeTables().get(0);
        assertEquals("平日", tt.getTypeString());
        assertEquals("1", tt.getMyGid());
        assertEquals(18, tt.getTimeLines().size());
        
        TimeLine tl = tt.getTimeLines().get(0);
        assertEquals("然", tl.getTimes().get(0).getBoundFor());
        assertEquals("石", tl.getTimes().get(1).getBoundFor());
        assertEquals("石", tl.getTimes().get(2).getBoundFor());
    }
    
}
