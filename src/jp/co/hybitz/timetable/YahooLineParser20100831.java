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
import jp.co.hybitz.timetable.model.Line;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

import org.xmlpull.v1.XmlPullParser;

class YahooLineParser20100831 extends AbstractParser<TimeTableQuery, TimeTableResult> {
    private TimeTableResult result;
    private String company;
    private Line line;
    private boolean inRail;
    private boolean inDt;
    private boolean inDd;

    public YahooLineParser20100831(Platform platform) {
        super(platform);
    }

    @Override
    protected TimeTableResult endDocument() {
        return result;
    }

    @Override
    protected void startDocument(TimeTableQuery in) {
        result = new TimeTableResult();
        result.addArea(in.getArea());
        result.getAreas().get(0).addPrefecture(in.getPrefecture());
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (matchId("div", "rail")) {
            inRail = true;
        }
        else if (inRail && is("dt")) {
            inDt = true;
        }
        else if (inRail && is("dd")) {
            inDd = true;
        }
        else if (inDd && isA()) {
            line = new Line();
            line.setCompany(company);
            line.setUrl(parser.getAttributeValue(null, "href"));
        }

        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (inRail && is("div")) {
            if (result.getAreas().get(0).getPrefectures().get(0).getLines().isEmpty()) {
                return false;
            }
            return true;
        }
        else if (is("dt")) {
            inDt = false;
        }
        else if (is("dd")) {
            inDd = false;
        }
        else if (inDd && isA()) {
            result.getAreas().get(0).getPrefectures().get(0).addLine(line);
            line = null;
        }
        
        return false;
    }

    @Override
    protected void text(String text, XmlPullParser parser) {
        if (inDt) {
            company = text;
        }
        else if (line != null) {
            line.setName(text);
        }
    }

}
