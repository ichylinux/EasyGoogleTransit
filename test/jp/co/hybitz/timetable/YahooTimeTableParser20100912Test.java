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
import jp.co.hybitz.timetable.model.TimeTable;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class YahooTimeTableParser20100912Test extends YahooTimeTableParser20100831Test {

    public void testParse20100912() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.getArea().setName("東海");
        query.setPrefecture(new Prefecture());
        query.getPrefecture().setName("愛知");
        query.setLine(new Line());
        query.getLine().setCompany("名古屋市交通局");
        query.getLine().setName("名城線");
        query.setStation(new Station());
        query.getStation().setName("妙音通");
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_timetable_20100912.html"), query);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getAreas().size());
        Area a = result.getAreas().get(0);
        assertEquals("東海", a.getName());
        Prefecture p = a.getPrefectures().get(0);
        assertEquals("愛知", p.getName());
        Line l = p.getLines().get(0);
        assertEquals("名古屋市交通局", l.getCompany());
        assertEquals("名城線", l.getName());
        Station s = l.getStations().get(0);
        assertEquals("妙音通", s.getName());
        assertEquals(1, s.getTimeTables().size());

        TimeTable tt = s.getTimeTables().get(0);
        assertEquals("平日", tt.getTypeString());
        assertEquals("1", tt.getMyGid());
    }
    
}
