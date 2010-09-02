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

import java.math.BigDecimal;

import jp.co.hybitz.common.HttpResponse;
import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.stationapi.StationApiSearcher;
import jp.co.hybitz.stationapi.model.StationApiQuery;
import jp.co.hybitz.stationapi.model.StationApiResult;
import jp.co.hybitz.stationapi.parser.StationApiParser20100825;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class StationApiSearcher20100825 implements StationApiSearcher {
	private Platform platform;
	
	public StationApiSearcher20100825(Platform platform) {
		this.platform = platform;
	}
	
	public StationApiResult search(StationApiQuery query) throws HttpSearchException {
        HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));

        try {
            StationApiResult result = response.isOK() ? createParser(query).parse(response.getInputStream(), query) : new StationApiResult();
            result.setResponseCode(response.getResponseCode());
            result.setGeoLocation(query.getGeoLocation());
            return result;
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
	}
	
	public Parser<StationApiQuery, StationApiResult> createParser(StationApiQuery query) {
		return new StationApiParser20100825(platform);
	}
	
	public String createUrl(StationApiQuery query) {
	    BigDecimal longitude = new BigDecimal(query.getGeoLocation().getLongitude());
	    longitude = longitude.setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal latitude = new BigDecimal(query.getGeoLocation().getLatitude());
        latitude = latitude.setScale(4, BigDecimal.ROUND_HALF_UP);
	    
        return STATION_API_URL + "?longitude=" + longitude + "&latitude=" + latitude;
	}
}
