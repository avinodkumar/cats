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
 * @author subinsugunan
 * 
 */
public class DomainServiceException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = -5485422602691413833L;
    private Integer errorCode;
    /**
     * 
     */
    public DomainServiceException()
    {
    }

    /**
     * @param message
     */
    public DomainServiceException( String message )
    {
        super( message );
    }
    /**
     * @param message
     */
    public DomainServiceException( String message,Integer errorCode )
    {
        super( message );
        this.errorCode=errorCode;
    }

    /**
     * @param cause
     */
    public DomainServiceException( Throwable cause )
    {
        super( cause );
    }

    /**
     * @param message
     * @param cause
     */
    public DomainServiceException( String message, Throwable cause )
    {
        super( message, cause );
    }
    public Integer getErrorCode(){
    	return errorCode;
    }
}
