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
import jp.co.hybitz.timetable.model.Prefecture;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class YahooAreaParser20100831Test extends ParserTestCase {

    @Override
    protected Parser<TimeTableQuery, TimeTableResult> getParser() {
        return new YahooAreaParser20100831(Platform.LOOSE_HTML);
    }    

    public void testParse20100825() {
        TimeTableResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/timetable_result_area_20100831.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(11, result.getAreas().size());

        Area a = result.getAreas().get(0);
        assertEquals("北海道", a.getName());
        Prefecture p = a.getPrefectures().get(0);
        assertEquals("北海道", p.getName());
        assertEquals("http://transit.map.yahoo.co.jp/station/list?pref=1&prefname=%E5%8C%97%E6%B5%B7%E9%81%93", p.getUrl());
        
        a = result.getAreas().get(2);
        assertEquals("関東", a.getName());
        p = a.getPrefectures().get(4);
        assertEquals("茨城", p.getName());
        assertEquals("http://transit.map.yahoo.co.jp/station/list?pref=8&prefname=%E8%8C%A8%E5%9F%8E", p.getUrl());

        a = result.getAreas().get(10);
        assertEquals("沖縄", a.getName());
        p = a.getPrefectures().get(0);
        assertEquals("沖縄", p.getName());
        assertEquals("http://transit.map.yahoo.co.jp/station/list?pref=47&prefname=%E6%B2%96%E7%B8%84", p.getUrl());
    }
}
