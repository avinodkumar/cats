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
package com.comcast.cats;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import com.comcast.cats.domain.configuration.CatsHome;

public class SettopLoggerFileAppender extends FileAppender
{

    /**
     * This customized Log4j appender will separate the log messages based on
     * the Settop MAC id and will write them into separate files. For example,
     * all logs related to settop A will be placed in a file which is under
     * directory A
     * 
     */
    private String dirName  = "";
    private String fileName = "";

    /**
     * Constructor
     * 
     */
    public SettopLoggerFileAppender()
    {
        CatsHome.initializeSystemProperties();
    }

    /**
     * Constructor
     * 
     * @param layout
     *            {@linkplain Layout}
     * @param fileName
     * @param append
     * @param bufferedIO
     * @param bufferSize
     * @throws IOException
     */
    public SettopLoggerFileAppender( Layout layout, String fileName, boolean append, boolean bufferedIO, int bufferSize )
                                                                                                                         throws IOException
    {
        super( layout, fileName, append, bufferedIO, bufferSize );
    }

    /**
     * Constructor
     * 
     * @param layout
     *            {@linkplain Layout}
     * @param fileName
     * @param append
     * @throws IOException
     */
    public SettopLoggerFileAppender( Layout layout, String fileName, boolean append ) throws IOException
    {
        super( layout, fileName, append );
    }

    /**
     * Constructor
     * 
     * @param layout
     *            {@linkplain Layout}
     * @param fileName
     * @throws IOException
     */
    public SettopLoggerFileAppender( Layout layout, String fileName ) throws IOException
    {
        super( layout, fileName );
    }

    /**
     * To find the active options and set to the logger.
     * 
     */
    @Override
    public void activateOptions()
    {
        try
        {
            Hashtable< String, String > mdcTable = MDC.getContext();
            dirName = ( String ) mdcTable.get( "SettopMac" );
            fileName = ( String ) mdcTable.get( "LogFileName" );
            String logFileName = System.getProperty( "cats.home" ) + File.separator + dirName + File.separator
                    + fileName;
            setFile( logFileName, fileAppend, bufferedIO, bufferSize );
        }
        catch ( Exception exc )
        {}
        super.activateOptions();
    }

    /**
     * To a log event to the logger.
     * 
     * @param event
     *            LoggingEvent.
     */
    @Override
    public void append( LoggingEvent event )
    {
        try
        {
            dirName = ( String ) event.getMDC( "SettopMac" );
            fileName = ( String ) event.getMDC( "LogFileName" );
            String logFileName = System.getProperty( "cats.home" ) + File.separator + dirName + File.separator
                    + fileName;
            setFile( logFileName, fileAppend, bufferedIO, bufferSize );
            MDC.remove( "SettopMac" );
            MDC.remove( "LogFileName" );
        }
        catch ( IOException ie )
        {
            errorHandler.error( "Error occured while setting file for the log level " + event.getLevel(), ie,
                    ErrorCode.FILE_OPEN_FAILURE );
        }
        super.append( event );
    }
}
