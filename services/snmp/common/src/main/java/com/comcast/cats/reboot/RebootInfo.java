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

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO representing the reboot information for a particular MonitorTarget.
 * 
 * @author cfrede001
 * 
 */
@XmlRootElement
public class RebootInfo implements Serializable
{
    /**
	 * 
	 */
    private static final long         serialVersionUID = 7291910159269286355L;

    protected RebootDetectionCategory category;
    protected RebootDetectionStatus   status;
    protected Long                    upTime;
    protected String                  formattedUpTime  = "";
    protected Date                    executionDate;
    protected String                  message;

    public RebootInfo()
    {
        // Do Nothing;
    }

    public RebootInfo( RebootDetectionCategory category, RebootDetectionStatus status, Long upTime, Date executionDate )
    {
        super();
        this.category = category;
        this.status = status;
        setUpTime( upTime );
        this.executionDate = executionDate;
    }

    public RebootInfo( RebootDetectionCategory category, RebootDetectionStatus status, Long upTime, Date executionDate,
            String message )
    {
        super();
        this.category = category;
        this.status = status;
        setUpTime( upTime );
        this.executionDate = executionDate;
        this.message = message;
    }

    public String getFormattedUpTime()
    {
        return formattedUpTime;
    }

    @XmlAttribute
    public RebootDetectionStatus getStatus()
    {
        return status;
    }

    @XmlElement
    public String getMessage()
    {
        return message;
    }

    public void setStatus( RebootDetectionStatus status )
    {
        this.status = status;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @XmlAttribute
    public Date getExecutionDate()
    {
        return executionDate;
    }

    @XmlAttribute
    public RebootDetectionCategory getCategory()
    {
        return category;
    }

    @XmlAttribute
    public Long getUpTime()
    {
        return upTime;
    }

    public void setExecutionDate( Date executionDate )
    {
        this.executionDate = executionDate;
    }

    public void setCategory( RebootDetectionCategory category )
    {
        this.category = category;
    }

    public void setUpTime( Long upTime )
    {
        this.upTime = upTime;
        this.formattedUpTime = RebootUtil.formatUptime( upTime );
    }

    @Override
    public String toString()
    {
        return "RebootInfo [category=" + category + ", status=" + status + ", upTime=" + upTime + ", formattedUpTime="
                + formattedUpTime + ", executionDate=" + executionDate + ", message=" + message + "]";
    }
}
