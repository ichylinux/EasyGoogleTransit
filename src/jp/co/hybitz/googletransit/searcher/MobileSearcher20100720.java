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

import jp.co.hybitz.googletransit.GoogleConst;
import jp.co.hybitz.googletransit.Platform;
import jp.co.hybitz.googletransit.TransitParser;
import jp.co.hybitz.googletransit.TransitSearchException;
import jp.co.hybitz.googletransit.TransitSearcher;
import jp.co.hybitz.googletransit.TransitUtil;
import jp.co.hybitz.googletransit.model.Time;
import jp.co.hybitz.googletransit.model.TimeType;
import jp.co.hybitz.googletransit.model.TransitQuery;
import jp.co.hybitz.googletransit.model.TransitResult;
import jp.co.hybitz.googletransit.parser.MobileParser20100722;
import jp.co.hybitz.util.StreamUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Xml;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileSearcher20100720 implements TransitSearcher, GoogleConst {
	
	private Platform platform;
	
	public MobileSearcher20100720(Platform platform) {
		this.platform = platform;
	}
	
	public TransitResult search(TransitQuery query) throws TransitSearchException {
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
		catch (IOException e) {
            throw new TransitSearchException(e.getMessage(), baos.toByteArray(), e);
		}
		catch (XmlPullParserException e) {
            throw new TransitSearchException(e.getMessage(), baos.toByteArray(), e);
		}
		catch (Exception e) {
            throw new TransitSearchException(e.getMessage(), baos.toByteArray(), e);
		}
	}
	
	private TransitParser createParser() throws XmlPullParserException {
		XmlPullParser xmlParser = createXmlPullParser();
        return new MobileParser20100722(xmlParser);
	}
	
	private XmlPullParser createXmlPullParser() throws XmlPullParserException {
        if (platform == Platform.ANDROID) {
            return Xml.newPullParser();
        }
        else if (platform == Platform.GENERIC) {
            return XmlPullParserFactory.newInstance().newPullParser();
        }
        else {
            throw new UnsupportedOperationException("サポートしていないプラットフォームです。");
        }
	}
	
	protected HttpURLConnection openConnection(TransitQuery query) throws IOException {
        URL url = new URL(GOOGLE_TRANSIT_MOBILE_URL + createQueryString(query));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        return con;
	}
	
	private TransitResult searchFirst(TransitQuery query) throws TransitSearchException {
        // 始発検索の場合は、まず終電を検索して、始発検索用の出発時刻を算出する
	    TransitQuery queryForLast = new TransitQuery();
	    queryForLast.setFrom(query.getFrom());
	    queryForLast.setTo(query.getTo());
	    queryForLast.setTimeType(TimeType.LAST);
	    queryForLast.setDate(query.getDate());
	    queryForLast.setUseExpress(query.isUseExpress());
	    queryForLast.setUseAirline(query.isUseAirline());
	    
	    TransitResult result = search(queryForLast);
	    if (result.getResponseCode() != HttpURLConnection.HTTP_OK) {
	        return result;
	    }
	    
	    // ざっくりと、、
	    // 最後の出発時刻が現在時刻より遅い場合は今日の始発を検索
	    // 現在時刻が最後の出発時刻を過ぎている場合は翌日の始発を検索
	    // 始発検索はGoogleトランジットにはないので、時刻は最終が出発した1分後で検索する
        Calendar c = Calendar.getInstance();
        c.setTime(query.getDate());
	    Time timeToSearch = TransitUtil.getLastDepartureTime(result);
        if (timeToSearch != null) {
            Time now = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

            c.set(Calendar.HOUR_OF_DAY, timeToSearch.getHour());
            c.set(Calendar.MINUTE, timeToSearch.getMinute());

            if (timeToSearch.before(now)) {
                c.add(Calendar.DATE, 1);
            }
            
            c.add(Calendar.MINUTE, 1);
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
