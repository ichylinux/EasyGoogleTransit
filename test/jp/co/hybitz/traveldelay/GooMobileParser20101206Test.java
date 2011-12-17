/**
 * Copyright (C) 2010-2011 Hybitz.co.ltd
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

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.OperationCompany;
import jp.co.hybitz.traveldelay.model.TravelDelay;
import jp.co.hybitz.traveldelay.model.TravelDelayQuery;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooMobileParser20101206Test extends jp.co.hybitz.common.ParserTestCase {

    @Override
    protected Parser<TravelDelayQuery, TravelDelayResult> getParser() {
        return new GooMobileParser(Platform.HTML, "UTF-8");
    }    

    public void testParse20101206() {
        TravelDelayQuery query = new TravelDelayQuery();
        
        TravelDelayResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/delay/travel_delay_result_20101206.html"), query);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(8, result.getCategories().size());

        Category c = result.getCategories().get(1);
        assertEquals("中部", c.getName());
        OperationCompany oc = c.getOperationCompanies().get(0);
        TravelDelay td = oc.getTravelDelays().get(0);
        assertEquals("12/06 22:00 中央本線[大月～塩尻] － 列車遅延", td.getCondition());
    }
}
