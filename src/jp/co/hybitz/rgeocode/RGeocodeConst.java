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
package jp.co.hybitz.rgeocode;

public interface RGeocodeConst {
    public static final String RGEOCODE_URL = "http://www.finds.jp/ws/rgeocode.php";
    public static final int RGEOCODE_STATUS_SUCCESS = 200;
    public static final int RGEOCODE_STATUS_SUCCESS_NO_LOCAL = 201;
    public static final int RGEOCODE_STATUS_SUCCESS_NO_AZA = 202;
    public static final int RGEOCODE_STATUS_INVALID_PARAMETERS = 400;
    public static final int RGEOCODE_STATUS_SERVER_ERROR = 500;
}
