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

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.comcast.cats.SettopConstants;

/**
 * Token representing the data needed by QTP for performing WebService requests.
 * 
 * @author cfrede001
 * @since 2.0.0
 */
@XmlRootElement( name = "settopToken", namespace = SettopConstants.NAMESPACE )
public class SettopToken extends WebServiceReturn
{
    /**
     * 
     */
    private static final long serialVersionUID = -5747367897833285924L;
    private String            authToken;
    private String            allocationId;
    private String            settopId;

    private Date              created;
    private Date              lastAccessed;

    public SettopToken()
    {
        // TODO Auto-generated constructor stub
    }

    @XmlAttribute( name = "authToken" )
    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken( String authToken )
    {
        this.authToken = authToken;
    }

    @XmlAttribute( name = "allocationId" )
    public String getAllocationId()
    {
        return allocationId;
    }

    public void setAllocationId( String allocationId )
    {
        this.allocationId = allocationId;
    }

    @XmlAttribute( name = "settopId" )
    public String getSettopId()
    {
        return settopId;
    }

    public void setSettopId( String settopId )
    {
        this.settopId = settopId;
    }

    @XmlAttribute( name = "created" )
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    public Date getLastAccessed()
    {
        return lastAccessed;
    }

    @XmlAttribute( name = "lastAccessed" )
    public void setLastAccessed( Date lastAccessed )
    {
        this.lastAccessed = lastAccessed;
    }

    @Override
    public String toString()
    {
        return "SettopToken [authToken=" + authToken + ", allocationId=" + allocationId + ", settopId=" + settopId
                + "]";
    }
}
