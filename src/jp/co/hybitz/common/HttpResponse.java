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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;

public class HttpResponse implements Serializable {
    private int responseCode;
    private byte[] rawResponse = new byte[0];
    private String url;
    
    public int getResponseCode() {
        return responseCode;
    }
    
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    
    public boolean isOK() {
        return responseCode == HttpURLConnection.HTTP_OK;
    }

    public byte[] getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(byte[] rawResponse) {
        this.rawResponse = rawResponse;
    }
    
    public InputStream getInputStream() {
        return new ByteArrayInputStream(getRawResponse());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
