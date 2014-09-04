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
package com.comcast.cats.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteCommandShortcutResolver;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.info.RemoteCommandSequence;
import com.comcast.cats.keymanager.domain.Remote;
import com.comcast.cats.service.IR;

/**
 * This class implements REST based provider for IR service. This makes use of
 * resteasy client provided by jboss.
 * 
 * @author nishapk
 * 
 */
public class RemoteRest implements RemoteProvider
{
    /**
     * Local variables
     */
    private static final long serialVersionUID = 1L;
    private Logger            logger           = LoggerFactory.getLogger( RemoteRest.class );
    private IR                irRest;
    private Object            parent;
    private String            type;
    private String            keySet;
    private URI               irURI;
    private boolean           autoTuneEnabled  = false;
    private Integer           delay            = 0;

    /**
     * RemoteRest constructor which takes the service URL as one of the inputs
     * 
     * @param irServiceURL
     * @param host
     * @param port
     * @param type
     * @param keySet
     * @throws URISyntaxException
     */
    public RemoteRest( String irServiceURL, String host, Integer port, String type, String keySet )
                                                                                                   throws URISyntaxException
    {
        irServiceURL = irServiceURL + type + "/" + host + "/" + port;

        logger.info( "[IR Service REST URL][" + irServiceURL + "]" );

        this.irRest = ProxyFactory.create( IR.class, irServiceURL );
        this.type = type;
        this.keySet = keySet;
        this.irURI = new URI( type + "://" + host + "/?port=" + port );
    }

    /**
     * RemoteRest constructor which takes the REST interface as one of the
     * inputs
     * 
     * @param irServiceURL
     * @param host
     * @param port
     * @param type
     * @param keySet
     * @throws URISyntaxException
     */
    public RemoteRest( IR irRest, String host, Integer port, String type, String keySet ) throws URISyntaxException
    {
        this( irRest, host, port, type, keySet, null );
    }

    /**
     * RemoteRest constructor
     * 
     * @param irServiceURL
     * @param host
     * @param port
     * @param type
     * @param keySet
     * @throws URISyntaxException
     */
    public RemoteRest( IR irRest, String host, Integer port, String type, String keySet, Object parent )
                                                                                                        throws URISyntaxException
    {
        this.irRest = irRest;
        this.type = type;
        this.keySet = keySet;
        this.irURI = new URI( type + "://" + host + "/?port=" + port );
        this.parent = parent;
    }

    /**
     * This method suggests whether a delay is required or not.
     * 
     * @return boolean
     */
    private boolean mustDelay()
    {
        return ( delay > 0 );
    }

    /**
     * This method sleeps as per the condition provided as input.
     * 
     * @param condition
     *            - boolean
     * @return boolean
     */
    private boolean sleepOnTrue( boolean condition )
    {
        if ( condition && mustDelay() )
        {
            sleep( this.delay );
        }
        return condition;
    }

    /**
     * This method is called by sleepOnTrue to actually do the sleep function.
     * 
     * @param delay
     */
    protected void sleep( int delay )
    {
        try
        {
            Thread.sleep( delay );
        }
        catch ( InterruptedException e )
        {
            logger.warn( "Interrupted during sleep operation." );
        }
    }

    /**
     * Check whether the delay provided is within the allowed range.
     * 
     * @param delay
     */
    protected void verifyDelay( int delay )
    {
        if ( delay < 0 || delay > MAX_DELAY )
        {
            throw new IllegalArgumentException( "Remote delay must be 0 >= delay <= " + MAX_DELAY );
        }
    }

    /**
     * This method takes a list of Remote commands, list of repeat counts and
     * list of delays between each remote command.
     * 
     * @see com.comcast.cats.provider.RemoteProvider#enterCustomKeySequence(java.util.List,
     *      java.util.List, java.util.List)
     */
    @Override
    public boolean enterCustomKeySequence( List< RemoteCommand > commands, List< Integer > repeatCount,
            List< Integer > delay )
    {
        String commandStr = "";
        for ( RemoteCommand command : commands )
        {
            commandStr = commandStr + "," + command.name();
        }
        String delayStr = "";
        for ( Integer delayInt : delay )
        {
            delayStr = delayStr + "," + delayInt;
        }
        String countStr = "";
        for ( Integer cnt : repeatCount )
        {
            countStr = countStr + "," + cnt;
        }
        return sleepOnTrue( irRest.enterCustomKeySequence( keySet, commandStr, delayStr, countStr ) );
    }

