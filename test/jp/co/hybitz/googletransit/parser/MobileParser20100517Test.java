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
package jp.co.hybitz.googletransit.parser;

import jp.co.hybitz.googletransit.TransitParser;
import jp.co.hybitz.googletransit.model.TimeType;
import jp.co.hybitz.googletransit.model.TransitResult;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileParser20100517Test extends ParserTestCase {
    
    @Override
    protected TransitParser getParser() throws XmlPullParserException {
        return new MobileParser20100517(XmlPullParserFactory.newInstance().newPullParser());
    }    

    public void testParse20100517() {
        TransitParser parser = null;
        try {
            parser = getParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        TransitResult result = null;
        try {
            result = parser.parse(getClass().getResourceAsStream("/transit_result_20100517.html"));
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