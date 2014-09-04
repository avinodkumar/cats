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
package com.comcast.cats.reboot;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to define the monitoring target.
 * 
 * @author cfrede001
 * 
 */
@XmlRootElement
public class MonitorTarget
{
    String                  hostMacAddress;
    String                  ipAddress;
    String                  ecmMacAddress;
    RebootHostStatus        status;
    RebootDetectionCategory category;
    Date                    executionDate;
    Long                    upTime;
    String                  formattedUpTime = "";

    public MonitorTarget()
    {
        // Do nothing constructor for JAXB.
    }

    public MonitorTarget( String hostMacAddress, String ipAddress, String ecmMacAddress )
    {
        super();
        this.hostMacAddress = hostMacAddress;
        this.ipAddress = ipAddress;
        this.ecmMacAddress = ecmMacAddress;
    }

    public MonitorTarget( String hostMacAddress, String ipAddress, String ecmMacAddress, RebootHostStatus status,
            RebootDetectionCategory category, Date executionDate, Long upTime )
    {
        super();
        this.hostMacAddress = hostMacAddress;
        this.ipAddress = ipAddress;
        this.ecmMacAddress = ecmMacAddress;
        this.status = status;
        this.category = category;
        this.executionDate = executionDate;
        setUpTime( upTime );
    }

    @XmlAttribute
    public String getHostMacAddress()
    {
        return hostMacAddress;
    }

    @XmlAttribute
    public String getIpAddress()
    {
        return ipAddress;
    }

    @XmlAttribute
    public String getEcmMacAddress()
    {
        return ecmMacAddress;
    }

    @XmlAttribute
    public RebootDetectionCategory getCategory()
    {
        return category;
    }

    @XmlAttribute
    public Date getExecutionDate()
    {
        return executionDate;
    }

    @XmlAttribute
    public Long getUpTime()
    {
        return upTime;
    }

    @XmlAttribute
    public RebootHostStatus getStatus()
    {
        return status;
    }

    public String getFormattedUpTime()
    {
        return formattedUpTime;
    }

    public void setFormattedUpTime( String formattedUpTime )
    {
        this.formattedUpTime = formattedUpTime;
    }

    public void setStatus( RebootHostStatus status )
    {
        this.status = status;
    }

    public void setHostMacAddress( String hostMacAddress )
    {
        this.hostMacAddress = hostMacAddress;
    }

    public void setIpAddress( String ipAddress )
    {
        this.ipAddress = ipAddress;
    }

    public void setEcmMacAddress( String ecmMacAddress )
    {
        this.ecmMacAddress = ecmMacAddress;
    }

    public void setCategory( RebootDetectionCategory category )
    {
        this.category = category;
    }

    public void setExecutionDate( Date executionDate )
    {
        this.executionDate = executionDate;
    }

    public void setUpTime( Long upTime )
    {
        this.upTime = upTime;
        this.formattedUpTime = RebootUtil.formatUptime( upTime );
    }

    @Override
    public String toString()
    {
        return "MonitorTarget [hostMacAddress=" + hostMacAddress + ", ipAddress=" + ipAddress + ", ecmMacAddress="
                + ecmMacAddress + ", status=" + status + ", category=" + category + ", executionDate=" + executionDate
                + ", upTime=" + upTime + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( ecmMacAddress == null ) ? 0 : ecmMacAddress.hashCode() );
        result = prime * result + ( ( hostMacAddress == null ) ? 0 : hostMacAddress.hashCode() );
        result = prime * result + ( ( ipAddress == null ) ? 0 : ipAddress.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        MonitorTarget other = ( MonitorTarget ) obj;
        if ( ecmMacAddress == null )
        {
            if ( other.ecmMacAddress != null )
                return false;
        }
        else if ( !ecmMacAddress.equals( other.ecmMacAddress ) )
            return false;
        if ( hostMacAddress == null )
        {
            if ( other.hostMacAddress != null )
                return false;
        }
        else if ( !hostMacAddress.equals( other.hostMacAddress ) )
            return false;
        if ( ipAddress == null )
        {
            if ( other.ipAddress != null )
                return false;
        }
        else if ( !ipAddress.equals( other.ipAddress ) )
            return false;
        return true;
    }
}
