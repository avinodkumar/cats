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
package com.comcast.cats.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.comcast.cats.RemoteCommand;

/**
 * The Class IRnetBoxProStressTest.
 * 
 * @Author :aswathyann
 * @since :
 * @Description : Stress test for RedRat IR device.
 */

public class IRNetBoxProStressTest
{
    private Logger              logger            = LoggerFactory.getLogger( IRNetBoxProStressTest.class );

    private static final String endPointStr       = "http://192.168.160.201:8080/ir-service/IRService?wsdl";

    private String              type              = "irnetboxpro3";
    private String              keySet            = "COMCAST";

    private static final long   FIVE_MINS         = 1000 * 60 * 5;
    private static final int    PORTS             = 16;
    private static final int    IR_NET_BOXES      = 9;
    private static final long   TOTAL_INVOCATIONS = 100 * 24 * 7;

    private static final String IRNET_BOX_16      = "192.168.160.202";
    private static final String IRNET_BOX_17      = "192.168.160.203";
    private static final String IRNET_BOX_18      = "192.168.160.204";
    private static final String IRNET_BOX_19      = "192.168.160.205";
    private static final String IRNET_BOX_20      = "192.168.160.206";
    private static final String IRNET_BOX_21      = "192.168.160.207";
    private static final String IRNET_BOX_22      = "192.168.160.208";
    private static final String IRNET_BOX_23      = "192.168.160.209";
    private static final String IRNET_BOX_24      = "192.168.160.210";

