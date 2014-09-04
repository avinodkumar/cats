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
package com.comcast.cats.jenkins.domain;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 
 * @author SSugun00c
 * 
 */
@Root
public class JobSet extends BaseJenkinsDomain
{
    private static final long serialVersionUID = 1L;

    @Element( required = false )
    private String            assignedLabel;

    @Element( required = false )
    private String            mode;

    @Element( required = false )
    private String            nodeDescription;

    @Element( required = false )
    private String            nodeName;

    @Element( required = false )
    private int               numExecutors;

    @Element( required = false )
    private String            description;

    @ElementList( inline = true )
    private List< Job >       jobs;

    @Element( required = false )
    private String            overallLoad;

    @Element( required = false )
    private PrimaryView       primaryView;

    @Element( required = false )
    private boolean           quietingDown;

    @Element( required = false )
    private int               slaveAgentPort;

    @Element( required = false )
    private boolean           useCrumbs;

    @Element( required = false )
    private boolean           useSecurity;

    @ElementList( inline = true )
    private List< View >      views;

    @Element( required = false )
    private String            unlabeledLoad;

    public String getAssignedLabel()
    {
        return assignedLabel;
    }

    public void setAssignedLabel( String assignedLabel )
    {
        this.assignedLabel = assignedLabel;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode( String mode )
    {
        this.mode = mode;
    }

    public String getNodeDescription()
    {
        return nodeDescription;
    }

    public void setNodeDescription( String nodeDescription )
    {
        this.nodeDescription = nodeDescription;
    }

    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName( String nodeName )
    {
        this.nodeName = nodeName;
    }

    public int getNumExecutors()
    {
        return numExecutors;
    }

    public void setNumExecutors( int numExecutors )
    {
        this.numExecutors = numExecutors;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public List< Job > getJobs()
    {
        return jobs;
    }

    public void setJobs( List< Job > jobs )
    {
        this.jobs = jobs;
    }

    public String getOverallLoad()
    {
        return overallLoad;
    }

    public void setOverallLoad( String overallLoad )
    {
        this.overallLoad = overallLoad;
    }

    public PrimaryView getPrimaryView()
    {
        return primaryView;
    }

    public void setPrimaryView( PrimaryView primaryView )
    {
        this.primaryView = primaryView;
    }

    public boolean isQuietingDown()
    {
        return quietingDown;
    }

    public void setQuietingDown( boolean quietingDown )
    {
        this.quietingDown = quietingDown;
    }

    public int getSlaveAgentPort()
    {
        return slaveAgentPort;
    }

    public void setSlaveAgentPort( int slaveAgentPort )
    {
        this.slaveAgentPort = slaveAgentPort;
    }

    public boolean isUseCrumbs()
    {
        return useCrumbs;
    }

    public void setUseCrumbs( boolean useCrumbs )
    {
        this.useCrumbs = useCrumbs;
    }

    public boolean isUseSecurity()
    {
        return useSecurity;
    }

    public void setUseSecurity( boolean useSecurity )
    {
        this.useSecurity = useSecurity;
    }

    public List< View > getViews()
    {
        return views;
    }

    public void setView( List< View > views )
    {
        this.views = views;
    }

    @Override
    public String toString()
    {
        return "Hudson [assignedLabel=" + getAssignedLabel() + ", mode=" + getMode() + ", nodeDescription="
                + getNodeDescription() + ", nodeName=" + getNodeName() + ", numExecutors=" + getNumExecutors()
                + ", description=" + getDescription() + ", jobs=" + getJobs() + ", overallLoad=" + getOverallLoad()
                + ", primaryView=" + getPrimaryView() + ", quietingDown=" + isQuietingDown() + ", slaveAgentPort="
                + getSlaveAgentPort() + ", useCrumbs=" + isUseCrumbs() + ", useSecurity=" + isUseSecurity()
                + ", views=" + getViews() + "]";
    }

    public String getUnlabeledLoad()
    {
        return unlabeledLoad;
    }

    public void setUnlabeledLoad( String unlabeledLoad )
    {
        this.unlabeledLoad = unlabeledLoad;
    }

    public void setViews( List< View > views )
    {
        this.views = views;
    }
}
