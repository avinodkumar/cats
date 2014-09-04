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
package com.comcast.cats.service.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.info.DiskSpaceUsage;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.info.VideoRecordingOptions;

/**
 * Common utility methods used in video recording.
 * 
 * @author SSugun00c
 * 
 */
public final class VideoRecorderUtil
{
    /**
     * Regular expression for IP address.
     */
    private static final String IP_ADDR_REGEX    = "^(([0-9]|[1-9][0-9]"
                                                         + "|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|"
                                                         + "[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    /**
     * Regular expression for positive and valid timeout.
     */
    private static final String NUMBER_REGEX     = "^[0-9]+$";

    /**
     * Regular expression for STB mac address.
     */
    private static final String MAC_ADDR_REGEX   = "^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$";

    /**
     * Please note ':' is an illegal character in Windows file name.
     */
    private static final String TIME_FORMAT      = "HH-mm-ss-zzz";

    private static final String MEDIA_URL_PREFIX = "rtsp://";
    private static final String MEDIA_URL_SUFFIX = "/axis-media/media.amp?videocodec=h264&camera=";

    private static final Logger LOGGER           = LoggerFactory.getLogger( VideoRecorderUtil.class );

    private VideoRecorderUtil()
    {
        // TODO Auto-generated constructor stub
    }

    public static boolean isValidIp( final String ip )
    {
        boolean retVal = false;
        if ( null != ip && !ip.isEmpty() )
        {
            retVal = Pattern.compile( IP_ADDR_REGEX ).matcher( ip ).matches();
        }
        return retVal;
    }

    public static boolean isValidNumber( final String input )
    {
        boolean retVal = false;
        if ( null != input && !input.isEmpty() )
        {
            retVal = Pattern.compile( NUMBER_REGEX ).matcher( input ).matches();
        }
        return retVal;
    }

    /**
     * check whether valid macId.
     * 
     * @param macId
     *            macID of the STB.
     * @return boolean status
     */
    public static boolean isValidMacId( final String macId )
    {
        boolean retVal = false;
        if ( null != macId && !macId.isEmpty() )
        {
            retVal = Pattern.compile( MAC_ADDR_REGEX ).matcher( macId ).matches();
        }
        return retVal;
    }

    /**
     * "C:" or "c:" or "D:" or "d:" for windows "/volume" for Linux.
     * 
     * @param partition
     * @return
     */
    public static DiskSpaceUsage getDiskSpaceUsage( final File partition )
    {
        DiskSpaceUsage diskSpaceUsage = new DiskSpaceUsage( partition );
        return diskSpaceUsage;
    }

    /**
     * 
     * @return
     */
    public static List< DiskSpaceUsage > getDiskSpaceUsage()
    {
        File[] roots = File.listRoots();

        List< DiskSpaceUsage > diskSpaceUsageList = new ArrayList< DiskSpaceUsage >();

        for ( File file : roots )
        {
            diskSpaceUsageList.add( new DiskSpaceUsage( file ) );
        }

        return diskSpaceUsageList;
    }

    public static String getMrl( String macId, String videoServerIp, Integer port )
    {
        String mediaUrl = MEDIA_URL_PREFIX + videoServerIp + MEDIA_URL_SUFFIX + port;
        LOGGER.info( "Media Resource Locator for [" + macId + "][" + videoServerIp + "][" + port + "] is [" + mediaUrl
                + "]." );
        return mediaUrl;
    }

    private static synchronized File getTargetFile( String macId, String videoServerIp, Integer port, String alias,
            int fileCount )
    {
        File targetFile = null;

        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime( now );

        File baseDirectory = null;

        if ( null != macId )
        {
            alias = validateAndFixAlias( alias );
            macId = macId.replaceAll( ":", VideoRecorderServiceConstants.DEFAULT_FILE_NAME_SEPERATOR );

            baseDirectory = new File(
                    System.getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH ),
                    ( calendar.get( Calendar.YEAR ) + System.getProperty( "file.separator" )
                            + ( calendar.get( Calendar.MONTH ) + 1 ) + System.getProperty( "file.separator" )
                            + calendar.get( Calendar.DAY_OF_MONTH ) + System.getProperty( "file.separator" ) + macId ) );
            baseDirectory.mkdirs();

            // If alias is not there, fileCount is also ignored
            if ( ( null == alias ) || ( alias.isEmpty() ) )
            {
                targetFile = new File( baseDirectory.getAbsolutePath() + System.getProperty( "file.separator" )
                        + new SimpleDateFormat( TIME_FORMAT ).format( now )
                        + VideoRecordingOptions.DEFAULT_FILE_EXTENSION );
            }
            else
            {
                targetFile = new File( baseDirectory.getAbsolutePath() + System.getProperty( "file.separator" ) + alias
                        + VideoRecorderServiceConstants.DEFAULT_FILE_NAME_SEPERATOR
                        + new SimpleDateFormat( TIME_FORMAT ).format( now )
                        + VideoRecorderServiceConstants.DEFAULT_FILE_NAME_SEPERATOR + roundOffFileCount( fileCount )
                        + VideoRecordingOptions.DEFAULT_FILE_EXTENSION );
            }
        }

