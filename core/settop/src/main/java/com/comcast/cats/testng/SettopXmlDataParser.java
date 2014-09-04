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
package com.comcast.cats.testng;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Utility class to handle input xml file.
 * 
 * @author subinsugunan
 * 
 */
public class SettopXmlDataParser extends DefaultHandler
{
    static final Logger           log       = LoggerFactory.getLogger( SettopXmlDataParser.class );
    private Map< String, String > settopMap = new HashMap< String, String >();

    /**
     * For parsing the test data.
     * 
     * @param path
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void parseTestData( String path ) throws ParserConfigurationException, SAXException, IOException
    {
        if ( null != path )
        {
            if ( log.isDebugEnabled() )
            {
                log.debug( "Parsing test data xml" );
            }
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating( true );
            factory.setNamespaceAware( false );
            SAXParser saxParser = factory.newSAXParser();

            saxParser.parse( path, this );

            if ( log.isDebugEnabled() )
            {
                log.debug( "Parsing successfully completed" );
            }
        }
    }

    /**
     * For set elements in settop map.
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    // Element Events
    public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
    {
        // currentItem = qName;
        if ( ( null != qName ) && qName.equals( "settop" ) && ( null != attributes.getValue( "hostMac" ) )
                && ( null != attributes.getValue( "contentType" ) ) )
        {
            settopMap.put( attributes.getValue( "hostMac" ), attributes.getValue( "contentType" ) );
        }
    }

    /**
     * For getting the settop map.
     * 
     * @return map
     */
    public Map< String, String > getSettopMap()
    {
        return settopMap;
    }
}
