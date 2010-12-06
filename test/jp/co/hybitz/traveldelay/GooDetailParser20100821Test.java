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
package jp.co.hybitz.traveldelay;

import jp.co.hybitz.common.SimpleXmlPullParser;
import jp.co.hybitz.traveldelay.GooDetailParser;
import jp.co.hybitz.traveldelay.TravelDelayParser;
import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.OperationCompany;
import jp.co.hybitz.traveldelay.model.TravelDelay;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;

import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooDetailParser20100821Test extends ParserTestCase {

    @Override
    protected TravelDelayParser getParser() throws XmlPullParserException {
        return new GooDetailParser(new SimpleXmlPullParser());
    }    

    public void testParse20100821_01() {
        TravelDelayParser parser = null;
        try {
            parser = getParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        TravelDelayResult result = null;
        try {
            result = parser.parse(getClass().getResourceAsStream("/delay/travel_delay_result_detail_20100821_01.html"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getCategories().size());

        Category c = result.getCategories().get(0);
        OperationCompany oc = c.getOperationCompanies().get(0);
        assertEquals("JR西日本", oc.getName());
        assertEquals(3, oc.getTravelDelays().size());
        TravelDelay td = oc.getTravelDelays().get(2);
        assertEquals("08/21 12:00", td.getDate());
        assertEquals("美祢線", td.getLine());
        assertEquals("7月13日以降の大雨災害の影響で、本日も運転を見合わせ、バス代行輸送を行っています。復旧には時間がかかる見込みです。", td.getCondition());
    }

    public void testParse20100821_02() {
        TravelDelayParser parser = null;
        try {
            parser = getParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        TravelDelayResult result = null;
        try {
            result = parser.parse(getClass().getResourceAsStream("/delay/travel_delay_result_detail_20100821_02.html"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(1, result.getCategories().size());

        Category c = result.getCategories().get(0);
        OperationCompany oc = c.getOperationCompanies().get(0);
        assertEquals("JR東日本", oc.getName());
        TravelDelay td = oc.getTravelDelays().get(0);
        assertEquals("08/21 06:15", td.getDate());
        assertEquals("山手線", td.getLine());
        assertEquals("渋谷駅で発生した人身事故の影響で、一部列車に遅れが出ていましたが、06:15現在、ほぼ平常通り運転しています。", td.getCondition());
    }
}
