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

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Arrays;

import jp.co.hybitz.common.HttpResponse;
import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.Searcher;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.timetable.model.Area;
import jp.co.hybitz.timetable.model.Line;
import jp.co.hybitz.timetable.model.Prefecture;
import jp.co.hybitz.timetable.model.Station;
import jp.co.hybitz.timetable.model.TimeTable;
import jp.co.hybitz.timetable.model.TimeTableQuery;
import jp.co.hybitz.timetable.model.TimeTableResult;
import jp.co.hybitz.timetable.model.TimeTable.Type;

class TimeTableSearcher20100919 implements Searcher<TimeTableQuery, TimeTableResult>, YahooConfig {
    private Platform platform;
    
    public TimeTableSearcher20100919(Platform platform) {
        this.platform = platform;
    }
    
    @Override
    public Parser<TimeTableQuery, TimeTableResult> createParser(TimeTableQuery query, HttpResponse response) {
        if (isAreaSearch(query)) {
            return new YahooAreaParser20100831(platform);
        }
        else if (isLineSearch(query)) {
            return new YahooLineParser20100831(platform);
        }
        else if (isStationSearch(query)) {
            return new YahooStationParser20100831(platform);
        }
        else if (isTimeTableSearch(query)) {
            return new YahooTimeTableParser20100919(platform);
        }
        
        throw new IllegalArgumentException();
    }

    @Override
    public String createUrl(TimeTableQuery query) {
        if (isAreaSearch(query)) {
            return YAHOO_TRANSIT_URL;
        }
        else if (isLineSearch(query)) {
            return query.getPrefecture().getUrl().replaceAll("&amp;", "&");
        }
        else if (isStationSearch(query)) {
            return query.getLine().getUrl().replaceAll("&amp;", "&");
        }
        else if (isTimeTableSearch(query)) {
            String url = query.getStation().getUrl().replaceAll("&amp;", "&");

            String gid = getGid(query);
            if (gid != null) {
                url.replaceAll("&gid=[0-9]{0,2}", "&gid=" + gid);
                if (url.indexOf("&gid=") < 0) {
                    url += "&gid=" + gid;
                }
                return url;
            }
            
            String realLine = getRealLine(query);
            if (realLine != null) {
                url = url.replaceAll("&line=.*&", "&line=" + URLEncoder.encode(realLine) + "&");
                return url;
            }
            
            return url;
        }

        throw new IllegalArgumentException();
    }

    private String getRealLine(TimeTableQuery query) {
        for (int i = 0; i < lineMappings.length; i ++) {
            String[] info = lineMappings[i];
            if (info[0].equals(query.getLine().getCompany())
                    && info[1].equals(query.getLine().getName())) {
                return info[2];
            }
        }
        return null;
    }

    private String getGid(TimeTableQuery query) {
        for (int i = 0; i < gidList.length; i ++) {
            String[] info = gidList[i];
            if (info[0].equals(query.getLine().getCompany())
                    && info[1].equals(query.getLine().getName())
                    && info[2].equals(query.getStation().getName())) {
                return info[3];
            }
        }
        return null;
    }

    private String[] getExtraGid(TimeTableQuery query) {
        for (int i = 0; i < extraGidList.length; i ++) {
            String[] info = extraGidList[i];
            if (info[0].equals(query.getLine().getCompany())
                    && info[1].equals(query.getLine().getName())
                    && info[2].equals(query.getStation().getName())) {
                return info[3].split(",");
            }
        }
        return null;
    }

    @Override
    public TimeTableResult search(TimeTableQuery query) throws HttpSearchException {
        if (isStationSearch(query)) {
            return searchStations(query);
        }
        else if (isLineSearch(query)) {
            return searchLines(query);
        }
        else if (isTimeTableSearch(query)) {
            return searchTimeTables(query);
        }
        else {
            HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));

