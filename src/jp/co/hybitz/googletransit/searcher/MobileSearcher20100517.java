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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import jp.co.hybitz.googletransit.Platform;
import jp.co.hybitz.googletransit.TransitSearchException;
import jp.co.hybitz.googletransit.TransitSearcher;
import jp.co.hybitz.googletransit.model.TimeType;
import jp.co.hybitz.googletransit.model.TransitQuery;
import jp.co.hybitz.googletransit.model.TransitResult;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Xml;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileSearcher20100517 implements TransitSearcher, GoogleConst {
	
	private Platform platform;
	
	public MobileSearcher20100517(Platform platform) {
		this.platform = platform;
	}
	
	public TransitResult search(TransitQuery query) throws TransitSearchException {
		InputStream in = null;

		try {
		    HttpURLConnection con = openConnection(query);
		    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
		        return createParser().parse(con.getInputStream());
		    }
		    else {
		        TransitResult result = new TransitResult();
		        result.setResponseCode(con.getResponseCode());
		        return result;
		    }
		} catch (IOException e) {
            throw new TransitSearchException(e.getMessage(), e);
		} catch (XmlPullParserException e) {
            throw new TransitSearchException(e.getMessage(), e);
		} catch (Exception e) {
            throw new TransitSearchException(e.getMessage(), e);
		} finally {
			if (in != null) { try {in.close();} catch (IOException e){} }
		}
	}
	
	private TransitParser createParser() throws XmlPullParserException {
        if (platform == Platform.ANDROID) {
            return new MobileParser20100517(Xml.newPullParser());
        } else if (platform == Platform.GENERIC) {
            return new MobileParser20100517(XmlPullParserFactory.newInstance().newPullParser());
        } else {
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
	
	private String createQueryString(TransitQuery query) {
		StringBuilder sb = new StringBuilder();
		
		// 出発地
		sb.append("?saddr=").append(URLEncoder.encode(query.getFrom()));
		
		// 到着地
		sb.append("&daddr=").append(URLEncoder.encode(query.getTo()));
		
        // 出発時刻か到着時刻か
        if (query.getTimeType() != null) {
            String ttype = "";
            if (query.getTimeType() == TimeType.DEPARTURE) {
                ttype = "dep";
            }
            else if (query.getTimeType() == TimeType.ARRIVAL) {
                ttype = "arr";
            }
            else if (query.getTimeType() == TimeType.LAST) {
                ttype = "last";
            }
            sb.append("&ttype=").append(ttype);
        }
        
		// 日付
		if (query.getDate() != null && query.getDate().length() > 0) {
		    sb.append("&date=").append(query.getDate());
		}
		
		// 時刻
		if (query.getTime() != null && query.getTime().length() > 0) {
		    sb.append("&time=").append(query.getTime());
		}
		
		sb.append("&ie=UTF8&f=d&dirmode=transit&num=3&dirflg=r");
		return sb.toString();
	}
}
