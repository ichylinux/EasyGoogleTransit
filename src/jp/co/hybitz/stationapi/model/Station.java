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
package jp.co.hybitz.stationapi.model;

import java.io.Serializable;

public class Station implements Serializable {
    private String name;
    private String furigana;
    private String line;
    private String url;
    private String city;
    private String prefecture;
    private String direction;
    private String directionReverse;
    private int distance;
    private String distanceM;
    private String distanceKm;
    private String travelTime;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFurigana() {
        return furigana;
    }
    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPrefecture() {
        return prefecture;
    }
    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getDirectionReverse() {
        return directionReverse;
    }
    public void setDirectionReverse(String directionReverse) {
        this.directionReverse = directionReverse;
    }
    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public String getDistanceM() {
        return distanceM;
    }
    public void setDistanceM(String distanceM) {
        this.distanceM = distanceM;
    }
    public String getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(String distanceKm) {
        this.distanceKm = distanceKm;
    }
    public String getTravelTime() {
        return travelTime;
    }
    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }
    
}
