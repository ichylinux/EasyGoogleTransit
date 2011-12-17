/**
 * Copyright (C) 2011 Hybitz.co.ltd
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
import jp.co.hybitz.transit.model.Transit;
import jp.co.hybitz.transit.model.TransitDetail;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileParser20111218Test extends ParserTestCase {

    @Override
    protected Parser<TransitQuery, TransitResult> getParser() {
        return new MobileParser20111218(Platform.GENERIC);
    }    

    public void testParse20111218() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/google/transit_result_20111218.wml"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("枚方市駅（大阪）", result.getFrom());
        assertEquals("放出駅（大阪）", result.getTo());
        assertEquals("31 分", result.getTransits().get(0).getDuration());
    }

    public void testParse20111219() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/google/transit_result_20111219.wml"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("天王洲アイル駅（東京）", result.getFrom());
        assertEquals("秋葉原駅（東京）", result.getTo());
        
        Transit t = result.getTransits().get(0);
        assertEquals("40 分", t.getDuration());
        assertEquals("160円", t.getFare());
        
        TransitDetail d = t.getDetails().get(0);
        assertEquals("徒歩 - 品川駅（東京）", d.getRoute());
        
        d = t.getDetails().get(1);
        assertEquals("ＪＲ山手線 各停", d.getRoute());
        assertEquals("04:33", d.getDeparture().getTime().getTimeAsString(true));
        assertEquals("品川駅（東京）", d.getDeparture().getPlace());
        assertEquals("04:48", d.getArrival().getTime().getTimeAsString(true));
        assertEquals("秋葉原駅（東京）", d.getArrival().getPlace());
    }
}
