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

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.ParserTestCase;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

public class GooMobileStationParser20100930Test extends ParserTestCase {

    @Override
    protected Parser<TransitQuery, TransitResult> getParser() {
        return new GooMobileStationParser20100930(Platform.HTML, "UTF-8");
    }    

    public void testParse20100930() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/goo/transit_result_station_20100930.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("東京", result.getFromStations().get(0).getName());
        assertEquals("130405", result.getFromStations().get(0).getCode());
        assertEquals("東京ディズニーシー", result.getFromStations().get(1).getName());
        assertEquals("120211", result.getFromStations().get(1).getCode());
        assertEquals("東京ディズニーランド", result.getFromStations().get(2).getName());
        assertEquals("120212", result.getFromStations().get(2).getCode());
        assertEquals("東京テレポート", result.getFromStations().get(3).getName());
        assertEquals("130406", result.getFromStations().get(3).getCode());
        assertEquals("東京ビッグサイト", result.getFromStations().get(4).getName());
        assertEquals("13PO07", result.getFromStations().get(4).getCode());
        assertEquals("東京ＦＴ", result.getFromStations().get(5).getName());
        assertEquals("13PO08", result.getFromStations().get(5).getCode());
        assertEquals("品川", result.getToStations().get(0).getName());
        assertEquals("130255", result.getToStations().get(0).getCode());
        assertEquals("品川シーサイド", result.getToStations().get(1).getName());
        assertEquals("130256", result.getToStations().get(1).getCode());
    }
}
