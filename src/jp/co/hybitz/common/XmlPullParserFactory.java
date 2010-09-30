package jp.co.hybitz.common;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XmlPullParserFactory {

    public static XmlPullParser getParser(Platform platform) throws XmlPullParserException {
        if (platform == Platform.ANDROID) {
            return Xml.newPullParser();
        }
        else if (platform == Platform.GENERIC) {
            return org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
        }
        else if (platform == Platform.HTML) {
            return new SimpleXmlPullParser();
        }
        else {
            throw new UnsupportedOperationException("サポートしていないプラットフォームです。");
        }
    }
}
