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
package com.comcast.cats.vision.panel.power;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.comcast.cats.provider.PowerProvider;
import com.comcast.cats.provider.PowerProviderServiceImpl;

/**
 * CATS Vision Power Panel class provides a GUI interface for power to control
 * settop power ON, OFF and Toggle/Reboot.
 * 
 * @author jtyrre001
 */
public class CVPowerPanel 
{
    /**
     * default logger.
     */
    private static final Logger logger = Logger.getLogger(CVPowerPanel.class);
    
    /**
     * Sets the frame position on the screen.
     */
    public Rectangle framePos = new Rectangle();

    /**
     * Constructor.
     * Sets the power panel initial frame coordinates.
     * Same coordinates set for for the panel location.
     */
    public CVPowerPanel() {
        framePos.x = 815;
        framePos.y = 630;
        framePos.width = 275;
        framePos.height = 75;
    }
    
    /**
     * Creates the power panel and makes some calls to test the
     * power implementation.
     */
    private JFrame showPowerPanel() {
        String powerWSDL = "http://localhost:8080/power-service/PowerService?wsdl";
        String powerURI = "wti1600://192.168.160.202:23/?outlet=2";
        PowerPanel powerPanel = instantiatePowerPanel(powerWSDL, powerURI);
        /*
         * This code is here for testing only and allows us to display 
         * the jpanel within a jframe.
         */
        JFrame frame = new JFrame("Power Panel");
        frame.setSize(640, 480);
        frame.add(powerPanel);
        frame.setLayout(new GridLayout());
        frame.pack();
        frame.setBounds(framePos);
        frame.setVisible(true);
        return frame;
    }
    
    /**
     * This method instantiates the power panel UI and privides
     * a powerProvider to the interface.
     * 
     * For the real deployable system, a power provider will be provided
     * by the settop service.   
     * 
     * @param wsdl
     * @param uri
     * @return
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public PowerPanel instantiatePowerPanel(String wsdl, String uri) {
        PowerPanel pwPanel = new PowerPanel(null, null);
        pwPanel.setVisible(true);
        pwPanel.setBounds(framePos);        
        return pwPanel;
    }

    /**
     * Creates and returns a power provider instance.
     *
     * @param wsdl              WSDL of power device
     * @param uri               URI of power device
     * @return powerProvider    Power Provider instance
     */
    public PowerProvider getNewPowerProvider(String wsdl, String uri) {
        PowerProvider powerProvider = null;

        try
        {
            powerProvider = new PowerProviderServiceImpl(wsdl, new URI(uri));
            logger.info("Instantiated a power provider");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            logger.error("Malformed URL exception");
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
            logger.error("URI syntax exception");
        }
        return powerProvider;
    }

    /**
     * Creates the power panel UI and button controls.
     * 
     * @param args
     * @throws MalformedURLException
     * @throws URISyntaxException 
     */
    public static void main( String[] args ) throws MalformedURLException, URISyntaxException {
        CVPowerPanel pp = new CVPowerPanel();
        pp.showPowerPanel();
    }
}
