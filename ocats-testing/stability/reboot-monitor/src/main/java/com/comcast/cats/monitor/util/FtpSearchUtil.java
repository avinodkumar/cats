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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Collection of utility methods to search in remote files.
 * 
 * @author SSugun00c
 * 
 */
public class FtpSearchUtil
{
    private static final Logger LOGGER                   = LoggerFactory.getLogger( FtpSearchUtil.class );
    private static final String TYPE_SFTP                = "sftp";
    private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    private static final String NO                       = "no";
    private static final String OR                       = "|";

    private FtpSearchUtil()
    {
    }

    /**
     * 
     * @param host
     * @param username
     * @param password
     * @param directory
     * @param filename
     * @param expression
     * @return
     * @throws IOException
     */
    public static Integer countHitsByRegex( String host, String username, String password, String directory,
            String filename, String expression ) throws IOException
    {
        if ( !( isValidInput( host, username, password, directory, filename, expression ) ) )
        {
            throw new IllegalArgumentException( "Cannot perform FTP search. Make sure all inputs are valid" );
        }

        int hits = 0;

        JSch jsch = new JSch();
        Session session = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try
        {
            session = jsch.getSession( username, host );
            session.setConfig( getHostConfiguration() );
            session.setPassword( password );
            session.connect();

            LOGGER.info( "Session starting [" + host + "]" );

            Channel channel = session.openChannel( TYPE_SFTP );
            channel.connect();

            LOGGER.info( "Connected  [" + host + "]" );

            ChannelSftp sftpChannel = ( ChannelSftp ) channel;
            sftpChannel.cd( directory );

            LOGGER.info( "Directory changed  [" + directory + "]" );

            InputStream is = sftpChannel.get( filename );

            LOGGER.info( "Starting read [" + filename + "]" );

            Pattern pattern = Pattern.compile( expression );

            bufferedReader = new BufferedReader( new InputStreamReader( is ) );

            String line = "";
            while ( ( line = bufferedReader.readLine() ) != null )
            {
                if ( pattern.matcher( line ).find() )
                {
                    hits++;
                }
            }
            sftpChannel.exit();
            session.disconnect();
        }
        catch ( Exception e )
        {
            throw new IOException( e.getMessage() );
        }
        finally
        {
            try
            {
                if ( fileReader != null )
                {
                    fileReader.close();
                }
                if ( bufferedReader != null )
                {
                    bufferedReader.close();
                }
            }
            catch ( Exception e )
            {
                throw new IOException( e.getMessage() );
            }
        }

        return hits;
    }

    /**
     * 
     * @param host
     * @param username
     * @param password
     * @param directory
     * @param filename
     * @param expressions
     * @return
     * @throws IOException
     */
    public static Integer countHitsByRegexList( String host, String username, String password, String directory,
            String filename, List< String > expressions ) throws IOException
    {
        if ( !( isValidInput( host, username, password, directory, filename, expressions ) ) )
        {
            throw new IllegalArgumentException( "Cannot perform FTP search. Make sure all inputs are valid" );
        }

        LOGGER.info( "Expressions to be searched = " + expressions );

        String combainedExpression = null;

        for ( String expression : expressions )
        {
            if ( null == combainedExpression )
            {
                combainedExpression = expression;
            }
            else
            {
                combainedExpression += OR + expression;
            }
        }

        return countHitsByRegex( host, username, password, directory, filename, combainedExpression );
    }

    public static List< String > getLinesByRegex( String host, String username, String password, String directory,
            String filename, String expression ) throws IOException
    {
        if ( !( isValidInput( host, username, password, directory, filename, expression ) ) )
        {
            throw new IllegalArgumentException( "Cannot perform FTP search. Make sure all inputs are valid" );
        }

        List< String > lines = new LinkedList< String >();

        JSch jsch = new JSch();
        Session session = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try
        {
            session = jsch.getSession( username, host );
            session.setConfig( getHostConfiguration() );
            session.setPassword( password );
            session.connect();

            LOGGER.info( "Session starting [" + host + "]" );

            Channel channel = session.openChannel( TYPE_SFTP );
            channel.connect();

            LOGGER.info( "Connected  [" + host + "]" );

            ChannelSftp sftpChannel = ( ChannelSftp ) channel;
            sftpChannel.cd( directory );

            LOGGER.info( "Directory changed  [" + directory + "]" );

            InputStream is = sftpChannel.get( filename );

            LOGGER.info( "Starting read [" + filename + "]" );

            Pattern pattern = Pattern.compile( expression );

            bufferedReader = new BufferedReader( new InputStreamReader( is ) );

            String line = "";
            while ( ( line = bufferedReader.readLine() ) != null )
            {
                if ( pattern.matcher( line ).find() )
                {
                    lines.add( line );
                }
            }
            sftpChannel.exit();
            session.disconnect();
        }
        catch ( Exception e )
        {
            throw new IOException( e.getMessage() );
        }
        finally
        {
            try
            {
                if ( fileReader != null )
                {
                    fileReader.close();
                }
                if ( bufferedReader != null )
                {
                    bufferedReader.close();
                }
            }
            catch ( Exception e )
            {
                throw new IOException( e.getMessage() );
            }
        }

        return lines;
    }

    public static List< String > getLinesByRegexList( String host, String username, String password, String directory,
            String filename, List< String > expressions ) throws IOException
    {
        if ( !( isValidInput( host, username, password, directory, filename, expressions ) ) )
        {
            throw new IllegalArgumentException( "Cannot perform FTP search. Make sure all inputs are valid" );
        }

        LOGGER.info( "Expressions to be searched = " + expressions );

        String combainedExpression = null;

        for ( String expression : expressions )
        {
            if ( null == combainedExpression )
            {
                combainedExpression = expression;
            }
            else
            {
                combainedExpression += OR + expression;
            }
        }

        return getLinesByRegex( host, username, password, directory, filename, combainedExpression );
    }

    private static Properties getHostConfiguration()
    {
        Properties config = new Properties();
        config.put( STRICT_HOST_KEY_CHECKING, NO );

        return config;
    }

    private static boolean isValidInput( String host, String username, String password, String directory,
            String filename, String expression )
    {
        boolean isValid = false;

        if ( ( null != host ) && !( host.isEmpty() ) && ( null != username ) && !( username.isEmpty() )
                && ( null != password ) && !( password.isEmpty() ) && ( null != directory ) && !( directory.isEmpty() )
                && ( null != filename ) && !( filename.isEmpty() ) && ( null != expression )
                && !( expression.isEmpty() ) )
        {
            isValid = true;
        }
        return isValid;
    }

    private static boolean isValidInput( String host, String username, String password, String directory,
            String filename, List< String > expressions )
    {
        boolean isValid = false;

        if ( ( null != host ) && !( host.isEmpty() ) && ( null != username ) && !( username.isEmpty() )
                && ( null != password ) && !( password.isEmpty() ) && ( null != directory ) && !( directory.isEmpty() )
                && ( null != filename ) && !( filename.isEmpty() ) && ( null != expressions )
                && !( expressions.isEmpty() ) )
        {
            isValid = true;
        }
        return isValid;
    }

}
