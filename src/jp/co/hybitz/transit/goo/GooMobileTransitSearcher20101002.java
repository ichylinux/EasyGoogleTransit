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
import android.util.Log;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooMobileTransitSearcher20101002 extends AbstractTransitSearcher implements GooConst {	
	private Platform platform;

	public GooMobileTransitSearcher20101002(Platform platform) {
		this.platform = platform;
	}

    /**
     * @see jp.co.hybitz.common.Searcher#createParser()
     */
    @Override
    public Parser<TransitQuery, TransitResult> createParser(TransitQuery query, HttpResponse response) {
        if (response.getUrl().startsWith(TRANSIT_RESULT_URL)) {
            return new GooMobileTransitParser20101002(platform, ENCODING);
        }
        else {
            return new GooMobileStationParser20100930(platform, ENCODING); 
        }
    }

    @Override
    public String createUrl(TransitQuery query) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(query.getFromCode()) && StringUtils.isNotEmpty(query.getToCode())) {
            sb.append(TRANSIT_RESULT_URL);
        }
        else {
            sb.append(TRANSIT_SEARCH_URL);
        }

        // 出発地
        sb.append("?from_station=").append(StringUtils.urlEncode(query.getFrom(), ENCODING));
        if (StringUtils.isNotEmpty(query.getFromCode())) {
            sb.append("&from_code=").append(query.getFromCode());
        }

        // 到着地
        sb.append("&to_station=").append(StringUtils.urlEncode(query.getTo(), ENCODING));
        if (StringUtils.isNotEmpty(query.getToCode())) {
            sb.append("&to_code=").append(query.getToCode());
        }
        
        // TODO 経由
        
        // 日付・時刻
        if (query.getTimeType() != null) {
            if (query.getTimeType() == TimeType.DEPARTURE) {
                sb.append("&must=0");
                if (query.getDate() != null) {
                    sb.append("&date=").append(new SimpleDateFormat("yyyyMMdd").format(query.getDate()));
                    sb.append("&time=").append(new SimpleDateFormat("HHmm").format(query.getDate()));
                }
            }
            else if (query.getTimeType() == TimeType.ARRIVAL) {
                sb.append("&must=1");
                if (query.getDate() != null) {
                    sb.append("&date=").append(new SimpleDateFormat("yyyyMMdd").format(query.getDate()));
                    sb.append("&time=").append(new SimpleDateFormat("HHmm").format(query.getDate()));
                }
            }
            else if (query.getTimeType() == TimeType.LAST) {
                sb.append("&must=2");
                if (query.getDate() != null) {
                    sb.append("&date=").append(new SimpleDateFormat("yyyyMMdd").format(query.getDate()));
                }
            }
        }
        
        // 有料特急
        sb.append("&ex=" + (query.isUseExpress() ? "1" : "0"));
        
        // 表示順
        if ("time".equals(query.getSort())) {
            sb.append("&pri=0");
        }
        else if ("fare".equals(query.getSort())) {
            sb.append("&pri=1");
        }
        else if ("num".equals(query.getSort())) {
            sb.append("&pri=2");
        }

        Log.i("HELLO", sb.toString());
        return sb.toString();
    }

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
}
