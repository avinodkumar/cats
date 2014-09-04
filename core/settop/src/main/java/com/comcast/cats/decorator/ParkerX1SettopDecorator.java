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
 * {@link SettopDiagnostic} implementation for Parker boxes.</br></br>
 * 
 * The key combination for X1 is</br></br>
 * 
 * "Press and hold EXIT for 10 seconds, then DOWN,DOWN,TWO".</br></br>
 * 
 * <h4>Scenario 1</h4>
 * 
 * <ul>
 * <li>Press and hold EXIT for 10 seconds</li>
 * <li>A green LED will glow and it will remain static for 10 seconds to
 * indicate it received 10 seconds of "press and hold" for 'EXIT'.</li>
 * <li>Then do DOWN,DOWN,TWO -> It will show diagnostic screen.</li>
 * </ul>
 * 
 * <h4>Scenario 2</h4>
 * 
 * <ul>
 * <li>Press and hold EXIT for 10 seconds</li>
 * <li>A green LED will glow and will start blinking to indicate that it lost
 * the "press and hold" effect.</li>
 * <li>Then do DOWN,DOWN,TWO -> NO diagnostic screen.</li>
 * </ul>
 * 
 * If it lost the "press and hold" effect, It will start counting as soon as the
 * green LED become static again. </br></br>
 * 
 * @author ssugun00c
 * 
 */
@Named( SettopConstants.SETTOP_DECORATOR_PARKER_X1 )
@Scope( "prototype" )
public class ParkerX1SettopDecorator extends SettopDecorator implements SettopDiagnostic
{
    private static final long serialVersionUID = 1L;

    private final Logger         LOGGER           = LoggerFactory.getLogger( getClass() );

    /**
     * Constructor.
     * 
     * @param settop {@linkplain Settop}
     */
    public ParkerX1SettopDecorator( Settop settop )
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
        return decoratedSettop.getRemote().pressKey( RemoteCommand.PIPSWAP );
    }

    /**
     * To down diag item.
     * 
     * @return boolean.
     */
    @Override
    public boolean downDiagItem()
    {
        throw new UnsupportedOperationException( "Not supported yet. Please contact CATS team" );
    }

    /**
     * To up diag item.
     * 
     * @return boolean.
     */
    @Override
    public boolean updiagItem()
    {
        throw new UnsupportedOperationException( "Not supported yet. Please contact CATS team" );
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
        throw new UnsupportedOperationException( "Not supported yet. Please contact CATS team" );
    }

    /**
     * For previous diag screen.
     * 
     * @return boolean.
     */
    @Override
    public boolean prevDiagScreen()
    {
        throw new UnsupportedOperationException( "Not supported yet. Please contact CATS team" );
    }

    /**
     * For last diag screen.
     * 
     * @return boolean.
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
        LOGGER.info( "Selected Diagnostic sequence : Parker X1" );
        List< RemoteCommandSequence > commandSequences = new ArrayList< RemoteCommandSequence >();       
        commandSequences.add( new RemoteCommandSequence( RemoteCommand.PIPSWAP, NO_REPEAT, NO_DELAY ) );
        return commandSequences;
    }
}
