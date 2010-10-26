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

public class GooMobileStationParser20101012Test extends ParserTestCase {

    @Override
    protected Parser<TransitQuery, TransitResult> getParser() {
        return new GooMobileStationParser(Platform.HTML, "UTF-8");
    }    

    public void testParse20101012() {
        TransitQuery query = new TransitQuery();
        query.setFrom("品川");
        query.setTo("飯田橋");
        query.setStopOver("秋葉原");
        
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/goo/transit_result_station_20101012.html"), query);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("品川", result.getFromStations().get(0).getName());
        assertEquals("130255", result.getFromStations().get(0).getCode());
        assertEquals("品川シーサイド", result.getFromStations().get(1).getName());
        assertEquals("130256", result.getFromStations().get(1).getCode());
        assertEquals("飯田橋", result.getToStations().get(0).getName());
        assertEquals("130037", result.getToStations().get(0).getCode());
        assertEquals("秋葉原", result.getStopOverStations().get(0).getName());
        assertEquals("130016", result.getStopOverStations().get(0).getCode());
    }
}
