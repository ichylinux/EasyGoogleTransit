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
package jp.co.hybitz.traveldelay.searcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.common.XmlPullParserFactory;
import jp.co.hybitz.traveldelay.TravelDelayParser;
import jp.co.hybitz.traveldelay.TravelDelaySearcher;
import jp.co.hybitz.traveldelay.model.TravelDelayQuery;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;
import jp.co.hybitz.traveldelay.parser.GooDetailParser20100823;
import jp.co.hybitz.traveldelay.parser.GooParser20100818;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooSearcher20100823 implements TravelDelaySearcher {
	private static final String ENCODING = "EUC-JP";
	private Platform platform;
	
	public GooSearcher20100823(Platform platform) {
		this.platform = platform;
	}
	
	public TravelDelayResult search(TravelDelayQuery query) throws HttpSearchException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
		    TravelDelayResult result;
		    
		    HttpURLConnection con = openConnection(query);
		    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StreamUtils.write(con.getInputStream(), baos);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		        result = createParser(query).parse(bais);
		    }
		    else {
		        result = new TravelDelayResult();
		    }
		    
            result.setResponseCode(con.getResponseCode());
            return result;
		}
		catch (Exception e) {
            try {
                throw new HttpSearchException(e.getMessage(), new String(baos.toByteArray(), ENCODING), e);
            }
            catch (UnsupportedEncodingException e1) {
                throw new IllegalStateException(e.getMessage(), e);
            }
		}
	}
	
	private TravelDelayParser createParser(TravelDelayQuery query) throws XmlPullParserException {
		XmlPullParser xmlParser = XmlPullParserFactory.getParser(platform);
		TravelDelayParser ret = null;
		if (query.getCategory() == null) {
		    ret = new GooParser20100818(xmlParser);
		}
		else {
            ret = new GooDetailParser20100823(xmlParser);
            ret.setAirline(query.getCategory().isAirline());
            ret.setArrival(query.getCategory().isArrival());
		}
		ret.setEncoding(ENCODING);
		return ret;
	}
	
	protected HttpURLConnection openConnection(TravelDelayQuery query) throws IOException {
	    if (query.getCategory() == null) {
            URL url = new URL(GOO_TRAVEL_DELAY_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            return con;
	    }
	    else {
            URL url = new URL(GOO_TRAVEL_URL + query.getCategory().getUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            return con;
	    }
	}
}