    /**
     * This method takes a list of remote command sequences.
     * 
     * @see com.comcast.cats.provider.RemoteProvider#enterRemoteCommandSequence(java.util.List)
     */
    @Override
    public boolean enterRemoteCommandSequence( List< RemoteCommandSequence > commands )
    {

        List< String > commandList = new ArrayList< String >();

        for ( RemoteCommandSequence remCommand : commands )
        {
            String command = remCommand.getCommand().name();
            command = command + "," + remCommand.getRepeatCount();
            command = command + "," + remCommand.getDelay();
            commandList.add( command );
        }

        return sleepOnTrue( irRest.enterRemoteCommandSequence( keySet, commandList ) );
    }

    /**
     * This method lists all the remote types available
     * 
     * @see com.comcast.cats.provider.RemoteProvider#getAllRemoteTypes()
     */
    @Override
    public List< String > getAllRemoteTypes()
    {
        List< Remote > remotes = irRest.getRemotes();
        List< String > remoteTypes = new ArrayList< String >();
        for ( Remote remote : remotes )
        {
            remoteTypes.add( remote.getName() );
        }
        return remoteTypes;
    }

    /**
     * Getter method for delay
     * 
     * @see com.comcast.cats.provider.RemoteProvider#getDelay()
     */
    @Override
    public Integer getDelay()
    {
        return this.delay;
    }

    /**
     * Getter method for remote locator
     * 
     * @see com.comcast.cats.provider.RemoteProvider#getRemoteLocator()
     */
    @Override
    public URI getRemoteLocator()
    {
        return this.irURI;
    }

    /**
     * Getter method for remote type
     * 
     * @see com.comcast.cats.provider.RemoteProvider#getRemoteType()
     */
    @Override
    public String getRemoteType()
    {
        return this.type;
    }

    /**
     * Getter method for valid keys
     * 
     * @see com.comcast.cats.provider.RemoteProvider#getValidKeys()
     */
    @Override
    public List< RemoteLayout > getValidKeys()
    {
        throw new UnsupportedOperationException( "getValidKeys not supported yet." );
    }

    /**
     * Check whether auto tune is enabled
     * 
     * @see com.comcast.cats.provider.RemoteProvider#isAutoTuneEnabled()
     */
    @Override
    public boolean isAutoTuneEnabled()
    {
        return this.autoTuneEnabled;
    }

    /**
     * This method does the pressKey for a particular command
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKey(com.comcast.cats.RemoteCommand)
     */
    @Override
    public boolean pressKey( RemoteCommand command )
    {
        return sleepOnTrue( irRest.pressKey( keySet, command.toString() ) );
    }

    /**
     * This method does the pressKey for a particular command with a specific
     * delay
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKey(com.comcast.cats.RemoteCommand,java.lang.Integer)
     */
    @Override
    public boolean pressKey( RemoteCommand command, Integer delay )
    {
        setDelay( delay );
        return sleepOnTrue( irRest.pressKey( keySet, command.toString() ) );
    }

    /**
     * This method does the pressKey for a particular command with a repeat
     * count
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKey(java.lang.Integer,com.comcast.cats.RemoteCommand)
     */
    @Override
    public boolean pressKey( Integer count, RemoteCommand command )
    {
        boolean returnValue = true;
        for ( int index = 0; index < count; index++ )
        {
            returnValue = sleepOnTrue( irRest.pressKey( keySet, command.toString() ) );
            if ( !returnValue )
            {
                break;
            }
        }
        return returnValue;
    }

