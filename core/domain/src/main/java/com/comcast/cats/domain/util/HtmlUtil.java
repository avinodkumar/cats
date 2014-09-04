/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.domain.util;

import org.jsoup.Jsoup;

/**
 * HTML utility methods.
 * 
 * @author subinsugunan
 * 
 */
public final class HtmlUtil
{

    private static final String HTTP_ERROR_MSG_START_OFFSET = " - ";
    private static final String HTTP_ERROR_MSG_END_OFFSET   = " type Status report message";

    private HtmlUtil()
    {
    }

    /**
     * Converts html content to text.
     * 
     * @param htmlStr
     * @return converted string
     */
    public static String html2text( String htmlStr )
    {
        if ( ( null == htmlStr ) || ( htmlStr.isEmpty() ) )
        {
            throw new IllegalArgumentException( "The input html string was null or empty" );
        }
        return Jsoup.parse( htmlStr ).text();
    }

    /**
     * We need to show custom error message returned from the configuration
     * management system. As it returns standard error codes only in response
     * header, we need to parse the response body to read the exact cause of the
     * error. Again the response body will contain the error message 3 times and
     * we need it only once. So I added one stupid subString logic to extract
     * that.
     * 
     * @param htmlStr
     * @return converted string
     */
    public static String getErrorMessage( String htmlStr )
    {
        String errorMsg = html2text( htmlStr );
        errorMsg = errorMsg.substring(
                errorMsg.indexOf( HTTP_ERROR_MSG_START_OFFSET ) + HTTP_ERROR_MSG_START_OFFSET.length(),
                errorMsg.indexOf( HTTP_ERROR_MSG_END_OFFSET ) );
        return errorMsg;
    }
}
