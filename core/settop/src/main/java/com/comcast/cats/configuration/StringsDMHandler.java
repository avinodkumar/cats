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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.domain.configuration.CatsProperties;

/**
 * Simple class for handling Strings DM file.
 * 
 * @author cfrede001 Test for Jenkins again
 */
@Named
public class StringsDMHandler
{
    private static Logger        logger                     = LoggerFactory.getLogger( StringsDMHandler.class );
    public static final String   STRINGS_DM_LOCATION        = "strings.dm";
    public static final String   STRINGS_DM_SERVER_LOCATION = "/cats/config/strings.dm";

    private final CatsProperties catsProps;
    /**
     * Source location for strings.dm
     */
    private URI                  stringsDMSourceLocation    = null;

    /**
     * Local location that will be referenced.
     */
    private URI                  stringsDMLocation          = null;

    private boolean              previouslyAttempted        = false;

    @Inject
    public StringsDMHandler( CatsProperties catsProps )
    {
        this.catsProps = catsProps;
    }

    private void init() throws URISyntaxException
    {
        // Determine where the strings.dm will come from.
        stringsDMSourceLocation = determineStringsDMLocation( catsProps );
        logger.info( "Strings DM Source location = " + stringsDMSourceLocation );
        // Destination location for where the strings.dm will be placed.
        stringsDMLocation = getStringsDMFileLocation( catsProps.getCatsHome().getCatsHomeDirectory() );
        logger.info( "Strings DM Destination location = " + stringsDMLocation );
    }

    /**
     * Find Strings DM file from cats.props location or utilize default location
     * on server.
     * 
     * @return URI to the String DM location either on disk or on the server.
     * @throws URISyntaxException
     */
    protected URI determineStringsDMLocation( CatsProperties catsProps ) throws URISyntaxException
    {
        URI loc = null;
        String propLocation = catsProps.getStringsDMLocation();
        if ( propLocation != null && !propLocation.isEmpty() )
        {
            //
            // loc = new URI("file", null, propLocation, null);
            logger.info( "Utilize CatsProperties strings.dm file location = " + propLocation );
            loc = new URI( propLocation );
        }
        else
        {
            logger.info( "Utilize default server location for strings.dm" );
            loc = getDefaultServerLocation( catsProps.getServerHost() );
        }
        logger.info( "determineStringDMLocation = " + loc.toString() );
        return loc;
    }

    /**
     * To get default server location with host name.
     * 
     * @param hostName
     * @return URI to the server.
     * @throws URISyntaxException
     */
    protected URI getDefaultServerLocation( String hostName ) throws URISyntaxException
    {
        URI uri = new URI( "http://" + hostName + STRINGS_DM_SERVER_LOCATION );
        logger.info( "Strings DM Server Location: " + uri.toString() );
        return uri;
    }

    /**
     * Determine location for StringsDMFile and download it to our local CATS
     * temporary directory.
     * 
     * @return - Return the local location for this file as a path.
     * @throws URISyntaxException
     * @throws IOException
     */
    public String retrieveStringsDMFile() throws URISyntaxException, IOException
    {
        if ( !previouslyAttempted )
        {
            previouslyAttempted = true;
            init();
            downloadStringsDMFile();
        }
        return new File( stringsDMLocation ).getAbsolutePath();
    }

    /**
     * Return the local path URI for strings.dm.
     * 
     * @param catsHome
     *            - String representing the CATS_HOME location.
     * @return
     * @throws URISyntaxException
     */
    private URI getStringsDMFileLocation( String catsHome ) throws URISyntaxException
    {
        /*
         * For now, turn this into a file and then return the URI from the file.
         * It might make more sense to use URL, since this is a locator.
         */
        File catsHomeDir = new File( catsHome + "/tmp/" + STRINGS_DM_LOCATION );
        return catsHomeDir.toURI();
    }

    /**
     * Download the file from the location and place it in our tmp directory.
     * 
     * @return - String representing the path to the downloaded strings.dm file.
     * @throws URISyntaxException
     * @throws IOException
     */
    private String downloadStringsDMFile() throws URISyntaxException, IOException
    {
        // Get an InputStream from the stringsDMSourceLocation.
        InputStream is = stringsDMSourceLocation.toURL().openStream();

        // Output vars
        File dmFile = new File( stringsDMLocation );
        FileOutputStream os = new FileOutputStream( dmFile );
        IOUtils.copy( is, os );

        is.close();
        os.close();
        return stringsDMLocation.toString();
    }

}
