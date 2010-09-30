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

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.ParserTestCase;
import jp.co.hybitz.common.Platform;
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
public class YahooTimeTableParser20100831Test extends ParserTestCase {

    @Override
    protected Parser<TimeTableQuery, TimeTableResult> getParser() {
        return new YahooTimeTableParser20100919(Platform.HTML);
    }    

    public void testParse20100831_01() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.getArea().setName("北海道");
        query.setPrefecture(new Prefecture());
        query.getPrefecture().setName("北海道");
        query.setLine(new Line());
        query.getLine().setCompany("札幌市交通局");
        query.getLine().setName("南北線");
        query.setStation(new Station());
        query.getStation().setName("さっぽろ駅");
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_timetable_20100831_01.html"), query);
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
        assertEquals("札幌市交通局", l.getCompany());
        assertEquals("南北線", l.getName());
        Station s = l.getStations().get(0);
        assertEquals("さっぽろ駅", s.getName());
        assertEquals(1, s.getTimeTables().size());

        TimeTable tt = s.getTimeTables().get(0);
        assertEquals("平日", tt.getTypeString());
        assertEquals(19, tt.getTimeLines().size());
        
        TimeLine tl = tt.getTimeLines().get(2);
        assertEquals(15, tl.getTimes().size());
        assertEquals(2, tl.getTimes().get(0).getMinute());
        assertEquals(6, tl.getTimes().get(1).getMinute());
        assertEquals(10, tl.getTimes().get(2).getMinute());
        assertEquals(14, tl.getTimes().get(3).getMinute());
        assertEquals(18, tl.getTimes().get(4).getMinute());
    }
    
    public void testParse20100831_02() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.getArea().setName("北海道");
        query.setPrefecture(new Prefecture());
        query.getPrefecture().setName("北海道");
        query.setLine(new Line());
        query.getLine().setCompany("札幌市交通局");
        query.getLine().setName("南北線");
        query.setStation(new Station());
        query.getStation().setName("さっぽろ駅");
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_timetable_20100831_02.html"), query);
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
        assertEquals("札幌市交通局", l.getCompany());
        assertEquals("南北線", l.getName());
        Station s = l.getStations().get(0);
        assertEquals("さっぽろ駅", s.getName());
        assertEquals(1, s.getTimeTables().size());

        TimeTable tt = s.getTimeTables().get(0);
        assertEquals("土曜", tt.getTypeString());
        assertEquals(19, tt.getTimeLines().size());
        
        TimeLine tl = tt.getTimeLines().get(1);
        assertEquals(9, tl.getTimes().size());
        assertEquals(3, tl.getTimes().get(0).getMinute());
        assertEquals(11, tl.getTimes().get(1).getMinute());
        assertEquals(18, tl.getTimes().get(2).getMinute());
        assertEquals(24, tl.getTimes().get(3).getMinute());
        assertEquals(31, tl.getTimes().get(4).getMinute());
    }
    
    public void testParse20100831_03() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.getArea().setName("北海道");
        query.setPrefecture(new Prefecture());
        query.getPrefecture().setName("北海道");
        query.setLine(new Line());
        query.getLine().setCompany("札幌市交通局");
        query.getLine().setName("南北線");
        query.setStation(new Station());
        query.getStation().setName("さっぽろ駅");
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_timetable_20100831_03.html"), query);
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
        assertEquals("札幌市交通局", l.getCompany());
        assertEquals("南北線", l.getName());
        Station s = l.getStations().get(0);
        assertEquals("さっぽろ駅", s.getName());
        assertEquals(1, s.getTimeTables().size());

        TimeTable tt = s.getTimeTables().get(0);
        assertEquals("日曜・祝日", tt.getTypeString());
        assertEquals(19, tt.getTimeLines().size());
        
        TimeLine tl = tt.getTimeLines().get(18);
        assertEquals(3, tl.getTimes().size());
        assertEquals(2, tl.getTimes().get(0).getMinute());
        assertEquals(9, tl.getTimes().get(1).getMinute());
        assertEquals(17, tl.getTimes().get(2).getMinute());
    }
    
    public void testParse20100831_04() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.setPrefecture(new Prefecture());
        query.setLine(new Line());
        query.setStation(new Station());
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_timetable_20100831_04.html"), query);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        Area a = result.getAreas().get(0);
        Prefecture p = a.getPrefectures().get(0);
        Line l = p.getLines().get(0);
        Station s = l.getStations().get(0);
        
        TimeTable tt = s.getTimeTables().get(0);
        assertEquals("平日", tt.getTypeString());
        assertEquals(18, tt.getTimeLines().size());
        
        TimeLine tl = tt.getTimeLines().get(7);
        assertEquals(12, tl.getTimes().size());
        assertEquals(0, tl.getTimes().get(0).getMinute());
        assertEquals(0, tl.getTimes().get(1).getMinute());
        assertEquals(5, tl.getTimes().get(2).getMinute());
    }
}
