package jp.co.hybitz.common;

import java.io.Serializable;

public interface HttpResult extends Serializable {
    public int getResponseCode();
    public void setResponseCode(int responseCode);
}