        if ( ( null != targetFile ) && ( targetFile.isFile() ) && ( targetFile.exists() ) )
        {
            LOGGER.info( "targetFile created [" + targetFile + "]" );
        }

        return targetFile;
    }

    public static synchronized File getSubsequentFile( String initialFilePath, int fileCount )
    {
        String subsequentFilePath = null;

        String folderPath = VideoRecorderUtil.getFolderPathFromFilePath( initialFilePath );
        String initialFileName = VideoRecorderUtil.getFilePathFromFilePath( initialFilePath );

        String newFileName = initialFileName.substring( 0, initialFileName.lastIndexOf( "-" ) + 1 );
        newFileName = newFileName + roundOffFileCount( fileCount ) + VideoRecordingOptions.DEFAULT_FILE_EXTENSION;

        subsequentFilePath = folderPath + System.getProperty( "file.separator" ) + newFileName;

        File targetFile = new File( subsequentFilePath );

        return targetFile;
    }

    private static String roundOffFileCount( int fileCount )
    {
        String fileCountStr = String.valueOf( fileCount );

        if ( fileCountStr.length() == 1 )
        {
            fileCountStr = "0" + fileCountStr;
        }

        return fileCountStr;
    }

    public static synchronized String getHttpPath( String absoluteFilePath )
    {
        String fileBasePath = System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_FILE_SERVER_BASE_PATH );
        String httpPath = System.getProperty(
                VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_HTTP_SERVER_BASE_PATH ).trim()
                + absoluteFilePath.substring( absoluteFilePath.indexOf( fileBasePath ) + fileBasePath.length(),
                        absoluteFilePath.length() );

        httpPath = httpPath.replace( '\\', '/' );

        LOGGER.trace( "HttpPath from  [" + absoluteFilePath + "] is [" + httpPath + "]" );

        return httpPath;
    }

    public static synchronized String getFilePath( String macId, String videoServerIp, Integer port, String alias,
            int fileCount )
    {
        String absoluteFilePath = getTargetFile( macId, videoServerIp, port, alias, fileCount ).getAbsolutePath()
                .trim();

        LOGGER.info( "filePath for [" + macId + "][" + videoServerIp + "][" + port + "] is [" + absoluteFilePath + "]" );

        return absoluteFilePath;
    }

    /**
     * Truncate hour to the next hour.
     * 
     * @param now
     * @return
     */
    public static Date getScheduledStartTime( Date now )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( DateUtils.truncate( now, Calendar.HOUR_OF_DAY ) );
        calendar.add( Calendar.HOUR_OF_DAY, 1 );
        Date scheduledStartTime = calendar.getTime();

        LOGGER.info( "Calculated scheduledStartTime [" + scheduledStartTime + "]" );

        return scheduledStartTime;
    }

    /**
     * Return file size in bytes.
     * 
     * @param filePath
     * @return
     */
    public static synchronized double getFileSize( String filePath )
    {
        double sizeInBytes = 0L;

        File file = new File( filePath );

        if ( !file.exists() || !file.isFile() )
        {
            sizeInBytes = -1;
        }
        else
        {
            sizeInBytes = file.length();
        }

        return sizeInBytes;
    }

    /**
     * Return folder size in bytes.
     * 
     * @param folder
     * @return
     */
    public static double getFolderSize( File folder )
    {
        double foldersize = 0;

        File[] filelist = folder.listFiles();

        if ( null != filelist )
        {
            for ( int i = 0; i < filelist.length; i++ )
            {
                if ( filelist[ i ].isDirectory() )
                {
                    foldersize += getFolderSize( filelist[ i ] );
                }
                else
                {
                    foldersize += filelist[ i ].length();
                }
            }
        }
        return foldersize;
    }

    /**
     * Checks if the file can be opened in s write mode.
     * 
     * Tested on RHEL 6 and Windows.
     * 
     * @param filePath
     * @return
     */
    public static boolean isPlayable( String filePath )
    {
        boolean isPlayable = false;

        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( ( os.indexOf( "nix" ) >= 0 ) || ( os.indexOf( "nux" ) >= 0 ) || ( os.indexOf( "aix" ) > 0 ) )
        {
            isPlayable = isPlayableInLinux( filePath );
        }
        else
        // ( ( os.indexOf( "win" ) >= 0 ) || ( os.indexOf( "mac" ) >= 0 ) || (
        // os.indexOf( "sunos" ) >= 0 ) )
        {
            try
            {
                File file = new File( filePath );

                if ( file.isFile() )
                {
                    // Will always return true in Linux as Linux allows multiple
                    // process to access file at the same time.
                    FileUtils.touch( file );
                    isPlayable = true;
                }
            }
            catch ( IOException e )
            {
                isPlayable = false;
            }
        }

        return isPlayable;
    }

    private static boolean isPlayableInLinux( String filePath )
    {
        boolean playableInLinux = false;

        /*
         * InputStream inputStream = null; InputStreamReader inputStreamReader =
         * null; BufferedReader bufferedReader = null;
         * 
         * try { // lsof (list open files) utility String command = "lsof " +
         * filePath;
         * 
         * LOGGER.trace( "[LSOF] [" + command + "]" );
         * 
         * Process process = Runtime.getRuntime().exec( command );
         * 
         * inputStream = process.getInputStream();
         * 
         * StringBuilder result = new StringBuilder();
         * 
         * inputStreamReader = new InputStreamReader( inputStream,
         * VideoRecorderServiceConstants.UTF ); bufferedReader = new
         * BufferedReader( inputStreamReader );
         * 
         * String data = null;
         * 
         * while ( ( data = bufferedReader.readLine() ) != null ) {
         * result.append( data ); }
         * 
         * if ( result.toString().isEmpty() ) { playableInLinux = true; } else {
         * LOGGER.trace( "[FILE IN USE][" + filePath + "] by [" +
         * result.toString() + "]" ); playableInLinux = false; }
         * 
         * } catch ( IOException e ) { playableInLinux = false; }
         * 
         * cleanUp( bufferedReader, inputStreamReader, inputStream );
         */

        return playableInLinux;
    }

    /*
     * private static void cleanUp( BufferedReader bufferedReader,
     * InputStreamReader inputStreamReader, InputStream inputStream ) { if (
     * bufferedReader != null ) { try { bufferedReader.close(); } catch (
     * IOException e ) { LOGGER.error(
     * "Exception caught trying to close bufferedReader: " + e.getMessage() ); }
     * } if ( inputStreamReader != null ) { try { inputStreamReader.close(); }
     * catch ( IOException e ) { LOGGER.error(
     * "Exception caught trying to close inputStreamReader: " + e.getMessage()
     * ); } } if ( inputStream != null ) { try { inputStream.close(); } catch (
     * IOException e ) { LOGGER.error(
     * "Exception caught trying to close inputStream: " + e.getMessage() ); } }
     * }
     */

    public static boolean isExists( String filePath )
    {
        boolean exists = false;

        File file = new File( filePath );

        if ( file.exists() )
        {
            exists = true;
        }

        return exists;
    }

    public static String validateAndFixAlias( String alias )
    {
        if ( ( null != alias ) && !( alias.isEmpty() ) )
        {
            alias = alias.trim();
            alias = alias.replaceAll( "[^\\dA-Za-z ]", VideoRecorderServiceConstants.DEFAULT_FILE_NAME_SEPERATOR )
                    .replaceAll( "\\s+", VideoRecorderServiceConstants.DEFAULT_FILE_NAME_SEPERATOR );
        }
        return alias;
    }

    public static String getFolderPathFromFilePath( String filePath )
    {
        if ( ( null != filePath ) && !( filePath.isEmpty() ) )
        {
            filePath = filePath.trim();
            filePath = filePath.substring( 0, filePath.lastIndexOf( System.getProperty( "file.separator" ) ) );
        }

        return filePath;
    }

    public static String getFilePathFromFilePath( String filePath )
    {
        String fileName = null;

        if ( ( null != filePath ) && !( filePath.isEmpty() ) )
        {
            filePath = filePath.trim();
            fileName = filePath.substring( filePath.lastIndexOf( System.getProperty( "file.separator" ) ) + 1,
                    filePath.length() );
        }

        return fileName;
    }

    public static String listVlcProcess()
    {
        String command = null;

        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( os.indexOf( "win" ) >= 0 )
        {
            command = "TASKLIST /FI \"IMAGENAME eq vlc.exe\"";
        }
        else
        {
            command = "ps -ef | grep vlc | grep -v grep | awk '{print $2}'";
        }

        // FIXME
        return command;// execcuteCommand( command );
    }

    // FIXME
    /*
     * public static String execcuteCommand( String command ) { StringBuilder
     * result = new StringBuilder();
     * 
     * InputStream inputStream = null; InputStreamReader inputStreamReader =
     * null; BufferedReader bufferedReader = null;
     * 
     * try { LOGGER.trace( "[COMMAND] [" + command + "]" );
     * 
     * Process process = Runtime.getRuntime().exec( command );
     * 
     * inputStream = process.getInputStream();
     * 
     * inputStreamReader = new InputStreamReader( inputStream,
     * VideoRecorderServiceConstants.UTF ); bufferedReader = new BufferedReader(
     * inputStreamReader );
     * 
     * String data = null;
     * 
     * while ( ( data = bufferedReader.readLine() ) != null ) { result.append(
     * data ); result.append( "\n" ); }
     * 
     * } catch ( IOException e ) { LOGGER.trace( "[COMMAND] IOException [" +
     * e.getMessage() + "]" ); }
     * 
     * cleanUp( bufferedReader, inputStreamReader, inputStream );
     * 
     * return result.toString(); }
     */
}
