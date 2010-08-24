package jp.co.hybitz.common;

import java.io.Serializable;
import java.net.HttpURLConnection;

public class HttpResult implements Serializable {
    private int responseCode;
    
    public int getResponseCode() {
        return responseCode;
    }
    
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    
    public boolean isOK() {
        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
