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

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.settop.SettopServiceCommand;

/**
 * Press key command
 * 
 * @author cfrede001
 */
public class PressKeyCommand extends SettopServiceBaseCommand implements SettopServiceCommand
{
    /**
     * 
     */
    private static final long serialVersionUID = 652238202283397718L;

    RemoteCommand             command;

    public PressKeyCommand( SettopToken settopToken, RemoteCommand c )
    {
        super( settopToken );
        this.command = c;
    }

    @Override
    public SettopServiceReturnMessage execute()
    {
        SettopServiceReturnMessage message = new SettopServiceReturnMessage();
        Settop settop = null;

        try
        {
            settop = this.getSettop();

            if ( !( settop.getRemote().pressKey( command ) ) )
            {
                throw new SettopNotFoundException( "Failed to send command to the to IR device)" );
            }
        }
        catch ( SettopNotFoundException snf )
        {
            handleException( message, snf.getMessage() );
        }

        return message;
    }
}