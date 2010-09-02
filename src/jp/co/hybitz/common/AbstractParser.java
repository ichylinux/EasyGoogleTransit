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
package jp.co.hybitz.common;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class AbstractParser<IN, OUT> implements Parser<IN, OUT> {
    private Platform platform;
    private XmlPullParser parser;

    public AbstractParser(Platform platform) {
        this.platform = platform;
    }
    
    protected abstract void startDocument(IN in);
    protected abstract boolean startTag(String name, XmlPullParser parser);
    protected abstract void text(String text, XmlPullParser parser);
    protected abstract boolean endTag(String name, XmlPullParser parser);
    protected abstract OUT endDocument();

    @Override
    public OUT parse(InputStream is, IN in) throws XmlPullParserException, IOException {
        try {
            parser = XmlPullParserFactory.getParser(platform);
            parser.setInput(is, null);
    
            boolean finished = false;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT :
                    startDocument(in);
                    break;
                case XmlPullParser.START_TAG :
                    finished = startTag(getName(parser), parser);
                    break;
                case XmlPullParser.TEXT :
                    String text = parser.getText().trim();
                    if (StringUtils.isNotEmpty(text)) {
                        text(text, parser);
                    }
                    break;
                case XmlPullParser.END_TAG :
                    finished = endTag(getName(parser), parser);
                    break;
                }
                
                if (finished) {
                    break;
                }
    
                try {
                    eventType = parser.next();
                }
                catch (XmlPullParserException e) {
                }
                catch (EOFException e) {
                    break;
                }
            }
            
            return endDocument();
        }
        finally {
            try { is.close(); } catch (IOException e) {}
        }
    }
    
    private String getName(XmlPullParser parser) {
        return parser.getName().toLowerCase();
    }
    
    protected boolean matchId(String tagName, String id) {
        if (is(tagName)) {
            if (id == null) {
                return true;
            }
            
            return id.equalsIgnoreCase(parser.getAttributeValue(null, "id"));
        }
        
        return false;
    }
    
    protected boolean matchClass(String tagName, String clazz) {
        if (is(tagName)) {
            if (clazz == null) {
                return true;
            }
            
            return clazz.equalsIgnoreCase(parser.getAttributeValue(null, "class"));
        }
        
        return false;
    }

    protected boolean is(String name) {
        return name.equalsIgnoreCase(parser.getName());
    }

}
