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
package com.comcast.cats.vision.panel.remote;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
//import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class CounterPanel extends JPanel
{

    private static final long   serialVersionUID       = -4528967150282172311L;
    private Counter             counter;

    public static final int     COUNT_MODE             = 1;
    public static final int     TIMER_MODE             = 2;
    public static final int     ALL_MODE               = 3;

    private static final int    PANEL_WIDTH_ALL_MODE   = 165;
    private static final int    PANEL_WIDTH_COUNT_MODE = 110;
    private static final int    PANEL_WIDTH_TIMER_MODE = 60;
    private static final int    PANEL_HEIGHT           = 30;
    private static final String BACKGROUND_PATH        = "/images/stopWatch.png";
    private ImageIcon           imageIcon;

    /**
     * Logger instance for HoldCounterPanel.
     */
    private static final Logger logger                 = Logger.getLogger( CounterPanel.class );

    public CounterPanel()
    {
        initComponents();
    }

    private void initComponents()
    {
        setLayout( null );
        setPreferredSize( new Dimension( PANEL_WIDTH_ALL_MODE, PANEL_HEIGHT ) );
        this.setVisible( false );
        URL url = this.getClass().getResource( BACKGROUND_PATH );
        imageIcon = new ImageIcon( url );
    }

    public void showCounterPanel( int mode )
    {
        counter = new Counter();
        counter.setMode( mode );
        counter.startCounter();
    }

    public void hideCounterPanel()
    {
        counter.stopCounter();;
    }

    private class Counter extends Thread
    {
        private int                    mode;
        private boolean                runCounter    = false;
        private int                    count          = 0;
        private final Color            FONT_COLOR     = Color.green;                                   // new
        // Color(23,65,96);
        private final Font             FONT           = new Font( "Serif", Font.BOLD, 18 );
        private final String           TIMER_FORMAT   = "ss.S";
        private final int              HEIGHT_OFFSET  = 22;
        private final int              COUNT_X_OFFSET = 18;
        private final int              TIMER_X_OFFSET = 110;
        private final int              X_OFFSET       = 15;
        private final String           TIME_UNIT      = "s";
        private final SimpleDateFormat timerFormat    = new java.text.SimpleDateFormat( TIMER_FORMAT );
        DecimalFormat                  df             = new DecimalFormat( "#.#" );
        private final String           COUNT_UNIT     = " repeats";
        int width = 0;
        int height = 0;

        public void setMode( int mode )
        {
            logger.debug( "Mode set to " + mode );
            stopCounter();
            this.mode = mode;
        	height = PANEL_HEIGHT;
            if ( mode == ALL_MODE )
            {
            	width = PANEL_WIDTH_ALL_MODE;
            }
            else if ( mode == COUNT_MODE )
            {
            	width = PANEL_WIDTH_COUNT_MODE;
            }
            else if ( mode == TIMER_MODE )
            {
            	width = PANEL_WIDTH_TIMER_MODE;
            }
            setSize(width,height);
            logger.debug( "PressAndHold Panel size " + getSize() );
        }

        public void startCounter()
        {
            logger.debug( "Counter started " );
            count = 0;
            this.start();
            runCounter = true;
        }

        public void stopCounter()
        {
            logger.debug( "Counter stopped" );
            runCounter = false;
        }

        public void run()
        {
            Graphics g = getGraphics();
            g.setFont( FONT );
            g.setColor( FONT_COLOR );
            long startTime = System.currentTimeMillis();

            while ( runCounter )
            {
                if ( ( System.currentTimeMillis() - startTime ) > RemoteController.DELAY )
                {
                	g.drawImage( imageIcon.getImage(), 0, 0, width, height, null );
                    if ( mode == ALL_MODE )
                    {
                        g.drawString( count + COUNT_UNIT, COUNT_X_OFFSET, HEIGHT_OFFSET );
                        g.drawString( getTimeString( startTime ), TIMER_X_OFFSET, HEIGHT_OFFSET );
                    }
                    else if ( mode == TIMER_MODE )
                    {
                        g.drawString( getTimeString( startTime ), X_OFFSET, HEIGHT_OFFSET );
                    }
                    else if ( mode == COUNT_MODE )
                    {
                        g.drawString( count + COUNT_UNIT, X_OFFSET, HEIGHT_OFFSET );
                    }
                }
                count++;
                try
                {
                    sleep( RemoteController.DIVISION_FACTOR );
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }

            }
        }

        private String getTimeString( long startTime )
        {
            long timeElapsed = System.currentTimeMillis() - startTime;
            String displayString = timerFormat.format( new java.util.Date( timeElapsed ) );
            displayString = df.format( Float.parseFloat( displayString ) );
            displayString += TIME_UNIT;
            return displayString;
        }
    }

/*    public static void main( String[] args )
    {
        CounterPanel rem = new CounterPanel();
        JFrame frame = new JFrame();
        frame.add( rem );
        frame.setLayout( null );
        frame.setSize( 300, 300 );
        frame.setVisible( true );
        rem.setLocation( 20, 20 );
        rem.showCounterPanel( COUNT_MODE );
    }*/

}
