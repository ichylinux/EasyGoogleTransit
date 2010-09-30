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
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class YahooStationParser20100831Test extends ParserTestCase {

    @Override
    protected Parser<TimeTableQuery, TimeTableResult> getParser() {
        return new YahooStationParser20100831(Platform.HTML);
    }    

    public void testParse20100825() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.getArea().setName("関東");
        query.setPrefecture(new Prefecture());
        query.getPrefecture().setName("埼玉");
        query.setLine(new Line());
        query.getLine().setCompany("西武鉄道");
        query.getLine().setName("新宿線");
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_station_20100831.html"), query);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getAreas().size());
        Area a = result.getAreas().get(0);
        assertEquals("関東", a.getName());
        Prefecture p = a.getPrefectures().get(0);
        assertEquals("埼玉", p.getName());
        Line l = p.getLines().get(0);
        assertEquals("西武鉄道", l.getCompany());
        assertEquals("新宿線", l.getName());

        assertEquals(8, l.getStations().size());
        Station s = l.getStations().get(4);
        assertEquals("狭山市", s.getName());
        assertEquals("http://transit.map.yahoo.co.jp/station/time?pref=11&amp;company=%E8%A5%BF%E6%AD%A6%E9%89%84%E9%81%93&amp;line=%E6%96%B0%E5%AE%BF%E7%B7%9A&amp;prefname=%E5%9F%BC%E7%8E%89&amp;st=%E7%8B%AD%E5%B1%B1%E5%B8%82", s.getUrl());
    }
}
