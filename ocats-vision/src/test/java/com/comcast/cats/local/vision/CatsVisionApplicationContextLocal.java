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

import com.comcast.cats.EnvironmentFactory;
import com.comcast.cats.SettopFactoryImpl;
import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.domain.configuration.CatsHome;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.service.AllocationServiceImpl;

/*import com.comcast.cats.CatsEnvironment;
import com.comcast.cats.EnvironmentFactory;
import com.comcast.cats.SettopFactoryImpl;
import com.comcast.cats.SettopLoggerFactory;
import com.comcast.cats.configuration.ApplicationContextProvider;
import com.comcast.cats.mock.service.AllocationServiceLocal;
import com.comcast.cats.mock.service.EnvironmentServiceLocal;
import com.comcast.cats.vision.configuration.CatsVisionApplicationContext;*/

public class CatsVisionApplicationContextLocal extends CatsContext
{

    public CatsVisionApplicationContextLocal()
    {
        super();
        addCatsPackagesToContext();
    }

    /**
     * Override this function to add additional packages to be scanned during
     * spring context creation.
     */
    @Override
    protected void addCatsPackagesToContext()
    {
        this.scan( "com.comcast.cats.vision" );
        this.scan( "com.comcast.cats.provider" );
        this.scan( "com.comcast.cats.event.impl" );
        this.scan( "com.comcast.cats.decorator" );

        this.register( CatsProperties.class );
        this.register( CatsHome.class );
        this.register( SettopFactoryImpl.class );
        this.register( EnvironmentFactory.class );
  //      this.register( SettopLoggerFactory.class );
        
        this.register( SettopDomainServiceLocal.class );
        this.register( AllocationServiceImpl.class );
    }

}
