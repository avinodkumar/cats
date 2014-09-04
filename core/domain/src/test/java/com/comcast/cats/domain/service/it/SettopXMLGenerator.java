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
package com.comcast.cats.domain.service.it;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import com.comcast.cats.domain.util.SSLUtil;

/**
 * Simple utility to create XML file for holding settop config
 * 
 * @author ssugun00c
 * 
 */
public class SettopXMLGenerator
{
    private final Logger           logger                 = LoggerFactory.getLogger( getClass() );

    private static final String REGEX = "[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\uD800\uDC00-\uDBFF\uDFFF]";
    private static final String YES                    = "yes";


    private static final String MAC_ID                 = "00:21:80:E5:75:01";


    @Before
    public void setup()
    {
        SSLUtil.disableCertificateValidation();
    }

    @Test
    public void findByMacId()
    {
        String name = new Object(){}.getClass().getEnclosingMethod().getName(); 
        String getSettopUrl = "http://localhost/rest/cats/" + "show?mac=" + MAC_ID;
        String fileName = "settop.xml";
        new SettopXMLGenerator().generateXmlFile( name, getSettopUrl, fileName );
    }

    @Test
    public void findAllAllocated()
    {
        String name = new Object(){}.getClass().getEnclosingMethod().getName(); 
        String getAllocatedSettopUrl = "http://localhost/rest/cats/" + "allocated?token=" + 8520;
        String fileName = "allocated-settops.xml";
        new SettopXMLGenerator().generateXmlFile( name, getAllocatedSettopUrl, fileName );
    }

    @Test
    public void findAllAvailable()
    {
        String name = new Object(){}.getClass().getEnclosingMethod().getName(); 
        String getAvailableSettopUrl = "http://localhost/rest/cats/" + "available?token=" + 8520;
        String fileName = "available-settops.xml";
        new SettopXMLGenerator().generateXmlFile( name, getAvailableSettopUrl, fileName );
    }

    /**
     * Invokes URL and reads the response
     * 
     * @param restUrl
     * @param fileName
     */
    private void generateXmlFile( String methodName, String restUrl, String fileName )
    {
        try
        {
            URL url = new URL( restUrl );
            URLConnection con = url.openConnection();
            Reader inputStreamReader = new InputStreamReader( con.getInputStream() );
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );

            StringBuilder result = new StringBuilder();
            String data = null;

            while ( ( data = bufferedReader.readLine() ) != null )
            {
                result.append( data );
            }

            String cleanString = result.toString();

            int index = cleanString.indexOf( '<' );

            // Remove extra prolog characters in the XML string.
            if ( index != 0 )
            {
                cleanString = cleanString.substring( index );
            }

            // Remove invalid XML characters.
            cleanString = cleanString.replaceAll( REGEX, "" );

            // XML tag <powerPath> is a special case.
            // Don't know what invalid character is in this string: "pw�owerPath"
            cleanString = cleanString.replaceAll( "pw.owerPath", "powerPath" );

            logger.info( methodName + ": " + cleanString );

            FileWriter fstream = new FileWriter( fileName );
            BufferedWriter out = new BufferedWriter( fstream );
            out.write( prettyPrintXml( cleanString ) );
            out.close();
        }
        catch ( Exception e )
        {
            logger.error( e.getMessage() );
        }
    }

    /**
     * Pretty print XML
     * 
     * @param sourceXml
     * @return
     */
    private String prettyPrintXml( String sourceXml )
    {
        String formattedXML = null;
        try
        {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();

            serializer.setOutputProperty( OutputKeys.INDENT, YES );
            serializer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
            Source xmlSource = new SAXSource( new InputSource( new ByteArrayInputStream( sourceXml.getBytes() ) ) );
            StreamResult res = new StreamResult( new ByteArrayOutputStream() );
            serializer.transform( xmlSource, res );

            formattedXML = new String( ( ( ByteArrayOutputStream ) res.getOutputStream() ).toByteArray() );
        }
        catch ( Exception e )
        {
            logger.error( e.getMessage() );
        }
        return formattedXML;
    }
}
