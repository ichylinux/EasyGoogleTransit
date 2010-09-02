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

import jp.co.hybitz.common.AbstractParser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.timetable.model.Area;
import jp.co.hybitz.timetable.model.Prefecture;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

import org.xmlpull.v1.XmlPullParser;

public class YahooAreaParser20100831 extends AbstractParser<TimeTableQuery, TimeTableResult> {
    private TimeTableResult result;
    private Area area;
    private Prefecture prefecture;
    private boolean inDt;
    private boolean inDd;

    public YahooAreaParser20100831(Platform platform) {
        super(platform);
    }

    @Override
    protected TimeTableResult endDocument() {
        return result;
    }

    @Override
    protected void startDocument(TimeTableQuery in) {
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (matchId("div", "area")) {
            result = new TimeTableResult();
        }
        else if (is("dl")) {
            area = new Area();
        }
        else if (is("dt")) {
            inDt = true;
        }
        else if (is("dd")) {
            inDd = true;
        }
        else if (is("a") && inDd) {
            prefecture = new Prefecture();
            if (prefecture != null) {
                prefecture.setUrl(parser.getAttributeValue(null, "href"));
            }
        }

        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (result == null) {
            return false;
        }
        
        if (is("div")) {
            if (result.getAreas().isEmpty()) {
                return false;
            }
            
            return true;
        }
        else if (is("dl")) {
            result.addArea(area);
            area = null;
        }
        else if (is("dt")) {
            inDt = false;
        }
        else if (is("dd")) {
            inDd = false;
        }
        else if (is("a") && inDd) {
            area.addPrefecture(prefecture);
            prefecture = null;
        }
        
        return false;
    }

    @Override
    protected void text(String text, XmlPullParser parser) {
        if (area != null && inDt) {
            area.setName(text);
        }
        else if (prefecture != null) {
            prefecture.setName(text);
            
        }
    }

}
