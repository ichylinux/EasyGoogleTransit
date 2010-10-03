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
package jp.co.hybitz.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

public abstract class AbstractSearcher<QUERY, RESULT> implements Searcher<QUERY, RESULT> {
    private HttpURLConnection con;
    
    public abstract Parser<QUERY, RESULT> createParser(QUERY query, HttpResponse response);
    public abstract String createUrl(QUERY query);
    protected abstract RESULT parse(QUERY query, HttpResponse response) throws Exception;
    
    public RESULT search(QUERY query) throws HttpSearchException {
        HttpResponse response = new HttpResponse();

        try {
            con = StreamUtils.openConnection(createUrl(query));
            response.setResponseCode(con.getResponseCode());
            
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                StreamUtils.write(con.getInputStream(), baos);
                response.setRawResponse(baos.toByteArray());
            }
        }
        catch (IOException e) {
            cancel();
            throw new HttpSearchException(e.getMessage(), e);
        }

        try {
            return parse(query, response);
        }
        catch (Exception e) {
            throw new HttpSearchException(e.getMessage(), new String(response.getRawResponse()), e);
        }
    }

    public void cancel() {
        if (con != null) {
            con.disconnect();
        }
    }

}
