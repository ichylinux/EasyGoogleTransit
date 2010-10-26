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

import jp.co.hybitz.common.Engine;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.Searcher;
import jp.co.hybitz.transit.goo.GooMobileTransitSearcher;
import jp.co.hybitz.transit.google.MobileSearcher20100827;
import jp.co.hybitz.transit.model.TransitQuery;
import jp.co.hybitz.transit.model.TransitResult;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class TransitSearcherFactory {

    public static Searcher<TransitQuery, TransitResult> createSearcher(Engine engine) {
        if (engine == Engine.GOO) {
            return new GooMobileTransitSearcher(Platform.HTML);
        }
        else if (engine == Engine.GOOGLE) {
            return new MobileSearcher20100827(Platform.GENERIC);
        }
        
        throw new IllegalArgumentException("検索エンジンの指定が不正です。engine=" + engine);
    }

    public static Searcher<TransitQuery, TransitResult> createSearcher(Engine engine, Platform platform) {
        if (engine == Engine.GOO) {
            return new GooMobileTransitSearcher(Platform.HTML);
        }
        else if (engine == Engine.GOOGLE) {
            return new MobileSearcher20100827(platform);
        }
        
        throw new IllegalArgumentException("検索エンジンの指定が不正です。engine=" + engine);
    }
}
