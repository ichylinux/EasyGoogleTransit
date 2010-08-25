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

import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.XmlPullParserFactory;
import jp.co.hybitz.rgeocode.RGeocodeParser;
import jp.co.hybitz.rgeocode.model.RGeocodeResult;

import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class RGeocodeParser20100826Test extends ParserTestCase {

    @Override
    protected RGeocodeParser getParser() throws XmlPullParserException {
        return new RGeocodeParser20100826(XmlPullParserFactory.getParser(Platform.LOOSE_HTML));
    }    

    public void testParse20100826() {
        RGeocodeParser parser = null;
        try {
            parser = getParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        RGeocodeResult result = null;
        try {
            result = parser.parse(getClass().getResourceAsStream("/rgeocode_result_20100826.xml"));
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
