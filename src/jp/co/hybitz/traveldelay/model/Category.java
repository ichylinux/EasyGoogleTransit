package jp.co.hybitz.traveldelay.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String name;
    private String url;
    private List<OperationCompany> companies = new ArrayList<OperationCompany>();

    public boolean isInternational() {
        return name != null && name.startsWith("国際線");
    }
    
    public boolean isDomestic() {
        return name != null && name.startsWith("国内線");
    }

    public boolean isAirline() {
        if (name == null) {
            return false;
        }
        
        return name.endsWith("出発") || name.endsWith("到着");
    }
    
    public boolean isArrival() {
        return url != null && url.endsWith("#arv");
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<OperationCompany> getOperationCompanies() {
        return companies;
    }
    
    public void addOperationCompany(OperationCompany oc) {
        companies.add(oc);
    }
}
