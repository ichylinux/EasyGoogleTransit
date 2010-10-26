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

import jp.co.hybitz.common.model.Time;
import jp.co.hybitz.transit.model.TimeType;
import jp.co.hybitz.transit.model.Transit;
import jp.co.hybitz.transit.model.TransitResult;

public class GooMobileTransitParser20101026Test extends GooMobileTransitParser20101013Test {

    public void testParse20101026() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/goo/transit_result_20101026.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("せんげん台", result.getFrom());
        assertEquals("長津田", result.getTo());
        assertEquals(TimeType.DEPARTURE, result.getTimeType());
        assertEquals(new Time(6, 46), result.getTime());
        assertEquals(3, result.getTransitCount());
        
        Transit t = result.getTransits().get(0);
        assertEquals(1, t.getTransferCount());
        assertEquals("せんげん台", t.getDetails().get(0).getDeparture().getPlace());
        assertEquals("06:55", t.getDetails().get(0).getDeparture().getTime().toString());
        assertEquals("渋谷", t.getDetails().get(0).getArrival().getPlace());
        assertEquals("08:05", t.getDetails().get(0).getArrival().getTime().toString());
        assertEquals("渋谷", t.getDetails().get(1).getDeparture().getPlace());
        assertEquals("08:09", t.getDetails().get(1).getDeparture().getTime().toString());
        assertEquals("長津田", t.getDetails().get(1).getArrival().getPlace());
        assertEquals("08:40", t.getDetails().get(1).getArrival().getTime().toString());
        
        t = result.getTransits().get(1);
        assertEquals(2, t.getTransferCount());
        assertEquals("せんげん台", t.getDetails().get(0).getDeparture().getPlace());
        assertEquals("06:51", t.getDetails().get(0).getDeparture().getTime().toString());
        assertEquals("北千住", t.getDetails().get(0).getArrival().getPlace());
        assertEquals("07:17", t.getDetails().get(0).getArrival().getTime().toString());
        assertEquals("北千住", t.getDetails().get(1).getDeparture().getPlace());
        assertEquals("07:24", t.getDetails().get(1).getDeparture().getTime().toString());
        assertEquals("表参道", t.getDetails().get(1).getArrival().getPlace());
        assertEquals("07:55", t.getDetails().get(1).getArrival().getTime().toString());
        assertEquals("表参道", t.getDetails().get(2).getDeparture().getPlace());
        assertEquals("08:05", t.getDetails().get(2).getDeparture().getTime().toString());
        assertEquals("長津田", t.getDetails().get(2).getArrival().getPlace());
        assertEquals("08:40", t.getDetails().get(2).getArrival().getTime().toString());
    }
}
