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
package com.comcast.cats.local.vision;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.mock.configuration.CatsFrameworkLocal;
import com.comcast.cats.vision.CATSVisionApplication;

/**
 * <pre>
 * Simple test application to work with CATSVision without having any dependency
 * with the server.
 * 
 * Configure data for this class:
 * 
 * Settop used for testing
 * src/test/resources/settop.xml
 * 
 * My Settop - Tab
 * 
 * Available Settops
 * src/test/resources/settop_available.xml
 * 
 * Allocated Settops
 * src/test/resources/settop-allocated.xml
 * 
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class CATSVisionApplicationLocal extends CATSVisionApplication
{
    /**
     * CATS Server against which we'll create providers.
     */
    private static final String SERVER_UR = "http://cats-dev3.cable.comcast.com/";

    /**
     * This has no effect to run CATSVision in local. This is just to avoid a
     * NPE.
     */
    private static final String MAC_ID    = "00:19:47:25:AC:A8";

    /**
     * Main method launching the application.
     */
    public static void main( String[] args )
    {
        launch( CATSVisionApplicationLocal.class, args );
    }

    @Override
    protected void initialize( String[] args )
    {
        if ( args.length <= 0 )
        {
            System.setProperty( CatsProperties.SERVER_URL_PROPERTY, SERVER_UR );
            System.setProperty( CatsProperties.SETTOP_DEFAULT_PROPERTY, MAC_ID );
        }
        super.initialize( args );
    }

    @Override
    protected void setupApplicationContext()
    {
        CatsFramework framework = new CatsFrameworkLocal( new CatsVisionApplicationContextLocal() );
        context = framework.getContext();
    }
}
