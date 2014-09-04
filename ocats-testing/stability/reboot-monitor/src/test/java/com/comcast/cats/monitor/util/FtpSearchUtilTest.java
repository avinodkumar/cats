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
import java.util.List;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link FtpSearchUtil}.
 * 
 * @author SSugun00c
 * 
 */
public class FtpSearchUtilTest
{
    public static final String  REGEX_SA_E2100           = "Trap: number 0x09";
    public static final String  REGEX_RNG_200            = "Reason:";
    public static final String  REGEX_SA_3250HD          = "GmtsInitialize|\\+GWRK";
    public static final String  REGEX_SA_2200            = "data access exception";
    public static final String  REGEX_MOTO_DCH_3416      = "Dump complete.|ThinClient ver [0-90-9.0-90-9]";
    public static final String  REGEX_CISCO_TRUE_TWO_WAY = "LINUX started...";

    public static final String  FILE_PATH                = "LINUX started...";

    private static final Logger LOGGER                   = LoggerFactory.getLogger( FtpSearchUtilTest.class );
    private static final String EMPTY_STRING             = "";

    private String              host;
    private String              username;
    private String              password;
    private String              directory;
    private String              filename;

    @Before
    public void setup()
    {
        host = "10.253.207.210";
        username = "dncsftp";
        password = "dncsftp";
        directory = "/dvs/resapp/Tools/";
        filename = "cmd2000_log_120727.txt";
    }

    @Test
    public void testCountHitsByRegex()
    {
        long startTime = 0;

        for ( String expression : getAllRegex() )
        {
            try
            {
                startTime = System.nanoTime();
                LOGGER.info( "Searching [" + expression + "] in sftp://" + username + ":password@" + host + ":"
                        + directory + filename );

                int hits = FtpSearchUtil.countHitsByRegex( host, username, password, directory, filename, expression );

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

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexEmptyUserName() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, EMPTY_STRING, password, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexNullUserName() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, null, password, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexEmptyPassword() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, EMPTY_STRING, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexNullPassword() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, null, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexEmptyDirectory() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, password, EMPTY_STRING, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexNullDirectory() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, password, null, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexEmptyFilename() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, password, directory, EMPTY_STRING, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexNullFilename() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, password, directory, null, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexEmptyExpression() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, password, directory, filename, EMPTY_STRING );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexNullExpression() throws IOException
    {
        FtpSearchUtil.countHitsByRegex( host, username, password, directory, filename, null );
    }

    @Test
    public void testCountHitsByRegexList()
    {
        long startTime = 0;

        startTime = System.nanoTime();

        int hits;
        try
        {
            hits = FtpSearchUtil.countHitsByRegexList( host, username, password, directory, filename, getAllRegex() );
            LOGGER.info( "Found [" + hits + "] matches" );
            LOGGER.info( "Took (" + ( System.nanoTime() - startTime ) / 1000000000.0 + ") seconds" );
            LOGGER.info( "-----------------------------------------------------------------------------" );
        }
        catch ( IOException e )
        {
            Assert.fail( e.getMessage() );
        }
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListEmptyUserName() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, EMPTY_STRING, password, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListNullUserName() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, null, password, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListEmptyPassword() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, EMPTY_STRING, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListNullPassword() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, null, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListEmptyDirectory() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, password, EMPTY_STRING, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListNullDirectory() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, password, null, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListEmptyFilename() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, password, directory, EMPTY_STRING, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListNullFilename() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, password, directory, null, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListEmptyExpression() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, password, directory, filename, new ArrayList< String >() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCountHitsByRegexListNullExpression() throws IOException
    {
        FtpSearchUtil.countHitsByRegexList( host, username, password, directory, filename, null );
    }

    @Test
    public void testGetLinesByRegex()
    {
        long startTime = 0;

        for ( String expression : getAllRegex() )
        {
            try
            {
                startTime = System.nanoTime();
                LOGGER.info( "Searching [" + expression + "] in sftp://" + username + ":password@" + host + ":"
                        + directory + filename );

                List< String > lines = FtpSearchUtil.getLinesByRegex( host, username, password, directory, filename,
                        expression );
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

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexEmptyUserName() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, EMPTY_STRING, password, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexNullUserName() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, null, password, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexEmptyPassword() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, EMPTY_STRING, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexNullPassword() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, null, directory, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexEmptyDirectory() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, password, EMPTY_STRING, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexNullDirectory() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, password, null, filename, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexEmptyFilename() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, password, directory, EMPTY_STRING, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexNullFilename() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, password, directory, null, getAllRegex().get( 0 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexEmptyExpression() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, password, directory, filename, EMPTY_STRING );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexNullExpression() throws IOException
    {
        FtpSearchUtil.getLinesByRegex( host, username, password, directory, filename, null );
    }

    @Test
    public void testGetLinesByRegexList()
    {
        long startTime = 0;

        startTime = System.nanoTime();

        int hits;
        try
        {
            List< String > lines = FtpSearchUtil.getLinesByRegexList( host, username, password, directory, filename,
                    getAllRegex() );
            hits = lines.size();
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

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListEmptyUserName() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, EMPTY_STRING, password, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListNullUserName() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, null, password, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListEmptyPassword() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, EMPTY_STRING, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListNullPassword() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, null, directory, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListEmptyDirectory() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, password, EMPTY_STRING, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListNullDirectory() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, password, null, filename, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListEmptyFilename() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, password, directory, EMPTY_STRING, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListNullFilename() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, password, directory, null, getAllRegex() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListEmptyExpression() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, password, directory, filename, new ArrayList< String >() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLinesByRegexListNullExpression() throws IOException
    {
        FtpSearchUtil.getLinesByRegexList( host, username, password, directory, filename, null );
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
