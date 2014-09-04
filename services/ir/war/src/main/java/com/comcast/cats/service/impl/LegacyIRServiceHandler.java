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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.service.IRManager;
import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.KeyManager;

@Stateless
@TransactionAttribute( TransactionAttributeType.NOT_SUPPORTED )
public class LegacyIRServiceHandler implements LegacyIRServiceFacade
{

    /**
     * The regex used to extract the ir port #.
     */
    private static final Pattern IR_PORT_PATTERN         = Pattern.compile( ".*port=([1-6]+).*" );

    private static final Logger  logger                  = LoggerFactory.getLogger( IRServiceWSImpl.class );
    /**
     * The regular expression that should be used to validate a direct tune
     * channel.
     */
    private static final Pattern CHANNEL_VALIDATOR       = Pattern.compile( "\\d{1,4}" );

    /**
     * When identical keys are sent to a device in succession the device can
     * misinterpret these as a "holding" of the button not a secondary command.
     * Add a unique delay for this scenario to improve tune and pressKey
     * commands.
     */
    private static final Integer REPEAT_IR_COMMAND_DELAY = 100;

    @EJB
    private IRManager            irManager;

    /**
     * Create the singleton class here, because it makes sense.
     */

    @EJB
    private KeyManager           keyManager;

    public LegacyIRServiceHandler()
    {
        // Do nothing @EJB should inject dependencies.
    }

    public LegacyIRServiceHandler( IRManager irManager, KeyManager keyManager )
    {
        this.irManager = irManager;
        this.keyManager = keyManager;
    }

    public boolean pressKey( final URI path, final String irKeySet, final RemoteCommand command )
    {
        Long tstart = System.currentTimeMillis();
        final String irCode = keyManager.getIrCode( irKeySet, "PRONTO", command.name() );
        Long tgetCode = System.currentTimeMillis();
        if ( irCode != null )
        {
            logger.debug( "irCode found for KeySet[" + irKeySet + "]" + irCode );
            Long tbeforeIR = System.currentTimeMillis();
            boolean rtn = performIRCommand( path, irCode, 1, 1 );
            Long tafterIR = System.currentTimeMillis();
            logger.info( "Key Retrieval: " + ( tgetCode - tstart ) + "ms | IR Transmission = "
                    + ( tafterIR - tbeforeIR ) );
            return rtn;

        }
        else
        {
            logger.warn( "irCode not found for KeySet[" + irKeySet + "] Command[" + command.name() + "]" );
        }
        return false;
    }

    public boolean pressKeyAndHold( final URI path, final String irKeySet, final RemoteCommand command,
            final Integer count )
    {
        final String irCode = keyManager.getIrCode( irKeySet, "PRONTO", command.name() );
        if ( irCode != null )
        {
            logger.info( "irCode found for KeySet[" + irKeySet + "]" + irCode );
            return performIRCommand( path, irCode, count, 0 );
        }
        else
        {
            logger.warn( "irCode not found for KeySet[" + irKeySet );
        }
        return false;
    }

    /**
     * I hate having to catch the stupid exception in my real code, so create a
     * method to handle this for me.
     * 
     * @param delay
     */
    private void sleep( int delay )
    {
        try
        {
            Thread.sleep( delay );
        }
        catch ( InterruptedException ex )
        {
            logger.warn( "Interrupted during IRServiceWSImpl sleep", ex );
        }
    }

    public boolean pressKeys( URI path, String irKeySet, List< RemoteCommand > commands, int delayMillis )
    {
        if ( path == null || irKeySet == null || commands == null || commands.isEmpty() )
        {
            logger.warn( "pressKeys() has been called with invalid arguments." );
            return false;
        }
        boolean rtn = true;
        RemoteCommand lastCommand = null;
        int delay = delayMillis;
        for ( RemoteCommand command : commands )
        {
            // If command is same as last command insert a delay of
            // REPEAT_IR_COMMAND_DELAY
            delay = ( null != lastCommand && lastCommand.equals( command ) ) ? REPEAT_IR_COMMAND_DELAY : delayMillis;
            /**
             * If lastCommand is not set, then this is the first key, so ignore
             * the sleep.
             */
            if ( null != lastCommand )
            {
                sleep( delay );
            }
            rtn = pressKey( path, irKeySet, command );
            lastCommand = command;
            if ( rtn == false )
            {
                break;
            }
        }
        return rtn;
    }

    public boolean tune( URI path, String irKeySet, String channel, boolean autoTuneEnabled, int delayMillis )
    {
        if ( path == null )
        {
            logger.warn( "tune() path=null" );
            return false;
        }
        List< RemoteCommand > commands = getRemoteCommandFromChannel( channel );
        if ( commands == null || commands.size() == 0 )
        {
            return false;
        }
        if ( !autoTuneEnabled )
        {
            commands.add( RemoteCommand.SELECT );
        }

        // When debug is enabled show all the keys being transmitted.
        if ( logger.isDebugEnabled() )
        {
            String keyStr = new String();
            for ( RemoteCommand command : commands )
            {
                keyStr += command.toString() + ":";
            }
            logger.debug( "Sending Keys [" + keyStr + "]" );
        }
        return pressKeys( path, irKeySet, commands, delayMillis );
    }

    /**
     * Send Raw IR code based on path
     */
    public boolean sendIR( URI path, String irCode )
    {
        // return sendIR(path, irCode, 1, 1);
        if ( irCode != null )
        {
            return performIRCommand( path, irCode, 1, 1 );
        }
        return false;
    }

    @Override
    public List< RemoteLayout > getRemoteCommands( String irKeySet )
    {
        return null;
    }

