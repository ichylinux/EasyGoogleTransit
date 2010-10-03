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
import jp.co.hybitz.timetable.model.Station;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;

import org.xmlpull.v1.XmlPullParser;

class YahooStationParser20100831 extends AbstractParser<TimeTableQuery, TimeTableResult> {
    private TimeTableResult result;
    private Station station;
    private boolean inStation;
    private boolean inUl;
    private boolean inLi;

    public YahooStationParser20100831(Platform platform) {
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
        result.getAreas().get(0).getPrefectures().get(0).addLine(in.getLine());
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (matchId("div", "station")) {
            inStation = true;
        }
        else if (inStation && is("ul")) {
            inUl = true;
        }
        else if (inUl && is("li")) {
            inLi = true;
        }
        else if (inLi && isA()) {
            station = new Station();
            station.setUrl(parser.getAttributeValue(null, "href"));
        }

        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (inStation && is("div")) {
            if (result.getAreas().get(0).getPrefectures().get(0).getLines().get(0).getStations().isEmpty()) {
                return false;
            }
            return true;
        }
        else if (is("ul")) {
            inUl = false;
        }
        else if (is("li")) {
            inLi = false;
        }
        else if (inLi && isA()) {
            result.getAreas().get(0).getPrefectures().get(0).getLines().get(0).addStation(station);
            station = null;
        }
        
        return false;
    }

    @Override
    protected boolean text(String text, XmlPullParser parser) {
        if (station != null) {
            station.setName(text);
        }
        
        return false;
    }

}
