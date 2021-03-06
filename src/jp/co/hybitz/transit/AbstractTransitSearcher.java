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
package jp.co.hybitz.transit;

import java.util.Calendar;

import jp.co.hybitz.common.HttpResponse;
import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Searcher;
import jp.co.hybitz.common.model.Time;
import jp.co.hybitz.transit.model.TimeType;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

public abstract class AbstractTransitSearcher implements Searcher<TransitQuery, TransitResult> {

    @Override
    public void cancel() {
    }

    @Override
    public abstract Parser<TransitQuery, TransitResult> createParser(TransitQuery query, HttpResponse response);

    @Override
    public abstract String createUrl(TransitQuery query);

    protected abstract TransitResult searchImpl(TransitQuery query) throws HttpSearchException;

    /**
     * @see jp.co.hybitz.common.Searcher#search(java.lang.Object)
     */
    @Override
    public TransitResult search(TransitQuery query) throws HttpSearchException {
        if (query.getTimeType() == TimeType.FIRST) {
            return searchFirst(query);
        }
        else if (query.getTimeType() == TimeType.PREVIOUS_DEPARTURE) {
            return searchPreviousDeparture(query);
        }
        else if (query.getTimeType() == TimeType.NEXT_ARRIVAL) {
            return searchNextArraival(query);
        }
        else {
            return searchImpl(query);
        }
    }
    
    private TransitResult searchFirst(TransitQuery query) throws HttpSearchException {
        // 始発検索の場合は、まず終電を検索して、始発検索用の出発時刻を算出する
        TransitQuery queryForLast = new TransitQuery();
        queryForLast.setFrom(query.getFrom());
        queryForLast.setTo(query.getTo());
        queryForLast.setTimeType(TimeType.LAST);
        queryForLast.setDate(query.getDate());
        queryForLast.setUseExpress(query.isUseExpress());
        queryForLast.setUseAirline(query.isUseAirline());

        TransitResult result = searchImpl(queryForLast);
        if (!result.isOK()) {
            return result;
        }

        // ざっくりと、、
        // 最後の出発時刻が現在時刻より遅い場合は今日の始発を検索
        // 現在時刻が最後の出発時刻を過ぎている場合は翌日の始発を検索
        // 始発検索はGoogleトランジットにはないので、時刻は最終が出発した1分後で検索する
        Calendar c = Calendar.getInstance();
        c.setTime(query.getDate());
        Time timeToSearch = TransitUtil.getLastDepartureTime(result);
        if (timeToSearch != null) {
            Time now = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

            c.set(Calendar.HOUR_OF_DAY, timeToSearch.getHour());
            c.set(Calendar.MINUTE, timeToSearch.getMinute());

            if (timeToSearch.before(now)) {
                c.add(Calendar.DATE, 1);
            }

            c.add(Calendar.MINUTE, 1);
        }

        TransitQuery queryForFirst = new TransitQuery();
        queryForFirst.setFrom(query.getFrom());
        queryForFirst.setTo(query.getTo());
        queryForFirst.setTimeType(TimeType.DEPARTURE);
        queryForFirst.setDate(c.getTime());
        queryForFirst.setUseExpress(query.isUseExpress());
        queryForFirst.setUseAirline(query.isUseAirline());

        TransitResult ret = searchImpl(queryForFirst);
        ret.setTimeType(TimeType.FIRST);
        ret.setTime(null);

        return ret;
    }

    private TransitResult searchPreviousDeparture(TransitQuery query) throws HttpSearchException {
        // まずは直前に到着するルートを検索して最後に出発する時刻を取得する
        TransitQuery queryForArrival = new TransitQuery();
        queryForArrival.setFrom(query.getFrom());
        queryForArrival.setTo(query.getTo());
        queryForArrival.setTimeType(TimeType.ARRIVAL);
        queryForArrival.setDate(query.getDate());
        queryForArrival.setUseExpress(query.isUseExpress());
        queryForArrival.setUseAirline(query.isUseAirline());

        TransitResult result = searchImpl(queryForArrival);
        if (!result.isOK()) {
            return result;
        }

        // 最後に出発する時刻を利用して検索
        Calendar c = Calendar.getInstance();
        c.setTime(query.getDate());
        Time timeToSearch = TransitUtil.getLastDepartureTime(result);
        if (timeToSearch != null) {
            Time now = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

            c.set(Calendar.HOUR_OF_DAY, timeToSearch.getHour());
            c.set(Calendar.MINUTE, timeToSearch.getMinute());

            if (timeToSearch.after(now)) {
                c.add(Calendar.DATE, -1);
            }
        }

        TransitQuery queryForDepartrue = new TransitQuery();
        queryForDepartrue.setFrom(query.getFrom());
        queryForDepartrue.setTo(query.getTo());
        queryForDepartrue.setTimeType(TimeType.DEPARTURE);
        queryForDepartrue.setDate(c.getTime());
        queryForDepartrue.setUseExpress(query.isUseExpress());
        queryForDepartrue.setUseAirline(query.isUseAirline());

        return searchImpl(queryForDepartrue);
    }

    private TransitResult searchNextArraival(TransitQuery query) throws HttpSearchException {
        // まずは直後に出発するルートを検索して最初に到着する時刻を取得する
        TransitQuery queryForDeparture = new TransitQuery();
        queryForDeparture.setFrom(query.getFrom());
        queryForDeparture.setTo(query.getTo());
        queryForDeparture.setTimeType(TimeType.DEPARTURE);
        queryForDeparture.setDate(query.getDate());
        queryForDeparture.setUseExpress(query.isUseExpress());
        queryForDeparture.setUseAirline(query.isUseAirline());

        TransitResult result = search(queryForDeparture);
        if (!result.isOK()) {
            return result;
        }

        // 最初に到着する時刻を利用して検索
        Calendar c = Calendar.getInstance();
        c.setTime(query.getDate());
        Time timeToSearch = TransitUtil.getFirstArrivalTime(result);
        if (timeToSearch != null) {
            Time now = new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

            c.set(Calendar.HOUR_OF_DAY, timeToSearch.getHour());
            c.set(Calendar.MINUTE, timeToSearch.getMinute());

            if (timeToSearch.before(now)) {
                c.add(Calendar.DATE, 1);
            }
        }

        TransitQuery queryForArrival = new TransitQuery();
        queryForArrival.setFrom(query.getFrom());
        queryForArrival.setTo(query.getTo());
        queryForArrival.setTimeType(TimeType.ARRIVAL);
        queryForArrival.setDate(c.getTime());
        queryForArrival.setUseExpress(query.isUseExpress());
        queryForArrival.setUseAirline(query.isUseAirline());

        return searchImpl(queryForArrival);
    }
}