    private static int[][]      failureCountArray = new int[ IR_NET_BOXES ][ PORTS ];

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box16( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {

        performKeyPressTask( IRNET_BOX_16, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box17( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {

        performKeyPressTask( IRNET_BOX_17, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box18( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {

        performKeyPressTask( IRNET_BOX_18, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box19( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {

        performKeyPressTask( IRNET_BOX_19, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box20( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {
        performKeyPressTask( IRNET_BOX_20, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box21( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {
        performKeyPressTask( IRNET_BOX_21, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box22( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {
        performKeyPressTask( IRNET_BOX_22, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box23( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {
        performKeyPressTask( IRNET_BOX_23, port );
    }

    @Test( dataProvider = "port" )
    public void testIRnetBoxPro3Box24( int port ) throws MalformedURLException, URISyntaxException,
            InterruptedException
    {
        performKeyPressTask( IRNET_BOX_24, port );
    }

    private void performKeyPressTask( String irNetBox, int port ) throws URISyntaxException, MalformedURLException,
            InterruptedException
    {
        Date oldDate = new Date();
        int invocationCount = 0;

        for ( int i = 0; i < TOTAL_INVOCATIONS; i++ )
        {
            Date newDate = new Date();

            long timeInterval = getTimeInterval( newDate, oldDate );

            Thread.sleep( 500 );

            sendIRCommand( ++invocationCount, timeInterval, irNetBox, port, RemoteCommand.GUIDE );

            Thread.sleep( 500 );

            sendIRCommand( ++invocationCount, timeInterval, irNetBox, port, RemoteCommand.EXIT );

            // Reset oldDate value
            if ( isTimeIntervalGreaterThanFiveMins( timeInterval ) )
            {
                oldDate = new Date();
            }
        }

        int totalFailureCount = getFailureCount( irNetBox, port );

        if ( totalFailureCount > 0 )
        {

            System.out.println( " irNetBox = " + irNetBox + ",  failed at port = " + port + " & Total Failure count = "
                    + getFailureCount( irNetBox, port ) + " Total invocation count = " + invocationCount );
        }

        Assert.assertFalse( ( totalFailureCount > 0 ), " irNetBox = " + irNetBox + ",  failed at port = " + port
                + " & Total Failure count = " + getFailureCount( irNetBox, port ) + " Total invocation count = "
                + invocationCount );
    }

    private void sendIRCommand( int invocationCount, long timeInterval, String irNetBox, int port,
            RemoteCommand remoteCommand ) throws MalformedURLException, URISyntaxException
    {

        Service srv = Service.create( new URL( endPointStr ), new QName( IRServiceConstants.NAMESPACE,
                IRServiceConstants.IMPL_STRING ) );

        IRService irService = srv.getPort( IRService.class );

        URI path = new URI( type + "://" + irNetBox + "/?port=" + port );

        if ( null == irService )
        {
            logger.info( "irService is null." );
        }
        Assert.assertNotNull( irService, "irService is null." );

        /*
         * If pressKey() fails, then increment failure count and log the status
         * in every 5 minutes.
         */

        if ( !irService.pressKey( path, keySet, remoteCommand ) )

        {
            incrementFailureCount( irNetBox, port );

            if ( isTimeIntervalGreaterThanFiveMins( timeInterval ) )
            {
                logStatus( irNetBox, port, invocationCount );
            }
        }
    }

    private void logStatus( String irNetBox, int port, int invocationCount )
    {

        System.out.println( " irNetBox = " + irNetBox + ",  failed at port = " + port + " & Total Failure count = "
                + getFailureCount( irNetBox, port ) + " Total invocation count = " + invocationCount );

    }

    /*
     * Check if time interval is greater than 5 mins
     */
    private boolean isTimeIntervalGreaterThanFiveMins( long diff )
    {
        if ( diff > FIVE_MINS )
        {
            return true;
        }
        return false;
    }

    private long getTimeInterval( Date latestDate, Date oldDate )
    {
        return latestDate.getTime() - oldDate.getTime();

    }

    private void incrementFailureCount( String irNetBox, int port )
    {
        switch ( irNetBox )
        {
        case IRNET_BOX_16:
            incrementArray( 0, port - 1 );
            break;
        case IRNET_BOX_17:
            incrementArray( 1, port - 1 );
            break;
        case IRNET_BOX_18:
            incrementArray( 2, port - 1 );
            break;
        case IRNET_BOX_19:
            incrementArray( 3, port - 1 );
            break;
        case IRNET_BOX_20:
            incrementArray( 4, port - 1 );
            break;
        case IRNET_BOX_21:
            incrementArray( 5, port - 1 );
            break;
        case IRNET_BOX_22:
            incrementArray( 6, port - 1 );
            break;
        case IRNET_BOX_23:
            incrementArray( 7, port - 1 );
            break;
        case IRNET_BOX_24:
            incrementArray( 8, port - 1 );
            break;

        }
    }

    private int getFailureCount( String irNetBox, int port )
    {
        int failureCount = 0;
        switch ( irNetBox )
        {
        case IRNET_BOX_16:
            failureCount = getFailureCountValue( 0, port - 1 );
            break;
        case IRNET_BOX_17:
            failureCount = getFailureCountValue( 1, port - 1 );
            break;
        case IRNET_BOX_18:
            failureCount = getFailureCountValue( 2, port - 1 );
            break;
        case IRNET_BOX_19:
            failureCount = getFailureCountValue( 3, port - 1 );
            break;
        case IRNET_BOX_20:
            failureCount = getFailureCountValue( 4, port - 1 );
            break;
        case IRNET_BOX_21:
            failureCount = getFailureCountValue( 5, port - 1 );
            break;
        case IRNET_BOX_22:
            failureCount = getFailureCountValue( 6, port - 1 );
            break;
        case IRNET_BOX_23:
            failureCount = getFailureCountValue( 7, port - 1 );
            break;
        case IRNET_BOX_24:
            failureCount = getFailureCountValue( 8, port - 1 );
            break;

        }
        return failureCount;
    }

    private int getFailureCountValue( int boxNo, int port )
    {
        return failureCountArray[ boxNo ][ port ];

    }

    private void incrementArray( int boxNo, int port )
    {
        int failureCount = failureCountArray[ boxNo ][ port ];
        failureCountArray[ boxNo ][ port ] = ++failureCount;
    }

    @DataProvider( name = "port", parallel = true )
    public Object[][] irNetBoxPorts()
    {

        return new Object[][]
            { new Object[]
                { new Integer( 1 ) }, new Object[]
                { new Integer( 2 ) }, new Object[]
                { new Integer( 3 ) }, new Object[]
                { new Integer( 4 ) }, new Object[]
                { new Integer( 5 ) }, new Object[]
                { new Integer( 6 ) }, new Object[]
                { new Integer( 7 ) }, new Object[]
                { new Integer( 8 ) }, new Object[]
                { new Integer( 9 ) }, new Object[]
                { new Integer( 10 ) }, new Object[]
                { new Integer( 11 ) }, new Object[]
                { new Integer( 12 ) }, new Object[]
                { new Integer( 13 ) }, new Object[]
                { new Integer( 14 ) }, new Object[]
                { new Integer( 15 ) }, new Object[]
                { new Integer( 16 ) },

            };
    }

}
