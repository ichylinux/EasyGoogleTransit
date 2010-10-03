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
import jp.co.hybitz.common.model.Time;
import jp.co.hybitz.transit.model.TimeType;
import jp.co.hybitz.transit.model.Transit;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

public class GooMobileTransitParser20101002Test extends ParserTestCase {

    @Override
    protected Parser<TransitQuery, TransitResult> getParser() {
        return new GooMobileTransitParser20101002(Platform.HTML, "UTF-8");
    }    

    public void testParse20101002_01() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/goo/transit_result_20101002_01.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("富田林", result.getFrom());
        assertEquals("南小樽", result.getTo());
        assertEquals(TimeType.DEPARTURE, result.getTimeType());
        assertEquals(new Time(15, 12), result.getTime());
        assertEquals(3, result.getTransitCount());

        Transit t = result.getTransits().get(0);
        assertEquals("356分", t.getDuration());
        assertEquals("44,670円", t.getFare());
        
        assertEquals("近鉄長野線(河内長野行)", t.getDetails().get(0).getRoute());
        assertEquals(new Time(15, 17), t.getDetails().get(0).getDeparture().getTime());
        assertEquals("富田林", t.getDetails().get(0).getDeparture().getPlace());
        assertEquals(new Time(15, 28), t.getDetails().get(0).getArrival().getTime());
        assertEquals("河内長野", t.getDetails().get(0).getArrival().getPlace());

        assertEquals("南海高野線急行(南海難波行)", t.getDetails().get(1).getRoute());
        assertEquals(new Time(15, 32), t.getDetails().get(1).getDeparture().getTime());
        assertEquals("河内長野", t.getDetails().get(1).getDeparture().getPlace());
        assertEquals(new Time(15, 55), t.getDetails().get(1).getArrival().getTime());
        assertEquals("天下茶屋", t.getDetails().get(1).getArrival().getPlace());

        assertEquals("南海特急ラピートβ59号", t.getDetails().get(2).getRoute());
        assertEquals(new Time(16, 04), t.getDetails().get(2).getDeparture().getTime());
        assertEquals("天下茶屋", t.getDetails().get(2).getDeparture().getPlace());
        assertEquals(new Time(16, 37), t.getDetails().get(2).getArrival().getTime());
        assertEquals("関西空港", t.getDetails().get(2).getArrival().getPlace());

        assertEquals("[飛行機]ANA1719便", t.getDetails().get(3).getRoute());
        assertEquals(new Time(17, 45), t.getDetails().get(3).getDeparture().getTime());
        assertEquals("関西空港", t.getDetails().get(3).getDeparture().getPlace());
        assertEquals(new Time(19, 35), t.getDetails().get(3).getArrival().getTime());
        assertEquals("新千歳空港", t.getDetails().get(3).getArrival().getPlace());

        assertEquals("エアポート201号(小樽行)", t.getDetails().get(4).getRoute());
        assertEquals(new Time(20, 04), t.getDetails().get(4).getDeparture().getTime());
        assertEquals("新千歳空港", t.getDetails().get(4).getDeparture().getPlace());
        assertEquals(new Time(21, 13), t.getDetails().get(4).getArrival().getTime());
        assertEquals("南小樽", t.getDetails().get(4).getArrival().getPlace());

        t = result.getTransits().get(1);
        assertEquals("356分", t.getDuration());
        assertEquals("44,350円", t.getFare());

        t = result.getTransits().get(2);
        assertEquals("465分", t.getDuration());
        assertEquals("43,900円", t.getFare());
    }
    
    public void testParse20101002_02() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit/goo/transit_result_20101002_02.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("ドーム前", result.getFrom());
        assertEquals("五反田", result.getTo());
        assertEquals(TimeType.DEPARTURE, result.getTimeType());
        assertEquals(new Time(02, 33), result.getTime());
        assertEquals(3, result.getTransitCount());

        Transit t = result.getTransits().get(0);
        assertEquals("216分", t.getDuration());
        assertEquals("13,510円", t.getFare());
        assertEquals(3, t.getTransferCount());
        
        assertEquals("徒歩", t.getDetails().get(0).getRoute());
        assertEquals(new Time(05, 04), t.getDetails().get(0).getDeparture().getTime());
        assertEquals("ドーム前", t.getDetails().get(0).getDeparture().getPlace());
        assertNull(t.getDetails().get(0).getArrival());

        assertEquals("大阪市営長堀鶴見緑地線(門真南行)", t.getDetails().get(1).getRoute());
        assertEquals(new Time(05, 10), t.getDetails().get(1).getDeparture().getTime());
        assertEquals("ドーム前千代崎", t.getDetails().get(1).getDeparture().getPlace());
        assertEquals(new Time(05, 15), t.getDetails().get(1).getArrival().getTime());
        assertEquals("心斎橋", t.getDetails().get(1).getArrival().getPlace());

        assertEquals("大阪市営御堂筋線(江坂行)", t.getDetails().get(2).getRoute());
        assertEquals(new Time(05, 23), t.getDetails().get(2).getDeparture().getTime());
        assertEquals("心斎橋", t.getDetails().get(2).getDeparture().getPlace());
        assertEquals(new Time(05, 35), t.getDetails().get(2).getArrival().getTime());
        assertEquals("新大阪", t.getDetails().get(2).getArrival().getPlace());

        assertEquals("のぞみ200号", t.getDetails().get(3).getRoute());
        assertEquals(new Time(06, 00), t.getDetails().get(3).getDeparture().getTime());
        assertEquals("新大阪", t.getDetails().get(3).getDeparture().getPlace());
        assertEquals(new Time(8, 19), t.getDetails().get(3).getArrival().getTime());
        assertEquals("品川", t.getDetails().get(3).getArrival().getPlace());

        assertEquals("山手線渋谷方面行", t.getDetails().get(4).getRoute());
        assertEquals(new Time(8, 35), t.getDetails().get(4).getDeparture().getTime());
        assertEquals("品川", t.getDetails().get(4).getDeparture().getPlace());
        assertEquals(new Time(8, 40), t.getDetails().get(4).getArrival().getTime());
        assertEquals("五反田", t.getDetails().get(4).getArrival().getPlace());

        t = result.getTransits().get(1);
        assertEquals("235分", t.getDuration());
        assertEquals("15,720円", t.getFare());
        assertEquals(4, t.getTransferCount());

        t = result.getTransits().get(2);
        assertEquals("266分", t.getDuration());
        assertEquals("15,450円", t.getFare());
        assertEquals(4, t.getTransferCount());
    }
}
