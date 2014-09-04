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
package com.comcast.cats.keymanager.domain;

import javax.xml.bind.annotation.XmlRootElement;

import com.comcast.cats.service.IRHardwareEnum;

/**
 * DTO object to represent IRDevices from applications like key-manager.
 * @author skurup00c
 *
 */
@XmlRootElement
public class IrDeviceDTO
{

    IRHardwareEnum deviceType;
    String ipAdress;
    long id;
    String macAddress;
    String redRatHubIp;
    Integer redRatHubPort;
    
    public IrDeviceDTO( ){
        
    }

    public IrDeviceDTO( IRHardwareEnum deviceType,long id, String ipAdress,  String macAddress, String redRatHubIp,
    		Integer redRatHubPort)
    {
        this.deviceType = deviceType;
        this.ipAdress = ipAdress;
        this.id = id;
        this.macAddress = macAddress;
        this.redRatHubIp = redRatHubIp;
        this.redRatHubPort = redRatHubPort;
    }
    
    
    public IRHardwareEnum getDeviceType()
    {
        return deviceType;
    }
    public void setDeviceType( IRHardwareEnum deviceType )
    {
        this.deviceType = deviceType;
    }
    public String getIpAdress()
    {
        return ipAdress;
    }
    public void setIpAdress( String ipAdress )
    {
        this.ipAdress = ipAdress;
    }
    public long getId()
    {
        return id;
    }
    public void setId( long id )
    {
        this.id = id;
    }
    public String getMacAddress()
    {
        return macAddress;
    }
    public void setMacAddress( String macAddress )
    {
        this.macAddress = macAddress;
    }

	public String getRedRatHubIp() 
	{
		return redRatHubIp;
	}

	public void setRedRatHubIp(String redratHubIp) 
	{
		this.redRatHubIp = redratHubIp;
	}

	public Integer getRedRatHubPort() {
		return redRatHubPort;
	}

	public void setRedRatHubPort(Integer redRatHubPort) {
		this.redRatHubPort = redRatHubPort;
	}
}
