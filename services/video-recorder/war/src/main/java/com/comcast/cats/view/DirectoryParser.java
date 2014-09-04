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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.service.util.VideoRecorderUtil;

/**
 * Utility class to parse video storage directory.
 * 
 * @author ssugun00c
 * @see DocumentBean
 * @see DocumentType
 */
public class DirectoryParser
{
    public static List< DocumentBean > parse( String baseDirectory, String host )
    {
        List< String > yearNames = getDirList( baseDirectory, 4 );

        List< DocumentBean > years = new ArrayList< DocumentBean >();

        for ( String yearName : yearNames )
        {
            DocumentBean year = new DocumentBean( yearName, ( int ) ( getFolderSizeInMB( baseDirectory
                    + System.getProperty( "file.separator" ) + yearName ) ) );

            List< String > monthNames = getDirList( baseDirectory + System.getProperty( "file.separator" ) + yearName,
                    2 );
            List< DocumentBean > months = new ArrayList< DocumentBean >();

            for ( String monthName : monthNames )
            {
                DocumentBean month = new DocumentBean( monthName, ( int ) ( getFolderSizeInMB( baseDirectory
                        + System.getProperty( "file.separator" ) + yearName + System.getProperty( "file.separator" )
                        + monthName ) ) );

                List< String > dayNames = getDirList( baseDirectory + System.getProperty( "file.separator" ) + yearName
                        + System.getProperty( "file.separator" ) + monthName, 2 );
                List< DocumentBean > days = new ArrayList< DocumentBean >();

                for ( String dayName : dayNames )
                {
                    DocumentBean day = new DocumentBean( dayName, ( int ) ( getFolderSizeInMB( baseDirectory
                            + System.getProperty( "file.separator" ) + yearName + System.getProperty( "file.separator" )
                            + monthName + System.getProperty( "file.separator" ) + dayName ) ) );

                    List< String > macNames = getDirList(
                            baseDirectory + System.getProperty( "file.separator" ) + yearName
                                    + System.getProperty( "file.separator" ) + monthName
                                    + System.getProperty( "file.separator" ) + dayName, 17 );

                    List< DocumentBean > macs = new ArrayList< DocumentBean >();

                    for ( String macName : macNames )
                    {
                        DocumentBean mac = new DocumentBean( macName, ( int ) ( getFolderSizeInMB( baseDirectory
                                + System.getProperty( "file.separator" ) + yearName
                                + System.getProperty( "file.separator" ) + monthName
                                + System.getProperty( "file.separator" ) + dayName
                                + System.getProperty( "file.separator" ) + macName ) ) );

                        List< DocumentBean > files = getFileList(
                                baseDirectory + System.getProperty( "file.separator" ) + yearName
                                        + System.getProperty( "file.separator" ) + monthName
                                        + System.getProperty( "file.separator" ) + dayName
                                        + System.getProperty( "file.separator" ) + macName, host );
                        mac.setChilds( files );

                        macs.add( mac );
                    }
                    day.setChilds( macs );

                    days.add( day );
                }
                month.setChilds( days );

                months.add( month );
            }
            year.setChilds( months );

            years.add( year );
        }

        return years;
    }

    private static List< DocumentBean > getFileList( String baseDirectory, String host )
    {
        List< DocumentBean > files = new ArrayList< DocumentBean >();

        File dir = new File( baseDirectory );

        String[] chld = dir.list();

        if ( dir.isDirectory() )
        {
            for ( int i = 0; i < chld.length; i++ )
            {
                File child = new File( baseDirectory + System.getProperty( "file.separator" ) + chld[ i ] );

                if ( child.isFile() )
                {
                    DocumentBean fileBean = new DocumentBean( chld[ i ] );
                    fileBean.setAbsolutePath( child.getAbsolutePath() );

                    String httpPath = getHttpPathFromAbsolutePath( child.getAbsolutePath() );

                    httpPath = httpPath.replace( VideoRecorderServiceConstants.LOCALHOST_IP, host );
                    httpPath = httpPath.replace( VideoRecorderServiceConstants.LOCALHOST_NAME, host );

                    fileBean.setHttpPath( httpPath );
                    fileBean.setSize( ( int ) getFileSizeInMB( child.getAbsolutePath() ) );
                    fileBean.setType( DocumentType.FILE );
                    fileBean.setPlayable( VideoRecorderUtil.isPlayable( child.getAbsolutePath() ) );
                    files.add( fileBean );
                }
            }
        }

        return files;
    }

    private static double getFileSizeInMB( String absolutePath )
    {
        return ( ( VideoRecorderUtil.getFileSize( absolutePath ) ) / 1024 ) / 1024;
    }

    private static double getFolderSizeInMB( String absolutePath )
    {
        return ( ( VideoRecorderUtil.getFolderSize( new File( absolutePath ) ) ) / 1024 ) / 1024;
    }

    private static String getHttpPathFromAbsolutePath( String absolutePath )
    {
        return VideoRecorderUtil.getHttpPath( absolutePath );
    }

    private static List< String > getDirList( String baseDirectory, int lenghtLimit )
    {
        List< String > directoryNames = new ArrayList< String >();

        File dir = new File( baseDirectory );

        String[] chld = dir.list();

        if ( dir.isDirectory() )
        {

            for ( int i = 0; i < chld.length; i++ )
            {
                if ( chld[ i ].length() <= lenghtLimit )
                {
                    directoryNames.add( chld[ i ] );
                }
            }
        }

        return directoryNames;
    }

}
