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

import jp.co.hybitz.common.HttpResponse;
import jp.co.hybitz.common.HttpSearchException;
import jp.co.hybitz.common.Parser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.common.Searcher;
import jp.co.hybitz.common.StreamUtils;
import jp.co.hybitz.jorudanlive.model.JorudanLiveQuery;
import jp.co.hybitz.jorudanlive.model.JorudanLiveResult;

public class JorudanLiveSearcher20100910 implements Searcher<JorudanLiveQuery, JorudanLiveResult> {
    private static final String JORUDAN_LIVE_URL = "http://www.jorudan.co.jp/unk/live.html";

    private Platform platform;
    
    public JorudanLiveSearcher20100910(Platform platform) {
        this.platform = platform;
    }
    
    @Override
    public Parser<JorudanLiveQuery, JorudanLiveResult> createParser(JorudanLiveQuery query) {
        return new JorudanLiveParser20100910(platform);
    }

    @Override
    public String createUrl(JorudanLiveQuery query) {
        return JORUDAN_LIVE_URL;
    }

    @Override
    public JorudanLiveResult search(JorudanLiveQuery query) throws HttpSearchException {
        HttpResponse response = StreamUtils.getHttpResponse(createUrl(query));

        try {
            JorudanLiveResult result = response.isOK() ? createParser(query).parse(response.getInputStream(), query) : new JorudanLiveResult();
            result.setResponseCode(response.getResponseCode());
            return result;
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
    }
    
    @Override
    public void cancel() {
    }
}
