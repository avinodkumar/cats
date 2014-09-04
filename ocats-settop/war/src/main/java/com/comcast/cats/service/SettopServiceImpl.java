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
package com.comcast.cats.service;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebService;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.SettopConstants;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.service.settop.SettopCatalog;
import com.comcast.cats.service.settop.SettopServiceCommandExecutor;
import com.comcast.cats.service.settop.command.GetPowerStateCommand;
import com.comcast.cats.service.settop.command.HardPowerOffCommand;
import com.comcast.cats.service.settop.command.HardPowerOnCommand;
import com.comcast.cats.service.settop.command.HardPowerToggleCommand;
import com.comcast.cats.service.settop.command.PressKeyAndHoldCommand;
import com.comcast.cats.service.settop.command.PressKeyCommand;
import com.comcast.cats.service.settop.command.PressKeySequenceCommand;
import com.comcast.cats.service.settop.command.ReleaseSettopCommand;
import com.comcast.cats.service.settop.command.TuneCommand;
import com.comcast.cats.service.settop.command.VerifyCommand;

/**
 * 
 * @author cfrede001
 */
@Remote( SettopService.class )
@WebService( name = SettopConstants.SETTOP_SERVICE_NAME, portName = SettopConstants.SETTOP_SERVICE_PORT_NAME, targetNamespace = SettopConstants.NAMESPACE, endpointInterface = SettopConstants.SETTOP_SERVICE_ENDPOINT_INTERFACE )
@Stateless
public class SettopServiceImpl implements SettopService
{
    @EJB
    SettopCatalog                catalog;

    @EJB
    SettopServiceCommandExecutor executor;

    @Override
    public SettopServiceReturnMessage getPowerState( SettopToken settop )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new GetPowerStateCommand( settop ) );
        return msg;
    }

    @Override
    public SettopToken getSettop( String mac, String userToken ) throws SettopNotFoundException
    {
        SettopToken settopToken = null;

        if ( ( null != mac ) && ( !mac.isEmpty() ) && ( null != userToken ) && ( !userToken.isEmpty() ) )
        {
            try
            {
                return catalog.obtainSettopByMac( mac, userToken );
            }
            catch ( Exception e )
            {
            	e.printStackTrace();
                throw new SettopNotFoundException( e.getMessage() );
            }
        }
        return settopToken;
    }

    @Override
    public SettopDesc getSettopInfo( SettopToken settopToken ) throws SettopNotFoundException
    {
        SettopDesc settopDesc = null;

        if ( ( null != settopToken.getAllocationId() ) && ( !settopToken.getAllocationId().isEmpty() ) )
        {
            try
            {
                settopDesc = ( SettopDesc ) ( ( SettopImpl ) catalog.lookupSettop( settopToken ) ).getSettopInfo();
            }
            catch ( Exception e )
            {
                throw new SettopNotFoundException( e.getMessage() );
            }
        }
        return settopDesc;
    }

    @Override
    public SettopServiceReturnMessage hardPowerOff( SettopToken settop )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new HardPowerOffCommand( settop ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage hardPowerOn( SettopToken settop )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new HardPowerOnCommand( settop ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage hardPowerToggle( SettopToken settop )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new HardPowerToggleCommand( settop ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage isLocked( SettopToken settop )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new VerifyCommand( settop ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage pressKey( SettopToken settop, RemoteCommand command )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new PressKeyCommand( settop, command ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage pressKeyAndHold( SettopToken settop, RemoteCommand command, Integer count )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new PressKeyAndHoldCommand( settop, command, count ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage releaseSettop( SettopToken settop )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new ReleaseSettopCommand( settop ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage streamVideo( SettopToken settop )
    {
        SettopServiceReturnMessage message = new SettopServiceReturnMessage();

        try
        {
            SettopDesc settopDesc = getSettopInfo( settop );
            String videoUrl = settopDesc.getVideoPath().toString();
            String ip = videoUrl.substring( videoUrl.indexOf( "//" ) + 2, videoUrl.lastIndexOf( '/' ) );
            String camera = videoUrl.substring( videoUrl.indexOf( "=" ) + 1, videoUrl.length() );
            String streamUrl = "http://" + ip + "/axis-cgi/mjpg/video.cgi?camera=" + camera;
            message.setUrl( streamUrl );
        }
        catch ( Exception exception )
        {
            message.setResult( WebServiceReturnEnum.FAILURE );
            message.setServiceCode( SettopServiceReturnEnum.SETTOP_SERVICE_FAILURE );
            message.setMessage( exception.getMessage() );
            exception.printStackTrace();
        }
        return message;
    }

    @Override
    public SettopServiceReturnMessage tune( SettopToken settop, String channel, boolean autoTuneEnabled )
    {
        SettopServiceReturnMessage msg = executor.executeCommand( new TuneCommand( settop, channel, autoTuneEnabled ) );
        return msg;
    }

    @Override
    public SettopServiceReturnMessage getLastError( SettopToken settop )
    {
        SettopServiceReturnMessage returnMsg = new SettopServiceReturnMessage();
        try
        {
            String lastError = catalog.getLastError( settop );
            returnMsg.setMessage( lastError );
        }
        catch ( SettopNotFoundException e )
        {
            returnMsg.setServiceCode( SettopServiceReturnEnum.SETTOP_SERVICE_FAILURE );
            returnMsg.setResult( WebServiceReturnEnum.FAILURE );
            returnMsg.setMessage( e.getMessage() );
        }
        return returnMsg;
    }

	@Override
	public SettopServiceReturnMessage pressKeySequence(SettopToken settop,
			String keySeq, String repCount, String sleepTime) {
        SettopServiceReturnMessage msg = executor.executeCommand( new PressKeySequenceCommand( settop, keySeq,repCount, sleepTime ) );
        return msg;
	}
}
