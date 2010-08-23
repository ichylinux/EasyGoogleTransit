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
package jp.co.hybitz.traveldelay.searcher;

import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.TravelDelayQuery;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;
import junit.framework.TestCase;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class GooSearcher20100818Test extends TestCase {

    public void testSearch() {
        GooSearcher20100818 searcher = new GooSearcher20100818(Platform.LOOSE_HTML);

        TravelDelayResult result = null;
        try {
            result = searcher.search(new TravelDelayQuery());
        } catch (HttpSearchException e) {
            e.printStackTrace();
            fail(e.getMessage());// + "\n" + e.getHtml());
        }
        
        for (Category c : result.getCategories()) {
            System.out.println(c.getName());
        }
    }
}
