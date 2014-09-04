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
 * {@link SettopDiagnostic} implementation for DTA.</br></br>
 * 
 * <h4>Diagnostic key sequence</h4>
 * <ul>
 * <li>Press and hold 'INFO' for 3 seconds</li>
 * </ul>
 * 
 * @author ssugun00c
 * 
 */
@Named(SettopConstants.SETTOP_DECORATOR_DTA)
@Scope("prototype")
public class DtaSettopDecorator extends SettopDecorator implements
		SettopDiagnostic {
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
     * Constructor.
     * 
     * @param settop {@linkplain Settop}
     */
	public DtaSettopDecorator(Settop settop) {
		super(settop);
		this.decoratedSettop = settop;
	}

	/**
     * To show diag menu.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
	@Override
	public boolean showDiagMenu() {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("showDiagMenu() called in - "
					+ this.getClass().getSimpleName());
		}
		return decoratedSettop.getRemote().enterRemoteCommandSequence(
				getDiagCommandSequence());
	}

	/**
     * To down diag item.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
	@Override
	public boolean downDiagItem() {
		return decoratedSettop.getRemote().pressKey(RemoteCommand.DOWN);
	}

	/**
     * To up diag item.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
	@Override
	public boolean updiagItem() {
		return decoratedSettop.getRemote().pressKey(RemoteCommand.UP);
	}

	/**
     * To enter diag screen.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
	@Override
	public boolean enterDiagScreen() {
		return decoratedSettop.getRemote().pressKey(RemoteCommand.RIGHT);
	}

	/**
     * For next diag screen.
     * 
     * @return boolean -true if pressKey successful, false otherwise.
     */
	@Override
	public boolean nextDiagScreen() {
		throw new UnsupportedOperationException(
				"Not supported yet. Please contact CATS team");
	}

	/**
     * For previous diag screen.
     * 
     * @return boolean.
     */
	@Override
	public boolean prevDiagScreen() {
		throw new UnsupportedOperationException(
				"Not supported yet. Please contact CATS team");
	}

	/**
     * For last diag screen.
     * 
     * @return boolean.
     */
	@Override
	public boolean lastDiagScreen() {
		return decoratedSettop.getRemote().pressKey(RemoteCommand.LEFT);
	}

	/**
     * For exit diag screen.
     * 
     * @return boolean.
     */
	@Override
	public boolean exitDiagScreen() {
		if (isDtaUI()) {
			return decoratedSettop.getRemote().pressKey(RemoteCommand.SEVEN);
		} else {
			return decoratedSettop.getRemote().pressKey(RemoteCommand.INFO);
		}
	}

	/**
     * To get the diag command sequence.
     * 
     * @return List of {@linkplain RemoteCommandSequence}.
     */
	@Override
	public List<RemoteCommandSequence> getDiagCommandSequence() {
		List<RemoteCommandSequence> commandSequences = new ArrayList<RemoteCommandSequence>();
		if (isDtaUI()) {
			LOGGER.info("Selected Diagnostic sequence : DTA[DTA_UI]");
			commandSequences.add(new RemoteCommandSequence(RemoteCommand.ZERO,
					4 * REPEAT_ONE_SEC, NO_DELAY));
		} else {
			LOGGER.info("Selected Diagnostic sequence : DTA");
			commandSequences.add(new RemoteCommandSequence(RemoteCommand.INFO,
					4 * REPEAT_ONE_SEC, NO_DELAY));
		}
		return commandSequences;
	}

	/**
     * To check for a DTA UI.
     * 
     * @return boolean - true if settop content is DTA_UI.
     */
	public boolean isDtaUI() {
		boolean retVal = false;
		if (DTA_UI.equalsIgnoreCase(decoratedSettop.getContent())) {
			retVal = true;
		}
		return retVal;
	}
}
