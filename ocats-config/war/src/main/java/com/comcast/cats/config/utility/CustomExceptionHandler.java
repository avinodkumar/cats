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
package com.comcast.cats.config.utility;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.inject.Inject;

import com.comcast.cats.config.ui.AuthController;

public class CustomExceptionHandler extends ExceptionHandlerWrapper
{
    private ExceptionHandler wrapped;
    
    @Inject
    AuthController authController;

    CustomExceptionHandler( ExceptionHandler exception )
    {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped()
    {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException
    {

        Iterator< ExceptionQueuedEvent > i = getUnhandledExceptionQueuedEvents().iterator();
        while ( i.hasNext() )
        {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = ( ExceptionQueuedEventContext ) event.getSource();
            Throwable throwable = context.getException();
            final FacesContext fc = FacesContext.getCurrentInstance();
            final Map< String, Object > requestMap = fc.getExternalContext().getRequestMap();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();
            try
            {
                requestMap.put( "exceptionMessage", throwable );
                if ( throwable instanceof ViewExpiredException )
                {
                    if(authController != null){
                        authController.logout();
                    }else{
                        nav.handleNavigation( fc, null, "/errors/session_expired.xhtml" );
                    }
                }
                else
                {
                    nav.handleNavigation( fc, null, "/errors/error.xhtml" );
                }
                fc.renderResponse();
            }
            finally
            {
                i.remove();
            }
        }
        getWrapped().handle();
    }
}
