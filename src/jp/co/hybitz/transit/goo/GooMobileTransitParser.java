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

import java.util.ArrayList;
import java.util.List;

import jp.co.hybitz.common.AbstractParser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.model.Time;
import jp.co.hybitz.transit.model.TimeAndPlace;
import jp.co.hybitz.transit.model.TimeType;
import jp.co.hybitz.transit.model.Transit;
import jp.co.hybitz.transit.model.TransitDetail;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author ichy <ichylinux@gmail.com>
 */
class GooMobileTransitParser extends AbstractParser<TransitQuery, TransitResult> {
    private TransitResult result;
	private Transit transit;
	private List<String> details;
	private boolean inTitle;
	private boolean inTransit;
	private boolean inDiv;
	private boolean inTable;
	
	public GooMobileTransitParser(Platform platform) {
	    super(platform);
	}

    public GooMobileTransitParser(Platform platform, String encoding) {
        super(platform, encoding);
    }

    @Override
    protected void startDocument(TransitQuery in) {
        result = new TransitResult();
    }

    @Override
    protected TransitResult endDocument() {
        return result;
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (is("title")) {
            inTitle = true;
        }
        else if (isDiv()) {
            inDiv = true;
        }
        else if (isTable()) {
            inTable = true;
        }

        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (is("title")) {
            inTitle = false;
        }
        else if (isDiv()) {
            inDiv = false;
        }
        else if (isTable()) {
            inTable = false;
        }

        return false;
    }

    @Override
    protected boolean text(String text, XmlPullParser parser) {
        if (inTitle) {
            handleTitle(text);
        }
        else if (inTransit) {
            if (inTable) {
                if ("再検索".equals(text)) {
                    List<TransitDetail> list = createTransitDetails();
                    if (!list.isEmpty()) {
                        transit.setDetails(list);
                        result.addTransit(transit);
                    }
                    transit = null;
                    details = null;
                    inTransit = false;
                }
                else {
                    text = text.replaceAll("&nbsp;", "");
                    
                    if (details == null) {
                        if (transit.getDuration() == null) {
                            if (text.matches("([0-9],)*[0-9]+分")) {
                                transit.setDuration(text);
                            }
                        }
                        else if (transit.getFare() == null) {
                            int index = text.indexOf("乗換");
                            if (index >= 0) {
                                text = text.substring(0, index);
                            }
                            transit.setFare(text + "円");
                            details = new ArrayList<String>();
                        }
                    }
                    else {
                        details.add(text);
                    }
                }
            }
        }
        else if (inDiv) {
            if (inTable) {
                if (text.matches("経路(１|２|３)&nbsp;")) {
                    inTransit = true;
                    transit = new Transit();
                }
                else if ("メニュー".equals(text)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    private void handleTitle(String title) {
        String[] split = title.replaceAll(" - goo路線", "").split("　");

        String[] locations = split[0].split("～");
        result.setFrom(locations[0]);
        result.setTo(locations[1]);
        
        if (split[2].endsWith("発")) {
            result.setTimeType(TimeType.DEPARTURE);
            String[] time = split[2].replaceAll("発", "").split(":");
            result.setTime(new Time(time[0], time[1]));
        }
        else if (split[2].endsWith("着")) {
            result.setTimeType(TimeType.ARRIVAL);
            String[] time = split[2].replaceAll("着", "").split(":");
            result.setTime(new Time(time[0], time[1]));
        }
        else if ("終電".equals(split[2])) {
            result.setTimeType(TimeType.LAST);
        }
    }
    
    private List<TransitDetail> createTransitDetails() {
        List<TransitDetail> ret = new ArrayList<TransitDetail>();
        TransitDetail td = null;
        
        for (int i = 0; i < details.size(); i ++) {
            String s = details.get(i);
            
            if ("↓".equals(s)) {
                String route = details.get(++i);
                if ("[徒歩]".equals(route)) {
                    td.setRoute("徒歩");
                }
                else {
                    td.setRoute(route);
                }
            }
            else if (s.matches("[0-9]{2}:[0-9]{2}")) {
                String depOrArr = details.get(++i);
                if ("発".equals(depOrArr)) {
                    if (td != null) {
                        // 徒歩の場合は追加
                        if (td.isWalking()) {
                            ret.add(td);
                        }
                        // 到着地がない場合は通過と思われるので無視
                        else if (td.getArrival() != null) {
                            ret.add(td);
                        }
                    }

                    String place = details.get(i+1);
                    if ("↓".equals(place)) {
                        // 出発地が特定できないパターンはおそらくルートなし
                        if (td == null) {
                            return new ArrayList<TransitDetail>();
                        }

                        place = td.getArrival().getPlace();
                    }
                    
                    if (td == null) {
                        td = new TransitDetail();
                        td.setDeparture(new TimeAndPlace(new Time(s), place));
                    }
                    else if (td.isWalking()) {
                        td = new TransitDetail();
                        td.setDeparture(new TimeAndPlace(new Time(s), place));
                    }
                    else if (td.getArrival() != null) {
                        td = new TransitDetail();
                        td.setDeparture(new TimeAndPlace(new Time(s), place));
                    }
                }
                else if ("着".equals(depOrArr)) {
                    String place = details.get(++i);
                    td.setArrival(new TimeAndPlace(new Time(s), place));
                }
            }
        }
        
        if (td != null) {
            ret.add(td);
        }
        
        return ret;
    }
}
