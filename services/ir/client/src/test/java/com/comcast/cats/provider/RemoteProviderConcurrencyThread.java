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
package com.comcast.cats.provider;

import static com.comcast.cats.RemoteCommand.*;
import com.comcast.cats.RemoteCommand;

/**
 * The Class RemoteProviderConcurrencyThread.
 * 
 * @Author :
 * @since : Description : RemoteProviderConcurrencyThread is a Helper class for  the test RemoteProviderConcurrencyIT
 */
public class RemoteProviderConcurrencyThread extends Thread
{
    private RemoteProvider remote;
    public static int      DELAY            = 250;
    public static int      LOOPS            = 20;
    public static int      OUTER_LOOPS      = 10;
    // Minimum and maxim delay time in minutes between loops.
    public static Integer  MIN              = 0;
    public static Integer  MAX              = 1;
    public static Integer  MILLS_PER_MINUTE = 60 * 1000;
    public Integer         errors           = 0;
    private Integer        keyCount         = 0;
    private Long           totalTime        = 0L;

    public RemoteProviderConcurrencyThread( RemoteProvider remote )
    {
        this.remote = remote;
        this.remote.setAutoTuneEnabled( false );
    }

    public Integer getDelayInMillis()
    {
        Integer delay = MIN + ( int ) ( Math.random() * ( ( MAX - MIN ) + 1 ) );
        return ( delay * MILLS_PER_MINUTE );
    }

    @Override
    public void run()
    {
        Integer delay;
        for ( int outer = 0; outer < OUTER_LOOPS; outer++ )
        {
            delay = getDelayInMillis();
            for ( int i = 0; i < LOOPS; i++ )
            {
                script();
            }
            System.out.println( "===== Outer loop for " + remote.getRemoteLocator() + " =======" );
            System.out.println( "Keys " + keyCount + " : " + "Avg Processing Time = " + totalTime / keyCount + " : "
                    + "Errors = " + errors );

            if ( outer + 1 != OUTER_LOOPS )
            {
                System.out.println( "Delaying for " + delay + "milliseconds" );
                sleep( delay );
            }

            System.out.println( "===== Outer loop for " + remote.getRemoteLocator() + " End =======" );
        }
    }

    protected void sleep()
    {
        sleep( DELAY );
    }

    protected void sleep( int delay )
    {
        try
        {
            Thread.sleep( delay );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    protected void press( RemoteCommand command )
    {
        Long keyStart = System.currentTimeMillis();
        boolean rtn = remote.pressKey( command );
        Long keySendTime = System.currentTimeMillis() - keyStart;
        if ( !rtn )
        {
            System.out.println( "Failed to send key[" + command + "] + " + " to " + remote.getRemoteLocator() + " in "
                    + keySendTime + " ms" );
            errors++;
        }
        totalTime += keySendTime;
        keyCount++;
        sleep( DELAY );
    }

    public void script()
    {
        press( EXIT );
        press( ONE );
        press( ZERO );
        sleep();
        // Provide a nice loop that should return us to where we started.
        press( GUIDE );
        press( DOWN );
        press( DOWN );
        press( DOWN );
        press( UP );
        press( UP );
        press( UP );
        press( SELECT );
        press( EXIT );
    }
}
