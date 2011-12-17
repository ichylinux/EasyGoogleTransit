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

import jp.co.hybitz.common.AbstractParser;
import jp.co.hybitz.common.Platform;
import jp.co.hybitz.traveldelay.model.Category;
import jp.co.hybitz.traveldelay.model.OperationCompany;
import jp.co.hybitz.traveldelay.model.TravelDelay;
import jp.co.hybitz.traveldelay.model.TravelDelayQuery;
import jp.co.hybitz.traveldelay.model.TravelDelayResult;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author ichy <ichylinux@gmail.com>
 */
class GooMobileParser extends AbstractParser<TravelDelayQuery, TravelDelayResult> {
    private TravelDelayResult result;
	private Category category;
	private OperationCompany company;
	private TravelDelay delay;
	private boolean inTable;
	private boolean inTd;
	
    public GooMobileParser(Platform platform, String encoding) {
        super(platform, encoding);
    }

    @Override
    protected TravelDelayResult endDocument() {
        return result;
    }

    @Override
    protected void startDocument(TravelDelayQuery query) {
        result = new TravelDelayResult();
    }

    @Override
    protected boolean startTag(String name, XmlPullParser parser) {
        if (isTable() && "#daebf2".equals(getAttribute("bgcolor"))) {
            category = new Category();
            inTable = true;
        }
        else if (isTd()) {
            inTd = true;
        }
        return false;
    }

    @Override
    protected boolean endTag(String name, XmlPullParser parser) {
        if (isTable()) {
            if (category != null) {
                result.addCategory(category);
                category = null;
            }
            inTable = false;
        }
        else if (isTd()) {
            inTd = false;
        }
        return false;
    }

    @Override
    protected boolean text(String text, XmlPullParser parser) {
        if (inTd && category != null) {
            category.setName(text);
        }
        return false;
    }

}
