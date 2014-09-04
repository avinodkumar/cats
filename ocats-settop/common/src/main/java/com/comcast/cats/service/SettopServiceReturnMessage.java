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
package com.comcast.cats.service;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.comcast.cats.service.WebServiceReturn;

/**
 * General SettopServiceReturnMessage class used to encapsulate all
 * SettopService messages.
 * 
 * @author cfrede001
 */
@XmlRootElement
public class SettopServiceReturnMessage extends WebServiceReturn
{
    /**
     * 
     */
    private static final long serialVersionUID = -1058914002847316703L;

    SettopServiceReturnEnum   serviceCode;

    /**
     * A returned URL for a particular request.
     */
    String                    url;

    /**
     * A particular status like getPowerStatus().
     * 
     * @return
     */
    String                    status;

    /**
     * Is this box locked, which will only be available after a call to
     * isLocked.
     * 
     * @return
     */
    boolean                   locked           = false;

    @XmlElement( name = "serviceCode" )
    public SettopServiceReturnEnum getServiceCode()
    {
        return serviceCode;
    }

    public void setServiceCode( SettopServiceReturnEnum serviceCode )
    {
        this.serviceCode = serviceCode;
    }

    public SettopServiceReturnMessage( final SettopServiceReturnEnum serviceCode )
    {
        this.serviceCode = serviceCode;
    }

    /**
     * By default we want to select the general SETTOP_SERVICE_SUCCESS. Make
     * sure the base return message is also set.
     */
    public SettopServiceReturnMessage()
    {
        this.serviceCode = SettopServiceReturnEnum.SETTOP_SERVICE_SUCCESS;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked( boolean locked )
    {
        this.locked = locked;
    }

    @XmlElement( name = "status" )
    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    @XmlElement( name = "url" )
    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Result=" + this.getResult() + ", Service Code=" + serviceCode + ", Url=" + url + ", Status=" + status
                + ", Locked=" + locked + ", Message=" + this.getMessage();
    }
}