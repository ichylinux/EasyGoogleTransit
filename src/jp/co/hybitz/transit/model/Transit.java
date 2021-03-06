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
package jp.co.hybitz.transit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ichy <ichylinux@gmail.com>
 */
public class Transit implements Serializable {

	private String duration;
	private String fare;
	private List<TransitDetail> details = new ArrayList<TransitDetail>();

	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getFare() {
        return fare;
    }
    
	public void setFare(String fare) {
        this.fare = fare;
    }
    
    public List<TransitDetail> getDetails() {
		return details;
	}
	
	public void addDetail(TransitDetail detail) {
		details.add(detail);
	}
	
	public void setDetails(List<TransitDetail> details) {
	    this.details = details;
	}
	
	public TransitDetail getFirstPublicTransportation() {
	    for (int i = 0; i < details.size(); i ++) {
	        TransitDetail detail = details.get(i);
	        if (detail.isWalking()) {
	            continue;
	        }
	        
	        return detail;
	    }
	    
	    return null;
	}
	
    public TransitDetail getLastPublicTransportation() {
        for (int i = details.size() - 1; i >= 0; i --) {
            TransitDetail detail = details.get(i);
            if (detail.isWalking()) {
                continue;
            }
            
            return detail;
        }
        
        return null;
    }

    public int getTransferCount() {
		int ret = 0;
		boolean isFirst = true;
		for (Iterator<TransitDetail> it = details.iterator(); it.hasNext();) {
			TransitDetail detail = it.next();
			if (detail.isWalking()) {
				continue;
			}
			
			// 最初の交通機関の乗車は乗換回数には含めない
			if (isFirst) {
			    isFirst = false;
			    continue;
			}
			
			// 到着情報がない場合は、通過だと思われるので乗換回数には含めない
			if (detail.getArrival() == null) {
			    continue;
			}
			
            ret ++;
		}
		
		return ret;
	}
}
