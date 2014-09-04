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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.comcast.cats.reboot.RebootDetectionCategory;
import com.comcast.cats.reboot.RebootDetectionStatus;
import com.comcast.cats.reboot.RebootUtil;

@Entity
@NamedQueries(
    {
            @NamedQuery( name = "DetectionInfo.CountByHostMac", query = "SELECT COUNT(det.id) FROM DetectionInfo det WHERE det.host.macAddress = :macAddress" ),
            @NamedQuery( name = "DetectionInfo.FindByHost", query = "SELECT det FROM DetectionInfo det WHERE det.host = :host" ),
            @NamedQuery( name = "DetectionInfo.FindByHostMac", query = "SELECT det FROM DetectionInfo det WHERE det.host.macAddress = :macAddress ORDER BY det.executionDate DESC" ), } )
public class DetectionInfo extends BaseEntity
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -4805516372711667580L;

    @ManyToOne( optional = false )
    Host                      host;

    @Column
    Date                      executionDate;

    @Enumerated
    RebootDetectionCategory   category;

    @Enumerated
    RebootDetectionStatus     status;

    @Column
    Long                      upTime;

    public Host getHost()
    {
        return host;
    }

    public Date getExecutionDate()
    {
        return executionDate;
    }

    public RebootDetectionCategory getCategory()
    {
        return category;
    }

    public Long getUpTime()
    {
        return upTime;
    }

    public String getFormattedUpTime()
    {
        return RebootUtil.formatUptime( upTime );
    }

    public void setHost( Host host )
    {
        this.host = host;
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
    }

    public RebootDetectionStatus getStatus()
    {
        return status;
    }

    public void setStatus( RebootDetectionStatus status )
    {
        this.status = status;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( category == null ) ? 0 : category.hashCode() );
        result = prime * result + ( ( executionDate == null ) ? 0 : executionDate.hashCode() );
        result = prime * result + ( ( host == null ) ? 0 : host.hashCode() );
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
        DetectionInfo other = ( DetectionInfo ) obj;
        if ( category != other.category )
            return false;
        if ( executionDate == null )
        {
            if ( other.executionDate != null )
                return false;
        }
        else if ( !executionDate.equals( other.executionDate ) )
            return false;
        if ( host == null )
        {
            if ( other.host != null )
                return false;
        }
        else if ( !host.equals( other.host ) )
            return false;
        return true;
    }
}
