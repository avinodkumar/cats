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
package com.comcast.cats.service.settop;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.info.ConfigServiceConstants;
import com.comcast.cats.service.SettopAllocationService;
import com.comcast.cats.service.SettopServiceReturnEnum;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * 
 * @author cfrede001
 * 
 */
@Remote( SettopServiceCommandExecutor.class )
@Stateless( mappedName = "cats/services/SettopServiceCommandExecutor" )
@TransactionAttribute( TransactionAttributeType.SUPPORTS )
public class SettopServiceCommandExecutorImpl implements SettopServiceCommandExecutor
{
    @EJB( mappedName = ConfigServiceConstants.ALLOCATION_SERVICE_MAPPED_NAME )
    SettopAllocationService allocationService;

    @EJB( mappedName = "cats/services/SettopCatalog" )
    SettopCatalog           catalog;

    @Override
    public SettopServiceReturnMessage executeCommand( SettopServiceCommand command )
    {
        SettopServiceReturnMessage msg = null;
        SettopToken settopToken = command.getSettopToken();

        try
        {
            if ( isValidAllocation( settopToken ) )
            {
                msg = command.execute();
                msg.setLocked( true );
            }
        }
        catch ( Exception e )
        {
            msg = new SettopServiceReturnMessage( SettopServiceReturnEnum.SETTOP_SERVICE_FAILURE );
            msg.setResult( WebServiceReturnEnum.FAILURE );
            msg.setMessage( e.getMessage() );
            catalog.putSettopError( settopToken.getAuthToken(), e.getMessage() );
        }
        return msg;
    }

    /**
     * Input validation. Check Allocation and Authentication UUID are
     * available..
     * 
     * @param settop
     * @return
     * @throws AllocationException
     */
    private boolean isValidAllocation( SettopToken settopToken ) throws AllocationException
    {
        boolean isValid = false;

        if ( ( null != settopToken ) && ( null != settopToken.getAuthToken() )
                && ( null != settopToken.getAllocationId() ) && ( !( settopToken.getAuthToken().isEmpty() ) )
                && ( !( settopToken.getAllocationId().isEmpty() ) )
                && ( allocationService.verify( settopToken.getAllocationId(), settopToken.getAuthToken() ) ) )
        {
            isValid = true;
        }

        return isValid;
    }
}
