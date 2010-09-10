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
package jp.co.hybitz.jorudanlive.model;

import java.util.ArrayList;
import java.util.List;

import jp.co.hybitz.common.HttpResponse;

public class JorudanLiveResult extends HttpResponse {
    private String lastUpdate;
    private List<LiveInfo> liveInfoList = new ArrayList<LiveInfo>();

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public void addLiveInfo(LiveInfo liveInfo) {
        liveInfoList.add(liveInfo);
    }
    
    public List<LiveInfo> getLiveInfoList() {
        return liveInfoList;
    }
}
