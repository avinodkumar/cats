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

import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_POWER_OFF;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_POWER_ON;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_POWER_REBOOT;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_PRESS;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_PRESS_HOLD;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_SLEEP;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_TUNE;
import static com.comcast.cats.vision.script.ScriptConstants.DSL_CONSECUTIVE_PRESSES_SYNTAX;
import static com.comcast.cats.vision.script.ScriptConstants.DSL_PRESS_KEY_SYNTAX;
import static com.comcast.cats.vision.util.CatsVisionConstants.EMPTY_STRING;
import static com.comcast.cats.vision.script.ScriptConstants.CATS_SCRIPT_DIAG_SCREEN;

import org.apache.log4j.Logger;

import com.comcast.cats.RemoteCommand;
import com.comcast.cats.event.CatsEvent;
import com.comcast.cats.vision.event.ConfigButtonEvent;
import com.comcast.cats.vision.event.PowerEvent;
import com.comcast.cats.vision.event.RemoteEvent;
import com.comcast.cats.vision.event.ScreenCaptureEvent;

/**
 * The CatsScripter helps in scripting Cats related activities
 * 
 * @author aswathyann
 * 
 */
public class CatsScripter implements Scripter
{
    private static Logger logger                      = Logger.getLogger( CatsScripter.class );

    private long          catsStartTime;
    private static int    defaultKeyPressIntervel     = 2;
    private String        lastScriptedCatsText        = EMPTY_STRING;
    private boolean       isScriptExistsOnStartRecord = false;

    public CatsScripter()
    {
        super();
    }

    @Override
    public String generateScript( final String input )
    {
        return input + "\n";
    }

    @Override
    public String generateScript( final CatsEvent catsEvent )
    {
        String output = EMPTY_STRING;
        logger.debug( "Received CatsEvent" );

        if ( catsEvent instanceof RemoteEvent )
        {
            RemoteEvent remoteEvent = ( RemoteEvent ) catsEvent;

            switch ( remoteEvent.getActionType() )
            {

            case PRESS_AND_HOLD:
                output = getFormattedCatsScript( CATS_SCRIPT_PRESS_HOLD + remoteEvent.getRemoteCommand().name() + ", "
                        + remoteEvent.getCount() );
                break;

            case PRESS:
                output = getFormattedCatsScript( CATS_SCRIPT_PRESS + getCatsCommand( remoteEvent.getRemoteCommand() ) );
                break;

            case TUNE:
                output = getFormattedCatsScript( CATS_SCRIPT_TUNE + remoteEvent.getChannelNumber() );
                break;
            }
        }
        else if ( catsEvent instanceof PowerEvent )
        {
            PowerEvent powerEvent = ( PowerEvent ) catsEvent;

            switch ( powerEvent.getActionCommand() )
            {
            case ON:
                output = CATS_SCRIPT_POWER_ON;
                break;

            case OFF:
                output = CATS_SCRIPT_POWER_OFF;
                break;

            case REBOOT:
                output = CATS_SCRIPT_POWER_REBOOT;
                break;
            }
            output = getFormattedCatsScript( output );
        }
        else if ( catsEvent instanceof ScreenCaptureEvent )
        {
            output = getFormattedCatsScript( "captureScreen()" );
        }
        else if ( catsEvent instanceof ConfigButtonEvent )
        {
            output = getFormattedCatsScript( CATS_SCRIPT_DIAG_SCREEN );
        }
        return output;
    }

    /**
     * Set method for storing the last scripted cats text
     * 
     * @param lastScriptedCatsText
     *            last scripted cats text
     */
    public void setLastScriptedCatsText( String lastScriptedCatsText )
    {
        this.lastScriptedCatsText = lastScriptedCatsText;
    }

    /**
     * Get method for last scripted cats text
     * 
     * @return last scripted cats text
     */
    public String getLastScriptedCatsText()
    {
        return lastScriptedCatsText;
    }

