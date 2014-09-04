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
package com.comcast.cats.service.impl;

import java.net.URI;
import com.comcast.cats.RemoteCommand;
import org.slf4j.MarkerFactory;
import org.slf4j.LoggerFactory;
import com.comcast.cats.service.IRService;

/**
 * The Class SimpleIRThread.
 * 
 * @Author : cfrede001
 * @since :
 * @Description :
 */
public class SimpleIRThread extends Thread
{
    private IRService service;
    private URI       path;
    private String    stbModel;
    private long      count;
    private long      total;

    public void sendKey( IRService service, URI path, String model, RemoteCommand command )
    {
        long start = System.currentTimeMillis();
        service.pressKey( path, model, command );
        long end = System.currentTimeMillis();
        total += ( end - start );
        count++;
        System.out.println( "sendKey Difference " + Long.toString( end - start ) + " URI = " + path.toString() );
    }

    public void tune( IRService service, URI path, String model, String channel )
    {
        long start = System.currentTimeMillis();
        service.tune( path, model, channel, false, 0 );
        long end = System.currentTimeMillis();
        total += ( end - start );
        count++;
        System.out.println( "tune Difference " + Long.toString( end - start ) );
    }

    @Override
    public void run()
    {
        try
        {
            int totalLoops = 10;
            Thread.sleep( 0 );
            int i = 0;
            do
            {
                tune( service, path, stbModel, "25" );
                Thread.sleep( 3000 );
                tune( service, path, stbModel, "37" );
                i++;
            } while ( i < totalLoops );
        }
        catch ( InterruptedException ex )
        {
            LoggerFactory.getLogger(SimpleIRThread.class.getName()).error(MarkerFactory.getMarker("SEVERE"), null, ex);
        }
    }

    public SimpleIRThread( IRService service, String stbModel, URI path )
    {
        this.service = service;
        this.path = path;
        this.stbModel = stbModel;
    }

    public URI getPath()
    {
        return path;
    }

    public void setPath( URI path )
    {
        this.path = path;
    }

    public IRService getService()
    {
        return service;
    }

    public void setService( IRService service )
    {
        this.service = service;
    }

    public String getStbModel()
    {
        return stbModel;
    }

    public void setStbModel( String stbModel )
    {
        this.stbModel = stbModel;
    }

    public void printStats()
    {
        System.out.println( "========== " + path.toString() + " ================" );
        System.out.println( "  Total time = " + Long.toString( total ) );
        System.out.println( " Total count = " + Long.toString( count ) );
        System.out.println( "Average time = " + Long.toString( total / count ) );
        System.out.println( "============================================================" );
    }
}
