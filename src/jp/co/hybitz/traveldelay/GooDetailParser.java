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

import jp.co.hybitz.common.StringUtils;
import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.OperationCompany;
import jp.co.hybitz.traveldelay.model.TravelDelay;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author ichy <ichylinux@gmail.com>
 */
class GooDetailParser implements TravelDelayParser {
    private XmlPullParser parser;
    private String encoding;
    private TravelDelayResult result = new TravelDelayResult();
	private Category category;
	private OperationCompany company;
	private TravelDelay delay;
	private boolean airline;
    private boolean seaway;
	private boolean arrival;
	
	public GooDetailParser(XmlPullParser parser) {
	    this.parser = parser;
	}
	
	public void setEncoding(String encoding) {
	    this.encoding = encoding;
	}

    @Override
    public void setAirline(boolean airline) {
        this.airline = airline;
    }

    @Override
    public void setSeaway(boolean seaway) {
        this.seaway = seaway;
    }

    @Override
    public void setArrival(boolean arrival) {
        this.arrival = arrival;
    }

    @Override
    public TravelDelayResult parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            parser.setInput(in, encoding);
    
            boolean inCompany = false;
            boolean start = false;
            boolean inArrival = false;
            
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                case XmlPullParser.START_TAG :
                    if ("h2".equalsIgnoreCase(parser.getName())) {
                        if ("ch03".equalsIgnoreCase(parser.getAttributeValue(null, "class"))) {
                            start = true; 
                        }
                    }
                    else if ("table".equalsIgnoreCase(parser.getName())) {
                        if ("t01".equalsIgnoreCase(parser.getAttributeValue(null, "class"))) {
                            if (arrival == inArrival) {
                                category = new Category();
                            }
                        }
                    }
                    else if ("th".equalsIgnoreCase(parser.getName())) {
                        if (category != null) {
                            if (company == null) {
                                company = new OperationCompany();
                            }
                        }
                    }
                    else if ("strong".equalsIgnoreCase(parser.getName())) {
                        if (company != null) {
                            inCompany = true;
                        }
                    }
                    else if ("tr".equalsIgnoreCase(parser.getName())) {
                        if (company != null) {
                            delay = new TravelDelay();
                        }
                    }
                    else if ("div".equalsIgnoreCase(parser.getName())) {
                        if ("news".equalsIgnoreCase(parser.getAttributeValue(null, "class"))) {
                            return result;
                        }
                    }
                    break;
                case XmlPullParser.TEXT :
                    if (category == null && start) {
                        String text = parser.getText().trim();
                        inArrival = text.endsWith("到着");
                    }
                    else if (category != null && company != null && delay == null) {
                        if (inCompany && ! airline) {
                            String text = parser.getText().trim();
                            if (StringUtils.isNotEmpty(text)) {
                                company.setName(text);
                            }
                        }
                    }
                    else if (category != null && company != null && delay != null) {
                        String text = parser.getText().trim();
                        if (StringUtils.isNotEmpty(text)) {
                            if (airline) {
                                if (delay.getDate() == null) {
                                    delay.setDate(text);
                                }
                                else if (delay.getAirline() == null) {
                                    delay.setAirline(text);
                                }
                                else if (delay.getPlace() == null) {
                                    delay.setPlace(text);
                                }
                                else if (delay.getCondition() == null) {
                                    delay.setCondition(text);
                                }
                            }
                            else if (seaway) {
                                if (delay.getDate() == null) {
                                    delay.setDate(text);
                                }
                                else if (delay.getAirline() == null) {
                                    delay.setAirline(text);
                                }
                                else if (delay.getPlace() == null) {
                                    delay.setPlace(text);
                                }
                                else if (delay.getCondition() == null) {
                                    delay.setCondition(text);
                                }
                            }
                            else {
                                if (delay.getDate() == null) {
                                    delay.setDate(text);
                                }
                                else if (delay.getLine() == null) {
                                    delay.setLine(text);
                                }
                                else if (delay.getCondition() == null) {
                                    delay.setCondition(text);
                                }
                            }
                        }
                    }
                    break;
                case XmlPullParser.END_TAG :
                    if ("h2".equalsIgnoreCase(parser.getName())) {
                        start = false;
                    }
                    else if ("table".equalsIgnoreCase(parser.getName())) {
                        if (category != null) {
                            result.addCategory(category);
                            category.addOperationCompany(company);
                            category = null;
                            company = null;
                        }
                    }    
                    else if ("strong".equalsIgnoreCase(parser.getName())) {
                        inCompany = false;
                    }    
                    else if ("tr".equalsIgnoreCase(parser.getName())) {
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

            return result;
        }
        finally {
            try { in.close(); } catch (IOException e) {}
        }
    }
}
