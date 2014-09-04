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
/**
 * 
 */
package com.comcast.cats.imageutility;

/**
 * Custom exception class for handling ImageUtility Invalid Argument Exception.
 * 
 * @author maneshthomas
 * 
 */
public class ImageUtilityException extends Exception
{
    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 1L;
    String                    message          = null;

    /**
     * Creates ImageUtilityInvalidArgumentException with message.
     * 
     * @param message
     */
    public ImageUtilityException( String message )
    {
        super( message );
    }

    /**
     * Creates ImageUtilityInvalidArgumentException with throwable argument.
     * 
     * @param cause
     *            Throwable object.
     */
    public ImageUtilityException( Throwable cause )
    {
        super( cause );
    }

    /**
     * Creates ImageUtilityInvalidArgumentException with Throwable object and
     * message as argument.
     * 
     * @param message
     *            message
     * @param cause
     *            Throwable object.
     */
    public ImageUtilityException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
