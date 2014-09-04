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
package com.comcast.cats.service.ir.redrat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.service.IrDevice;
import com.comcast.cats.service.IrPort;

/**
 * This class represents an abstract for all RedRat devices.
 * 
 * @author skurup00c
 * 
 */
public abstract class RedRatDevicePort implements IrPort
{
    /**
     * RedRat device ID.
     */
    int          portNumber;

    /**
     * The redrat device this port belongs to.
     */
    RedRatDevice redratDevice;
    
    private static final Logger logger = LoggerFactory.getLogger( RedRatDevicePort.class );

    public RedRatDevicePort( int portNumber, RedRatDevice redratDevice )
    {
        this.portNumber = portNumber;
        this.redratDevice = redratDevice;
    }

    @Override
    public int getPortNumber()
    {
        return portNumber;
    }

    @Override
    public IrDevice getIrDevice()
    {
        return redratDevice;
    }

    @Override
    public boolean equals( Object irPort )
    {
        boolean isEqual = false;

        if ( irPort != null && irPort instanceof RedRatDevicePort )
        {
            RedRatDevicePort rrPort = ( RedRatDevicePort ) irPort;
            if ( rrPort.getIrDevice() != null && rrPort.getIrDevice().equals( this.getIrDevice() ) )
            {
                if ( rrPort.getPortNumber() == this.portNumber )
                {
                    isEqual = true;
                }
            }
        }
        logger.debug( "isEqual: irPort "+irPort+" : this : "+this+" equals? "+isEqual );
        return isEqual;
    }

    @Override
    public int hashCode()
    {
        // all ports of this device can be grouped.
        int hashCode = 0;
        if ( getIrDevice() != null )
        {
            hashCode = ( int ) getIrDevice().getId();
        }
        return hashCode;
    }
    
    @Override
    public String toString(){
        return "device "+getIrDevice()+" port "+getPortNumber();
    }

}