    public void setScriptExistsOnStartRecord( boolean isScriptExistsOnStartRecord )
    {
        this.isScriptExistsOnStartRecord = isScriptExistsOnStartRecord;
    }

    public boolean isScriptExistsOnStartRecord()
    {
        return isScriptExistsOnStartRecord;
    }

    /**
     * Method to format the CatsScript generated from the Scripter
     * implementation for Cats. According to the time elapsed between the key
     * presses, the commands need to be appended. Also the time delay need to be
     * captured.
     * 
     * @param catsScript
     *            The script generated from Cats Scripter implementation.
     * @return
     */
    private String getFormattedCatsScript( String catsScript )
    {
        String output = EMPTY_STRING;
        long elapsedTimeInSec = getElapsedTime( catsStartTime );

        if ( getLastScriptedCatsText().isEmpty() )
        {
            if ( isScriptExistsOnStartRecord )
            {
                output = "\n";
                setScriptExistsOnStartRecord( false );
            }

            output = output + catsScript;
            setLastScriptedCatsText( catsScript );
        }
        else if ( getLastScriptedCatsText().matches( DSL_PRESS_KEY_SYNTAX ) )
        {
            if ( elapsedTimeInSec > defaultKeyPressIntervel )
            {
                output = ", " + elapsedTimeInSec * 1000 + "\n" + catsScript;
                setLastScriptedCatsText( catsScript );
            }
            else
            {
                output = getOutputTextFromCatsScript( catsScript );
            }
        }
        else if ( getLastScriptedCatsText().matches( DSL_CONSECUTIVE_PRESSES_SYNTAX ) )
        {
            if ( elapsedTimeInSec > defaultKeyPressIntervel )
            {
                output = "\n" + CATS_SCRIPT_SLEEP + elapsedTimeInSec * 1000 + "\n" + catsScript;
                setLastScriptedCatsText( catsScript );
            }
            else
            {
                output = getOutputTextFromCatsScript( catsScript );
            }
        }
        else
        {
            if ( elapsedTimeInSec > defaultKeyPressIntervel )
            {
                output = "\n" + CATS_SCRIPT_SLEEP + elapsedTimeInSec * 1000;
            }
            output = output + "\n" + catsScript;
            setLastScriptedCatsText( catsScript );
        }

        catsStartTime = System.currentTimeMillis();
        return output;
    }

    /**
     * Method to extract the remote command name from the cats script generated
     * from the scripter implementation.
     * 
     * @param catsScript
     *            cats script generated from the scripter implementation
     * @return remote command name
     */
    private String getOutputTextFromCatsScript( String catsScript )
    {

        String output = "";
        if ( catsScript.matches( DSL_PRESS_KEY_SYNTAX ) )
        {
            String command = catsScript.replace( CATS_SCRIPT_PRESS, EMPTY_STRING );
            setLastScriptedCatsText( getLastScriptedCatsText() + ", " + command );
            output = ", " + command;
        }
        else
        {
            output = "\n" + catsScript;
            setLastScriptedCatsText( catsScript );
        }
        return output;
    }

    /*
     * Gets the elapsed time
     */
    private long getElapsedTime( long startTime )
    {

        long elapsedTimeMillis = System.currentTimeMillis() - startTime;

        long elapsedTimeSec = elapsedTimeMillis / 1000;

        return elapsedTimeSec;
    }

    private String getCatsCommand( RemoteCommand remoteCommand )
    {
        String command = "";
        switch ( remoteCommand )
        {
        case ONE:
            command = "1";
            break;

        case TWO:
            command = "2";
            break;

        case THREE:
            command = "3";
            break;

        case FOUR:
            command = "4";
            break;

        case FIVE:
            command = "5";
            break;

        case SIX:
            command = "6";
            break;

        case SEVEN:
            command = "7";
            break;

        case EIGHT:
            command = "8";
            break;

        case NINE:
            command = "9";
            break;

        case ZERO:
            command = "0";
            break;

        default:
            command = remoteCommand.name();
            break;
        }
        return command;
    }
}
