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
import jp.co.hybitz.transit.model.TransitResult;

public class GooMobileTransitParser20101013Test extends GooMobileTransitParser20101002Test {

    public void testParse20101013() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/goo/transit_result_20101013.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("品川", result.getFrom());
        assertEquals("飯田橋", result.getTo());
        assertEquals(TimeType.DEPARTURE, result.getTimeType());
        assertEquals(new Time(9, 35), result.getTime());
        assertEquals(0, result.getTransitCount());
    }
}
