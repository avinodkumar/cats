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

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collection of utility file search methods.
 * 
 * @author SSugun00c
 * 
 */
public final class FileSearchUtil
{
    private static final Logger LOGGER         = LoggerFactory.getLogger( FileSearchUtil.class );

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String OR             = "|";

    private FileSearchUtil()
    {
    }

    public static Integer countHitsByRegex( String filePath, String expression ) throws IOException
    {
        int hits = 0;

        if ( ( null == expression ) || ( expression.isEmpty() ) || ( null == filePath ) || ( filePath.isEmpty() ) )
        {
            throw new IllegalArgumentException( "Expression/FilePath is NULL of EMPTY !!!" );
        }
        else
        {
            File file = new File( filePath );
            Pattern pattern = Pattern.compile( expression );

            if ( file.exists() )
            {
                LineIterator lineIterator = FileUtils.lineIterator( file, UTF_8_ENCODING );

                LOGGER.info( "Expression under search = " + expression );

                try
                {
                    while ( lineIterator.hasNext() )
                    {
                        String line = lineIterator.nextLine();

                        if ( pattern.matcher( line ).find() )
                        {
                            hits++;
                        }
                    }
                }
                finally
                {
                    LineIterator.closeQuietly( lineIterator );
                }
            }
        }

        return hits;
    }

    public static Integer countHitsByRegexList( String filePath, List< String > expressions ) throws IOException
    {
        if ( ( null == expressions ) || ( expressions.isEmpty() ) || ( null == filePath ) || ( filePath.isEmpty() ) )
        {
            throw new IllegalArgumentException( "Expressions/FilePath is NULL of EMPTY !!!" );
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

        return countHitsByRegex( filePath, combainedExpression );
    }

    public static List< String > getLinesByRegex( String filePath, String expression ) throws IOException
    {
        if ( ( null == expression ) || ( expression.isEmpty() ) || ( null == filePath ) || ( filePath.isEmpty() ) )
        {
            throw new IllegalArgumentException( "Expressions/FilePath is NULL of EMPTY !!!" );
        }

        File file = new File( filePath );
        Pattern pattern = Pattern.compile( expression );

        List< String > lines = new LinkedList< String >();

        if ( file.exists() )
        {
            LineIterator lineIterator = FileUtils.lineIterator( file, UTF_8_ENCODING );
            try
            {
                while ( lineIterator.hasNext() )
                {
                    String line = lineIterator.nextLine();

                    if ( pattern.matcher( line ).find() )
                    {
                        lines.add( line );
                    }
                }
            }
            finally
            {
                LineIterator.closeQuietly( lineIterator );
            }
        }

        return lines;
    }

    public static List< String > getLinesByRegexList( String filePath, List< String > expressions ) throws IOException
    {
        if ( ( null == expressions ) || ( expressions.isEmpty() ) || ( null == filePath ) || ( filePath.isEmpty() ) )
        {
            throw new IllegalArgumentException( "Expressions/FilePath is NULL of EMPTY !!!" );
        }

        String combainedExpression = null;

        for ( String expression : expressions )
        {
            combainedExpression += OR + expression;
        }

        return getLinesByRegex( filePath, combainedExpression );
    }

}
