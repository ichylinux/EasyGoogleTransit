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
package jp.co.hybitz.rgeocode.parser;

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.ParserTestCase;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.rgeocode.model.RGeocodeResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class RGeocodeParser20100826Test extends ParserTestCase {

    @Override
    protected Parser<RGeocodeResult> getParser() {
        return new RGeocodeParser20100826(Platform.GENERIC);
    }    

    public void testParse20100826() {
        RGeocodeResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/rgeocode_result_20100826.xml"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals(200, result.getStatus());
        assertEquals("広島県", result.getPrefecture());
        assertEquals("福山市", result.getMunicipality());
        assertEquals("西深津町六丁目", result.getLocal());
        assertEquals("広島県福山市西深津町六丁目", result.toString());
    }
}
