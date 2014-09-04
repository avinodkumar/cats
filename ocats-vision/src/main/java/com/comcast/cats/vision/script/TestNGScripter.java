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
package com.comcast.cats.vision.script;

import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_DIAG_SCREEN;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_END;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_POWER_OFF;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_POWER_ON;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_POWER_REBOOT;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_PRESS_KEY_HOLD_START;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_PRESS_KEY_START;
import static com.comcast.cats.vision.script.ScriptConstants.TEST_NG_SCRIPT_TUNE;
import static com.comcast.cats.vision.util.CatsVisionConstants.EMPTY_STRING;

import org.apache.log4j.Logger;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.vision.event.ConfigButtonEvent;
import com.comcast.cats.vision.event.PowerEvent;
import com.comcast.cats.vision.event.RemoteEvent;

/**
 * The TestNGScripter helps in scripting TestNG commands
 * 
 * @author aswathyann
 * 
 */
public class TestNGScripter implements Scripter {

	private static Logger logger = Logger.getLogger(TestNGScripter.class);

	public TestNGScripter() {
		super();
	}

	@Override
	public String generateScript(final String input) {
		return input + "\n";
	}

	@Override
	public String generateScript(final CatsEvent catsEvent) {

		String output = EMPTY_STRING;
		logger.debug("Received CatsEvent");

		if (catsEvent instanceof RemoteEvent) {

			RemoteEvent remoteEvent = (RemoteEvent) catsEvent;

			switch (remoteEvent.getActionType()) {

			case PRESS_AND_HOLD:
				output = TEST_NG_SCRIPT_PRESS_KEY_HOLD_START
						+ remoteEvent.getRemoteCommand().name() + ", "
						+ remoteEvent.getCount() + TEST_NG_SCRIPT_END;
				break;

			case PRESS:
				output = TEST_NG_SCRIPT_PRESS_KEY_START
						+ remoteEvent.getRemoteCommand().name()
						+ TEST_NG_SCRIPT_END;
				break;
			
            case TUNE:
                output = TEST_NG_SCRIPT_TUNE + remoteEvent.getChannelNumber() + 
                TEST_NG_SCRIPT_END;
                break;

			}
		} else if (catsEvent instanceof PowerEvent) {

			PowerEvent powerEvent = (PowerEvent) catsEvent;

			switch (powerEvent.getActionCommand()) {

			case ON:
				output = TEST_NG_SCRIPT_POWER_ON + "\n";
				break;

			case OFF:
				output = TEST_NG_SCRIPT_POWER_OFF + "\n";
				break;

			case REBOOT:
				output = TEST_NG_SCRIPT_POWER_REBOOT + "\n";
				break;

			}
		}
		else if ( catsEvent instanceof ConfigButtonEvent){
		    
		    output = TEST_NG_SCRIPT_DIAG_SCREEN ;
		}
		return output;
	}
}
