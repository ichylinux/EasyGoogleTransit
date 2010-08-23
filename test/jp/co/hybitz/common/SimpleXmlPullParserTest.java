package jp.co.hybitz.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SimpleXmlPullParserTest extends TestCase {
    private SimpleXmlPullParser parser = new SimpleXmlPullParser();
    
    @Override
    protected void setUp() {
        
    }

    public void testDocType() throws XmlPullParserException, IOException {
        String s = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
        parser.setInput(new ByteArrayInputStream(s.getBytes()), null);
        assertEquals(0, parser.getEventType());
        parser.next();
        assertEquals(XmlPullParser.START_TAG, parser.getEventType());
        assertEquals("!DOCTYPE", parser.getName());
        assertEquals("HTML", parser.getAttributeName(0));
        assertEquals("PUBLIC", parser.getAttributeName(1));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", parser.getAttributeName(2));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", parser.getAttributeName(3));
        assertEquals(4, parser.getAttributeCount());
        assertEquals(null, parser.getAttributeValue(null, "HTML"));
        assertEquals(null, parser.getAttributeValue(null, "PUBLIC"));
        parser.next();
        assertEquals(XmlPullParser.END_DOCUMENT, parser.getEventType());
    }
    
    public void testTag() throws XmlPullParserException, IOException {
        String s = "<ul><li></li></ul>";
        parser.setInput(new ByteArrayInputStream(s.getBytes()), null);
        assertEquals(0, parser.getEventType());
        parser.next();
        assertEquals(XmlPullParser.START_TAG, parser.getEventType());
        assertEquals("ul", parser.getName());
        parser.next();
        assertEquals(XmlPullParser.START_TAG, parser.getEventType());
        assertEquals("li", parser.getName());
        parser.next();
        assertEquals(XmlPullParser.END_TAG, parser.getEventType());
        assertEquals("li", parser.getName());
        parser.next();
        assertEquals(XmlPullParser.END_TAG, parser.getEventType());
        assertEquals("ul", parser.getName());
        parser.next();
        assertEquals(XmlPullParser.END_DOCUMENT, parser.getEventType());
    }
}
