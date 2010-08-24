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
package jp.co.hybitz.stationapi;

import jp.co.hybitz.common.Platform;
import jp.co.hybitz.stationapi.searcher.StationApiSearcher20100825;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class StationApiSearcherFactory {

    public static StationApiSearcher createSearcher() {
        return new StationApiSearcher20100825(Platform.LOOSE_HTML);
    }
}
