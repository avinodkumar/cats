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
package com.comcast.cats.vision.configuration;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * 
 * @author cfrede001
 */
public class ConfigurationManager
{
    private static final Logger logger = Logger.getLogger( ConfigurationManager.class );

    public static boolean storeVisionInfo( File file, VisionInfo info )
    {
        try
        {
            // String packageName = VisionInfo.class.getPackage().getName();
            JAXBContext jc = JAXBContext.newInstance( VisionInfo.class );
            Marshaller m = jc.createMarshaller();
            m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            m.marshal( info, file );
            return true;
        }
        catch ( JAXBException ex )
        {
            logger.error( "JAXBException caught " + ex.getMessage() );
        }
        return false;
    }

    public static VisionInfo loadVisionInfo( File file )
    {
        try
        {
            String packageName = VisionInfo.class.getPackage().getName();
            JAXBContext jc = JAXBContext.newInstance( packageName );
            Unmarshaller u = jc.createUnmarshaller();
            return ( VisionInfo ) u.unmarshal( file );
        }
        catch ( JAXBException jaxEx )
        {
            logger.error( "JAXBException caught" );
        }
        return null;
    }
}
