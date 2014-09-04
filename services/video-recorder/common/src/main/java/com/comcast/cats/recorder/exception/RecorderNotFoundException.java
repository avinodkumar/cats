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
package com.comcast.cats.recorder.exception;

import com.comcast.cats.recorder.domain.Recording;

/**
 * A custom {@link Exception} for video recording. Throws normally when a
 * {@link Recording} is not found in database.
 * 
 * @author SSugun00c
 * 
 */
public class RecorderNotFoundException extends VideoRecorderException
{
    private static final long serialVersionUID = 1L;

    public RecorderNotFoundException()
    {
        // TODO Auto-generated constructor stub
    }

    public RecorderNotFoundException( String message )
    {
        super( message );
        // TODO Auto-generated constructor stub
    }

    public RecorderNotFoundException( Throwable cause )
    {
        super( cause );
        // TODO Auto-generated constructor stub
    }

    public RecorderNotFoundException( String message, Throwable cause )
    {
        super( message, cause );
        // TODO Auto-generated constructor stub
    }
}
