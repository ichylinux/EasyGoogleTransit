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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import jp.co.hybitz.common.Platform;
import jp.co.hybitz.googletransit.model.TimeType;
import jp.co.hybitz.googletransit.model.TransitQuery;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class MobileHtmlDump20100517 extends MobileSearcher20100517 {

    public MobileHtmlDump20100517(Platform platform) {
        super(platform);
    }

    public static void main(String[] args) throws IOException {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 10);
        
        TransitQuery query = new TransitQuery();
        query.setFrom("品川");
        query.setTo("甲州街道");
        query.setDate(c.getTime());
        query.setTimeType(TimeType.DEPARTURE);

        InputStream in = new MobileHtmlDump20100517(Platform.GENERIC).openConnection(query).getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
