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
package jp.co.hybitz.googletransit.parser;

import java.io.IOException;
import java.io.InputStream;

import jp.co.hybitz.googletransit.TransitParser;
import jp.co.hybitz.googletransit.TransitUtil;
import jp.co.hybitz.googletransit.model.Maybe;
import jp.co.hybitz.googletransit.model.Time;
import jp.co.hybitz.googletransit.model.TimeAndPlace;
import jp.co.hybitz.googletransit.model.TimeType;
import jp.co.hybitz.googletransit.model.Transit;
import jp.co.hybitz.googletransit.model.TransitDetail;
import jp.co.hybitz.googletransit.model.TransitResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileParser20100722 implements TransitParser {
    private static final int PARSE_TRANSIT_START = 1;
    private static final int PARSE_TRANSIT_END = 2;
    private static final int PARSE_MAYBE_START = 3;
    private static final int PARSE_MAYBE_END = 4;
    
    private XmlPullParser parser;
    private TransitResult result = new TransitResult();
	private Transit transit;
	private TransitDetail transitDetail;
	private int parseStatus = PARSE_TRANSIT_START;
	
	public MobileParser20100722(XmlPullParser parser) {
	    this.parser = parser;
	}

	public TransitResult parse(InputStream in) throws XmlPullParserException, IOException {
		parser.setInput(in, null);

        while (true) {
            int eventType = parser.getEventType();
            boolean stopParsing = false;

            switch (eventType) {
            case XmlPullParser.TEXT :
                String text = parser.getText().trim();
                
                if (parseStatus == PARSE_TRANSIT_START) {
                    stopParsing = handleText(text);
                }
                else {
                    stopParsing = handleMaybe(text);
                }

                break;
            }
            
            if (stopParsing) {
                break;
            }
            
            eventType = parser.next();
            if (eventType == XmlPullParser.END_DOCUMENT) {
                break;
            }
        }

        result.setPrefecture(TransitUtil.isSamePrefecture(result.getFrom(), result.getTo()));
		return result;
	}
	
	private boolean handleMaybe(String text) {
        if ("もしかして:".equals(text)) {
            parseStatus = PARSE_MAYBE_START;
        }
        else if ("表示モード:".equals(text)) {
            return true;
        }
        else if (parseStatus == PARSE_MAYBE_START) {
            if (text.matches(".*～.*")) {
                String[] split = text.split("～");
                Maybe m = new Maybe(split[0], split[1]);
                result.setMaybe(m);
                parseStatus = PARSE_MAYBE_END;
                return true;
            }
        }
        
        return false;
	}
	
	/**
	 * テキスト部分を解析します。
	 * 
	 * @param text
	 * @return true：検索結果のパースを終了し残りの情報のパースに遷移、false：検索結果のパース続行
	 */
	private boolean handleText(String text) {
	    if (text.matches(".*～.* [0-9]{1,2}:[0-9]{2}発")) {
	        handleSummary(text, TimeType.DEPARTURE);
	    }
	    else if (text.matches(".*～.* [0-9]{1,2}:[0-9]{2}着")) {
            handleSummary(text, TimeType.ARRIVAL);
        }
	    else if (text.matches(".*～.* 終電")) {
            handleSummary(text, TimeType.LAST);
	    }
	    else if (text.matches("([0-9]+日 )?([0-9]+(分|時間))+ - .*[0-9]*円")) {
	        handleTimeAndFare(text);
        }
        else if ("逆方向の経路を表示".equals(text)) {
            if (transit != null) {
                if (transitDetail != null) {
                    transit.addDetail(transitDetail);
                    transitDetail = null;
                }
                
                result.addTransit(transit);
            }
            transit = null;
            parseStatus = PARSE_TRANSIT_END;
        }
	    else if (text.length() > 0) {
            if (text.matches("[0-9]{1,2}:[0-9]{2}発 .*")) {
                handleDeparture(text);            }
            else if (text.matches("[0-9]{1,2}:[0-9]{2}着 .*")) {
                handleArrival(text);
            } else {
                handleRoute(text);
            }
        }
	    
	    return false;
	}
	
	private void handleSummary(String text, TimeType timeType) {
	    result.setTimeType(timeType);
	    
	    if (timeType == TimeType.LAST) {
	        String[] split = text.split("～");
	        result.setFrom(split[0].trim());
	        result.setTo(split[1].trim().split(" ")[0]);
	    }
	    else {
            String[] split = text.split("～");
            result.setFrom(split[0].trim());
            
            // 到着地にスペースが混じっている場合がある。最後のスペース以降が時刻となる
            String[] split2 = split[1].trim().split(" ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < split2.length - 1; i ++) {
            	if (i != 0) {
            		sb.append(" ");
            	}
            	sb.append(split2[i]);
            }
            result.setTo(sb.toString());
            
            String[] split3 = split2[split2.length - 1].replaceAll("(発|着)", "").split(":");
            result.setTime(new Time(split3[0], split3[1]));
	    }
	    
	}
	
	private void handleTimeAndFare(String text) {
        if (transit != null) {
            if (transitDetail != null) {
                transit.addDetail(transitDetail);
                transitDetail = null;
            }

            result.addTransit(transit);
        }
        transit = new Transit();
        transit.setDurationAndFare(text);
	}
	
	private void handleDeparture(String text) {
        String[] split = text.split("発 ");
        String[] splitTime = split[0].split(":");
        Time time = new Time(Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
        transitDetail.setDeparture(new TimeAndPlace(time, split[1]));
	}
	
	private void handleArrival(String text) {
        String[] split = text.split("着 ");
        String[] splitTime = split[0].split(":");
        Time time = new Time(Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]));
        transitDetail.setArrival(new TimeAndPlace(time, split[1]));
	}
	
	private void handleRoute(String text) {
		if (transit == null) {
			return;
		}
		
		if (transitDetail != null) {
			transit.addDetail(transitDetail);
		}
		
		transitDetail = new TransitDetail();
		transitDetail.setRoute(text);
	}
}
