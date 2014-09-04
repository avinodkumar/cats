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

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.ir.commands.CatsCommand;
import com.comcast.cats.ir.commands.DelayCommand;
import com.comcast.cats.ir.commands.PressKeyAndHoldCommand;
import com.comcast.cats.ir.commands.PressKeyCommand;
import com.comcast.cats.service.IRHardwareEnum;
import com.comcast.cats.service.IrDevice;
import com.comcast.cats.service.IrPort;
import com.comcast.cats.service.RedRatManager;
import com.comcast.cats.service.WebServiceReturn;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * IrWebService implementation for Redrat devices.
 * 
 * @author skurup00c
 * 
 */
@Stateless
public class RedRatIRServiceHandler implements RedRatIRServiceFacade
{

    @EJB
    RedRatManager                redratManager;

    /**
     * The regex used to extract the ir port #.
     */
    private static final Pattern IR_PORT_PATTERN         = Pattern.compile( "port=([1-9]$|1[0-6]$)" );

    /**
     * The regular expression that should be used to validate a direct tune
     * channel.
     */
    private static final Pattern CHANNEL_VALIDATOR       = Pattern.compile( "\\d{1,4}" );

    private static final Integer REPEAT_IR_COMMAND_DELAY = 100;
    
    private static final Logger         logger            = LoggerFactory.getLogger( RedRatIRServiceHandler.class );

    @Override
    public boolean pressKey( URI path, String irKeySet, RemoteCommand command )
    {
        logger.trace("RedRatIRServiceHandler pressKey "+path+" irKeySet "+irKeySet+" command "+command);
        boolean response = false;
        if(path != null && irKeySet != null && command != null){
            IrPort irPort = getIrPort( path );
            logger.debug( "irPort "+irPort );
            if ( irPort != null )
            {
                PressKeyCommand pressKeyCommand = new PressKeyCommand( command, irKeySet );
                WebServiceReturn result = irPort.sendCommand( pressKeyCommand );
                logger.debug( "result "+result );
                response = ( result.getResult() == WebServiceReturnEnum.SUCCESS );
            }
        }
        return response;
    }

    @Override
    public boolean pressKeyAndHold( URI path, String irKeySet, RemoteCommand command, Integer count )
    {
        logger.trace( "IRServiceWSImpl : pressKeyAndHold path " + path + " irKeySet " + irKeySet + " command "
                + command + " count " + count );
        boolean response = false;
        if(path != null && irKeySet != null && command != null && count >= 0){
            IrPort irPort = getIrPort( path );
            logger.debug( "irPort "+irPort );
            if ( irPort != null )
            {
                PressKeyAndHoldCommand pressKeyHoldCommand = new PressKeyAndHoldCommand( command, irKeySet, count );
                WebServiceReturn result = irPort.sendCommand( pressKeyHoldCommand );
                logger.debug( "result "+result );
                response = ( result.getResult() == WebServiceReturnEnum.SUCCESS );
            }
        }
        return response;
    }

    @Override
    public boolean pressKeys( URI path, String irKeySet, List< RemoteCommand > commands, int delayMillis )
    {
        logger.trace( "IRServiceWSImpl : pressKeys path " + path + " irKeySet " + irKeySet + " commands " + commands
                + " delayMillis " + delayMillis );

        boolean response = false;
        if(path != null && irKeySet != null && commands != null && delayMillis >= 0){
            IrPort irPort = getIrPort( path );
            logger.debug( "irPort "+irPort );
            if ( irPort != null )
            {
                CatsCommand catsCommand = new CatsCommand( "PressKeys" );
                for ( RemoteCommand command : commands )
                {
                    catsCommand.add( new PressKeyCommand( command, irKeySet ) ).add( new DelayCommand( delayMillis ) );
                }
                WebServiceReturn result = irPort.sendCommand( catsCommand );
                logger.debug( "result "+result );
                response = ( result.getResult() == WebServiceReturnEnum.SUCCESS );
            }
        }
        return response;
    }

