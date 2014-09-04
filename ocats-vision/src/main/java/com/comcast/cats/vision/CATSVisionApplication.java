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
package com.comcast.cats.vision;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.springframework.context.ApplicationContext;

import com.comcast.cats.CatsFramework;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.vision.configuration.CatsVisionApplicationContext;
import com.comcast.cats.vision.configuration.LogConfiguration;
import com.comcast.cats.vision.util.CATSVisionLoadingDialog;

/**
 * The main class of the application.
 */
public class CATSVisionApplication extends SingleFrameApplication
{
    static Logger                logger;
    Properties                   props = new Properties();
    protected ApplicationContext context;

    /**
     * Pull out the command line arguments prior to loading the form. Properties
     * are being used so that they can be written out to disk.
     * 
     * @param args
     */
    @Override
    protected void initialize( String[] args )
    {
        Options options = new Options();

        options.addOption( "s", "server", true, "CATS server URL" );
        options.addOption( "m", "mac", true, "MAC id of Settop" );

        String arg;
        CommandLineParser parser = new PosixParser();

        try
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "Command Line Arguments", options );
            CommandLine cmd = parser.parse( options, args );

            for ( Option opt : cmd.getOptions() )
            {

                switch ( opt.getLongOpt() )
                {

                case "server":
                    arg = opt.getValue();
                    System.setProperty( CatsProperties.SERVER_URL_PROPERTY, arg );
                    break;
                case "mac":
                    arg = opt.getValue();
                    System.setProperty( CatsProperties.SETTOP_DEFAULT_PROPERTY, arg );
                    break;
                default:
                    arg = opt.getValue();
                }

            }

        }
        catch ( ParseException e )
        {
            logger.error( "Command line argument parsing error" );
            e.printStackTrace();
        }
        // Time to grab our ApplicationContext.
        setupApplicationContext();

        /*
         * Now that we have CATS_HOME established, let's setup our logging for
         * CATS Vision.
         */
        LogConfiguration logConfiguration = new LogConfiguration();
        logConfiguration.configureLogging();

        logger = Logger.getLogger( CATSVisionApplication.class );
    }

    protected void setupApplicationContext()
    {
        CatsFramework framework = new CatsFramework( new CatsVisionApplicationContext() );
        context = framework.getContext();
    }

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup()
    {
        CatsVisionController catsVisionController = context.getBean( CatsVisionController.class );
        CATSVisionView catsVisionView = new CATSVisionView( this );
        catsVisionView.setAuthToken( catsVisionController.getAuthToken() );

        catsVisionController.setCatsVisionView( catsVisionView );
        catsVisionController.addCatsVisionViewListeners();
        catsVisionController.addTabs();
        catsVisionController.fillConfigDataTables();

        show( catsVisionView );
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow( java.awt.Window root )
    {
    }

    /**
     * A convenient static getter for the application instance.
     * 
     * @return the instance of DesktopApplication1
     */
    public static CATSVisionApplication getApplication()
    {
        return Application.getInstance( CATSVisionApplication.class );
    }

    /**
     * Main method launching the application.
     */
    public static void main( String[] args )
    {
        CATSVisionLoadingDialog.showDialog();
        launch( CATSVisionApplication.class, args );
    }

}
