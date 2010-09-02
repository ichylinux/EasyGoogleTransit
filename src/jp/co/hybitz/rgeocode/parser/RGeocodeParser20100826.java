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
package jp.co.hybitz.rgeocode.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.XmlPullParserFactory;
import jp.co.hybitz.rgeocode.model.RGeocodeQuery;
import jp.co.hybitz.rgeocode.model.RGeocodeResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class RGeocodeParser20100826 implements Parser<RGeocodeQuery, RGeocodeResult> {
    private Platform platform;
    private RGeocodeResult result = new RGeocodeResult();
	private String name;
	
	public RGeocodeParser20100826(Platform platform) {
        this.platform = platform;
	}
	
    @Override
	public RGeocodeResult parse(InputStream is, RGeocodeQuery in) throws XmlPullParserException, IOException {
	    try {
	        XmlPullParser parser = XmlPullParserFactory.getParser(platform);
    	    parser.setInput(is, null);
    
    		int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_TAG :
                    name = parser.getName();
                    break;
                case XmlPullParser.TEXT :
                    String text = parser.getText().trim();
                    if ("pname".equalsIgnoreCase(name)) {
                        result.setPrefecture(text);
                    }
                    else if ("mname".equalsIgnoreCase(name)) {
                        result.setMunicipality(text);
                    }
                    else if ("section".equalsIgnoreCase(name)) {
                        result.setLocal(text);
                    }
                    else if ("status".equalsIgnoreCase(name)) {
                        result.setStatus(Integer.parseInt(text));
                    }
                    break;
                case XmlPullParser.END_TAG :
                    name = null;
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
	        try { is.close(); } catch (IOException e) {}
	    }
	}
}