    @Override
    public boolean tune( URI path, String irKeySet, String channel, boolean autoTuneEnabled, int delayMillis )
    {
        logger.trace( "IRServiceWSImpl : tune path " + path + " irKeySet " + irKeySet + " channel " + channel
                + " delayMillis " + delayMillis + " autoTuneEnabled " + autoTuneEnabled );
        boolean retVal = false;
        if(channel != null){
            List< RemoteCommand > commands = getRemoteCommandFromChannel( channel );
            if ( commands == null || commands.size() == 0 )
            {
                return false;
            }
            if ( !autoTuneEnabled )
            {
                commands.add( RemoteCommand.SELECT );
            }
    
            retVal = pressKeys( path, irKeySet, commands, delayMillis );
        }
        
        return retVal;
    }

    @Override
    public List< RemoteLayout > getRemoteCommands( String irKeySet )
    {
        return null;
    }

    @Override
    public boolean sendIR( URI path, String irCode )
    {
        throw new UnsupportedOperationException( "RedRatIRServiceFacade.sendIR() is not supported" );
    }

    @Override
    public boolean enterCustomKeySequence( URI path, String irKeySet, List< RemoteCommand > commands,
            List< Integer > repeatCount, List< Integer > delay )
    {
        logger.trace( "IRServiceWSImpl : enterCustomKeySequence path " + path + " irKeySet " + irKeySet + " commands "
                + commands + " repeatCount " + repeatCount + " delay " + delay );
        boolean retVal = true;
        
        if ( null == path || null == irKeySet || null == commands || null == repeatCount || null == delay )
        {

            return false;
        }
        if ( commands.size() != repeatCount.size() )
        {
            return false;
        }
        if ( commands.size() != delay.size() )
        {
            return false;
        }

        IrPort irPort = getIrPort( path );
        logger.debug( "irPort "+irPort );
        if ( irPort != null )
        {

            Iterator< RemoteCommand > commandIter = commands.iterator();
            Iterator< Integer > repeatCountIetr = repeatCount.iterator();
            Iterator< Integer > delayIterator = delay.iterator();
            CatsCommand catsCommand = new CatsCommand( "enterCustomKeySequence" );
            while ( commandIter.hasNext() )
            {
                RemoteCommand command = commandIter.next();
                Integer repCount = repeatCountIetr.next();
                Integer delayMs = delayIterator.next();
                if ( repCount > 0 )
                {
                    catsCommand.add( new PressKeyAndHoldCommand( command, irKeySet, repCount ) );
                }
                else
                {
                    catsCommand.add( new PressKeyCommand( command, irKeySet ) );
                }
                if ( !retVal )
                {
                    break;
                }
                catsCommand.add( new DelayCommand( delayMs ) );
            }

            WebServiceReturn result = irPort.sendCommand( catsCommand );
            logger.debug( "result "+result );
            retVal = ( result.getResult() == WebServiceReturnEnum.SUCCESS );
        }
        return retVal;
    }

    @Override
    public boolean enterRemoteCommandSequence( URI path, String irKeySet, List< RemoteCommandSequence > commands )
    {
        logger.trace( "IRServiceWSImpl : enterRemoteCommandSequence path " + path + " irKeySet " + irKeySet
                + " commands " + commands );
        boolean retVal = true;
        if ( null == path || null == irKeySet || null == commands )
        {
            return false;
        }
        if ( commands.size() <= 0 )
        {
            return false;
        }

        IrPort irPort = getIrPort( path );
        if ( irPort != null )
        {
            Iterator< RemoteCommandSequence > commandIter = commands.iterator();
            CatsCommand catsCommand = new CatsCommand( "enterCustomKeySequence" );
            while ( commandIter.hasNext() )
            {
                RemoteCommandSequence commandSeq = commandIter.next();
                RemoteCommand command = commandSeq.getCommand();
                Integer repeatCount = commandSeq.getRepeatCount();
                Integer delay = commandSeq.getDelay();
                if ( repeatCount == null || delay == null )
                {
                    retVal = false;
                    break;
                }
                if ( repeatCount > 0 )
                {
                    catsCommand.add( new PressKeyAndHoldCommand( command, irKeySet, repeatCount ) );
                }
                else
                {
                    catsCommand.add( new PressKeyCommand( command, irKeySet ) );
                }
                if ( !retVal )
                {
                    break;
                }
                catsCommand.add( new DelayCommand( delay ) );
            }
            WebServiceReturn result = irPort.sendCommand( catsCommand );
            logger.debug( "result "+result );
            retVal = ( result.getResult() == WebServiceReturnEnum.SUCCESS );
        }
        return retVal;
    }

