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

import org.apache.log4j.Logger;

import com.comcast.cats.Settop;
import com.comcast.cats.event.impl.AbstractManagedTask;
import com.comcast.cats.provider.VideoProvider;

/**
 * Task that sets the frame rate of settop video. The execution of this class
 * will be handled by ManagedThreads.
 * 
 * @author minu
 * 
 */
public class SetVideoFrameRateTask extends AbstractManagedTask
{
    private Settop        settop;
    private int           frameRate;
    private static Logger logger = Logger.getLogger( SetVideoFrameRateTask.class );

    public SetVideoFrameRateTask( Settop settop, int frameRate )
    {
        this.settop = settop;
        this.frameRate = frameRate;
    }

    @Override
    public void run()
    {
        if ( settop != null && settop.getVideo() != null )
        {
            VideoProvider settopVideo = settop.getVideo();
            settopVideo.setFrameRate( frameRate );
            if ( logger.isDebugEnabled() )
            {
                logger.debug( "Video frame rate set to: " + frameRate + " for settop: " + settop.getHostMacAddress()
                        + " - " + settopVideo.getVideoURL() );
            }
        }
    }

    @Override
    public Object getIdentifier()
    {
        return settop;
    }
}
