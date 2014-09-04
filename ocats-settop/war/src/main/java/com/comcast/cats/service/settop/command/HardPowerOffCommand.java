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

import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.settop.SettopServiceCommand;

/**
 * Hard power off command.
 * 
 * @author subinsugunan
 * 
 */
public class HardPowerOffCommand extends SettopServiceBaseCommand implements SettopServiceCommand
{

    /**
     * 
     */
    private static final long serialVersionUID = 4373273840441236778L;

    public HardPowerOffCommand( SettopToken settopToken )
    {
        super( settopToken );
    }

    @Override
    public SettopServiceReturnMessage execute()
    {
        SettopServiceReturnMessage message = new SettopServiceReturnMessage();
        Settop settop = null;

        try
        {
            settop = this.getSettop();
            settop.getPower().powerOff();
        }
        catch ( SettopNotFoundException snf )
        {
            handleException( message, snf.getMessage() );
        }catch(PowerProviderException ppe){
        	 handleException( message, ppe.getMessage() );
        }

        return message;
    }
}
