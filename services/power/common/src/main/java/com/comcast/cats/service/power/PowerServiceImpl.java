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
package com.comcast.cats.service.power;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerStatistics;


/**
 * a simple EJB service that can affect the power to a STB based on the path to
 * the power control device
 * 
 */

public class PowerServiceImpl implements PowerService
{
	protected final Logger log = LoggerFactory
	         .getLogger(PowerServiceImpl.class);

	/**
     * Regex to parse the outlet number from a URL.
     */
    private static final Pattern OUTLET_PATTERN = Pattern.compile( ".*port=([0-9]+).*" );
	
	
    /**
     * Return the Power URI for a settop. TBD
     * 
     * @return URL of a settop's power strip
     */
    public String powerLocator()
    {
        String powerLocatorURI = null;
        return powerLocatorURI;
    }
    

    /**
     * Communicate with the power strip to determine if it is powered on or off.
     * 
     * @param path
     *            The path to the power control device
     * @return The power state returned by the WTI or Netboost power strips.
     */
    public String powerStatus( URI path )
    {
        String outletOn = createDevice( path ).getOutletStatus( parseOutlet( path ) );
        return outletOn;
    }

    /**
     * Communicate with the power strip tell the it to switch off the outlet.
     * 
     * @param path
     *            The path to the power control device
     */
    public boolean hardPowerOff( final URI path )
    {
        return createDevice( path ).powerOff( parseOutlet( path ) );
    }

    /**
     * Communicate with the power strip tell the it to switch on the outlet.
     * 
     * @param path
     *            The path to the power control device
     */
    public boolean hardPowerOn( final URI path )
    {
        return createDevice( path ).powerOn( parseOutlet( path ) );
    }

    /**
     * Communicate with the power strip tell the it to toggle the outlet. The
     * outlet should toggle off then on.
     * 
     * @param path
     *            The path to the power control device
     */
    public boolean hardPowerToggle( final URI path )
    {
        return createDevice( path ).powerToggle( parseOutlet( path ) );
    }

    /**
     * use the path provided to create instances of the power control devices.
     * 
     * @param path
     *            The address of the power control devices
     * @return A power control device
     */
    protected  PowerControllerDevice createDevice( final URI path )
    {
    	log.error("createDevice() in PowerServiceImpl should never get invoked..");
    	return null;
    }

    /**
     * Parse the path to find the output to use.
     * 
     * @param path
     *            The path to parse
     * @return The outlet requested
     */
    private int parseOutlet( final URI path )
    {
        final Matcher m = OUTLET_PATTERN.matcher( path.getQuery() );
        if ( !m.find() )
        {
            throw new IllegalArgumentException( "The power outlet must be specified" );
        }
        final String outletStr = m.group( 1 );
        return Integer.parseInt( outletStr );
    }

	public List<PowerInfo>  getAllPowerDevicesInfo(){
		log.error("getAllPowerDevicesInfo in PowerServiceImpl should never get invoked..");
		return null;
	}
	
	public List<PowerStatistics> getPowerStatisticsPerDevice(final String ip) {
		log.error("getPowerStatisticsPerDevice in PowerServiceImpl should never get invoked..");
		return null;
	}
	
	public PowerStatistics getPowerOutletStatistics(final String ip, final int outlet) {
		log.error("getPowerOuletStatistics in PowerServiceImpl should never get invoked..");
		return null;
	}
	
	public void removePowerDevice(String ip){
		log.error("removePowerDevice in PowerServiceImpl should never get invoked..");
	}
	
	@Override
	public String getVersion() {
		log.error("getVersion in PowerServiceImpl should never get invoked..");
		return null;
	}
}
