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
package com.comcast.cats.reboot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.comcast.cats.reboot.RebootHostStatus;

@Entity
@NamedQueries(
    { @NamedQuery( name = "Host.FindByMac", query = "SELECT h FROM Host h WHERE h.macAddress = :macAddress" ) } )
public class Host extends BaseEntity
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 980517275198018968L;

    @Column( unique = true )
    String                    macAddress;

    @Column
    String                    ipAddress;

    @Column
    String                    ecm;

    /**
     * Host will be enabled by default.
     */
    @Enumerated
    RebootHostStatus          status           = RebootHostStatus.ENABLED;

    @Column
    Long                      uptime           = 0L;

    @Column
    Date                      lastModified     = new Date();

    public Host()
    {
        // Do nothing constructor
    }

    public Host( String macAddress, String ipAddress, String ecm )
    {
        this( macAddress, ipAddress, ecm, RebootHostStatus.ENABLED );
    }

    public Host( String macAddress, String ipAddress, String ecm, RebootHostStatus status )
    {
        this( macAddress, ipAddress, ecm, status, new Date() );
    }

    public Host( String macAddress, String ipAddress, String ecm, RebootHostStatus status, Date lastModified )
    {
        super();
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.ecm = ecm;
        this.status = status;
        this.lastModified = lastModified;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public String getEcm()
    {
        return ecm;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public void setMacAddress( String macAddress )
    {
        this.macAddress = macAddress;
    }

    public void setIpAddress( String ipAddress )
    {
        this.ipAddress = ipAddress;
    }

    public void setEcm( String ecm )
    {
        this.ecm = ecm;
    }

    public void setLastModified( Date lastModified )
    {
        this.lastModified = lastModified;
    }

    public RebootHostStatus getStatus()
    {
        return status;
    }

    public Long getUptime()
    {
        return uptime;
    }

    public void setUptime( Long uptime )
    {
        this.uptime = uptime;
    }

    public void setStatus( RebootHostStatus status )
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "Host [macAddress=" + macAddress + ", ipAddress=" + ipAddress + ", ecm=" + ecm + ", status=" + status
                + ", uptime=" + uptime + ", lastModified=" + lastModified + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( ecm == null ) ? 0 : ecm.hashCode() );
        result = prime * result + ( ( ipAddress == null ) ? 0 : ipAddress.hashCode() );
        result = prime * result + ( ( macAddress == null ) ? 0 : macAddress.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( !super.equals( obj ) )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Host other = ( Host ) obj;
        if ( ecm == null )
        {
            if ( other.ecm != null )
                return false;
        }
        else if ( !ecm.equals( other.ecm ) )
            return false;
        if ( ipAddress == null )
        {
            if ( other.ipAddress != null )
                return false;
        }
        else if ( !ipAddress.equals( other.ipAddress ) )
            return false;
        if ( macAddress == null )
        {
            if ( other.macAddress != null )
                return false;
        }
        else if ( !macAddress.equals( other.macAddress ) )
            return false;
        return true;
    }
}
