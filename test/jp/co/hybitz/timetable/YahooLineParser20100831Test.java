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
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class YahooLineParser20100831Test extends ParserTestCase {

    @Override
    protected Parser<TimeTableQuery, TimeTableResult> getParser() {
        return new YahooLineParser20100831(Platform.LOOSE_HTML);
    }    

    public void testParse20100825() {
        TimeTableQuery query = new TimeTableQuery();
        query.setArea(new Area());
        query.getArea().setName("関東");
        query.setPrefecture(new Prefecture());
        query.getPrefecture().setName("東京");
        
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_line_20100831.html"), query);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getAreas().size());
        Area a = result.getAreas().get(0);
        assertEquals("関東", a.getName());
        Prefecture p = a.getPrefectures().get(0);
        assertEquals("東京", p.getName());
        
        assertEquals(80, p.getLines().size());
        Line l = p.getLines().get(4);
        assertEquals("JR", l.getCompany());
        assertEquals("京浜東北線", l.getName());
        assertEquals("http://transit.map.yahoo.co.jp/station/station?pref=13&amp;company=JR&amp;prefname=%E6%9D%B1%E4%BA%AC&amp;line=%E4%BA%AC%E6%B5%9C%E6%9D%B1%E5%8C%97%E7%B7%9A", l.getUrl());

        l = p.getLines().get(79);
        assertEquals("首都圏新都市鉄道", l.getCompany());
        assertEquals("つくばエクスプレス", l.getName());
        assertEquals("http://transit.map.yahoo.co.jp/station/station?pref=13&amp;company=%E9%A6%96%E9%83%BD%E5%9C%8F%E6%96%B0%E9%83%BD%E5%B8%82%E9%89%84%E9%81%93&amp;prefname=%E6%9D%B1%E4%BA%AC&amp;line=%E3%81%A4%E3%81%8F%E3%81%B0%E3%82%A8%E3%82%AF%E3%82%B9%E3%83%97%E3%83%AC%E3%82%B9", l.getUrl());
    }
}
