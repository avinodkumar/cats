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

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.comcast.cats.domain.Location;
import com.comcast.cats.domain.SettopDesc;

/**
 * 
 * @author subinsugunan
 * 
 */
public class JAXBHelperTest
{
    protected final Logger LOGGER = LoggerFactory.getLogger( JAXBHelperTest.class );
    private static SettopDesc settop1, settop2;

    @BeforeClass
    public static void setup() throws URISyntaxException
    {
        Location location = new Location();
        location.setZone( "04" );
        location.setRow( "04" );
        location.setRack( "13" );
        location.setLab( "USA" );

        Map< String, String > extraProperties = new HashMap< String, String >();
        extraProperties.put( "Owner", "community" );
        extraProperties.put( "Plant", "1" );
        extraProperties.put( "Controller", "09" );

        settop1 = new SettopDesc();
        settop1.setLocation( location );
        settop1.setExtraProperties( extraProperties );
        settop1.setFirmwareVersion( "12345" );

        settop2 = new SettopDesc();
        settop2.setLocation( location );
        settop2.setExtraProperties( extraProperties );
        settop2.setFirmwareVersion( "12345" );

    }

    @Test
    public void testWriteExternal()
    {
        String xml = JAXBHelper.writeExternal( settop1 );
        LOGGER.info( xml + "\n" );

        xml = JAXBHelper.writeExternal( settop1, true );
        LOGGER.info( xml );
    }

    @Test
    public void testWriteExternalList() throws URISyntaxException
    {
        List< SettopDesc > settopList = new LinkedList< SettopDesc >();
        settopList.add( settop1 );
        settopList.add( settop2 );

        String xml = JAXBHelper.writeExternalList( settopList );
        LOGGER.info( xml + "\n" );

        xml = JAXBHelper.writeExternalList( settopList, true );
        LOGGER.info( xml );
    }

}
