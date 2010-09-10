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

import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.Searcher;
import jp.co.hybitz.jorudanlive.model.JorudanLiveQuery;
import jp.co.hybitz.jorudanlive.model.JorudanLiveResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class JorudanLiveSearcherFactory {

    public static Searcher<JorudanLiveQuery, JorudanLiveResult> createSearcher() {
        return new JorudanLiveSearcher20100910(Platform.GENERIC);
    }
    
    public static Searcher<JorudanLiveQuery, JorudanLiveResult> createSearcher(Platform platform) {
        return new JorudanLiveSearcher20100910(platform);
    }
}
