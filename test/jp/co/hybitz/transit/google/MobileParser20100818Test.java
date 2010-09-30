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

import jp.co.hybitz.transit.model.TransitResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileParser20100818Test extends MobileParser20100813Test {

    public void testParse20100818() {
        TransitResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/transit_result_20100813.wml"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("出発地", "新宿駅（東京）（東京メトロ）", result.getFrom());
        assertEquals("到着地", "〒160-0022 新宿三丁目駅（東京）", result.getTo());
    }
}
