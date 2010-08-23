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
