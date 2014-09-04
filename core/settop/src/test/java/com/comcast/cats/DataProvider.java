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
package com.comcast.cats;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.domain.SettopDesc;

/**
 * Data provider class to support testing.
 * 
 * @author subinsugunan
 * 
 */
@Singleton
public class DataProvider
{
    private final Logger        LOGGER                = LoggerFactory.getLogger( getClass() );

    private String              remotePath            = "gc100://192.168.120.2/?port=1";
    private String              powerPath             = "wti1600://192.168.120.2:23/?outlet=1";
    private String              videoPath             = "axis://192.168.120.2/?camera=1";


    public static final String  DUMMY_COUNT           = "10";
    public static final String  EMPTY_STRING          = "";

    public static final String  INVALID_MAC_ID        = "00:00:00:00:00:00";
    public static final String  INVALID_MAC_ID_FORMAT = "1234567890";
    public static final String  INVALID_ID            = "1234567890";
    public static final String  INVALID_NAME          = "1234567890";
    public static final String  INVALID_URL           = "http://invalid.domain.com";
    public static final String  INVALID_IP_ADDRESS    = "1234567890";

    public static final String  PARAM_MAC_ID          = "mac";
    public static final String  PARAM_NAME            = "name";
    public static final String  PARAM_VALUE           = "value";
    public static final String  STB_PROP_MANUFACTURER = "Manufacturer";
    public static final String  STB_PROP_MODEL        = "Model";

    private static final String MAC_ID                = "00:00:00:00:00:00";

    private static final String SETTOP_GROUP_NAME     = "CATS Development and Test Group";

    public SettopDesc getSettopDesc()
    {
        return getSettopDesc( MAC_ID );
    }

    public SettopDesc getSettopDesc( String macId )
    {
        SettopDesc settop = new SettopDesc();
        try
        {
            JAXBContext jc = JAXBContext.newInstance( SettopDesc.class );
            Unmarshaller um = jc.createUnmarshaller();
            settop = ( SettopDesc ) um.unmarshal( new java.io.FileInputStream( "src/test/resources/settop.xml" ) );
            settop.setHostMacAddress( macId );
            settop.setId( "1234567890" );

            settop.setRemotePath( new URI( remotePath ) );
            settop.setPowerPath( new URI( powerPath ) );
            settop.setVideoPath( new URI( videoPath ) );
            settop.setRemoteType( "COMCAST" );
            settop.setEnvironmentId( "environmentId" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return settop;
    }

    public String getMacId()
    {
        return "00:21:80:E5:75:01";
    }

    public URI getRemotePath()
    {
        URI uri = null;
        try
        {
            uri = new URI( remotePath );
        }
        catch ( URISyntaxException e )
        {
            LOGGER.error( "Error creating dummy RemotePath" );
        }
        return uri;
    }

    public URI getPowerPath()
    {
        URI uri = null;
        try
        {
            uri = new URI( powerPath );
        }
        catch ( URISyntaxException e )
        {
            LOGGER.error( "Error creating dummy PowerPath" );
        }
        return uri;
    }

    public String getRemoteType()
    {
        return "COMCAST";
    }

    public List< SettopDesc > getSettopDescList()
    {
        List< SettopDesc > settopDescList = new ArrayList< SettopDesc >();
        SettopDesc settopDesc = getSettopDesc();
        // settopDesc.setEnvironmentId( EnvironmentServiceMockImpl.CATS_DEV );
        settopDescList.add( settopDesc );
        settopDescList.add( settopDesc );

        return settopDescList;
    }

    public String getUnitAdderess()
    {
        return "1234";
    }

    public String getIpAdderess()
    {
        return "192.168.160.2";
    }

    public String getModel()
    {
        return "1234";
    }

    public static String getSettopGroupName()
    {
        return SETTOP_GROUP_NAME;
    }

}
