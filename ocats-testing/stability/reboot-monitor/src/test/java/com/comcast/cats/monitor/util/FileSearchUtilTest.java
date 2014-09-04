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
package com.comcast.cats.monitor.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Test;

/**
 * Test cases for {@link FileSearchUtil}.
 * 
 * @author SSugun00c
 * 
 */
public class FileSearchUtilTest
{
    public static final String  REGEX_SA_E2100           = "Trap: number 0x09";
    public static final String  REGEX_RNG_200            = "Reason:";
    public static final String  REGEX_SA_3250HD          = "GmtsInitialize|\\+GWRK";
    public static final String  REGEX_SA_2200            = "data access exception";
    public static final String  REGEX_MOTO_DCH_3416      = "Dump complete.|ThinClient ver [0-90-9.0-90-9]";
    public static final String  REGEX_CISCO_TRUE_TWO_WAY = "LINUX started...";

    private static final Logger LOGGER                   = LoggerFactory.getLogger( FileSearchUtilTest.class );

    @Test
    public void testCountHitsByRegex()
    {
        long startTime = 0;

        for ( String filePath : geAllLogFiles() )
        {
            for ( String expression : getAllRegex() )
            {
                try
                {
                    startTime = System.nanoTime();
                    LOGGER.info( "Searching [" + expression + "] in [" + filePath + "]" );

                    int hits = FileSearchUtil.countHitsByRegex( filePath, expression );

                    LOGGER.info( "Found [" + hits + "] matches" );
                    LOGGER.info( "Took (" + ( System.nanoTime() - startTime ) / 1000000000.0 + ") seconds" );
                    LOGGER.info( "-----------------------------------------------------------------------------" );
                }
                catch ( IOException e )
                {
                    Assert.fail( e.getMessage() );
                }
            }
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexEmpty() throws IOException
    {
        FileSearchUtil.countHitsByRegex( geAllLogFiles().get( 0 ), "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexNull() throws IOException
    {
        FileSearchUtil.countHitsByRegex( geAllLogFiles().get( 0 ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexFilePathEmpty() throws IOException
    {
        FileSearchUtil.countHitsByRegex( "", getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexFilePathNull() throws IOException
    {
        FileSearchUtil.countHitsByRegex( null, getAllRegex().get( 0 ) );
    }

    @Test
    public void testCountHitsByRegexList()
    {
        long startTime = 0;
        for ( String filePath : geAllLogFiles() )
        {
            try
            {
                startTime = System.nanoTime();
                LOGGER.info( "Searching  in [" + filePath + "]" );

                int hits = FileSearchUtil.countHitsByRegexList( filePath, getAllRegex() );

                LOGGER.info( "Found [" + hits + "] matches" );
                LOGGER.info( "Took (" + ( System.nanoTime() - startTime ) / 1000000000.0 + ") seconds" );
                LOGGER.info( "-----------------------------------------------------------------------------" );
            }
            catch ( IOException e )
            {
                Assert.fail( e.getMessage() );
            }
        }

    }

    @SuppressWarnings( "unchecked" )
    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListEmpty() throws IOException
    {
        FileSearchUtil.countHitsByRegexList( geAllLogFiles().get( 0 ), Collections.EMPTY_LIST );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListNull() throws IOException
    {
        FileSearchUtil.countHitsByRegexList( geAllLogFiles().get( 0 ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListFilePathEmpty() throws IOException
    {
        FileSearchUtil.countHitsByRegexList( "", getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListFilePathNull() throws IOException
    {
        FileSearchUtil.countHitsByRegexList( null, getAllRegex() );
    }

    @Test
    public void testGetLinesByRegex()
    {
        long startTime = 0;

        for ( String filePath : geAllLogFiles() )
        {
            for ( String expression : getAllRegex() )
            {
                try
                {
                    startTime = System.nanoTime();
                    LOGGER.info( "Searching [" + expression + "] in [" + filePath + "]" );

                    List< String > lines = FileSearchUtil.getLinesByRegex( filePath, expression );
                    int hits = lines.size();

                    LOGGER.info( "Found [" + hits + "] matches" );
                    LOGGER.info( "{}", lines );
                    LOGGER.info( "Took (" + ( System.nanoTime() - startTime ) / 1000000000.0 + ") seconds" );
                    LOGGER.info( "-----------------------------------------------------------------------------" );
                }
                catch ( IOException e )
                {
                    Assert.fail( e.getMessage() );
                }
            }
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexEmpty() throws IOException
    {
        FileSearchUtil.getLinesByRegex( geAllLogFiles().get( 0 ), "" );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexNull() throws IOException
    {
        FileSearchUtil.getLinesByRegex( geAllLogFiles().get( 0 ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexFilePathEmpty() throws IOException
    {
        FileSearchUtil.getLinesByRegex( "", getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexFilePathNull() throws IOException
    {
        FileSearchUtil.getLinesByRegex( null, getAllRegex().get( 0 ) );
    }

    @Test
    public void testGetLinesByRegexList()
    {
        long startTime = 0;
        for ( String filePath : geAllLogFiles() )
        {
            try
            {
                startTime = System.nanoTime();
                LOGGER.info( "Searching  in [" + filePath + "]" );

                List< String > lines = FileSearchUtil.getLinesByRegexList( filePath, getAllRegex() );
                int hits = lines.size();

                LOGGER.info( "Found [" + hits + "] matches" );
                LOGGER.info( "{}", lines );
                LOGGER.info( "Took (" + ( System.nanoTime() - startTime ) / 1000000000.0 + ") seconds" );
                LOGGER.info( "-----------------------------------------------------------------------------" );
            }
            catch ( IOException e )
            {
                Assert.fail( e.getMessage() );
            }
        }

    }

    @SuppressWarnings( "unchecked" )
    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListEmpty() throws IOException
    {
        FileSearchUtil.getLinesByRegexList( geAllLogFiles().get( 0 ), Collections.EMPTY_LIST );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListNull() throws IOException
    {
        FileSearchUtil.getLinesByRegexList( geAllLogFiles().get( 0 ), null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListFilePathEmpty() throws IOException
    {
        FileSearchUtil.getLinesByRegexList( "", getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListFilePathNull() throws IOException
    {
        FileSearchUtil.getLinesByRegexList( null, getAllRegex() );
    }

    private List< String > geAllLogFiles()
    {
        List< String > fileList = new ArrayList< String >();

        fileList.add( "src/test/resources/logs/Reboot_10.244.82.150_0419_DAE_E2100.txt" );
        fileList.add( "src/test/resources/logs/Reboot_10.244.31.90_0204_QueCAMQ_RNG200.txt" );
        fileList.add( "src/test/resources/logs/Serial_0601_E3250HD_10.244.82.74.txt" );
        fileList.add( "src/test/resources/logs/SerialLog_111216-19_PORT107_10.244.31.48.txt" );
        fileList.add( "src/test/resources/logs/Serial1_0205_E2200_10.244.31.136_DAE_SgMgr.txt" );
        fileList.add( "src/test/resources/logs/DCH3416 reset on 6-1-2012 at 220pmET.txt" );
        fileList.add( "src/test/resources/logs/TB9_Box15_serial_com130_Feb16_Feb17.txt" );
        fileList.add( "src/test/resources/logs/CiscoBoot.log" );

        return fileList;
    }

    private List< String > getAllRegex()
    {
        List< String > fileList = new ArrayList< String >();

        fileList.add( REGEX_CISCO_TRUE_TWO_WAY );
        fileList.add( REGEX_MOTO_DCH_3416 );
        fileList.add( REGEX_RNG_200 );
        fileList.add( REGEX_SA_2200 );
        fileList.add( REGEX_SA_3250HD );
        fileList.add( REGEX_SA_E2100 );

        return fileList;
    }
}
