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
package jp.co.hybitz.jorudanlive;

import jp.co.hybitz.common.AbstractParser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.jorudanlive.model.JorudanLiveQuery;
import jp.co.hybitz.jorudanlive.model.JorudanLiveResult;
import jp.co.hybitz.jorudanlive.model.LiveInfo;

import org.xmlpull.v1.XmlPullParser;

public class JorudanLiveParser20100910 extends AbstractParser<JorudanLiveQuery, JorudanLiveResult> {
    private JorudanLiveResult result;
    private LiveInfo liveInfo;
    private boolean inHBig;
    private boolean inEm;
    private boolean inTableLive;
    private boolean inTrLive;
    private boolean isOld;

    public JorudanLiveParser20100910(Platform platform) {
        super(platform);
    }

    @Override
    protected void startDocument(JorudanLiveQuery in) {
        result = new JorudanLiveResult();
    }

    @Override
    protected JorudanLiveResult endDocument() {
        return result;
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (matchClass("div", "h_big")) {
            inHBig = true;
        }
        else if (is("em")) {
            inEm = true;
        }
        else if (matchClass("table", "unktable unktable_live")) {
            inTableLive = true;
        }
        else if (inTableLive && is("tr")) {
            if (matchClass("tr", "old")) {
                isOld = true;
            }

            inTrLive = true;
        }
        else if (inTrLive && is("td")) {
            if (liveInfo == null) {
                liveInfo = new LiveInfo();
            }
        }

        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (inHBig && is("div")) {
            inHBig = false;
        }
        else if (is("em")) {
            inEm = false;
        }
        else if (inTableLive && is("table")) {
            inTableLive = false;
            return true;
        }
        else if (inTrLive && is("tr")) {
            if (liveInfo != null) {
                result.addLiveInfo(liveInfo);
                liveInfo = null;
            }
            isOld = false;
            inTrLive = false;
        }

        return false;
    }

    @Override
    protected boolean text(String text, XmlPullParser parser) {
        if (inHBig && inEm) {
            result.setLastUpdate(text);
        }
        else if (inTrLive) {
            if (liveInfo != null) {
                if (isOld && liveInfo.getDate() == null) {
                    liveInfo.setDate(text);
                }
                else if (liveInfo.getTime() == null) {
                    liveInfo.setTime(text);
                }
                else if (liveInfo.getLine() == null) {
                    liveInfo.setLine(text);
                }
                else if (liveInfo.getSummary() == null) {
                    liveInfo.setSummary(text);
                }
                else if (liveInfo.getDetail() == null) {
                    liveInfo.setDetail(text);
                }
            }
        }

        return false;
    }

}
