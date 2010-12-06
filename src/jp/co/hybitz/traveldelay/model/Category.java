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

    public boolean isSeaway() {
        return name != null && name.indexOf("フェリー") >= 0;
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
