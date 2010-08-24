package jp.co.hybitz.stationapi;

import java.io.InputStream;

import jp.co.hybitz.stationapi.model.StationApiResult;

public interface StationApiParser {
    public StationApiResult parse(InputStream in) throws Exception;
}