    private List< RemoteCommand > getRemoteCommandFromChannel( String channel )
    {
        if ( !CHANNEL_VALIDATOR.matcher( channel ).matches() )
        {
            logger.warn( "Channel doesn't match expression: " + CHANNEL_VALIDATOR.toString() );
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

    private boolean performIRCommand( final URI path, final String irCode, final Integer count, final Integer offset )
    {
        IRCommunicator com;
        boolean rtn = false;
        try
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "================================ Begin: performIRCommand =================================" );
            }
            com = irManager.retrieveIRCommunicator( path );

            int port = parsePortParameter( path );

            long start = System.currentTimeMillis();

            rtn = com.transmitIR( irCode, port, count, offset );

            long end = System.currentTimeMillis();
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "IR Command for " + path.toString() + " took " + Long.toString( end - start ) + " ms" );
                logger.debug( "================================   End: performIRCommand =================================" );
            }
        }
        catch ( UnknownHostException e )
        {
            logger.error( "Error found looking up IRCommunicator", e );
        }
        return rtn;
    }

    /**
     * Convenience method to parse out the port parameter of the path.
     * 
     * @param path
     *            The path to parse
     * @return The ir port number present in the path
     */
    private int parsePortParameter( final URI path )
    {
        final Matcher m = IR_PORT_PATTERN.matcher( path.getQuery() );
        if ( !m.find() )
        {
            throw new IllegalArgumentException(
                    "The URI is missing the port parameter (a number between 1 an n where n is the number of ir ports installed" );
        }
        return Integer.parseInt( m.group( 1 ) );
    }

    @Override
    public boolean enterCustomKeySequence( final URI path, String irKeySet, List< RemoteCommand > commands,
            List< Integer > repeatCount, final List< Integer > delay )
    {
        boolean retVal = true;
        if ( null == path || null == irKeySet || null == commands || null == repeatCount || null == delay )
        {
            logger.warn( "enterCustomKeySequence() has been called with invalid arguments." );

            return false;
        }
        if ( commands.size() != repeatCount.size() )
        {
            logger.warn( "enterCustomKeySequence() has been called commands size and repeatSize not matching" );
            return false;
        }
        if ( commands.size() != delay.size() )
        {
            logger.warn( "enterCustomKeySequence() has been called with invalid arguments." );
            return false;
        }

        Iterator< RemoteCommand > commandIter = commands.iterator();
        Iterator< Integer > repeatCountIetr = repeatCount.iterator();
        Iterator< Integer > delayIterator = delay.iterator();

        while ( commandIter.hasNext() )
        {
            RemoteCommand command = commandIter.next();
            Integer repCount = repeatCountIetr.next();
            Integer del = delayIterator.next();
            logger.info( "enterCustomKeySeq : command:" + command + " repeatCount :" + repCount + " delay:" + delay );
            if ( repCount > 0 )
            {
                retVal = pressKeyAndHold( path, irKeySet, command, repCount );
            }
            else
            {
                retVal = pressKey( path, irKeySet, command );
            }
            if ( !retVal )
            {
                logger.error( "enterCustomKeySeq failed during pressKey" );
                break;
            }
            sleep( del );
        }
        return retVal;
    }

    @Override
    public boolean sendText( URI path, String irKeySet, String stringToBeEntered )
    {
        boolean returnVal = false;
        if ( !( null == stringToBeEntered || stringToBeEntered.isEmpty() ) )
        {
            char[] digits = stringToBeEntered.toCharArray();
            List< RemoteCommand > commands = new ArrayList< RemoteCommand >();
            for ( char digit : digits )
            {
                commands.add( RemoteCommand.parse( Character.toString( digit ) ) );
            }
            logger.info( "sendText : commands:" + commands );
            returnVal = pressKeys( path, irKeySet, commands, REPEAT_IR_COMMAND_DELAY );
        }
        return returnVal;
    }

    @Override
    public boolean enterRemoteCommandSequence( URI path, String irKeySet, List< RemoteCommandSequence > commands )
    {
        boolean retVal = true;
        if ( null == path || null == irKeySet || null == commands )
        {
            logger.warn( "enterCustomKeySequence() has been called with invalid arguments." );
            return false;
        }
        if ( commands.size() <= 0 )
        {
            logger.warn( "enterCustomKeySequence() has been called commands size =" + commands.size() );
            return false;
        }

        Iterator< RemoteCommandSequence > commandIter = commands.iterator();

        while ( commandIter.hasNext() )
        {
            RemoteCommandSequence commandSeq = commandIter.next();
            RemoteCommand command = commandSeq.getCommand();
            Integer repeatCount = commandSeq.getRepeatCount();
            Integer delay = commandSeq.getDelay();
            logger.info( "enterCustomKeySeq : command:" + command + " repeatCount :" + repeatCount + " delay:" + delay );
            if ( repeatCount == null || delay == null )
            {
                retVal = false;
                break;
            }
            if ( repeatCount > 0 )
            {
                retVal = pressKeyAndHold( path, irKeySet, command, repeatCount );
            }
            else
            {
                retVal = pressKey( path, irKeySet, command );
            }
            if ( !retVal )
            {
                logger.error( "enterCustomKeySeq failed during pressKey" );
                break;
            }
            sleep( delay );
        }
        return retVal;
    }

    @Override
    public List< com.comcast.cats.keymanager.domain.Remote > getRemotes()
    {
        throw new UnsupportedOperationException( "LegacyIRServiceFacade.getRemotes() is not supported" );
    }

    @Override
    public String getVersion()
    {
        throw new UnsupportedOperationException( "LegacyIRServiceFacade.getVersion() is not supported" );
    }
	
	@Override
	public List<RemoteLayout> getRemoteLayout(String remoteType) {
		return null;
	}
}
