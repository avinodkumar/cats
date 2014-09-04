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
package com.comcast.cats.view;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.VideoRecorderServiceConstants;

/**
 * Test case to validate directory parser which is used in video recoder browser
 * UI.
 * 
 * @author ssugun00c
 * 
 */
public class DirectoryParserTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DirectoryParserTest.class );

    @Before
    public void setup()
    {
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH,
                "E:/temp/vlc-file-server" );
        System.setProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH,
                "http://127.0.0.1/" );
    }

    @Test
    public void parse()
    {
        String baseDirectory = System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH )
                + System.getProperty( "file.separator" );

        DocumentBean node = new DocumentBean();

        String host = "http://subin.com/";

        node.setChilds( DirectoryParser.parse( baseDirectory, host ) );

        for ( DocumentBean year : node.getChilds() )
        {
            LOGGER.info( "+----" + year.getName() );

            for ( DocumentBean month : year.getChilds() )
            {
                LOGGER.info( "    +----" + month.getName() );

                for ( DocumentBean day : month.getChilds() )
                {
                    LOGGER.info( "        +----" + day.getName() );

                    for ( DocumentBean mac : day.getChilds() )
                    {
                        LOGGER.info( "            +----" + mac.getName() );

                        for ( DocumentBean file : mac.getChilds() )
                        {
                            LOGGER.info( "                +----" + file.getAbsolutePath() );
                        }
                    }
                }
            }
        }
    }

}
