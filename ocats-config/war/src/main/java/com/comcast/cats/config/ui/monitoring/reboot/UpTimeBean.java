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
package com.comcast.cats.config.ui.monitoring.reboot;

/**
 * UptimeBean represents information to show current Uptimes.
 */
public class UpTimeBean
{
    String settopName;
    String settopMac;
    String settopIP;
    String settopMCardMac;
    String settopId;
    String upTime;
    String settopType;
    String rebootDetectionTime;

    public String getSettopName()
    {
        return settopName;
    }
    public void setSettopName( String settopName )
    {
        this.settopName = settopName;
    }
    public String getSettopMac()
    {
        return settopMac;
    }
    public void setSettopMac( String settopMac )
    {
        this.settopMac = settopMac;
    }
    public String getSettopIP()
    {
        return settopIP;
    }
    public void setSettopIP( String settopIP )
    {
        this.settopIP = settopIP;
    }
    public String getUpTime()
    {
        return upTime;
    }
    public void setUpTime( String upTime )
    {
        this.upTime = upTime;
    }
    public String getSettopType()
    {
        return settopType;
    }
    public void setSettopType( String settopType )
    {
        this.settopType = settopType;
    }
    public void setSettopId( String id )
    {
        this.settopId = id;
        
    }
    public String getSettopMCardMac()
    {
        return settopMCardMac;
    }
    public void setSettopMCardMac( String settopMCardMac )
    {
        this.settopMCardMac = settopMCardMac;
    }
    public String getRebootDetectionTime()
    {
        return rebootDetectionTime;
    }
    public void setRebootDetectionTime( String rebootDetectionTime )
    {
        this.rebootDetectionTime = rebootDetectionTime;
    }
    public String getSettopId()
    {
        return settopId;
    }
}
