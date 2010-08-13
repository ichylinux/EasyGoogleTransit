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
package jp.co.hybitz.googletransit.searcher;

import java.util.Calendar;
import java.util.Date;

import jp.co.hybitz.googletransit.Platform;
import jp.co.hybitz.googletransit.TransitSearchException;
import jp.co.hybitz.googletransit.model.TimeType;
import jp.co.hybitz.googletransit.model.TransitQuery;
import jp.co.hybitz.googletransit.model.TransitResult;
import junit.framework.TestCase;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileSearcher20100517Test extends TestCase {

    public void testSearch() {
        MobileSearcher20100517 searcher = new MobileSearcher20100517(Platform.GENERIC);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 10);
        Date now = c.getTime();

        TransitResult result = null;
        try {
            TransitQuery query = new TransitQuery();
            query.setFrom("鹿児島");
            query.setTo("博多");
            query.setTimeType(TimeType.DEPARTURE);
            query.setDate(now);

            result = searcher.search(query);
        } catch (TransitSearchException e) {
            e.printStackTrace();
            fail(e.getMessage() + "\n" + e.getHtml());
        }
        
        assertEquals("出発地", "鹿児島県鹿児島市", result.getFrom());
        assertEquals("到着地", "〒812-0012 博多駅（福岡）", result.getTo());
        assertEquals("時刻タイプ", TimeType.DEPARTURE, result.getTimeType());
        assertEquals("検索日時", now, result.getQueryDate());
    }
}
