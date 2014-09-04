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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * 
 * @author cfrede001
 */
public class VisionInfoTest
{
    protected final Log LOGGER = LogFactory.getLog( getClass() );

    @Test
    public void storeVisionInfo()
    {
        VisionInfo info = new VisionInfo();
        SettopAccess access1 = new SettopAccess();
        access1.setMac( "00:00:00:00:00:01" );
        access1.setAlias( "TestMe1" );
        SettopAccess access2 = new SettopAccess();
        access2.setMac( "00:00:00:00:00:02" );
        access2.setAlias( "TestMe2" );
        info.setLastUserName( "cfrede001" );
        info.getAccessList().add( access1 );
        info.getAccessList().add( access2 );
        ConfigurationManager.storeVisionInfo( new File( "output.xml" ), info );
    }

    @Test
    public void loadVisionInfo()
    {
        VisionInfo info = ConfigurationManager.loadVisionInfo( new File( "output.xml" ) );
        LOGGER.info( info );
    }
}
