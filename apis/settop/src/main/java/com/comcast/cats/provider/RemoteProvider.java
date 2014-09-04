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
import java.util.List;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.RemoteLayout;
import com.comcast.cats.annotation.ExclusiveAccess;
import com.comcast.cats.info.RemoteCommandSequence;

/**
 * Provide remote IR capabilities on Settop.
 * 
 * @author cfrede001
 */
public interface RemoteProvider extends BaseProvider
{
    /**
     * Constant for the maximum delay of 30000ms
     */
    public static final Integer MAX_DELAY = 30000;

    /**
     * Return locator for hardware that facilitates Remote (IR) handling. This
     * will point to the device used for sending ir keypresses. ie:
     * gc100://<ip>:<port>/?port=x
     * 
     * @return URI containing remote hardware definition.
     */
    public URI getRemoteLocator();

    /**
     * Retrieve the remote type for this provider.
     * 
     * @return String representing the type for this remote.
     */
    public String getRemoteType();

    /**
     * Auto tune determines how a direct tune should occur.
     * 
     * @param autoTuneEnabled
     *            - Set auto tune configuration information.
     */
    public void setAutoTuneEnabled( boolean autoTuneEnabled );

    /**
     * Get the auto tune state.
     * 
     * @return - True if auto tune is enabled, false otherwise.
     */
    public boolean isAutoTuneEnabled();

    /**
     * Direct tune to a channel that is up to 4 digits in length.
     * 
     * @param channel
     *            - Channel number(as String) to tune to.
     * @return - True if tuning is successful, false otherwise.
     */
    @ExclusiveAccess
    public boolean tune( String channel );

    /**
     * Direct tune to a channel(integer) that is up to 4 digits in length.
     * 
     * @param channel
     *            - Channel number(Integer) to tune to.
     * @return - True if tuning is successful, false otherwise.
     */
    @ExclusiveAccess
    public boolean tune( Integer channel );

    /**
     * Update the delay used between remote commands. This value is specified in
     * milliseconds.
     * 
     * @param delay
     *            as Integer
     */
    public void setDelay( Integer delay );

    /**
     * Returns the default delay between remote commands.
     * 
     * @return - Integer - delay in milliseconds
     */
    public Integer getDelay();

    /**
     * Press Key on the remote.
     * 
     * @param command
     *            - as RemoteCommand - Key to be sent.
     * @return - True if remote command operation is successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKey( RemoteCommand command );

    /**
     * Perform a remote command and afterwards delay for the specified time in
     * milliseconds.
     * 
     * @param command
     *            - Remote command for transmission.
     * @param delay
     *            - Delay time in milliseconds after command.
     * @return - True if the remote command operations are successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKey( RemoteCommand command, Integer delay );

    /**
     * Perform a remote command for a specified number of times.
     * 
     * @param count
     *            - The number of times the remote command to be performed.
     * @param command
     *            - Remote command
     * @return - True if all the remote command operations are successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKey( Integer count, RemoteCommand command );

    /**
     * Perform a remote command and afterwards delay for the specified time, for
     * a specified number of times.
     * 
     * @param count
     *            - The number of times the remote command with the delay to be
     *            performed.
     * @param command
     *            - Remote command
     * @param delay
     *            - Delay time in milliseconds after command.
     * @return - True if all the remote command operations are successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKey( Integer count, RemoteCommand command, Integer delay );

    /**
     * Perform a set of remote commands and afterwards delay for the specified
     * time, for a specified number of times.
     * 
     * @param count
     *            - The number of times the remote commands with the delay to be
     *            performed.
     * @param delay
     *            - Delay time in milliseconds after each command.
     * @param commands
     *            - Array of Remote Commands
     * @return - True if all the remote command operations are successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKey( Integer count, Integer delay, RemoteCommand[] commands );

    /**
     * Perform a set of remote commands one after the other.
     * 
     * @param commands
     *            - Array of Remote Commands
     * @return - True if all the remote command operations are successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKey( RemoteCommand[] commands );

    /**
     * Press key for integers ranging from 0 - 9.
     * 
     * @param command
     *            - Integer 0 to 9
     * @return - True if the remote command operation is successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKey( Integer command );

    /**
     * Press key and hold it down for the specified count.
     * 
     * @param command
     *            - Key to be sent.
     * @param count
     *            - Number of times key should be repeated.
     * @return - True if the remote command operation is successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKeyAndHold( RemoteCommand command, Integer count );

    /**
     * Press arbitrary list of keys. If a direct tune is needed, don't use this
     * method as a way of sending tunes.
     * 
     * @param commands
     *            - List of keys to be sent.
     * @return - True if all the remote command operation is successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKeys( List< RemoteCommand > commands );

    /**
     * Press keys with a delay between. This delay will NOT account for delays
     * associated with physical hardware sending the key.
     * 
     * @param commands
     *            - List of keys to be sent.
     * @param delay
     *            - Delay in milliseconds between key presses.
     * @return - True if all the remote command operation is successful, false
     *         otherwise.
     */
    @ExclusiveAccess
    public boolean pressKeys( List< RemoteCommand > commands, Integer delay );

