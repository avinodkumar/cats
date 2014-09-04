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
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceConstants;
import com.comcast.cats.service.PowerStatistics;
import com.comcast.cats.service.power.PowerServiceImpl;
import com.comcast.cats.service.PowerServiceVersionGetter;
/**
 * A simple EJB service that can send power commands to a STB based on the path to the power device.
 */

@Remote(PowerService.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@WebService(name=PowerServiceConstants.SERVICE_NAME,
		endpointInterface=PowerServiceConstants.ENDPOINT_INTERFACE,
		targetNamespace=PowerServiceConstants.NAMESPACE)
public class PowerServiceWSImpl extends PowerServiceImpl
{
	
    @EJB
    private PowerControllerDeviceFactory powerControllerFactory;
	
    @EJB (name="PowerServiceVersionGetter")
    PowerServiceVersionGetter versionGetter;
	  
	/**
     * use the path provided to create instances of the power control devices.
     * 
     * @param path
     *            The address of the power control devices
     * @return A power control device
     */
    protected  PowerControllerDevice createDevice( final URI path )
    {
    	log.info("\n\n Inside PowerControllerWSImpl createDevice()"+powerControllerFactory);
    	PowerControllerDevice pwrControllerDevice = null;
    	if (powerControllerFactory == null)
    	{
    		log.error("\n\n powerControllerFactory was not injected \n\n");//Logs the details and returns null PowerControllerDevice
    	} 
    	else 
    	{
    	    pwrControllerDevice = powerControllerFactory.createPowerControllerDevice( path );
    	}
        return pwrControllerDevice;
    }
    
    @Override
    public List<PowerInfo> getAllPowerDevicesInfo(){
		return powerControllerFactory.getAllPowerDevicesInfo();
	}
        
    @Override
    public List<PowerStatistics> getPowerStatisticsPerDevice(final String ip) {
    	
    	List<PowerStatistics> powerStatisticsList = null;
    	PowerInfo powerInfo = getPowerInfo(ip);
    	if (powerInfo != null) {
            powerStatisticsList = powerInfo.getPowerStatistics();
		}
		return powerStatisticsList;
	}
		
    @Override
	public PowerStatistics getPowerOutletStatistics(final String ip, final int outlet) {
		PowerStatistics powerStatistics = null;
		PowerInfo powerInfo = getPowerInfo(ip);
		if(powerInfo != null){
		    powerStatistics = powerInfo.getPowerStatisticsAt(outlet);
		}
		return powerStatistics;
	}
    
    @Override
    public void removePowerDevice(String ip){
    	//Remove from powerControllerMap and powerInfoList
    	powerControllerFactory.removePowerDevice(ip);
    }
	
	@Override
	public String getVersion() {
		return versionGetter.getVersion();
	}
    
    private PowerInfo getPowerInfo(String ip){
		List<PowerInfo> powerInfoList = powerControllerFactory.getAllPowerDevicesInfo();
		for(PowerInfo powerInfo: powerInfoList ){
			if(powerInfo.getIp().contains(ip)){
			    return powerInfo;	
			}
		}
		return null;
    }
	
	
}
