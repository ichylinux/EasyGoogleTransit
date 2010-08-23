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
package jp.co.hybitz.traveldelay.parser;

import jp.co.hybitz.common.SimpleXmlPullParser;
import jp.co.hybitz.traveldelay.TravelDelayParser;
import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.OperationCompany;
import jp.co.hybitz.traveldelay.model.TravelDelay;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;

import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooDetailParser20100823Test extends GooDetailParser20100821Test {

    @Override
    protected TravelDelayParser getParser() throws XmlPullParserException {
        return new GooDetailParser20100823(new SimpleXmlPullParser());
    }    

    public void testParse20100823_departure() {
        TravelDelayParser parser = null;
        try {
            parser = getParser();
            parser.setEncoding("EUC-JP");
            parser.setAirline(true);
            parser.setArrival(false);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        TravelDelayResult result = null;
        try {
            result = parser.parse(getClass().getResourceAsStream("/travel_delay_result_detail_20100823.html"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getCategories().size());

        Category c = result.getCategories().get(0);
        OperationCompany oc = c.getOperationCompanies().get(0);
        assertEquals(4, oc.getTravelDelays().size());
        TravelDelay td = oc.getTravelDelays().get(3);
        assertEquals("08/23 08:30", td.getDate());
        assertEquals("日本航空", td.getAirline());
        assertEquals("旭川", td.getPlace());
        assertEquals("JAL1103便(羽田→旭川) は出発に遅れが出ています。07:25→08:29", td.getCondition());
    }

    public void testParse20100823_arrival() {
        TravelDelayParser parser = null;
        try {
            parser = getParser();
            parser.setEncoding("EUC-JP");
            parser.setAirline(true);
            parser.setArrival(true);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        TravelDelayResult result = null;
        try {
            result = parser.parse(getClass().getResourceAsStream("/travel_delay_result_detail_20100823.html"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getCategories().size());

        Category c = result.getCategories().get(0);
        OperationCompany oc = c.getOperationCompanies().get(0);
        assertEquals(3, oc.getTravelDelays().size());
        TravelDelay td = oc.getTravelDelays().get(1);
        assertEquals("08/23 11:30", td.getDate());
        assertEquals("全日本空輸", td.getAirline());
        assertEquals("三宅島", td.getPlace());
        assertEquals("定刻13:40着 ANA1850便(三宅島→羽田) は欠航です。", td.getCondition());
    }
}
