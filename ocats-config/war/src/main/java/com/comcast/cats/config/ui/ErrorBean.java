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
package com.comcast.cats.config.ui;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ErrorBean that can be queried for the stacktrace of the last occurred
 * exception.
 * 
 * @author skurup00c
 * 
 */
@ManagedBean
@RequestScoped
public class ErrorBean
{
    private static Logger      logger        = LoggerFactory.getLogger( ErrorBean.class );
    public final static String EXCEPTION_KEY = "exceptionMessage";

    public String getStackTrace()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        Map< String, Object > map = context.getExternalContext().getRequestMap();
        Throwable throwable = ( Throwable ) map.remove( EXCEPTION_KEY );
        StringBuilder builder = new StringBuilder();
        if ( throwable == null ) // check in session
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "checking for exceptions in session " );
            }
            if ( context.getExternalContext().getSessionMap().containsKey( "javax.servlet.error.exception" ) )
            {
                throwable = ( Throwable ) context.getExternalContext().getSessionMap().remove( EXCEPTION_KEY );
            }
        }
        if ( throwable != null )
        {
            builder.append( "Exception : " + throwable.getMessage() ).append( "\n" ).append( "\n" );
            for ( StackTraceElement element : throwable.getStackTrace() )
            {
                builder.append( "\t" ).append( element ).append( "\n" );
            }

            Throwable rootCause = throwable.getCause();
            if ( rootCause != null )
            {
                builder.append( "\nRoot Cause : " + rootCause.getMessage() ).append( "\n" );
                for ( StackTraceElement element : rootCause.getStackTrace() )
                {
                    builder.append( "\t" ).append( element ).append( "\n" );
                }
            }
        }
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "ExceptionStackTrace " + builder );
        }
        return builder.toString();
    }
}