            try {
                TimeTableResult result = response.isOK() ? createParser(query, response).parse(response.getInputStream(), query) : new TimeTableResult();
                result.setResponseCode(response.getResponseCode());
                return result;
            }
            catch (Exception e) {
                throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
            }
        }
    }
    
    @Override
    public void cancel() {
    }
    
    private TimeTableResult searchLines(TimeTableQuery query) throws HttpSearchException {
        HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));

        try {
            TimeTableResult result;
            
            if (response.isOK()) {
                result = createParser(query, response).parse(response.getInputStream(), query);
                
                // 湘南新宿ラインがリストにないので独自に含める
                Prefecture p = result.getAreas().get(0).getPrefectures().get(0);
                if ("東京".equals(p.getName()) || "神奈川".equals(p.getName()) || "埼玉".equals(p.getName())) {
                    Line l = new Line();
                    l.setName("湘南新宿ライン");
                    l.setCompany("JR");
                    l.setUrl("");
                    for (int i = 0; i < p.getLines().size(); i ++) {
                        Line line = p.getLines().get(i);
                        if ("京浜東北線".equals(line.getName())) {
                            p.getLines().add(i, l);
                            break;
                        }
                    }
                }
            }
            else {
                result = new TimeTableResult();
            }

            result.setResponseCode(response.getResponseCode());
            return result;
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
    }
    
    private TimeTableResult getShonanShinjukuStatsions(TimeTableQuery query) {
        TimeTableResult result = new TimeTableResult();
        result.setResponseCode(HttpURLConnection.HTTP_OK);
        result.addArea(query.getArea());
        result.getAreas().get(0).addPrefecture(query.getPrefecture());
        result.getAreas().get(0).getPrefectures().get(0).addLine(query.getLine());
        
        Line l = result.getAreas().get(0).getPrefectures().get(0).getLines().get(0);
        
        String[][] stations = new String[0][2];
        if ("埼玉".equals(query.getPrefecture().getName())) {
            stations = shonanShinjukuStationsSaitama;
        }
        else if ("東京".equals(query.getPrefecture().getName())) {
            stations = shonanShinjukuStationsTokyo;
        }
        else if ("神奈川".equals(query.getPrefecture().getName())) {
            stations = shonanShinjukuStationsKanagawa;
        }
        
        for (String[] stataionInfo : stations) {
            Station s = new Station();
            s.setName(stataionInfo[0]);
            s.setUrl(stataionInfo[1]);
            l.addStation(s);
        }
        
        return result;
    }

    private TimeTableResult searchStations(TimeTableQuery query) throws HttpSearchException {
        // 湘南新宿ラインがリストにないので独自に含める
        if ("湘南新宿ライン".equals(query.getLine().getName())) {
            return getShonanShinjukuStatsions(query);
        }

        HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));

        try {
            TimeTableResult result;
            
            if (response.isOK()) {
                result = createParser(query, response).parse(response.getInputStream(), query);
                
            }
            else {
                result = new TimeTableResult();
            }

            result.setResponseCode(response.getResponseCode());
            return result;
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
    }

    private TimeTableResult searchTimeTables(TimeTableQuery query) throws HttpSearchException {
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
        
        TimeTableResult result = new TimeTableResult();
        result.addArea(area);
        
        // まずは検索
        HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));
        result.setResponseCode(response.getResponseCode());
        if (response.isOK()) {
            try {
                TimeTableResult ret = createParser(query, response).parse(response.getInputStream(), query);
                TimeTable timeTable = getResultTimeTable(ret);
                station.addTimeTable(timeTable);

                // 残りのタイプを検索
                String[] kind = new String[]{"1", "2", "4"};
                for (int i = 0; i < kind.length; i ++) {
                    if (timeTable.getType() == Type.WEEKDAY && i == 0) {
                        continue;
                    }
                    else if (timeTable.getType() == Type.SATURDAY && i == 1) {
                        continue;
                    }
                    else if (timeTable.getType() == Type.SUNDAY && i == 2) {
                        continue;
                    }
                    
                    String url = createUrl(query) + "&kind=" + kind[i];
                    response = StreamUtils.getHttpResponse(url);
                    result.setResponseCode(response.getResponseCode());
                    if (response.isOK()) {
                        ret = createParser(query, response).parse(response.getInputStream(), query);
                        TimeTable tt = getResultTimeTable(ret);
                        if (!tt.getTimeLines().isEmpty()) {
                            station.addTimeTable(tt);
                        }
                    }
                }
                
                // 環状線はなぜか1方向しか表示されていないので、逆方向も取得
                if (isCircualrLine(line)) {
                    try {
                        int gid = Integer.parseInt(timeTable.getMyGid());
                        String nextGid = String.valueOf(gid+1); 
                        if (!timeTable.getGidList().contains(nextGid)) {
                            timeTable.getGidList().add(nextGid);
                        }
                    }
                    catch (Exception e) {
                    }
                }
                
                // 上から辿れない路線を持っている場合は含める
                String[] extra = getExtraGid(query);
                if (extra != null) {
                    timeTable.getGidList().addAll(Arrays.asList(extra));
                }

                // 取得したgidから残りの方面を検索
                for (int i = 0; i < timeTable.getGidList().size(); i ++) {
                    String gid = timeTable.getGidList().get(i);
                    
                    for (int j = 0; j < kind.length; j ++) {
                        // 自身のgidがURLに存在する場合は省く
                        String url = createUrl(query).replaceAll("&gid=" + timeTable.getMyGid(), "");
                        url = url + "&gid=" + gid + "&kind=" + kind[j];

                        response = StreamUtils.getHttpResponse(url);
                        result.setResponseCode(response.getResponseCode());
                        if (response.isOK()) {
                            ret = createParser(query, response).parse(response.getInputStream(), query);
                            TimeTable tt = getResultTimeTable(ret);
                            if (!tt.getTimeLines().isEmpty()) {
                                station.addTimeTable(tt);
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
            }
        }
        
        return result;
    }
    
    private boolean isCircualrLine(Line line) {
        String[] lines = new String[]{
                "山手線",
                "大阪環状線",
                "名城線",
        };
        
        return Arrays.asList(lines).contains(line.getName());
    }
    
    private TimeTable getResultTimeTable(TimeTableResult result) {
        return result.getAreas().get(0).getPrefectures().get(0).getLines().get(0).getStations().get(0).getTimeTables().get(0);
    }
    
    private boolean isAreaSearch(TimeTableQuery query) {
        return query.getArea() == null && query.getPrefecture() == null && query.getLine() == null && query.getStation() == null;
    }
    
    private boolean isLineSearch(TimeTableQuery query) {
        return query.getArea() != null && query.getPrefecture() != null && query.getLine() == null && query.getStation() == null;
    }
    
    private boolean isStationSearch(TimeTableQuery query) {
        return query.getArea() != null && query.getPrefecture() != null && query.getLine() != null && query.getStation() == null;
    }
    
    private boolean isTimeTableSearch(TimeTableQuery query) {
        return query.getArea() != null && query.getPrefecture() != null && query.getLine() != null && query.getStation() != null;
    }
}
