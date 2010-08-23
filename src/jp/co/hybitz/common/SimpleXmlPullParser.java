package jp.co.hybitz.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SimpleXmlPullParser implements XmlPullParser {
    private static final int DO_TEXT = 1;
    private static final int DO_NAME = 2;
    private static final int DO_ATTR_KEY = 3;
    private static final int DO_ATTR_VALUE = 4;

    private int action = DO_TEXT;
    private InputStreamReader reader;
    private int eventType;
    private StringBuilder name = new StringBuilder();
    private StringBuilder attrKey = new StringBuilder();
    private StringBuilder attrValue = new StringBuilder();
    private StringBuilder text = new StringBuilder();
    private Map<String, String> attrs = new HashMap<String, String>();
    private List<String> attrKeys = new ArrayList<String>();

    @Override
    public int next() throws XmlPullParserException, IOException {
        int i = -1;
        boolean inQuote = false;
        while ((i = reader.read()) != -1) {
            char c = (char) i;
            if (c == '\n' || c == '\r') {
                continue;
            }
            else if (c == '<') {
                name.setLength(0);
                attrKey.setLength(0);
                attrValue.setLength(0);
                attrs.clear();
                attrKeys.clear();
                action = DO_NAME;
                eventType = TEXT;
                if (text.length() > 0) {
                    return eventType;
                }
            }
            else if (c == '>') {
                action = DO_TEXT;
                text.setLength(0);
                if (eventType != END_TAG) {
                    eventType = START_TAG;
                }
                return eventType;
            }
            else if (c == ' ') {
                if (action == DO_NAME) {
                    if (name.length() > 0) {
                        action = DO_ATTR_KEY;
                    }
                }
                else if (action == DO_ATTR_KEY) {
                    if (inQuote) {
                        attrKey.append(c);
                    }
                    else {
                        if (attrKey.length() > 0) {
                            attrKeys.add(attrKey.toString());
                            attrKey.setLength(0);
                        }
                    }
                }
                else if (action == DO_ATTR_VALUE) {
                    if (inQuote) {
                        attrValue.append(c);
                    }
                    else {
                        attrKeys.add(attrKey.toString());
                        attrs.put(attrKey.toString(), attrValue.toString());
                        attrKey.setLength(0);
                        attrValue.setLength(0);
                        action = DO_ATTR_KEY;
                    }
                }
                else if (action == DO_TEXT) {
                    text.append(c);
                }
            }
            else if (c == '=') {
                if (action == DO_ATTR_KEY) {
                    action = DO_ATTR_VALUE;
                }
                else if (action == DO_ATTR_VALUE) {
                    attrValue.append(c);
                }
                else if (action == DO_TEXT) {
                    text.append(c);
                }
            }
            else if (c == '"') {
                if (action == DO_ATTR_KEY) {
                    if (attrKey.length() > 0) {
                        inQuote = false;
                        attrKeys.add(attrKey.toString());
                        attrKey.setLength(0);
                    }
                    else {
                        inQuote = true;
                    }
                }
                else if (action == DO_ATTR_VALUE) {
                    if (attrValue.length() > 0) {
                        inQuote = false;
                        action = DO_ATTR_KEY;
                        attrs.put(attrKey.toString(), attrValue.toString());
                        attrKey.setLength(0);
                        attrValue.setLength(0);
                    }
                    else {
                        inQuote = true;
                    }
                }
                else if (action == DO_TEXT) {
                    text.append(c);
                }
            }
            else if (c == '/') {
                if (action == DO_NAME) {
                    if (name.length() == 0) {
                        eventType = END_TAG;
                    }
                    else {
                        eventType = START_TAG;
                    }
                }
                else if (action == DO_ATTR_KEY) {
                    if (inQuote) {
                        attrKey.append(c);
                    }
                }
                else if (action == DO_ATTR_VALUE) {
                    if (inQuote) {
                        attrValue.append(c);
                    }
                }
                else if (action == DO_TEXT) {
                    text.append(c);
                }
            }
            else if (c == '-') {
                if (action == DO_NAME) {
                    if (name.length() <= 1) {
                    }
                }
                else if (action == DO_ATTR_KEY) {
                    attrKey.append(c);
                }
                else if (action == DO_ATTR_VALUE) {
                    attrValue.append(c);
                }
                else if (action == DO_TEXT) {
                    text.append(c);
                }
            }
            else if (c == '!') {
                if (action == DO_NAME) {
                    name.append(c);
                }
                else if (action == DO_ATTR_KEY) {
                    attrKey.append(c);
                }
                else if (action == DO_ATTR_VALUE) {
                    attrValue.append(c);
                }
                else if (action == DO_TEXT) {
                    text.append(c);
                }
            }
            else {
                if (action == DO_NAME) {
                    name.append(c);
                }
                else if (action == DO_ATTR_KEY) {
                    attrKey.append(c);
                }
                else if (action == DO_ATTR_VALUE) {
                    attrValue.append(c);
                }
                else if (action == DO_TEXT) {
                    text.append(c);
                }
            }
        }
        
        eventType = END_DOCUMENT;
        return eventType;
    }
    @Override
    public void defineEntityReplacementText(String arg0, String arg1)
            throws XmlPullParserException {
    }

    @Override
    public int getAttributeCount() {
        return attrKeys.size();
    }

    @Override
    public String getAttributeName(int index) {
        return attrKeys.get(index);
    }

    @Override
    public String getAttributeNamespace(int arg0) {
        return null;
    }

    @Override
    public String getAttributePrefix(int arg0) {
        return null;
    }

    @Override
    public String getAttributeType(int arg0) {
        return null;
    }

    @Override
    public String getAttributeValue(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAttributeValue(String namespace, String attrKey) {
        return attrs.get(attrKey);
    }

    @Override
    public int getColumnNumber() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getDepth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEventType() throws XmlPullParserException {
        return eventType;
    }

    @Override
    public boolean getFeature(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getInputEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLineNumber() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        return name.toString();
    }

    @Override
    public String getNamespace() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNamespace(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNamespaceCount(int arg0) throws XmlPullParserException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getNamespacePrefix(int arg0) throws XmlPullParserException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNamespaceUri(int arg0) throws XmlPullParserException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPositionDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPrefix() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getProperty(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getText() {
        return text.toString();
    }

    @Override
    public char[] getTextCharacters(int[] arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAttributeDefault(int arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmptyElementTag() throws XmlPullParserException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isWhitespace() throws XmlPullParserException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int nextTag() throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String nextText() throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int nextToken() throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void require(int arg0, String arg1, String arg2)
            throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFeature(String arg0, boolean arg1)
            throws XmlPullParserException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setInput(Reader arg0) throws XmlPullParserException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setInput(InputStream is, String encoding) throws XmlPullParserException {
        if (encoding == null) {
            encoding = "UTF-8";
        }
        try {
            this.reader = new InputStreamReader(is, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new XmlPullParserException(e.getMessage(), this, e);
        }
    }

    @Override
    public void setProperty(String arg0, Object arg1)
            throws XmlPullParserException {
        // TODO Auto-generated method stub
        
    }

}
