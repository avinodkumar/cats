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
package com.comcast.cats.application;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * Added to deal {@link ViewExpiredException} gracefully.
 * 
 */
public class FacesViewExceptionHandler extends ExceptionHandlerWrapper
{

    private ExceptionHandler wrapped;

    public FacesViewExceptionHandler( ExceptionHandler wrapped )
    {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped()
    {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException
    {
        Iterable< ExceptionQueuedEvent > events = this.wrapped.getUnhandledExceptionQueuedEvents();
        for ( Iterator< ExceptionQueuedEvent > it = events.iterator(); it.hasNext(); )
        {
            ExceptionQueuedEvent event = it.next();
            ExceptionQueuedEventContext eqec = event.getContext();

            if ( eqec.getException() instanceof ViewExpiredException )
            {
                FacesContext context = eqec.getContext();
                NavigationHandler navHandler = context.getApplication().getNavigationHandler();

                try
                {
                    navHandler.handleNavigation( context, null, "home?faces-redirect=true&expired=true" );
                }
                finally
                {
                    it.remove();
                }
            }
        }

        this.wrapped.handle();
    }
}
