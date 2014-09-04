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
 * {@link SettopDiagnostic} implementation for Motorola Legacy boxes
 * (DCH,DCT,DCX etc).</br></br>
 * 
 * <h4>Diagnostic key sequence</h4>
 * <ul>
 * <li>Press 'POWER'</li>
 * <li>Wait for 2 seconds</li>
 * <li>Press 'SELECT'</li>
 * </ul>
 * 
 * @author ssugun00c
 * 
 */
@Named( SettopConstants.SETTOP_DECORATOR_MOTOROLA_LEGACY )
@Scope( "prototype" )
public class MotorolaLegacySettopDecorator extends SettopDecorator implements SettopDiagnostic
{
    private static final long serialVersionUID = 1L;

    private final Logger         LOGGER           = LoggerFactory.getLogger( getClass() );

    /**
     * Constructor.
     * 
     * @param settop {@linkplain Settop}
     */
    public MotorolaLegacySettopDecorator( Settop settop )
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
     * @return boolean.
     */
    @Override
    public boolean enterDiagScreen()
    {
        throw new UnsupportedOperationException( "Not supported yet. Please contact CATS team" );
    }

    /**
     * For next diag screen.
     * 
     * @return boolean.
     */
    @Override
    public boolean nextDiagScreen()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.RIGHT );
    }

    /**
     * For previous diag screen.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
    @Override
    public boolean prevDiagScreen()
    {
        return decoratedSettop.getRemote().pressKey( RemoteCommand.LEFT );
    }

    /**
     * For last diag screen.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
    @Override
    public boolean lastDiagScreen()
    {
        throw new UnsupportedOperationException( "Not supported yet. Please contact CATS team" );
    }

    /**
     * For exit diag screen.
     * 
     * @return boolean.
     */
    @Override
    public boolean exitDiagScreen()
    {
        throw new UnsupportedOperationException( "Not supported yet. Please contact CATS team" );
    }

    /**
     * To get the diag command sequence.
     * 
     * @return List of {@linkplain RemoteCommandSequence}.
     */
    @Override
    public List< RemoteCommandSequence > getDiagCommandSequence()
    {
        LOGGER.info( "Selected Diagnostic sequence : Motorola Legacy" );
        
        List< RemoteCommandSequence > commandSequences = new ArrayList< RemoteCommandSequence >();
        commandSequences.add( new RemoteCommandSequence( RemoteCommand.POWER, NO_REPEAT,
                2 * DELAY_ONE_SEC_IN_MILLISECONDS ) );
        commandSequences.add( new RemoteCommandSequence( RemoteCommand.SELECT, NO_REPEAT, NO_DELAY ) );
        return commandSequences;
    }
}
