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
package com.comcast.cats.domain.configuration;

import static org.junit.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.comcast.cats.domain.configuration.CatsHome;

/**
 * 
 * @author cfrede001
 * 
 */
public class CatsHomeTest
{
    protected final Log LOGGER = LogFactory.getLog( getClass() );

    ApplicationContext  ctx;

    public CatsHomeTest()
    {
        setCatsHome( getTargetDirectory() );

        AnnotationConfigApplicationContext appCtx = new AnnotationConfigApplicationContext();
        appCtx.register( CatsHome.class );
        appCtx.refresh();
        ctx = appCtx;

    }

    public static void setCatsHome( String catsHome )
    {
        System.setProperty( "cats.home", catsHome );
    }

    public static String getTargetDirectory()
    {
        return System.getProperty( "user.dir" ) + CatsHome.FILE_SEPERATOR + "target" + CatsHome.FILE_SEPERATOR
                + CatsHome.CATS_HOME_RELATIVE_DIR;
    }

    public String getTmpDirectory()
    {
        return getTargetDirectory() + CatsHome.FILE_SEPERATOR + CatsHome.CATS_HOME_TEMP_RELATIVE_DIR;
    }

    protected void printAssertEqual( String expected, String actual )
    {
        LOGGER.info( "Expected = " + expected );
        LOGGER.info( "  Actual = " + actual );
        assertEquals( expected, actual );
    }

    @Test
    public void testCatsHomeDirectory()
    {
        CatsHome home = ctx.getBean( CatsHome.class );
        printAssertEqual( getTargetDirectory(), home.getCatsHomeDirectory() );
    }

    @Test
    public void testCatsTmpDirectory()
    {
        CatsHome home = ctx.getBean( CatsHome.class );
        printAssertEqual( getTmpDirectory(), home.getCatsTempDirectory() );
    }
}
