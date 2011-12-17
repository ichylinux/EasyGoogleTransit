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
import jp.co.hybitz.traveldelay.GooPcParser;
import jp.co.hybitz.traveldelay.TravelDelayParser;
import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.OperationCompany;
import jp.co.hybitz.traveldelay.model.TravelDelay;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;

import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooPcParser20100818Test extends ParserTestCase {

    @Override
    protected TravelDelayParser getParser() throws XmlPullParserException {
        return new GooPcParser(new SimpleXmlPullParser());
    }    

    public void testParse20100818() {
        TravelDelayParser parser = null;
        try {
            parser = getParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        TravelDelayResult result = null;
        try {
            result = parser.parse(getClass().getResourceAsStream("/delay/travel_delay_result_20100818.html"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(9, result.getCategories().size());

        Category c = result.getCategories().get(0);
        assertEquals("東北", c.getName());
        OperationCompany oc = c.getOperationCompanies().get(0);
        TravelDelay td = oc.getTravelDelays().get(0);
        assertEquals("08/18 21:14 奥羽本線[新庄～青森] － 運転見合わせ", td.getCondition());
    }
}
