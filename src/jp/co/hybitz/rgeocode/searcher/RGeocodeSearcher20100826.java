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
package jp.co.hybitz.rgeocode.searcher;

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
import jp.co.hybitz.rgeocode.RGeocodeConst;
import jp.co.hybitz.rgeocode.RGeocodeParser;
import jp.co.hybitz.rgeocode.RGeocodeSearcher;
import jp.co.hybitz.rgeocode.model.RGeocodeQuery;
import jp.co.hybitz.rgeocode.model.RGeocodeResult;
import jp.co.hybitz.rgeocode.parser.RGeocodeParser20100826;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class RGeocodeSearcher20100826 implements RGeocodeSearcher, RGeocodeConst {
	private Platform platform;
	
	public RGeocodeSearcher20100826(Platform platform) {
		this.platform = platform;
	}
	
	public RGeocodeResult search(RGeocodeQuery query) throws HttpSearchException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
		    RGeocodeResult result;
		    
		    HttpURLConnection con = openConnection(query);
		    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StreamUtils.write(con.getInputStream(), baos);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		        result = createParser().parse(bais);
		    }
		    else {
		        result = new RGeocodeResult();
		    }
		    
            result.setResponseCode(con.getResponseCode());
            result.setGeoLocation(query.getGeoLocation());
            return result;
		}
		catch (Exception e) {
		    throw new HttpSearchException(e.getMessage(), new String(baos.toByteArray()), e);
		}
	}
	
	private RGeocodeParser createParser() throws XmlPullParserException {
		XmlPullParser xmlParser = XmlPullParserFactory.getParser(platform);
		return new RGeocodeParser20100826(xmlParser);
	}
	
	protected HttpURLConnection openConnection(RGeocodeQuery query) throws IOException {
	    BigDecimal longitude = new BigDecimal(query.getGeoLocation().getLongitude());
	    longitude = longitude.setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal latitude = new BigDecimal(query.getGeoLocation().getLatitude());
        latitude = latitude.setScale(4, BigDecimal.ROUND_HALF_UP);
	    
        URL url = new URL(RGEOCODE_URL + "?lon=" + longitude + "&lat=" + latitude);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        return con;
	}
}
