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
package com.comcast.cats.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Represents a system process.
 * 
 * @author SSugun00c
 * 
 */
@ManagedBean
@RequestScoped
public class ProcessBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String            imageName;
    private String            pid;
    private String            sessionName;
    private String            sessionNumber;
    private String            memoryUsage;
    private String            memoryUsageUnit;

    public ProcessBean()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ProcessBean( String imageName, String pid, String sessionName, String sessionNumber, String memoryUsage,
            String memoryUsageUnit )
    {
        super();
        this.imageName = imageName;
        this.pid = pid;
        this.sessionName = sessionName;
        this.sessionNumber = sessionNumber;
        this.memoryUsage = memoryUsage;
        this.memoryUsageUnit = memoryUsageUnit;
    }

    public ProcessBean( String pid )
    {
        this.pid = pid;
    }

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName( String imageName )
    {
        this.imageName = imageName;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid( String pid )
    {
        this.pid = pid;
    }

    public String getSessionName()
    {
        return sessionName;
    }

    public void setSessionName( String sessionName )
    {
        this.sessionName = sessionName;
    }

    public String getSessionNumber()
    {
        return sessionNumber;
    }

    public void setSessionNumber( String sessionNumber )
    {
        this.sessionNumber = sessionNumber;
    }

    public String getMemoryUsage()
    {
        return memoryUsage;
    }

    public void setMemoryUsage( String memoryUsage )
    {
        this.memoryUsage = memoryUsage;
    }

    public String getMemoryUsageUnit()
    {
        return memoryUsageUnit;
    }

    public void setMemoryUsageUnit( String memoryUsageUnit )
    {
        this.memoryUsageUnit = memoryUsageUnit;
    }
}
