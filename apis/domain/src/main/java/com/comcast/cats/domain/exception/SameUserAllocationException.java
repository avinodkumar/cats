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
package com.comcast.cats.domain.exception;

/**
 * Exception thrown when same user tries for a second allocation
 * @author bobyemmanuvel
 *
 */
public class SameUserAllocationException extends AllocationInstantiationException{

	    private static final long serialVersionUID = 7074381180110789306L;

	    public SameUserAllocationException()
	    {
	        super();
	    }

	    public SameUserAllocationException( String message, Throwable cause )
	    {
	        super( message, cause );
	    }

	    public SameUserAllocationException( String message )
	    {
	        super( message );
	    }

	    public SameUserAllocationException( Throwable cause )
	    {
	        super( cause );
	    }
}
