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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.RemoteProviderServiceImpl;
import com.comcast.cats.service.IRService;
import com.comcast.cats.service.IRServiceEndpoint;

public class RemoteApplication { 
	/**
	 * JFrame object.
	 */
	JFrame mainFrame = null;

	/**
     * Sets the frame position on the screen.
     */
    public Rectangle framePos = new Rectangle();

    /**
     * RemoteApplication object.
     */
	static RemoteApplication ra;
	//Setup some default IR Service parameters.
	String server = "http://localhost:8080/ir-service/IRService?wsdl";
	String irPath = "gc100://192.168.160.202/?port=2";
	String keySet = "COMCAST";
	
	/**
     * Sets the frame position on the screen.
     */
	public RemoteApplication(String [] args) {
	    parseCommandLineArgs(args);

		framePos.x = 800;
        framePos.y = 100;
        framePos.width = 300;
        framePos.height = 450;
	}
	
	/**
	 * Logger instance for VideoSourcePanel.
	 */
	private static final Logger logger = Logger
			.getLogger(RemoteApplication.class);
	
	public void parseCommandLineArgs(String[] args) {
		
		String arg;
		
		
	       Options options = new Options();

	        options.addOption( "s", "server", true, "CATS server URL" );
	        options.addOption( "i", "irPath", true, "IR path off settop" );
	        options.addOption( "k", "keyset", true, "key set" );
		
		
		
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
	                    arg =  opt.getValue();
	                    logger.debug("Found mac address: " + arg);
	                    server = arg;
	                break;
	                case "irPath":
	                    arg =  opt.getValue();
	                    logger.debug("Found Server address: " + arg);
	                    irPath = arg;
	                break;
	                case "keyset":
	                    arg = opt.getValue();
	                    logger.debug("Found login endpoint address: " + arg);
	                    keySet = arg;
	                break;
	                default:
	                    arg = opt.getValue();
	                    logger.debug("Argument not found..." + arg);
	                }

	            }

	        }
	        catch ( ParseException e )
	        {
	            logger.error( "Command line argument parsing error" );
	            e.printStackTrace();
	        }
		
	
		
		
	}
	
	public RemoteControlView startup() throws MalformedURLException {
		RemoteController remoteController;
	    RemoteControlView view;
		RemoteProvider remoteProvider = new RemoteProviderEmpty();
		IRServiceEndpoint endpoint = new IRServiceEndpoint(new URL(server));
		IRService irService = endpoint.getIRServiceImplPort();
        try {
			remoteProvider = new RemoteProviderServiceImpl(
					irService,	new URI(irPath), keySet);
		} catch (URISyntaxException ex) {
			logger.error(ex);
		} catch (Exception ex) {
			logger.error(ex);
		}
        remoteController = new RemoteController(null,null);
		view=remoteController.getRemote();
		view.setBounds(framePos);
		view.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		return view;
	}
	
	public JFrame showRemotePanel() throws MalformedURLException {
		RemoteControlView remotePanel = ra.startup();
	    
	    mainFrame = new JFrame("Test RemotePanel");
        mainFrame.add(remotePanel);
        mainFrame.setPreferredSize(new Dimension(300,450));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setBounds(framePos);
        mainFrame.setVisible(true);
        return mainFrame;
	}
	
	public static void main(String[] args) throws MalformedURLException {
		
	    ra = new RemoteApplication(args);
	    ra.showRemotePanel();
    }
}
