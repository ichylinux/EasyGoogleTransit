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
package jp.co.hybitz.googletransit.searcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.common.XmlPullParserFactory;
import jp.co.hybitz.googletransit.GoogleConst;
import jp.co.hybitz.googletransit.TransitParser;
import jp.co.hybitz.googletransit.TransitSearcher;
import jp.co.hybitz.googletransit.TransitUtil;
import jp.co.hybitz.googletransit.model.Time;
import jp.co.hybitz.googletransit.model.TimeType;
import jp.co.hybitz.googletransit.model.TransitQuery;
import jp.co.hybitz.googletransit.model.TransitResult;
import jp.co.hybitz.googletransit.parser.MobileParser20100718;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
class MobileSearcher20100517 implements TransitSearcher, GoogleConst {
	
	private Platform platform;
	
	public MobileSearcher20100517(Platform platform) {
		this.platform = platform;
	}
	
	public TransitResult search(TransitQuery query) throws HttpSearchException {
	    if (query.getTimeType() == TimeType.FIRST) {
	        return searchFirst(query);
	    }
	    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
		    TransitResult result;
		    
		    HttpURLConnection con = openConnection(query);
		    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
		        StreamUtils.write(con.getInputStream(), baos);
		        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		        result = createParser().parse(bais);
		    }
		    else {
		        result = new TransitResult();
		    }
		    
            result.setResponseCode(con.getResponseCode());
            result.setQueryDate(query.getDate());
            return result;
		}
		catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(baos.toByteArray()), e);
		}
	}
	
	private TransitParser createParser() throws XmlPullParserException {
        XmlPullParser xmlParser = XmlPullParserFactory.getParser(platform);
        return new MobileParser20100718(xmlParser);
	}
	
	protected HttpURLConnection openConnection(TransitQuery query) throws IOException {
        URL url = new URL(GOOGLE_TRANSIT_MOBILE_URL + createQueryString(query));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        return con;
	}
	
	private TransitResult searchFirst(TransitQuery query) throws HttpSearchException {
        // 始発検索の場合は、まず終電を検索して、始発検索用の出発時刻を算出する
	    TransitQuery queryForLast = new TransitQuery();
	    queryForLast.setFrom(query.getFrom());
	    queryForLast.setTo(query.getTo());
	    queryForLast.setTimeType(TimeType.LAST);
	    queryForLast.setUseExpress(query.isUseExpress());
	    queryForLast.setUseAirline(query.isUseAirline());
	    
	    TransitResult result = search(queryForLast);
	    if (result.getResponseCode() != HttpURLConnection.HTTP_OK) {
	        return result;
	    }
	    
	    // ざっくりと、、
	    // 現在時刻が午前中の場合は当日の始発を検索
	    // 現在時刻が午後の場合は翌日の始発を検索
	    // 時刻は最終が出発した1時間後
        Calendar c = Calendar.getInstance();
	    if (c.get(Calendar.AM_PM) == Calendar.PM) {
	        c.add(Calendar.DATE, 1);
	    }
	    Time timeToSearch = TransitUtil.getFirstDepartureTime(result);
        if (timeToSearch != null) {
            c.set(Calendar.HOUR_OF_DAY, timeToSearch.getHour() + 1);
            c.set(Calendar.MINUTE, timeToSearch.getMinute());
        }
	    
        TransitQuery queryForFirst = new TransitQuery();
        queryForFirst.setFrom(query.getFrom());
        queryForFirst.setTo(query.getTo());
        queryForFirst.setTimeType(TimeType.DEPARTURE);
        queryForFirst.setDate(c.getTime());
        queryForFirst.setUseExpress(query.isUseExpress());
        queryForFirst.setUseAirline(query.isUseAirline());
	    
        TransitResult ret = search(queryForFirst);
        ret.setTimeType(TimeType.FIRST);
        ret.setTime(null);
        
        return ret;
	}
	
	private String createQueryString(TransitQuery query) {
		StringBuilder sb = new StringBuilder();
		
		// 出発地
		sb.append("?saddr=").append(URLEncoder.encode(query.getFrom()));
		
		// 到着地
		sb.append("&daddr=").append(URLEncoder.encode(query.getTo()));
		
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
		
		sb.append("&ie=UTF8&f=d&dirmode=transit&num=3&dirflg=r");
		return sb.toString();
	}
}
