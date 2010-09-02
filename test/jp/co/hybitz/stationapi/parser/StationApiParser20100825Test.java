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
package jp.co.hybitz.stationapi.parser;

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.ParserTestCase;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.stationapi.model.Station;
import jp.co.hybitz.stationapi.model.StationApiQuery;
import jp.co.hybitz.stationapi.model.StationApiResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class StationApiParser20100825Test extends ParserTestCase {

    @Override
    protected Parser<StationApiQuery, StationApiResult> getParser() {
        return new StationApiParser20100825(Platform.GENERIC);
    }    

    public void testParse20100825() {
        StationApiResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/station_api_result_20100825.xml"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(4, result.getStations().size());

        Station s = result.getStations().get(0);
        assertEquals("高幡不動駅", s.getName());
        assertEquals(1030, s.getDistance());
    }
}
