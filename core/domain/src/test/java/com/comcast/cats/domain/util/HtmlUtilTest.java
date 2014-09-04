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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import com.comcast.cats.domain.test.DataProvider;

/**
 * 
 * @author subinsugunan
 * 
 */
public class HtmlUtilTest
{
    protected final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    String              html   = "<html><head><title>Apache Tomcat/6.0.29 - Error report</title><style><!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style> </head><body><h1>HTTP Status 400 - Component 'Settop.00:19:47:25:AC:B8' is already allocated for this timespan by the User: ssugun00c [Sugunan, Subin] [subinsugunan@tataelxsi.co.in].</h1><HR size=\"1\" noshade=\"noshade\"><p><b>type</b> Status report</p><p><b>message</b> <u>Component 'Settop.00:19:47:25:AC:B8' is already allocated for this timespan by the User: ssugun00c [Sugunan, Subin] [subinsugunan@tataelxsi.co.in].</u></p><p><b>description</b> <u>The request sent by the client was syntactically incorrect (Component 'Settop.00:19:47:25:AC:B8' is already allocated for this timespan by the User: ssugun00c [Sugunan, Subin] [subinsugunan@tataelxsi.co.in].).</u></p><HR size=\"1\" noshade=\"noshade\"><h3>Apache Tomcat/6.0.29</h3></body></html>";

    @Test
    public void testHtml2text() throws Exception
    {
        String str = HtmlUtil.html2text( html );
        LOGGER.info( str );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testHtml2textNull() throws Exception
    {
        HtmlUtil.html2text( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testHtml2textEmpty() throws Exception
    {
        HtmlUtil.html2text( DataProvider.EMPTY_STRING );
    }

    @Test
    public void testGetErrorMessage() throws Exception
    {
        String str = HtmlUtil.getErrorMessage( html );
        LOGGER.info( str );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetErrorMessageNull() throws Exception
    {
        HtmlUtil.getErrorMessage( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetErrorMessageEmpty() throws Exception
    {
        HtmlUtil.getErrorMessage( DataProvider.EMPTY_STRING );
    }
}
