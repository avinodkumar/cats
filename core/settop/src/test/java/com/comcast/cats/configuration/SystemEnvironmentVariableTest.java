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
package com.comcast.cats.configuration;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.comcast.cats.domain.configuration.CatsHome;

public class SystemEnvironmentVariableTest
{
    protected final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    public SystemEnvironmentVariableTest()
    {
        System.setProperty( "cats.home", getTargetDirectory() );
    }

    public String getTargetDirectory()
    {
        return System.getProperty( "user.dir" ) + File.separator + "target";
    }

    @Test
    public void testSystemProperties()
    {
        LOGGER.info( "Environment: CATS_HOME = " + System.getenv( "CATS_HOME" ) );
        LOGGER.info( "SystemProps: cats.home = " + System.getProperty( "cats.home" ) );
    }

    @Test
    public void testApplicationContextProperties()
    {
        AnnotationConfigApplicationContext annCtx = new AnnotationConfigApplicationContext();
        annCtx.register( CatsHome.class );
        annCtx.refresh();
        CatsHome home = annCtx.getBean( CatsHome.class );
        // Properties props = (Properties) annCtx.getBean("someProperty");
        LOGGER.info( "CATS Home Location = " + home.getCatsHomeDirectory() );
        // LOGGER.info("Props = " + props.getProperty("test.me"));
    }
}