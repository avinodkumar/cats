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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.comcast.cats.provider.exceptions.PowerProviderException;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceEndpoint;

/*
 * just update your IMPL class to add functionality for 
 * setting a parent object.
 * 
 */

public class PowerProviderServiceImpl implements PowerProvider {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private URI pwPath;
    private PowerService pwService;
    private Object parent;
    
	public PowerProviderServiceImpl(String serviceWsdl, URI powerPath) throws MalformedURLException {
		PowerServiceEndpoint powerServiceEndPoint = new PowerServiceEndpoint(new URL(serviceWsdl));
		pwService = powerServiceEndPoint.getPowerServiceImplPort();
		assignParams(powerPath);
	}

    public PowerProviderServiceImpl(PowerServiceEndpoint powerServiceEndpoint, URI powerPath) {
        pwService = powerServiceEndpoint.getPowerServiceImplPort();
        assignParams(powerPath);
    }
    
    public PowerProviderServiceImpl(PowerService pwService, URI powerPath) {
        this.pwService = pwService;
        assignParams(powerPath);
    }

    /**
     * Helper method to assign parameters common for all constructors.
     * @param pwPath
     */
    private void assignParams(URI powerPath) {
        this.pwPath = powerPath;
    }

    public void setPowerLocator(URI powerPath) {
        this.pwPath = powerPath;
    }
    
    //@Override
    public URI getPowerLocator()
    {
        return pwPath;
    }

    //@Override
    public void powerOff() throws PowerProviderException
    {
        boolean ret= pwService.hardPowerOff(pwPath);
        if(!ret){
        	throw new PowerProviderException("Power off operation failed");
        }
    }

    //@Override
    public void powerOn() throws PowerProviderException
    {
    	boolean ret=  pwService.hardPowerOn(pwPath);
        if(!ret){
        	throw new PowerProviderException("Power On operation failed");
        }
    }

    //@Override
    public void reboot() throws PowerProviderException
    {
    	boolean ret=  pwService.hardPowerToggle(pwPath);
        if(!ret){
        	throw new PowerProviderException("Reboot operation failed");
        }
    }
    
    //@Override
    public String getPowerStatus()
    {
        return pwService.powerStatus(pwPath);
    }
    
    public void setParent(Object parent) {
        this.parent = parent;
    }
    
    //@Override
    public Object getParent() {
        return parent;
    }
  }
