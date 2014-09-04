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
package com.comcast.cats.vision.task;

import groovy.lang.Binding;

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.event.CatsEventType;
import com.comcast.cats.event.impl.AbstractManagedTask;
import com.comcast.cats.provider.impl.ImageCompareProviderImpl;
import com.comcast.cats.script.SettopBinding;
import com.comcast.cats.script.playback.ScriptPlayer;
import com.comcast.cats.script.playback.ScriptPlayerImpl;
import com.comcast.cats.script.playback.exceptions.ScriptPlaybackException;
import com.comcast.cats.vision.event.ScriptPlayBackEvent;
import com.comcast.cats.vision.event.ScriptPlayBackResponseEvent;

/**
 * The task which creates binding and doing the script evaluation for a settop.
 * The execution of this class will be handled by ManagedThreads.
 * 
 * @author minu
 * 
 */
public class ScriptPlayBackTask extends AbstractManagedTask
{

    private static Logger       logger = Logger.getLogger( ScriptPlayBackTask.class );

    private Settop              settop;

    private ScriptPlayBackEvent playBackEvent;

    public ScriptPlayBackTask( Settop settop, ScriptPlayBackEvent playBackEvent )
    {
        this.settop = settop;
        this.playBackEvent = playBackEvent;
        responseEvent =  new ScriptPlayBackResponseEvent();
    }

    @Override
    public void run()
    {
        Binding binding;
        ScriptPlayer scriptPlayer;
        try
        {
           // binding = new SettopBinding( settop );
            try{
            ((ImageCompareProviderImpl)settop.getImageCompareProvider()).setMatchPercent( 100.0f );
            }catch(Exception e){
                // dont wont to break this since this is a workaround
            }
            scriptPlayer = new ScriptPlayerImpl( settop );
            boolean playBackResult = scriptPlayer.playBackScript( playBackEvent.getScript() );

            responseEvent.setType( CatsEventType.SCRIPT_PLAY_BACK_RESPONSE );

            if ( playBackResult )
            {
                responseEvent.setMessage( "Playback completed successfully for settop: " + settop.getHostMacAddress() );
                logger.info( "Playback completed successfully for settop: " + settop.getHostMacAddress() );
            }
            else
            {
                responseEvent.setMessage( "Error in script playback for settop: " + settop.getHostMacAddress() );
                logger.error( "Error in script playback for settop: " + settop.getHostMacAddress() );
            }
        }
        catch ( ScriptPlaybackException e )
        {
            responseEvent.setType( CatsEventType.SCRIPT_PLAY_BACK_RESPONSE );
            responseEvent.setMessage( "Error in script playback for settop - " + settop.getHostMacAddress()
                    + ". Invalid script. \n " + e.getMessage() );

            logger.error( "Playback exception for settop - " + settop.getHostMacAddress() + ": " + e.getMessage() );
        }
        finally
        {
            binding = null;
            scriptPlayer = null;
        }
    }

    @Override
    public Object getIdentifier()
    {
        return settop;
    }
}
