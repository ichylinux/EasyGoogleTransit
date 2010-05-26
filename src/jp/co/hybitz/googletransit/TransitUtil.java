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
package jp.co.hybitz.googletransit;

import java.util.Iterator;

import jp.co.hybitz.googletransit.model.Time;
import jp.co.hybitz.googletransit.model.Transit;
import jp.co.hybitz.googletransit.model.TransitDetail;
import jp.co.hybitz.googletransit.model.TransitResult;

/**
 * @author ichy <ichylinux@gmail.com>
 *
 */
public class TransitUtil {

    /**
     * 乗り換え候補の中から出発地の最初の出発時刻を取得します。
     * 
     * @param result
     * @return
     */
    public static Time getFirstDepartureTime(TransitResult result) {
        Time timeToSearch = null;
        for (Iterator<Transit> it = result.getTransits().iterator(); it.hasNext();) {
            Transit t = it.next();
            TransitDetail td = t.getFirstPublicTransportation();
            if (td == null) {
                continue;
            }

            Time time = td.getDeparture().getTime();
            if (timeToSearch == null || timeToSearch.compareTo(time) > 0) {
                timeToSearch = time;
            }
        }
        return timeToSearch;
    }
    
    /**
     * 乗り換え候補の中から到着地の最初の到着時刻を取得します。
     * 
     * @param result
     * @return
     */
    public static Time getFirstArrivalTime(TransitResult result) {
        Time timeToSearch = null;
        for (Iterator<Transit> it = result.getTransits().iterator(); it.hasNext();) {
            Transit t = it.next();
            TransitDetail td = t.getLastPublicTransportation();
            if (td == null) {
                continue;
            }

            Time time = td.getArrival().getTime();
            if (timeToSearch == null || timeToSearch.compareTo(time) < 0) {
                timeToSearch = time;
            }
        }
        return timeToSearch;
    }
}
