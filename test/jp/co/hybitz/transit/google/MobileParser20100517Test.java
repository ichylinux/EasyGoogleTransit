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
package jp.co.hybitz.transit.google;

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.ParserTestCase;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.transit.google.MobileParser20100827;
import jp.co.hybitz.transit.model.TimeType;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileParser20100517Test extends ParserTestCase {
    
    @Override
    protected Parser<TransitQuery, TransitResult> getParser() {
        return new MobileParser20100827(Platform.GENERIC);
    }    

    public void testParse20100517() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit_result_20100517.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("出発地", "八丁堀駅（東京）", result.getFrom());
        assertEquals("到着地", "東札幌駅（北海道）", result.getTo());
        assertEquals("時刻タイプ", TimeType.DEPARTURE, result.getTimeType());
        assertEquals("時刻", "0702", result.getTime().getTimeAsString());
        assertEquals("候補は3件", 3, result.getTransitCount());
    }

}
