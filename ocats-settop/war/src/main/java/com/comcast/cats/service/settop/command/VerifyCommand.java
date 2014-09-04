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

import com.comcast.cats.service.SettopServiceReturnMessage;
import com.comcast.cats.service.SettopToken;
import com.comcast.cats.service.settop.SettopServiceCommand;

/**
 * Verify command.
 * 
 * @author subinsugunan
 * 
 */
public class VerifyCommand extends SettopServiceBaseCommand implements SettopServiceCommand
{

    /**
     * 
     */
    private static final long serialVersionUID = -894382650536696366L;

    public VerifyCommand( SettopToken settopToken )
    {
        super( settopToken );
    }

    public VerifyCommand()
    {
        super();
    }

    @Override
    public SettopServiceReturnMessage execute()
    {
        // No need to do anything since the verification is already done inside
        // SettopServiceCommandExecutorImpl
        return new SettopServiceReturnMessage();
    }
}
