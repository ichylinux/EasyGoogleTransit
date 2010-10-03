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
package jp.co.hybitz.transit.goo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import jp.co.hybitz.common.HttpResponse;
import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.Searcher;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.transit.model.Station;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooMobileStationSearcher20100930 implements Searcher<TransitQuery, TransitResult>, GooConst {
	private Platform platform;

	public GooMobileStationSearcher20100930(Platform platform) {
		this.platform = platform;
	}

	/**
	 * @see jp.co.hybitz.common.Searcher#search(java.lang.Object)
	 */
    @Override
	public TransitResult search(TransitQuery query) throws HttpSearchException {
        HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));
        
        try {
            TransitResult result;
            if (response.isOK()) {
                if (response.getUrl().startsWith(TRANSIT_RESULT_URL)) {
                    String url = URLDecoder.decode(response.getUrl(), ENCODING);
                    String[] split = url.split("\\?");
                    String[] split2 = split[1].split("&");

                    Station from = new Station();
                    Station to = new Station();
                    for (String s : split2) {
                        if (s.startsWith("from_station=")) {
                            from.setName(query.getFrom());
                        }
                        else if (s.startsWith("from_code=")) {
                            from.setCode(s.split("=")[1]);
                        }
                        else if (s.startsWith("to_station=")) {
                            to.setName(query.getTo());
                        }
                        else if (s.startsWith("to_code=")) {
                            to.setCode(s.split("=")[1]);
                        }
                    }
                    
                    result = new TransitResult();
                    result.addFromStation(from);
                    result.addToStation(to);
                }
                else {
                    Parser<TransitQuery, TransitResult> parser = new GooMobileStationParser20100930(platform, ENCODING); 
                    result = parser.parse(response.getInputStream(), query);
                }
            }
            else {
                result = new TransitResult();
            }

            result.setResponseCode(response.getResponseCode());
            result.setRawResponse(response.getRawResponse());
            return result;
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
	}
	
    /**
     * @see jp.co.hybitz.common.Searcher#createParser()
     */
    @Override
    public Parser<TransitQuery, TransitResult> createParser(TransitQuery query, HttpResponse response) {
        return new GooMobileStationParser20100930(platform, ENCODING);
    }
    
    @Override
    public String createUrl(TransitQuery query) {
        try {
            StringBuilder sb = new StringBuilder(STATION_URL);
            
            // 出発地
            sb.append("?from_station=").append(URLEncoder.encode(query.getFrom(), ENCODING));
            
            // 到着地
            sb.append("&to_station=").append(URLEncoder.encode(query.getTo(), ENCODING));

            return sb.toString();
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * @see jp.co.hybitz.common.Searcher#cancel()
     */
    @Override
    public void cancel() {
    }    
}
