package jp.co.hybitz.rgeocode;

import java.io.InputStream;

import jp.co.hybitz.rgeocode.model.RGeocodeResult;

public interface RGeocodeParser {
    public RGeocodeResult parse(InputStream in) throws Exception;
}
