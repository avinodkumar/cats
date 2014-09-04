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

import java.util.List;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.info.RemoteCommandSequence;

/**
 * A {@link Settop} decorator with diagnostic related operations.
 * 
 * @author ssugun00c
 * 
 */
public interface SettopDiagnostic extends Settop
{
    /**
     * Returns {@link RemoteCommand} sequence for diagnostic screen for specific
     * Settop family.
     * 
     * @return List of RemoteCommandSequence
     */
    List< RemoteCommandSequence > getDiagCommandSequence();

    /**
     * Shows the main diagnostic screen.
     * 
     * @return boolean
     */
    boolean showDiagMenu();

    /**
     * Traverse up in diagnostic menu item.
     * 
     * @return boolean
     */
    boolean updiagItem();

    /**
     * Traverse down in diagnostic menu screen.
     * 
     * @return boolean
     */
    boolean downDiagItem();

    /**
     * Enter in to a particular diagnostic screen.
     * 
     * @return boolean
     */
    boolean enterDiagScreen();

    /**
     * Move to the next diagnostic screen.
     * 
     * @return boolean
     */
    boolean nextDiagScreen();

    /**
     * Move to the previous diagnostic screen.
     * 
     * @return boolean
     */
    boolean prevDiagScreen();

    /**
     * Move to the last diagnostic menu
     * 
     * @return boolean
     */
    boolean lastDiagScreen();

    /**
     * Exit diagnostic screen.
     * 
     * @return boolean
     */
    boolean exitDiagScreen();
}
