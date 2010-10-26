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

import jp.co.hybitz.common.AbstractParser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.transit.model.Station;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author ichy <ichylinux@gmail.com>
 */
class GooMobileStationParser extends AbstractParser<TransitQuery, TransitResult> {
    private TransitQuery query;
    private TransitResult result;
	private Station station;
	private boolean inForm;
	private boolean inFromCode;
	private boolean inToCode;
	private boolean inByCode;
	private boolean inOption;

	public GooMobileStationParser(Platform platform, String encoding) {
	    super(platform, encoding);
	}

    @Override
    protected void startDocument(TransitQuery in) {
        query = in;
        result = new TransitResult();
    }

    @Override
    protected TransitResult endDocument() {
        return result;
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (isForm("/transit/train_routes/result")) {
            inForm = true;
        }
        else if (inForm && isSelect("from_code")) {
            inFromCode = true;
        }
        else if (inForm && isSelect("to_code")) {
            inToCode = true;
        }
        else if (inForm && isSelect("sv1")) {
            inByCode = true;
        }
        else if (inFromCode && isOption()) {
            inOption = true;
            station = new Station();
            station.setCode(getAttribute("value").replaceAll("'", ""));
        }
        else if (inToCode && isOption()) {
            inOption = true;
            station = new Station();
            station.setCode(getAttribute("value").replaceAll("'", ""));
        }
        else if (inByCode && isOption()) {
            inOption = true;
            station = new Station();
            station.setCode(getAttribute("value").replaceAll("'", ""));
        }
        else if (inForm && isInput()) {
            if ("from_code".equals(getAttribute("name"))) {
                station = new Station();
                station.setName(normalize(query.getFrom()));
                station.setCode(getAttribute("value"));
                result.addFromStation(station);
            }
            else if ("to_code".equals(getAttribute("name"))) {
                station = new Station();
                station.setName(normalize(query.getTo()));
                station.setCode(getAttribute("value"));
                result.addToStation(station);
            }
            else if ("sv1".equals(getAttribute("name"))) {
                station = new Station();
                station.setName(normalize(query.getStopOver()));
                station.setCode(getAttribute("value"));
                result.addStopOverStation(station);
            }
        }

        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (isForm()) {
            inForm = false;
            return true;
        }
        else if (inForm && isSelect()) {
            inFromCode = false;
            inToCode = false;
            inByCode = false;
        }
        else if (inFromCode && isOption()) {
            result.addFromStation(station);
            station = null;
            inOption = false;
        }
        else if (inToCode && isOption()) {
            result.addToStation(station);
            station = null;
            inOption = false;
        }
        else if (inByCode && isOption()) {
            result.addStopOverStation(station);
            station = null;
            inOption = false;
        }

        return false;
    }

    @Override
    protected boolean text(String text, XmlPullParser parser) {
        if (inOption) {
            station.setName(normalize(text));
        }
        
        return false;
    }
    
    private String normalize(String name) {
        String ret = name.replaceFirst("▼", "");
        ret = ret.replaceAll("\\(", "（");
        ret = ret.replaceAll("\\)", "）");
        return ret;
    }
}
