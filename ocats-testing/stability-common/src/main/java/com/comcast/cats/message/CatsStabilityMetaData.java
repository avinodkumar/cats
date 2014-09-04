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
package com.comcast.cats.message;

import java.io.Serializable;

/**
 * 
 * @author SSugun00c
 *
 */
public class CatsStabilityMetaData implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String            buildId;
    private String            buildNumber;
    private String            buildTag;
    private String            buildUrl;
    private String            catsHome;
    private String            nodeName;
    private String            workspace;
    private String            hostName;
    private String            jobName;
    private String            jobUrl;
    private String            userDir;

    public String getBuildId()
    {
        return buildId;
    }

    public void setBuildId( String buildId )
    {
        this.buildId = buildId;
    }

    public String getBuildNumber()
    {
        return buildNumber;
    }

    public void setBuildNumber( String buildNumber )
    {
        this.buildNumber = buildNumber;
    }

    public String getBuildTag()
    {
        return buildTag;
    }

    public void setBuildTag( String buildTag )
    {
        this.buildTag = buildTag;
    }

    public String getBuildUrl()
    {
        return buildUrl;
    }

    public void setBuildUrl( String buildUrl )
    {
        this.buildUrl = buildUrl;
    }

    public String getCatsHome()
    {
        return catsHome;
    }

    public void setCatsHome( String catsHome )
    {
        this.catsHome = catsHome;
    }

    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName( String nodeName )
    {
        this.nodeName = nodeName;
    }

    public String getWorkspace()
    {
        return workspace;
    }

    public void setWorkspace( String workspace )
    {
        this.workspace = workspace;
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName( String hostName )
    {
        this.hostName = hostName;
    }

    public String getJobName()
    {
        return jobName;
    }

    public void setJobName( String jobName )
    {
        this.jobName = jobName;
    }

    public String getJobUrl()
    {
        return jobUrl;
    }

    public void setJobUrl( String jobUrl )
    {
        this.jobUrl = jobUrl;
    }

    public String getUserDir()
    {
        return userDir;
    }

    public void setUserDir( String userDir )
    {
        this.userDir = userDir;
    }

    @Override
    public String toString()
    {
        return "CatsStabilityMetaData [buildId=" + getBuildId() + ", buildNumber=" + getBuildNumber() + ", buildTag="
                + getBuildTag() + ", buildUrl=" + getBuildUrl() + ", catsHome=" + getCatsHome() + ", nodeName="
                + getNodeName() + ", workspace=" + getWorkspace() + ", hostName=" + getHostName() + ", jobName="
                + getJobName() + ", jobUrl=" + getJobUrl() + ", userDir=" + getUserDir() + "]";
    }
}
