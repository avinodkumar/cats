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

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.SettopAllocationService;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.settop.SettopCatalog;
import com.comcast.cats.service.settop.SettopServiceCommand;

/**
 * Release settop command.
 * 
 * @author subinsugunan
 * 
 */
public class ReleaseSettopCommand extends SettopServiceBaseCommand implements SettopServiceCommand
{
    /**
     * 
     */
    private static final long serialVersionUID = -5004060369147374221L;

    public ReleaseSettopCommand( SettopToken settopToken )
    {
        super( settopToken );
    }

    @Override
    public SettopServiceReturnMessage execute()
    {
        SettopServiceReturnMessage message = new SettopServiceReturnMessage();
        SettopToken settopToken = this.getSettopToken();
        SettopCatalog settopCatalog = this.getCatalog();
        try
        {
            getAllocationService().release( settopToken.getAllocationId(), settopToken.getAuthToken() );
            settopCatalog.removeSettop( settopToken );
        }
        catch ( Exception e )
        {
            handleException( message, e.getMessage() );
        }

        //No more locked
        message.setLocked( false );
        return message;
    }

    // AllocationService will not be available in Settop class. As a
    // work around , We're using lookup for the time being.
    private SettopAllocationService getAllocationService()
    {
        SettopAllocationService allocationService = null;
        try
        {
            InitialContext ctx = new InitialContext();
            allocationService = ( SettopAllocationService ) ctx
                    .lookup( ConfigServiceConstants.ALLOCATION_SERVICE_MAPPED_NAME );
        }
        catch ( NamingException e )
        {
            throw new RuntimeException( e );
        }
        return allocationService;
    }
}
