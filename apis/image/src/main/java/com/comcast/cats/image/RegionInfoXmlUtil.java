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
package com.comcast.cats.image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RegionInfoXmlUtil helps in marshalling and unmarshalling Xml files.
 * 
 * @author aswathyann
 * 
 */
public class RegionInfoXmlUtil
{
    private static final Logger logger = LoggerFactory.getLogger( RegionInfoXmlUtil.class );

    /**
     * Write data to the specified Xml file
     * 
     * @param imageRegionInfo
     *            instance of ImageRegionInfo
     * @param filePath
     *            file path
     * @throws IOException
     * @throws JAXBException
     */
    public static void writeToJaxbXML( final ImageRegionInfo imageRegionInfo, final OutputStream outputStream )
            throws JAXBException
    {
        logger.debug( "Marshalling imageRegionInfo." );
        if ( outputStream == null )
        {
            logger.debug( "OutputStream was null." );
            throw new IllegalArgumentException( "OutputStream cannot be null." );
        }

        JAXBContext jContext = JAXBContext.newInstance( ImageRegionInfo.class );

        Marshaller marshaller = jContext.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );

        marshaller.marshal( imageRegionInfo, outputStream );
    }

    /**
     * Load data from Xml file
     * 
     * @param inputStream
     * @return ImageRegionInfo
     * @throws FileNotFoundException
     * @throws JAXBException
     */
    public static ImageRegionInfo loadFromJaxbXML( final InputStream inputStream ) throws JAXBException
    {

        logger.debug( "Unmarshalling imageRegionInfo." );
        if ( inputStream == null )
        {
            logger.debug( "InputStream was null." );
            throw new IllegalArgumentException( "InputStream cannot be null." );
        }

        ImageRegionInfo imageRegionInfo = null;

        JAXBContext jContext = JAXBContext.newInstance( ImageRegionInfo.class );
        Unmarshaller unmarshaller = jContext.createUnmarshaller();

        imageRegionInfo = ( ImageRegionInfo ) unmarshaller.unmarshal( inputStream );

        return imageRegionInfo;

    }
}