    /**
     * Press the sequence of keys with specified repeat count a delays in
     * between. This delay will NOT account for delay associated with physical
     * hardware sending the key.
     * 
     * @param commands
     *            - List of keys to be sent.
     * @param repeatCount
     *            - Repeat counts of each of the keys.
     * @param delay
     *            -Delay between each of the keys
     * @return true if all the remote command operation is successful. false at
     *         the first failure.
     */
    @ExclusiveAccess
    public boolean enterCustomKeySequence( List< RemoteCommand > commands, List< Integer > repeatCount,
            List< Integer > delay );

    /**
     * Press the sequence of keys with specified repeat count a delays in
     * between. This delay will NOT account for delay associated with physical
     * hardware sending the key.
     * 
     * @param commands
     *            - List of Commands with the required keys,repeat commands and
     *            delays in between
     * @return true if all the remote command operation is successful. false at
     *         the first failure.
     */
    @ExclusiveAccess
    public boolean enterRemoteCommandSequence( List< RemoteCommandSequence > commands );

    /**
     * Return a list of valid commands for this remote.
     * 
     * @return A list of valid keys for this remote.
     */
    public List< RemoteLayout > getValidKeys();

    /**
     * Text entry for Astro remote. The string would be parsed and the
     * individual characters would be transformed to corresponding key codes in
     * the Astro remote.
     * 
     * @param text
     *            - Text Message
     * @return true if the text message executed successfully, false otherwise.
     */
    @ExclusiveAccess
    public boolean sendText( String text );

    /**
     * Function which enables users to send remote commands using single
     * character short hand notation Eg: M:Menu S:Select G:GUIDE 1:ONE etc. So
     * on specifying MS1G would be interpreted as Menu,Select,ONE and Guide
     * 
     * @param text
     *            - string having the single character representation for keys
     * @return true if the text message executed successfully, false otherwise.
     */
    public boolean performShorthandCommandSequence( String text );

    /**
     * Function which enables users to send remote commands using single
     * character short hand notation Eg: M:Menu S:Select G:GUIDE 1:ONE etc. So
     * on specifying MS1G would be interpreted as Menu,Select,ONE and Guide
     * 
     * @param text
     *            - string having the single character representation for keys
     * @param delay
     *            -delay in milliseconds between each key press.
     * @return true if the text message executed successfully, false otherwise.
     */
    public boolean performShorthandCommandSequence( String text, Integer delay );

    /**
     * Get all remote types available.
     * 
     * @return all remote types available in this CATS system.
     */
    List< String > getAllRemoteTypes();

    /**
     * Override remoteType to use the newly set remote type.
     * 
     * @param remoteType
     */
    void setRemoteType( String remoteType );
}
