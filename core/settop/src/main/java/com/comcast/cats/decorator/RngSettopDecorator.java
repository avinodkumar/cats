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
package com.comcast.cats.decorator;

import static com.comcast.cats.info.RemoteCommandSequence.DELAY_ONE_SEC_IN_MILLISECONDS;
import static com.comcast.cats.info.RemoteCommandSequence.NO_DELAY;
import static com.comcast.cats.info.RemoteCommandSequence.NO_REPEAT;
import static com.comcast.cats.info.RemoteCommandSequence.REPEAT_ONE_SEC;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopConstants;
import com.comcast.cats.info.RemoteCommandSequence;

/**
 * {@link SettopDiagnostic} implementation for Cisco RNG boxes.</br></br>
 * 
 * RNG boxes can run in two modes </br></br>
 * <ul>
 * <li>Native mode (contentType='Native')</li>
 * <li>True Two Way mode</li>
 * </ul>
 * 
 * <h4>Cisco RNG boxes in Native Mode</h4>
 * <ul>
 * <li>Press and hold 'POWER' for 3 seconds (30 repeats) untill a green light
 * starts blinking in settop.</li>
 * <li>Press 'POWER'</li>
 * </ul>
 * 
 * <h4>Motorola/Pace RNG boxes in Native Mode</h4>
 * <ul>
 * <li>Press 'POWER'</li>
 * <li>Wait for three second</li>
 * <li>Press 'SELECT'</li>
 * </ul>
 * 
 * <h4>Cisco/Motorola/Pace RNG boxes in True Two Way Mode</h4>
 * <ul>
 * <li>"Press and hold 'EXIT' for 3 seconds (30 repeats)</li>
 * <li>Wait for one second</li>
 * <li>Press 'DOWN'</li>
 * <li>Press 'DOWN'</li>
 * <li>Press '2'</li>
 * </ul>
 * 
 * @author ssugun00c
 * 
 */
@Named( SettopConstants.SETTOP_DECORATOR_RNG )
@Scope( "prototype" )
public class RngSettopDecorator extends SettopDecorator implements SettopDiagnostic
{
    private static final long   serialVersionUID = 1L;

    private static final String NATIVE           = "Native";
    private static final String CISCO            = "Cisco";

    private final Logger           LOGGER           = LoggerFactory.getLogger( getClass() );

    /**
     * Constructor.
     * 
     * @param settop {@linkplain Settop}
     */
    public RngSettopDecorator( Settop settop )
    {
        super( settop );
        this.decoratedSettop = settop;
    }

    /**
     * To show diag menu.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
    @Override
    public boolean showDiagMenu()
    {
        if ( LOGGER.isTraceEnabled() )
        {
            LOGGER.trace( "showDiagMenu() called in - " + this.getClass().getSimpleName() );
        }
        return decoratedSettop.getRemote().enterRemoteCommandSequence( getDiagCommandSequence() );
    }

    /**
     * To down diag item.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
    @Override
    public boolean downDiagItem()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.DOWN );
    }

    /**
     * To up diag item.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
    @Override
    public boolean updiagItem()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.UP );
    }

    /**
     * To enter diag screen.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
    @Override
    public boolean enterDiagScreen()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.SELECT );
    }

    /**
     * For next diag screen.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
    @Override
    public boolean nextDiagScreen()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.RIGHT );
    }

    /**
     * For previous diag screen.
     * 
     * @return boolean.
     */
    @Override
    public boolean prevDiagScreen()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.LEFT );
    }

	/**
     * For last diag screen.
     * 
     * @return boolean.
     */
    @Override
    public boolean lastDiagScreen()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.LAST );
    }

    /**
     * For exit diag screen.
     * 
     * @return boolean.
     */
    @Override
    public boolean exitDiagScreen()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.EXIT );
    }

    /**
     * To get the diag command sequence.
     * 
     * @return List of {@linkplain RemoteCommandSequence}.
     */
    @Override
    public List< RemoteCommandSequence > getDiagCommandSequence()
    {
        List< RemoteCommandSequence > commandSequences = new ArrayList< RemoteCommandSequence >();

        if ( isNative() )
        {
            if ( ( null != decoratedSettop.getManufacturer() )
                    && ( decoratedSettop.getManufacturer().equalsIgnoreCase( CISCO ) ) )
            {
                // Cisco Legacy
                commandSequences.add( new RemoteCommandSequence( RemoteCommand.POWER, NO_REPEAT,
                        2 * DELAY_ONE_SEC_IN_MILLISECONDS ) );
                commandSequences.add( new RemoteCommandSequence( RemoteCommand.HELP, NO_REPEAT, NO_DELAY ) );

                LOGGER.info( "Selected Diagnostic sequence : Cisco RNG in Native mode" );
            }
            else
            {
                // For Pace and Motorola RNG
                commandSequences.add( new RemoteCommandSequence( RemoteCommand.POWER, NO_REPEAT,
                        2 * DELAY_ONE_SEC_IN_MILLISECONDS ) );
                commandSequences.add( new RemoteCommandSequence( RemoteCommand.SELECT, NO_REPEAT, NO_DELAY ) );

                LOGGER.info( "Selected Diagnostic sequence : Pace/Motorola RNG in Native mode" );
            }

        }
        else
        {
            // Cisco RNG 200N
            commandSequences.add( new RemoteCommandSequence( RemoteCommand.EXIT, 3 * REPEAT_ONE_SEC, NO_DELAY ) );
            commandSequences.add( new RemoteCommandSequence( RemoteCommand.DOWN, NO_REPEAT, NO_DELAY ) );
            commandSequences.add( new RemoteCommandSequence( RemoteCommand.DOWN, NO_REPEAT, NO_DELAY ) );
            commandSequences.add( new RemoteCommandSequence( RemoteCommand.TWO, NO_REPEAT, NO_DELAY ) );

            LOGGER.info( "Selected Diagnostic sequence : Cisco RNG in True-Two way mode" );
        }

        return commandSequences;
    }

    private boolean isNative()
    {
        boolean isNative = false;

        if ( ( null != decoratedSettop.getContent() ) && ( decoratedSettop.getContent().equalsIgnoreCase( NATIVE ) ) )
        {
            isNative = true;
        }
        return isNative;
    }
}
