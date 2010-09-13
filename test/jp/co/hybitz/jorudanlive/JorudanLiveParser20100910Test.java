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
package jp.co.hybitz.jorudanlive;

import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.ParserTestCase;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.jorudanlive.model.JorudanLiveQuery;
import jp.co.hybitz.jorudanlive.model.JorudanLiveResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class JorudanLiveParser20100910Test extends ParserTestCase {

    @Override
    protected Parser<JorudanLiveQuery, JorudanLiveResult> getParser() {
        return new JorudanLiveParser20100910(Platform.GENERIC);
    }    

    public void testParse20100910() {
        JorudanLiveResult result = null;
        try {
            result = getParser().parse(getClass().getResourceAsStream("/jorudan_live_result_20100910.html"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertEquals("2010/09/10（金） 12:18 現在", result.getLastUpdate());
        assertEquals(100, result.getLiveInfoList().size());
        assertEquals("10:23", result.getLiveInfoList().get(4).getTime());
        assertEquals("マリンライナー［岡山－高松］", result.getLiveInfoList().get(4).getLine());
        assertEquals("遅れ(10分未満)", result.getLiveInfoList().get(4).getSummary());
        assertEquals("詳細 なし", result.getLiveInfoList().get(4).getDetail());
    }
}
