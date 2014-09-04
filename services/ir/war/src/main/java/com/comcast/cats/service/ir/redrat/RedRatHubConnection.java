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
package com.comcast.cats.service.ir.redrat;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.telnet.TelnetConnection;

public class RedRatHubConnection extends TelnetConnection
{
    HubConnectionPool pool;
    
    private static final Logger                    logger                 = LoggerFactory.getLogger( RedRatHubConnection.class );
   
    public RedRatHubConnection( String redratHubHost, int redratHubPort, String redratPromptString, HubConnectionPool hubConnectionPool )
    {
       super(redratHubHost,redratHubPort,redratPromptString);
       this.pool = hubConnectionPool;
    }
   
    /**
     * Wrapper to TelnetConnection to ensure that connection is released back to pool
     * after use.
     */
    @Override
    public synchronized String sendCommand( String command, String prompt ) throws IOException{
        logger.info("RedRatHubConnection sendingCommand "+command);
        String response = super.sendCommand( command,prompt );
        releaseConnection();
        return response;
    }
    
    public void releaseConnection(){
        pool.releaseConnection( this );
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        logger.info("hubConnection dying ");
        super.finalize();
    }
}