    /**
     * This method does the pressKey for a particular command with a repeat
     * count and delay
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKey(java.lang.Integer,com.comcast.cats.RemoteCommand,java.lang.Integer)
     */
    @Override
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay )
    {
        boolean returnValue = true;
        setDelay( delay );
        for ( int index = 0; index < count; index++ )
        {
            returnValue = sleepOnTrue( irRest.pressKey( keySet, command.toString() ) );
            if ( !returnValue )
            {
                break;
            }
        }
        return returnValue;
    }

    /**
     * This method does the pressKey for a particular array of commands with a
     * repeat count and delay
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKey(java.lang.Integer,
     *      java.lang.Integer, com.comcast.cats.RemoteCommand[])
     */
    @Override
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands )
    {
        boolean returnValue = false;
        for ( int index = 0; index < count; index++ )
        {
            returnValue = pressKeys( Arrays.asList( commands ), delay );
        }
        return returnValue;
    }

    /**
     * This method does the pressKey for a set of commands.
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKey(com.comcast.cats.RemoteCommand[])
     */
    @Override
    public boolean pressKey( RemoteCommand[] commands )
    {
        return pressKeys( Arrays.asList( commands ) );
    }

    /**
     * This method does the pressKey for an integer command
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKey(java.lang.Integer)
     */
    @Override
    public boolean pressKey( Integer command )
    {
        return sleepOnTrue( irRest.pressKey( keySet, String.valueOf( command ) ) );
    }

    /**
     * This method does the pressKey for a command and holds it for count time.
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKeyAndHold(com.comcast.cats.RemoteCommand,
     *      java.lang.Integer)
     */
    @Override
    public boolean pressKeyAndHold( RemoteCommand command, Integer count )
    {
        return irRest.pressKeyAndHold( keySet, command.toString(), String.valueOf( count ) );
    }

    /**
     * This method does the pressKeys for a list of remote commands.
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKeys(java.util.List)
     */
    @Override
    public boolean pressKeys( List< RemoteCommand > commands )
    {
        String commandStr = "";
        for ( RemoteCommand command : commands )
        {
            commandStr = commandStr + "," + command.name();
        }
        return sleepOnTrue( irRest.pressKeys( keySet, commandStr, 0 ) );
    }

    /**
     * This method does the pressKey for a list of commands with a given delay
     * between the commands.
     * 
     * @see com.comcast.cats.provider.RemoteProvider#pressKeys(java.util.List,
     *      java.lang.Integer)
     */
    @Override
    public boolean pressKeys( List< RemoteCommand > commands, Integer delay )
    {
        String commandStr = "";
        for ( RemoteCommand command : commands )
        {
            commandStr = commandStr + "," + command.name();
        }
        return sleepOnTrue( irRest.pressKeys( keySet, commandStr, delay ) );
    }

    /**
     * This method sends text. Note that the text has to be a numeric string.
     * 
     * @see com.comcast.cats.provider.RemoteProvider#sendText(java.lang.String)
     */
    @Override
    public boolean sendText( String text )
    {
        return irRest.sendText( keySet, text );
    }

    /**
     * Setter method for auto tune enable
     * 
     * @see com.comcast.cats.provider.RemoteProvider#setAutoTuneEnabled(boolean)
     */
    @Override
    public void setAutoTuneEnabled( boolean autoTuneEnabled )
    {
        this.autoTuneEnabled = autoTuneEnabled;
    }

    /**
     * Setter method for delay
     * 
     * @see com.comcast.cats.provider.RemoteProvider#setDelay(java.lang.Integer)
     */
    @Override
    public void setDelay( Integer delay )
    {
        verifyDelay( delay );
        this.delay = delay;
    }

    /**
     * Setter method for remote type
     * 
     * @see com.comcast.cats.provider.RemoteProvider#setRemoteType(java.lang.String)
     */
    @Override
    public void setRemoteType( String remoteType )
    {
        this.type = remoteType;
    }

    /**
     * This method tunes the channel (given in string format)
     * 
     * @see com.comcast.cats.provider.RemoteProvider#tune(java.lang.String)
     */
    @Override
    public boolean tune( String channel )
    {
        return irRest.tune( keySet, channel, String.valueOf( autoTuneEnabled ), String.valueOf( delay ) );
    }

    /**
     * This method tunes the channel (given in integer format)
     * 
     * @see com.comcast.cats.provider.RemoteProvider#tune(java.lang.Integer)
     */
    @Override
    public boolean tune( Integer channel )
    {
        return irRest.tune( keySet, String.valueOf( channel ), String.valueOf( autoTuneEnabled ),
                String.valueOf( delay ) );
    }

    /**
     * Getter method for parent object
     * 
     * @see com.comcast.cats.provider.BaseProvider#getParent()
     */
    @Override
    public Object getParent()
    {
        return parent;
    }

    /**
     * This method is to send the IR code. Note that the code is sent as the
     * payload of the REST request.
     * 
     * @param irCode
     *            - the raw IR code string
     * @return true/false
     */
    public boolean sendIR( String irCode )
    {
        return irRest.sendIR( irCode );
    }

    @Override
    public boolean performShorthandCommandSequence( String text )
    {
        return performShorthandCommandSequence( text, 500 );
    }

    @Override
    public boolean performShorthandCommandSequence( String text, Integer delay )
    {
        boolean returnVal = false;
        if ( !( null == text || text.isEmpty() ) )
        {
            char[] chars = text.toCharArray();
            List< RemoteCommand > commands = new ArrayList< RemoteCommand >();
            for ( char character : chars )
            {
                commands.add( RemoteCommandShortcutResolver.resolveRemoteCommand( character ) );
            }
            logger.info( "sendText : commands:" + commands );
            returnVal = pressKeys( commands, delay );
        }
        return returnVal;
    }
}
