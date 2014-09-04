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

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.comcast.cats.domain.configuration.CatsHome;
import com.comcast.cats.domain.configuration.CatsProperties;

public class StringsDMTest
{
    // private static final String DUMMY_CATS_PROPS_LOCATION =
    // "target/cats.props";

    public static ApplicationContext getApplicationContext( String catsHome )
    {
        CatsHomeTest.setCatsHome( catsHome );
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register( CatsHome.class );
        ctx.register( CatsProperties.class );
        ctx.register( StringsDMHandler.class );
        ctx.refresh();
        return ctx;
    }

    public StringsDMTest()
    {

    }

    /*
     * private void createTestCatsPropsWithStrings( String path ) throws
     * IOException, URISyntaxException { URI fakePath = new URI(
     * System.getProperty( "user.dir" ) + path ); FileWriter fw = new
     * FileWriter( new File( fakePath ) ); fw.write( "strings.dm.location=" +
     * fakePath.toString() ); fw.flush(); fw.close(); }
     */
    @Test
    public void getStringsDMFromCatsProps() throws URISyntaxException, IOException
    {
       /* String catsHomeDir = System.getProperty( "user.dir" ) + "/target/test-classes/stringsdm/";
        CatsHome catsHome = new CatsHome();
        catsHome.configure();
        catsHome.setSystemCatsHome( catsHomeDir );
        CatsProperties props = new CatsProperties( catsHome );
        // The local strings.dm.location that would typically be specified by
        // cats.props.
        String location = System.getProperty( "user.dir" ) + "/target/test-classes/stringsdm/strings.dm";
        File file = new File( location );
        if ( file.exists() )
        {
            LOGGER.info( "File Path = " + file.toURI() );
        }
        props.setStringsDMLocation( file.toURI().toString() );
        StringsDMHandler handler = new StringsDMHandler( props );
        handler.retrieveStringsDMFile();*/
    }
}
