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
package com.comcast.cats.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

import com.comcast.cats.AbstractSettop;
import com.comcast.cats.Settop;
import com.comcast.cats.provider.ImageCompareProvider;

public abstract class AbstractSettopTest extends AbstractSettop
{
    private static final long      serialVersionUID = 1L;
    protected Settop               settop;
    protected ImageCompareProvider image;
    protected final Logger            LOGGER           = LoggerFactory.getLogger( getClass() );

    @BeforeClass
    public static void setup()
    {
    }
    public AbstractSettopTest()
    {    	
    }
    
    @SuppressWarnings( "rawtypes" )
    public AbstractSettopTest( Settop settop )
    {
        super( settop, settop.getRemote(), settop.getPower(), settop.getAudio(), settop.getTrace(), settop.getVideo(),
                settop.getVideoSelection() );
        this.settop = settop;
        this.image = settop.getImageCompareProvider();

        LOGGER.info( "Running tests against [" + settop.getHostMacAddress() + "]" );

        LOGGER.info( "TESTING INTERFACES HERE" );

        Class clazz = AbstractSettopTest.class;

        Class[] classes = clazz.getClasses();
        for ( int c = 0; c < classes.length; c++ )
        {
            String className = classes[ c ].getName();
            LOGGER.info( className );
        }
        Class[] theInterfaces = clazz.getInterfaces();

        for ( int i = 0; i < theInterfaces.length; i++ )
        {
            String interfaceName = theInterfaces[ i ].getName();
            LOGGER.info( interfaceName );
        }
    }

    @AfterSuite
    public void afterSuite()
    {
    }
}