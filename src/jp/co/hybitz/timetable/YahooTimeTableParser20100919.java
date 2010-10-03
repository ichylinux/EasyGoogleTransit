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
import jp.co.hybitz.timetable.model.Line;
import jp.co.hybitz.timetable.model.Prefecture;
import jp.co.hybitz.timetable.model.Station;
import jp.co.hybitz.timetable.model.TimeLine;
import jp.co.hybitz.timetable.model.TimeTable;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;
import jp.co.hybitz.timetable.model.TransitTime;
import jp.co.hybitz.timetable.model.TimeTable.Type;

import org.xmlpull.v1.XmlPullParser;

class YahooTimeTableParser20100919 extends AbstractParser<TimeTableQuery, TimeTableResult> {
    private TimeTableResult result;
    private TimeTable timeTable;
    private TimeLine timeLine;
    private String lineName;
    private Integer hour;
    private Integer minute;
    private String transitClass;
    private String boundFor;
    private String myGid;
    private boolean inTimeTable;
    private boolean inDirection;
    private boolean inAttention;
    private boolean inH5;
    private boolean inWeekday;
    private boolean inSaturday;
    private boolean inSunday;
    private boolean inTBody;
    private boolean inTr;
    private boolean inHour;
    private boolean inTime;
    private boolean inMinute;
    private boolean inTransitClass;
    private boolean inBoundFor;
    private boolean inStationNavi;
    private boolean inLineName;
    private boolean inH4;

    public YahooTimeTableParser20100919(Platform platform) {
        super(platform);
    }

    @Override
    protected TimeTableResult endDocument() {
        return result;
    }

    @Override
    protected void startDocument(TimeTableQuery query) {
        Station station = new Station();
        station.setName(query.getStation().getName());
        station.setUrl(query.getStation().getUrl());
        
        Line line = new Line();
        line.setCompany(query.getLine().getCompany());
        line.setName(query.getLine().getName());
        line.setUrl(query.getLine().getUrl());
        line.addStation(station);
        
        Prefecture prefecture = new Prefecture();
        prefecture.setName(query.getPrefecture().getName());
        prefecture.setUrl(query.getPrefecture().getUrl());
        prefecture.addLine(line);
        
        Area area = new Area();
        area.setName(query.getArea().getName());
        area.setUrl(query.getArea().getUrl());
        area.addPrefecture(prefecture);
        
        result = new TimeTableResult();
        result.addArea(area);
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (matchId("div", "station-nav")) {
            inStationNavi = true;
        }
        else if (matchId("div", "timetable")) {
            inTimeTable = true;
            timeTable = new TimeTable();
            timeTable.setMyGid(myGid);
        }
        else if (matchId("div", "line-name")) {
            inLineName = true;
        }
        else if (inLineName && is("h4")) {
            inH4 = true;
        }
        else if (inTimeTable && matchClass("li", "tab-on")) {
            inDirection = true;
        }
        else if (inTimeTable && matchId("div", "attention")) {
            inAttention = true;
        }
        else if (inTimeTable && matchClass("li", "tab01-on")) {
            inWeekday = true;
        }
        else if (inTimeTable && matchClass("li", "tab02-on")) {
            inSaturday = true;
        }
        else if (inTimeTable && matchClass("li", "tab03-on")) {
            inSunday = true;
        }
        else if (inTimeTable && is("tbody")) {
            inTBody = true;
        }
        else if (inTBody && is("tr")) {
            inTr = true;
            timeLine = new TimeLine();
        }
        else if (inTr && matchClass("td", "col1")) {
            inHour = true;
        }
        else if (inTr && is("dl")) {
            inTime = true;
        }
        else if (inTr && is("dt")) {
            inMinute = true;
        }
        else if (inTr && matchClass("dd", "trn-cls")) {
            inTransitClass = true;
        }
        else if (inTr && matchClass("dd", "sta-for")) {
            inBoundFor = true;
        }
        else if (is("h5")) {
            inH5 = true;
        }
        else if (inTimeTable && inH5 && isA()) {
            String href = parser.getAttributeValue(null, "href");
            String gid = href.substring(href.indexOf("&gid=")+5);
            if (gid.indexOf("&") >= 0) {
                gid = gid.substring(0, gid.indexOf("&"));
            }
            timeTable.addGid(gid);
        }
        else if (inStationNavi && isA()) {
            String href = parser.getAttributeValue(null, "href");
            String gid = href.substring(href.indexOf("gid=")+4);
            if (gid.indexOf("&") >= 0) {
                gid = gid.substring(0, gid.indexOf("&"));
            }
            myGid = gid;
        }

        return false;
    }

    @Override
    protected boolean text(String text, XmlPullParser parser) {
        if (inH5) {
            if (inDirection) {
                timeTable.setDirection(text);
            }
        }
        else if (inLineName && inH4) {
            if (lineName == null) {
                lineName = text.replaceAll("&nbsp;", "");
            }
        }
        else if (inWeekday) {
            timeTable.setType(Type.WEEKDAY);
        }
        else if (inSaturday) {
            timeTable.setType(Type.SATURDAY);
        }
        else if (inSunday) {
            timeTable.setType(Type.SUNDAY);
        }
        else if (inHour) {
            hour = Integer.parseInt(text);
            timeLine.setHour(hour);
        }
        else if (inMinute) {
            if (minute == null) {
                minute = Integer.parseInt(text);
            }
        }
        else if (inTransitClass) {
            transitClass = text;
        }
        else if (inBoundFor) {
            boundFor = text;
        }
        
        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (is("div")) {
            if (inStationNavi) {
                inStationNavi = false;
            }
            else if (inAttention) {
                inAttention = false;
                return false;
            }
            else if (inTimeTable) {
                timeTable.setLineName(lineName);
                result.getAreas().get(0).getPrefectures().get(0).getLines().get(0).getStations().get(0).addTimeTable(timeTable);
                lineName = null;
                inTimeTable = false;
                return true;
            }
            else if (inLineName) {
                inLineName = false;
            }
        }
        else if (is("h5")) {
            inH5 = false;
        }
        else if (is("h4")) {
            inH4 = false;
        }
        else if (is("li")) {
            if (inDirection) {
                inDirection = false;
            }
            else if (inWeekday) {
                inWeekday = false;
            }
            else if (inSaturday) {
                inSaturday = false;
            }
            else if (inSunday) {
                inSunday = false;
            }
        }
        else if (is("tbody")) {
            inTBody = false;
        }
        else if (is("tr")) {
            inTr = false;
            if (timeLine != null) {
                if (!timeLine.getTimes().isEmpty()) {
                    timeTable.addTimeLine(timeLine);
                }
                timeLine = null;
                hour = null;
            }
        }
        else if (is("td")) {
            inHour = false;
        }
        else if (is("dl")) {
            if (inTime) {
                if (timeLine != null) {
                    TransitTime t = new TransitTime(hour, minute, transitClass, boundFor);
                    timeLine.addTime(t);
                    transitClass = null;
                    boundFor = null;
                    minute = null;
                }
                inTime = false;
            }
        }
        else if (is("dt")) {
            inMinute = false;
        }
        else if (is("dd")) {
            if (inTransitClass) {
                inTransitClass = false;
            }
            else if (inBoundFor) {
                inBoundFor = false;
            }
        }
        
        return false;
    }

}
