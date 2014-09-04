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
package com.comcast.cats.mock.configuration;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.mock.service.DeviceSearchServiceLocal;

/**
 * Class used to instantiate settops from a local file.
 * 
 * @author cfrede001
 * 
 */
public class CatsFrameworkLocal extends CatsFramework
{

    /**
     * Create a CatsSettopFramework class that includes a dummy list of settops
     * to wire.
     * 
     * @param context {@linkplain CatsContext}
     */
    public CatsFrameworkLocal( CatsContext context )
    {
        super( context );
    }

    /**
     * Create a CatsSettopFramework class.
     * 
     * @param context {@linkplain CatsContext}
     * @param settopListFile
     */
    public CatsFrameworkLocal( CatsContext context, String settopListFile )
    {
        super( context );

        if ( ( null != settopListFile ) && ( !settopListFile.isEmpty() ) )
        {
            context.getBean( DeviceSearchServiceLocal.class ).setAvailableSettopFilepath( settopListFile );
        }
    }

    /**
     * Create a CatsSettopFramework class.
     * 
     */
    public CatsFrameworkLocal()
    {
        super( new CatsContextLocal() );
    }
}