    @Override
    public boolean sendText( URI path, String irKeySet, String stringToBeEntered )
    {
        logger.trace( "IRServiceWSImpl : sendText path " + path + " irKeySet " + irKeySet + " stringToBeEntered "
                + stringToBeEntered );
        boolean returnVal = false;
        try{
            if ( null != stringToBeEntered && !stringToBeEntered.isEmpty() && path != null && irKeySet != null )
            {
                char[] digits = stringToBeEntered.toCharArray();
                List< RemoteCommand > commands = new ArrayList< RemoteCommand >();
                for ( char digit : digits )
                {
                    commands.add( RemoteCommand.parse( Character.toString( digit ) ) );
                }
                returnVal = pressKeys( path, irKeySet, commands, REPEAT_IR_COMMAND_DELAY );
            }
        }catch (IllegalArgumentException e) {
            logger.warn( "sendText exception "+e.getMessage() );
        }
        return returnVal;
    }

    @Override
    public List< com.comcast.cats.keymanager.domain.Remote > getRemotes()
    {
        throw new UnsupportedOperationException( "RedRatIRServiceFacade.getRemotes() is not supported" );
    }

    @Override
    public String getVersion()
    {
        throw new UnsupportedOperationException( "RedRatIRServiceFacade.getVersion() is not supported" );
    }

    private String getDeviceIP( URI path )
    {
        return path.getHost();
    }

    private int getDevicePort( URI path )
    {

        final Matcher m = IR_PORT_PATTERN.matcher( path.getQuery() );
        if ( !m.find() )
        {
            throw new IllegalArgumentException(
                    "The URI is missing the port parameter (a number between 1 an n where n is the number of ir ports installed" );
        }
        return Integer.parseInt( m.group( 1 ) );
    }

    private IRHardwareEnum getDeviceType( URI path )
    {
        return IRHardwareEnum.getByValue( path.getScheme() );
    }

    private IrPort getIrPort( URI path )
    {
        String rrDeviceIp = getDeviceIP( path );
        int port = getDevicePort( path );
        IRHardwareEnum deviceType = getDeviceType( path );
        IrDevice irDevice = null;
        IrPort irPort = null;

        switch ( deviceType )
        {
        case IRNETBOXPRO3:
            try
            {
                irDevice = redratManager.getIrDevice( rrDeviceIp );
                if ( irDevice != null )
                {
                    irPort = irDevice.getPort( port );
                }else{
                    logger.warn("irDevice is null. Could not get irDevice from redratManager "+irDevice);
                }
            }
            catch ( ClassCastException e )
            {
                // the box is not a redrat device. Dont send any commands.
            }
        }
        return irPort;
    }

    private List< RemoteCommand > getRemoteCommandFromChannel( String channel )
    {
        if ( !CHANNEL_VALIDATOR.matcher( channel ).matches() )
        {
            return null;
        }
        char[] digits = channel.toCharArray();
        List< RemoteCommand > commands = new ArrayList< RemoteCommand >();
        for ( char digit : digits )
        {
            String dStr = new String();
            dStr += digit;
            commands.add( RemoteCommand.parse( dStr ) );
        }
        return commands;
    }
	
	@Override
	public List<RemoteLayout> getRemoteLayout(String remoteType) {
		return null;
	}
}
