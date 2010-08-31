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
package jp.co.hybitz.stationapi.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.XmlPullParserFactory;
import jp.co.hybitz.stationapi.model.Station;
import jp.co.hybitz.stationapi.model.StationApiResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class StationApiParser20100825 implements Parser<StationApiResult> {
    private Platform platform;
    private StationApiResult result = new StationApiResult();
	private Station station;
	private String name;
	
	public StationApiParser20100825(Platform platform) {
        this.platform = platform;
	}
	
    @Override
	public StationApiResult parse(InputStream in) throws XmlPullParserException, IOException {
	    try {
	        XmlPullParser parser = XmlPullParserFactory.getParser(platform);
	        parser.setInput(in, null);
    
    		int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_TAG :
                    name = parser.getName();
                    if ("station".equalsIgnoreCase(name)) {
                        station = new Station();
                    }
                    break;
                case XmlPullParser.TEXT :
                    String text = parser.getText().trim();
                    if ("name".equalsIgnoreCase(name)) {
                        station.setName(text);
                    }
                    else if ("furigana".equalsIgnoreCase(name)) {
                        station.setFurigana(text);
                    }
                    else if ("line".equalsIgnoreCase(name)) {
                        station.setLine(text);
                    }
                    else if ("url".equalsIgnoreCase(name)) {
                        station.setUrl(text);
                    }
                    else if ("city".equalsIgnoreCase(name)) {
                        station.setCity(text);
                    }
                    else if ("prefecture".equalsIgnoreCase(name)) {
                        station.setPrefecture(text);
                    }
                    else if ("direction".equalsIgnoreCase(name)) {
                        station.setDirection(text);
                    }
                    else if ("directionReverse".equalsIgnoreCase(name)) {
                        station.setDirectionReverse(text);
                    }
                    else if ("distance".equalsIgnoreCase(name)) {
                        station.setDistance(Integer.parseInt(text));
                    }
                    else if ("distanceM".equalsIgnoreCase(name)) {
                        station.setDistanceM(text);
                    }
                    else if ("distanceKm".equalsIgnoreCase(name)) {
                        station.setDistanceKm(text);
                    }
                    else if ("traveltime".equalsIgnoreCase(name)) {
                        station.setTravelTime(text);
                    }
                    break;
                case XmlPullParser.END_TAG :
                    if ("station".equalsIgnoreCase(parser.getName())) {
                        result.addStation(station);
                        station = null;
                    }
                    else {
                        name = null;
                    }
                    break;
                }
    
                try {
                    eventType = parser.next();
                }
                catch (XmlPullParserException e) {
                }
                catch (EOFException e) {
                    break;
                }
            }

            return result;
	    }
	    finally {
	        try { in.close(); } catch (IOException e) {}
	    }
	}
}
