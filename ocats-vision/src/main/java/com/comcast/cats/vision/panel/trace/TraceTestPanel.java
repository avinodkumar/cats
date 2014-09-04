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
package com.comcast.cats.vision.panel.trace;

import java.util.Properties;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.comcast.cats.event.impl.CatsEventDispatcherImpl;
import com.comcast.cats.event.impl.TraceEventDispatcherImpl;

/**
 * @author aswathyann
 * 
 */
public class TraceTestPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    /**
     * Logger.
     */
    private static final Logger        logger = Logger.getLogger( TraceTestPanel.class );

    String                             path   = "traceserver://192.168.160.202/";
    String                             mac    = "00:15:9A:B7:E8:D6";
    AnnotationConfigApplicationContext ctx;

    Properties                         props  = new Properties();
    Properties                         giTraceProperties;

    /**
	 * 
	 */
    public TraceTestPanel()
    {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register( CatsEventDispatcherImpl.class );
        ctx.register( TraceEventDispatcherImpl.class );
        ctx.refresh();
        /*
         * }
         * 
         * (non-Javadoc)
         * 
         * @see org.jdesktop.application.Application#startup()
         * 
         * 
         * protected void startup() {
         */
        //setTitle( "Settop Trace" );
        setSize( 650, 850 );
        TracePanel tracePanel = new TracePanel( mac );

        // show(tracePanel);
        add( tracePanel );
        //tracePanel.startTrace();
        setVisible( true );
    }

    /**
     * Main method launching the application.
     */
    public static void main( String[] args )
    {
        logger.debug( "Launch Application" );
        // launch(TraceApplication.class, args);
        //new TraceDialog().setVisible( true );

    }
}
