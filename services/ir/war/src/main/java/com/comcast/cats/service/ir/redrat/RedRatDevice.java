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

/**
 * This class represents an abstract for all RedRat devices.
 * 
 * @author skurup00c
 * 
 */
public abstract class RedRatDevice implements IrDevice
{
    /**
     * RedRat device ID.
     */
    long id;
    String redratHubHost = null;
    int    redratHubPort;
    
    private static final Logger logger = LoggerFactory.getLogger(RedRatDevice.class);

    public RedRatDevice( long id )
    {
        this.id = id;
    }

    @Override
    public long getId()
    {
        return id;
    }

    public String getRedratHubHost()
    {
        return redratHubHost;
    }

    public void setRedratHubHost( String redratHubHost )
    {
        logger.info( "RedRatDevice redratHubHost "+redratHubHost );
        this.redratHubHost = redratHubHost;
    }

    public int getRedratHubPort()
    {
        return redratHubPort;
    }

    public void setRedratHubPort( int redratHubPort )
    {
        logger.info( "RedRatDevice redratHubPort "+redratHubPort );
        this.redratHubPort = redratHubPort;
    }
    
    @Override
    public String toString(){
        return "device "+getId();
    }
    
    @Override 
    public boolean equals(Object object){
        boolean isEqual = false;
        if(object instanceof RedRatDevice){
            if(( ( RedRatDevice ) object ).getId() == this.getId()){
                isEqual = true;
            }
        }
        return isEqual;
    }
}
