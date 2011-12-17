/**
 * Copyright (C) 2010-2011 Hybitz.co.ltd
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
package jp.co.hybitz.transit.google;

import java.text.SimpleDateFormat;

import jp.co.hybitz.common.HttpResponse;
import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.common.StringUtils;
import jp.co.hybitz.transit.AbstractTransitSearcher;
import jp.co.hybitz.transit.model.TimeType;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileSearcher20100827 extends AbstractTransitSearcher {
    private static final String GOOGLE_TRANSIT_MOBILE_URL = "http://www.google.co.jp/m/directions";
    private static final String ENCODING = "UTF-8";
	
	private Platform platform;
	
	public MobileSearcher20100827(Platform platform) {
		this.platform = platform;
	}
	
    /**
     * @see jp.co.hybitz.common.Searcher#createParser()
     */
    public Parser<TransitQuery, TransitResult> createParser(TransitQuery query, HttpResponse response) {
        return new MobileParser20111218(platform);
    }
    
    @Override
    protected TransitResult searchImpl(TransitQuery query) throws HttpSearchException {
        HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));

        try {
            TransitResult result = response.isOK() ? createParser(query, response).parse(response.getInputStream(), query) : new TransitResult();
            result.setResponseCode(response.getResponseCode());
            result.setRawResponse(response.getRawResponse());
            result.setQueryDate(query.getDate());
            return result;
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
	}
    
    public String createUrl(TransitQuery query) {
		StringBuilder sb = new StringBuilder(GOOGLE_TRANSIT_MOBILE_URL);
		
        // 出発地
        sb.append("?saddr=").append(StringUtils.urlEncode(query.getFrom(), ENCODING));

        // 到着地
        sb.append("&daddr=").append(StringUtils.urlEncode(query.getTo(), ENCODING));
		
        // 日付・時刻
        if (query.getTimeType() != null) {
            if (query.getTimeType() == TimeType.DEPARTURE) {
                sb.append("&ttype=dep");
                if (query.getDate() != null) {
                    sb.append("&date=").append(new SimpleDateFormat("yyyyMMdd").format(query.getDate()));
                    sb.append("&time=").append(new SimpleDateFormat("HHmm").format(query.getDate()));
                }
            }
            else if (query.getTimeType() == TimeType.ARRIVAL) {
                sb.append("&ttype=arr");
                if (query.getDate() != null) {
                    sb.append("&date=").append(new SimpleDateFormat("yyyyMMdd").format(query.getDate()));
                    sb.append("&time=").append(new SimpleDateFormat("HHmm").format(query.getDate()));
                }
            }
            else if (query.getTimeType() == TimeType.LAST) {
                sb.append("&ttype=last");
                if (query.getDate() != null) {
                    sb.append("&date=").append(new SimpleDateFormat("yyyyMMdd").format(query.getDate()));
                }
            }
        }
        
		// 有料特急
		if (!query.isUseExpress()) {
		    sb.append("&noexp=1");
		}
		
		// 飛行機
		if (!query.isUseAirline()) {
		    sb.append("&noal=1");
		}
		
		// 表示順
		if (StringUtils.isNotEmpty(query.getSort())) {
		    sb.append("&sort=" + query.getSort());
		}
		
		sb.append("&ie=UTF8&f=d&dirmode=transit&num=3&dirflg=r");
		return sb.toString();
	}
}
