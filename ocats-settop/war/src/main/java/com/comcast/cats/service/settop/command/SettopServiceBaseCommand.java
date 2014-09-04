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
package com.comcast.cats.service.settop.command;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.service.SettopServiceReturnEnum;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.settop.SettopCatalog;

/**
 * Settop service base command.
 * 
 * @author cfrede001
 * 
 */
public abstract class SettopServiceBaseCommand implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1983967007670229158L;

    protected final Log       LOGGER           = LogFactory.getLog( getClass() );

    private SettopToken       settopToken;
    private SettopCatalog     catalog;

    public SettopServiceBaseCommand()
    {
    }

    // JBoss 6 has issues with EJB injection across modules of same EAR. As a
    // work around , We're using lookup for the time being.
    public void lookupSettopCatalog()
    {
        try
        {
            InitialContext ctx = new InitialContext();
            catalog = ( SettopCatalog ) ctx.lookup( "cats/services/SettopCatalog" );
        }
        catch ( NamingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public SettopServiceBaseCommand( SettopToken settopToken )
    {
        this.settopToken = settopToken;
    }

    public SettopToken getSettopToken()
    {
        return settopToken;
    }

    public void setSettopToken( SettopToken settopToken )
    {
        this.settopToken = settopToken;
    }

    public Settop getSettop() throws SettopNotFoundException
    {
        if ( null == catalog )
        {
            lookupSettopCatalog();
        }
        return catalog.lookupSettop( settopToken );
    }

    public SettopCatalog getCatalog()
    {
        if ( null == catalog )
        {
            lookupSettopCatalog();
        }
        return catalog;
    }

    protected void handleException( SettopServiceReturnMessage message, String errorMessage )
    {
        message.setResult( WebServiceReturnEnum.FAILURE );
        message.setServiceCode( SettopServiceReturnEnum.SETTOP_SERVICE_FAILURE );
        message.setMessage( errorMessage );
        LOGGER.error( errorMessage );
    }
}
