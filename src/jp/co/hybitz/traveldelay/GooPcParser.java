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
package jp.co.hybitz.traveldelay;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.OperationCompany;
import jp.co.hybitz.traveldelay.model.TravelDelay;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
class GooPcParser implements TravelDelayParser {
    private XmlPullParser parser;
    private String encoding;
    private TravelDelayResult result = new TravelDelayResult();
	private Category category;
	private OperationCompany company;
	private TravelDelay delay;
	
	public GooPcParser(XmlPullParser parser) {
	    this.parser = parser;
	}
	
	public void setEncoding(String encoding) {
	    this.encoding = encoding;
	}

    @Override
    public void setAirline(boolean airline) {
    }

    @Override
    public void setSeaway(boolean seaway) {
    }

    @Override
    public void setArrival(boolean arrival) {
    }

    @Override
	public TravelDelayResult parse(InputStream in) throws XmlPullParserException, IOException {
	    try {
    	    parser.setInput(in, encoding);
    
    	    boolean inCategory = false;
    		int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_TAG :
                    if ("h2".equalsIgnoreCase(parser.getName())) {
                        if ("ch03 ma0".equalsIgnoreCase(parser.getAttributeValue(null, "class"))) {
                            if (category != null) {
                                result.addCategory(category);
                            }
                            category = new Category();
                            inCategory = true;
                        }
                    }
                    else if ("a".equalsIgnoreCase(parser.getName())) {
                        if (category != null) {
                            category.setUrl(parser.getAttributeValue(null, "href"));
                        }
                    }
                    else if ("div".equalsIgnoreCase(parser.getName())) {
                        if ("news".equalsIgnoreCase(parser.getAttributeValue(null, "class"))) {
                            if (category != null) {
                                result.addCategory(category);
                            }
                            return result;
                        }
                    }
                    else if ("ul".equalsIgnoreCase(parser.getName())) {
                        if (category != null) {
                            company = new OperationCompany();
                        }
                    }
                    else if ("li".equalsIgnoreCase(parser.getName())) {
                        if (company != null) {
                            delay = new TravelDelay();
                        }
                    }
                    break;
                case XmlPullParser.TEXT :
                    if (category != null && company == null && delay == null) {
                        if (inCategory) {
                            category.setName(parser.getText().replaceAll("（詳細）", ""));
                        }
                    }
                    else if (category != null && company != null && delay != null) {
                        delay.setCondition(parser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG :
                    if ("h2".equalsIgnoreCase(parser.getName())) {
                        inCategory = false;
                    }    
                    else if ("ul".equalsIgnoreCase(parser.getName())) {
                        if (company != null) {
                            category.addOperationCompany(company);
                            company = null;
                        }
                    }    
                    else if ("li".equalsIgnoreCase(parser.getName())) {
                        if (delay != null) {
                            company.addTravelDelay(delay);
                            delay = null;
                        }
                    }    
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

            if (category != null) {
                result.addCategory(category);
            }
            return result;
	    }
	    finally {
	        try { in.close(); } catch (IOException e) {}
	    }
	}
}
