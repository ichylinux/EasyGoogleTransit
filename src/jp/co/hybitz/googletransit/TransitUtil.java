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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
     * 現在からの相対的な日時を取得します。
     * 
     * @param time 対象時刻
     * @param incrementDate もし時刻が現在よりも過去の場合に次の日を取得するかどうか
     * @return
     */
    public static Date getRelativeDate(Time time, boolean incrementDate) {
        Calendar c = Calendar.getInstance();
        Time now = getTime(c.getTime());
        c.set(Calendar.HOUR_OF_DAY, time.getHour());
        c.set(Calendar.MINUTE, time.getMinute());
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if (time.before(now) && incrementDate) {
            c.add(Calendar.DATE, 1);
        }

        return c.getTime();
    }
    
    /**
     * 基準日時からの相対的な日時を取得します。
     * 
     * @param time 対象時刻
     * @param date 基準日時
     * @param incrementDate もし時刻が現在よりも過去の場合に次の日を取得するかどうか
     * @return
     */
    public static Date getRelativeDate(Time time, Date date, boolean incrementDate) {
        Time now = getTime(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, time.getHour());
        c.set(Calendar.MINUTE, time.getMinute());
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if (time.before(now) && incrementDate) {
            c.add(Calendar.DATE, 1);
        }

        return c.getTime();
    }

    public static Time getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return new Time(sdf.format(date));
    }
    
    /**
     * 乗り換え候補から出発時刻だけを抽出します。
     * 
     * @param result
     * @return
     */
    public static List<Time> getDepartureTimes(TransitResult result) {
        List<Time> ret = new ArrayList<Time>();
        
        for (Iterator<Transit> it = result.getTransits().iterator(); it.hasNext();) {
            Transit t = it.next();
            TransitDetail td = t.getFirstPublicTransportation();
            if (td == null) {
                continue;
            }

            ret.add(td.getDeparture().getTime());
        }
        return ret;
    }

    /**
     * 乗り換え候補から到着時刻だけを抽出します。
     * 
     * @param result
     * @return
     */
    public static List<Time> getArrivalTimes(TransitResult result) {
        List<Time> ret = new ArrayList<Time>();
        for (Iterator<Transit> it = result.getTransits().iterator(); it.hasNext();) {
            Transit t = it.next();
            TransitDetail td = t.getLastPublicTransportation();
            if (td == null) {
                continue;
            }

            ret.add(td.getArrival().getTime());
        }
        return ret;
    }

    /**
     * 乗り換え候補の中から最初に出発する出発時刻を取得します。
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
     * 乗り換え候補の中から最後に到着する到着時刻を取得します。
     * 
     * @param result
     * @return
     */
    public static Time getLastArrivalTime(TransitResult result) {
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
