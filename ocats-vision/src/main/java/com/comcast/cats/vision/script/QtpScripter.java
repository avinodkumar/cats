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

import static com.comcast.cats.vision.script.ScriptConstants.COMMA_SEPARATOR;
import static com.comcast.cats.vision.script.ScriptConstants.NEW_LINE;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_POWER_OFF;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_POWER_ON;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_POWER_TOGGLE;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_PRESS_KEY_AND_HOLD_START;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_PRESS_KEY_END;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_PRESS_KEY_START;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_TUNE_END;
import static com.comcast.cats.vision.script.ScriptConstants.QTP_SCRIPT_TUNE_START;
import static com.comcast.cats.vision.util.CatsVisionConstants.EMPTY_STRING;

import org.apache.log4j.Logger;

import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.vision.event.PowerEvent;
import com.comcast.cats.vision.event.RemoteEvent;

/**
 * The QtpScripter helps in scripting QTP commands
 * 
 * @author aswathyann
 * 
 */
public class QtpScripter implements Scripter
{

    private static Logger logger          = Logger.getLogger( QtpScripter.class );

    private String        lastUpdatedText = EMPTY_STRING;

    private String        output          = EMPTY_STRING;

    public QtpScripter()
    {
        super();
    }

    @Override
    public String generateScript( final String input )
    {
        return input + NEW_LINE;
    }

    @Override
    public String generateScript( final CatsEvent catsEvent )
    {

        logger.debug( "Received CatsEvent" );

        if ( catsEvent instanceof RemoteEvent )
        {

            RemoteEvent remoteEvent = ( RemoteEvent ) catsEvent;

            lastUpdatedText = output;

            switch ( remoteEvent.getActionType() )
            {

            case PRESS_AND_HOLD:
                output = QTP_SCRIPT_PRESS_KEY_AND_HOLD_START + remoteEvent.getRemoteCommand().name() + COMMA_SEPARATOR
                        + remoteEvent.getCount() + NEW_LINE;
                break;

            case PRESS:
                output = QTP_SCRIPT_PRESS_KEY_START + remoteEvent.getRemoteCommand().name() + QTP_SCRIPT_PRESS_KEY_END;
                break;

            case TUNE:
                output = QTP_SCRIPT_TUNE_START + remoteEvent.getChannelNumber() + QTP_SCRIPT_TUNE_END;
                break;
            }
        }
        else if ( catsEvent instanceof PowerEvent )
        {

            PowerEvent powerEvent = ( PowerEvent ) catsEvent;

            switch ( powerEvent.getActionCommand() )
            {

            case ON:
                output = QTP_SCRIPT_POWER_ON + NEW_LINE;
                break;
            case OFF:
                output = QTP_SCRIPT_POWER_OFF + NEW_LINE;
                break;
            case REBOOT:
                output = QTP_SCRIPT_POWER_TOGGLE + NEW_LINE;
                break;
            }
        }
        return output;
    }

    public String getLastUpdatedText()
    {
        return lastUpdatedText;
    }

    public void setLastUpdatedText( String lastUpdatedText )
    {
        this.lastUpdatedText = lastUpdatedText;
    }
}
