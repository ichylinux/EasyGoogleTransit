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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import jp.co.hybitz.googletransit.Platform;
import jp.co.hybitz.googletransit.TransitSearchException;
import jp.co.hybitz.googletransit.TransitSearcher;
import jp.co.hybitz.googletransit.model.TransitQuery;
import jp.co.hybitz.googletransit.model.TransitResult;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Xml;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileSearcher20100517 implements TransitSearcher {
	private static final String GOOGLE = "http://www.google.co.jp/m/directions";
	
	private Platform platform;
	
	public MobileSearcher20100517(Platform platform) {
		this.platform = platform;
	}
	
	public TransitResult search(TransitQuery query) throws TransitSearchException {
		InputStream in = null;

		try {
			URL url = new URL(GOOGLE + createQueryString(query));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			in = con.getInputStream();
			
			TransitParser transitParser = null;
			if (platform == Platform.ANDROID) {
				transitParser = new MobileParser20100517(Xml.newPullParser());
			} else if (platform == Platform.GENERIC) {
			    transitParser = new MobileParser20100517(XmlPullParserFactory.newInstance().newPullParser());
			} else {
				throw new UnsupportedOperationException("サポートしていないプラットフォームです。");
			}
			
			return transitParser.parse(in);
			
		} catch (MalformedURLException e) {
		    throw new TransitSearchException(e.getMessage(), e);
		} catch (ProtocolException e) {
            throw new TransitSearchException(e.getMessage(), e);
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
	
	private String createQueryString(TransitQuery query) {
		StringBuilder sb = new StringBuilder();
		sb.append("?saddr=").append(URLEncoder.encode(query.getFrom()));
		sb.append("&daddr=").append(URLEncoder.encode(query.getTo()));
		sb.append("&time=&ttype=dep&ie=UTF8&f=d&dirmode=transit&num=3&dirflg=r");
		return sb.toString();
	}
}
