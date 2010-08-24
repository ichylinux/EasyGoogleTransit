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
package jp.co.hybitz.stationapi.searcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.common.XmlPullParserFactory;
import jp.co.hybitz.stationapi.StationApiConst;
import jp.co.hybitz.stationapi.StationApiParser;
import jp.co.hybitz.stationapi.StationApiSearcher;
import jp.co.hybitz.stationapi.model.StationApiQuery;
import jp.co.hybitz.stationapi.model.StationApiResult;
import jp.co.hybitz.stationapi.parser.StationApiParser20100825;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class StationApiSearcher20100825 implements StationApiSearcher, StationApiConst {
	private Platform platform;
	
	public StationApiSearcher20100825(Platform platform) {
		this.platform = platform;
	}
	
	public StationApiResult search(StationApiQuery query) throws HttpSearchException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
		    StationApiResult result;
		    
		    HttpURLConnection con = openConnection(query);
		    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StreamUtils.write(con.getInputStream(), baos);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		        result = createParser().parse(bais);
		    }
		    else {
		        result = new StationApiResult();
		    }
		    
            result.setResponseCode(con.getResponseCode());
            return result;
		}
		catch (Exception e) {
		    throw new HttpSearchException(e.getMessage(), new String(baos.toByteArray()), e);
		}
	}
	
	private StationApiParser createParser() throws XmlPullParserException {
		XmlPullParser xmlParser = XmlPullParserFactory.getParser(platform);
		return new StationApiParser20100825(xmlParser);
	}
	
	protected HttpURLConnection openConnection(StationApiQuery query) throws IOException {
	    BigDecimal longitude = new BigDecimal(query.getLongitude());
	    longitude = longitude.setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal latitude = new BigDecimal(query.getLatitude());
        latitude = latitude.setScale(4, BigDecimal.ROUND_HALF_UP);
	    
        URL url = new URL(STATION_API_URL + "?longitude=" + longitude + "&latitude=" + latitude);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        return con;
	}
}
