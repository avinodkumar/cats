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
package com.comcast.cats.service.impl;

import java.net.URI;
import java.util.List;
import javax.jws.WebService;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.service.IRManager;
import com.comcast.cats.service.IRService;
import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.IRServiceProvider;
import com.comcast.cats.service.KeyManager;
import com.comcast.cats.service.IRServiceVersionGetter;

/**
 * A simple EJB service that can send a IR command to a STB based on the path to
 * the IR device. This class is defined as a singleton, since only one is needed
 * in the EJB context. However, the implementation need not implement a
 * singleton.
 */

@Remote( IRService.class )
@Stateless
@TransactionAttribute( TransactionAttributeType.NOT_SUPPORTED )
@WebService( name = IRServiceConstants.SERVICE_NAME, endpointInterface = IRServiceConstants.ENDPOINT_INTERFACE, targetNamespace = IRServiceConstants.NAMESPACE )
public class IRServiceWSImpl implements IRService
{

    @EJB
    IRServiceProvider           irServiceProvider;

    @EJB
    private KeyManager          keyManager;

    @EJB( name = "IRServiceVersionGetter" )
    IRServiceVersionGetter      versionGetter;

    private static final Logger logger = LoggerFactory.getLogger( IRServiceWSImpl.class );

    public IRServiceWSImpl()
    {
    }

    public IRServiceWSImpl( IRManager irManager, KeyManager keyManager )
    {
        this.keyManager = keyManager;
    }

    protected IRService getIRService( URI path )
    {
        return irServiceProvider.getIRService( path );
    }

    public boolean pressKey( final URI path, final String irKeySet, final RemoteCommand command )
    {
        logger.trace( "IRServiceWSImpl : pressKey path " + path + " irKeySet " + irKeySet + " command " + command );
        boolean response = false;
        IRService irService = getIRService( path );
        logger.debug( "IRServiceWSImpl : pressKey irService " + irService );
        if ( irService != null )
        {
            response = irService.pressKey( path, irKeySet, command );
        }
        return response;
    }

    public boolean pressKeyAndHold( final URI path, final String irKeySet, final RemoteCommand command,
            final Integer count )
    {
        logger.trace( "IRServiceWSImpl : pressKeyAndHold path " + path + " irKeySet " + irKeySet + " command "
                + command + " count " + count );
        boolean response = false;
        IRService irService = getIRService( path );
        logger.debug( "IRServiceWSImpl : pressKeyAndHold irService " + irService );
        if ( irService != null )
        {
            response = irService.pressKeyAndHold( path, irKeySet, command, count );
        }
        return response;
    }

    public boolean pressKeys( URI path, String irKeySet, List< RemoteCommand > commands, int delayMillis )
    {
        logger.trace( "IRServiceWSImpl : pressKeys path " + path + " irKeySet " + irKeySet + " commands " + commands
                + " delayMillis " + delayMillis );
        boolean response = false;
        IRService irService = getIRService( path );
        logger.debug( "IRServiceWSImpl : pressKeys irService " + irService );
        if ( irService != null )
        {
            response = irService.pressKeys( path, irKeySet, commands, delayMillis );
        }
        return response;
    }

    public boolean tune( URI path, String irKeySet, String channel, boolean autoTuneEnabled, int delayMillis )
    {
        logger.trace( "IRServiceWSImpl : tune path " + path + " irKeySet " + irKeySet + " channel " + channel
                + " delayMillis " + delayMillis + " autoTuneEnabled " + autoTuneEnabled );
        boolean response = false;
        IRService irService = getIRService( path );
        logger.debug( "IRServiceWSImpl : tune irService " + irService );
        if ( irService != null )
        {
            response = irService.tune( path, irKeySet, channel, autoTuneEnabled, delayMillis );
        }
        return response;
    }

    /**
     * Send Raw IR code based on path
     */
    public boolean sendIR( URI path, String irCode )
    {
        logger.trace( "IRServiceWSImpl : sendIR path " + path + " irCode " + irCode );
        IRService irService = getIRService( path );
        boolean response = false;
        try
        {
            logger.debug( "IRServiceWSImpl : sendIR irService " + irService );
            if ( irService != null )
            {
                response = irService.sendIR( path, irCode );
            }
        }
        catch ( UnsupportedOperationException e )
        {
            logger.warn( "IR Service " + irService + " does not support sendIR operation" );
        }
        return response;
    }

    @Override
    public boolean enterCustomKeySequence( final URI path, String irKeySet, List< RemoteCommand > commands,
            List< Integer > repeatCount, final List< Integer > delay )
    {

        logger.trace( "IRServiceWSImpl : enterCustomKeySequence path " + path + " irKeySet " + irKeySet + " commands "
                + commands + " repeatCount " + repeatCount + " delay " + delay );
        boolean response = false;
        IRService irService = getIRService( path );
        logger.debug( "IRServiceWSImpl : enterCustomKeySequence irService " + irService );
        if ( irService != null )
        {
            response = irService.enterCustomKeySequence( path, irKeySet, commands, repeatCount, delay );
        }
        return response;
    }

    @Override
    public boolean sendText( URI path, String irKeySet, String stringToBeEntered )
    {
        logger.trace( "IRServiceWSImpl : sendText path " + path + " irKeySet " + irKeySet + " stringToBeEntered "
                + stringToBeEntered );
        boolean response = false;
        IRService irService = getIRService( path );
        logger.debug( "IRServiceWSImpl : sendText irService " + irService );
        if ( irService != null )
        {
            response = irService.sendText( path, irKeySet, stringToBeEntered );
        }
        return response;
    }

    @Override
    public boolean enterRemoteCommandSequence( URI path, String irKeySet, List< RemoteCommandSequence > commands )
    {
        logger.trace( "IRServiceWSImpl : enterRemoteCommandSequence path " + path + " irKeySet " + irKeySet
                + " commands " + commands );
        boolean response = false;
        IRService irService = getIRService( path );
        logger.debug( "IRServiceWSImpl : enterRemoteCommandSequence irService " + irService );
        if ( irService != null )
        {
            response = irService.enterRemoteCommandSequence( path, irKeySet, commands );
        }
        return response;
    }

    @Override
    public List< com.comcast.cats.keymanager.domain.Remote > getRemotes()
    {
        return keyManager.getRemotes();
    }

    @Override
    public String getVersion()
    {
        return versionGetter.getVersion();
    }

    @Override
    public List< RemoteLayout > getRemoteCommands( String irKeySet )
    {
        return null;
    }
	
	 @Override
	 public List<RemoteLayout> getRemoteLayout(String remoteType) {
		logger.debug("getRemoteLayout called for remoteType: {}",remoteType);
		List<RemoteLayout> remoteLayoutList=null;
		remoteLayoutList=keyManager.getRemoteLayout(remoteType);
		if(logger.isDebugEnabled()){
			for(RemoteLayout remoteLaoyout:remoteLayoutList)
				logger.debug("Got Layout {}",remoteLaoyout);
		}
		return remoteLayoutList;
	}
}
