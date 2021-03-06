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
package jp.co.hybitz.rgeocode;

import java.math.BigDecimal;

import jp.co.hybitz.common.HttpResponse;
import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.rgeocode.model.RGeocodeQuery;
import jp.co.hybitz.rgeocode.model.RGeocodeResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class RGeocodeSearcher20100826 implements RGeocodeSearcher {
    private static final String RGEOCODE_URL = "http://www.finds.jp/ws/rgeocode.php";
    private static final int RGEOCODE_STATUS_SUCCESS = 200;
    private static final int RGEOCODE_STATUS_SUCCESS_NO_LOCAL = 201;
    private static final int RGEOCODE_STATUS_SUCCESS_NO_AZA = 202;
    private static final int RGEOCODE_STATUS_INVALID_PARAMETERS = 400;
    private static final int RGEOCODE_STATUS_SERVER_ERROR = 500;

    private Platform platform;
	
	public RGeocodeSearcher20100826(Platform platform) {
		this.platform = platform;
	}
	
	public RGeocodeResult search(RGeocodeQuery query) throws HttpSearchException {
	    HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));

	    try {
            RGeocodeResult result = response.isOK() ? createParser().parse(response.getInputStream(), null) : new RGeocodeResult();
            result.setResponseCode(response.getResponseCode());
            result.setRawResponse(response.getRawResponse());
            result.setGeoLocation(query.getGeoLocation());
            return result;
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
    }

	private Parser<RGeocodeQuery, RGeocodeResult> createParser() {
		return new RGeocodeParser20100826(platform);
	}
	
	private String createUrl(RGeocodeQuery query) {
	    BigDecimal longitude = new BigDecimal(query.getGeoLocation().getLongitude());
	    longitude = longitude.setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal latitude = new BigDecimal(query.getGeoLocation().getLatitude());
        latitude = latitude.setScale(4, BigDecimal.ROUND_HALF_UP);
	    
        return RGEOCODE_URL + "?lon=" + longitude + "&lat=" + latitude;
	}
}
