package jp.co.hybitz.rgeocode.model;

import jp.co.hybitz.common.GeoLocation;
import jp.co.hybitz.common.HttpResult;

public class RGeocodeResult extends HttpResult {
    private GeoLocation geoLocation;
    private int status;
    private String prefecture;
    private String municipality;
    private String local;
    
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getPrefecture() {
        return prefecture;
    }
    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }
    public String getMunicipality() {
        return municipality;
    }
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (prefecture != null) {
            sb.append(prefecture);
        }
        if (municipality != null) {
            sb.append(municipality);
        }
        if (local != null) {
            sb.append(local);
        }
        return sb.toString();
    }
}
