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
public class BuildSet extends BaseJenkinsDomain
{
    private static final long     serialVersionUID = 1L;

    @ElementList( inline = true )
    private List< Action >        actions;

    @Element( required = false )
    private String                description;

    @Element( required = false )
    private String                displayName;

    @Element( required = false )
    private String                name;

    @Element( required = false )
    private String                url;

    @Element( required = false )
    private boolean               buildable;

    @ElementList( inline = true )
    private List< Build >         builds;

    @Element( required = false )
    private String                color;

    @Element( required = false )
    private FirstBuild            firstBuild;

    @Element( required = false )
    private HealthReport          healthReport;

    @Element( required = false )
    private boolean               inQueue;

    @Element( required = false )
    private boolean               keepDependencies;

    @Element( required = false )
    private LastBuild             lastBuild;

    @Element( required = false )
    private LastCompletedBuild    lastCompletedBuild;

    @Element( required = false )
    private LastStableBuild       lastStableBuild;

    @Element( required = false )
    private LastSuccessfulBuild   lastSuccessfulBuild;

    @Element( required = false )
    private LastUnsuccessfulBuild lastUnsuccessfulBuild;

    @Element( required = false )
    private int                   nextBuildNumber;

    @Element( required = false )
    private Property              property;

    @Element( required = false )
    private boolean               concurrentBuild;

    @Element( required = false )
    private String                scm;

    @Element( required = false )
    private Module                module;

    public List< Action > getActions()
    {
        return actions;
    }

    public void setActions( List< Action > actions )
    {
        this.actions = actions;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public boolean isBuildable()
    {
        return buildable;
    }

    public void setBuildable( boolean buildable )
    {
        this.buildable = buildable;
    }

    public List< Build > getBuilds()
    {
        return builds;
    }

    public void setBuilds( List< Build > builds )
    {
        this.builds = builds;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor( String color )
    {
        this.color = color;
    }

    public FirstBuild getFirstBuild()
    {
        return firstBuild;
    }

    public void setFirstBuild( FirstBuild firstBuild )
    {
        this.firstBuild = firstBuild;
    }

    public HealthReport getHealthReport()
    {
        return healthReport;
    }

    public void setHealthReport( HealthReport healthReport )
    {
        this.healthReport = healthReport;
    }

    public boolean isInQueue()
    {
        return inQueue;
    }

    public void setInQueue( boolean inQueue )
    {
        this.inQueue = inQueue;
    }

    public boolean isKeepDependencies()
    {
        return keepDependencies;
    }

    public void setKeepDependencies( boolean keepDependencies )
    {
        this.keepDependencies = keepDependencies;
    }

    public LastBuild getLastBuild()
    {
        return lastBuild;
    }

    public void setLastBuild( LastBuild lastBuild )
    {
        this.lastBuild = lastBuild;
    }

    public LastCompletedBuild getLastCompletedBuild()
    {
        return lastCompletedBuild;
    }

    public void setLastCompletedBuild( LastCompletedBuild lastCompletedBuild )
    {
        this.lastCompletedBuild = lastCompletedBuild;
    }

    public LastSuccessfulBuild getLastSuccessfulBuild()
    {
        return lastSuccessfulBuild;
    }

    public void setLastSuccessfulBuild( LastSuccessfulBuild lastSuccessfulBuild )
    {
        this.lastSuccessfulBuild = lastSuccessfulBuild;
    }

    public LastUnsuccessfulBuild getLastUnsuccessfulBuild()
    {
        return lastUnsuccessfulBuild;
    }

    public void setLastUnsuccessfulBuild( LastUnsuccessfulBuild lastUnsuccessfulBuild )
    {
        this.lastUnsuccessfulBuild = lastUnsuccessfulBuild;
    }

    public int getNextBuildNumber()
    {
        return nextBuildNumber;
    }

    public void setNextBuildNumber( int nextBuildNumber )
    {
        this.nextBuildNumber = nextBuildNumber;
    }

    public Property getProperty()
    {
        return property;
    }

    public void setProperty( Property property )
    {
        this.property = property;
    }

    public boolean isConcurrentBuild()
    {
        return concurrentBuild;
    }

    public void setConcurrentBuild( boolean concurrentBuild )
    {
        this.concurrentBuild = concurrentBuild;
    }

    public String getScm()
    {
        return scm;
    }

    public void setScm( String scm )
    {
        this.scm = scm;
    }

    public Module getModule()
    {
        return module;
    }

    public void setModule( Module module )
    {
        this.module = module;
    }

    public LastStableBuild getLastStableBuild()
    {
        return lastStableBuild;
    }

    public void setLastStableBuild( LastStableBuild lastStableBuild )
    {
        this.lastStableBuild = lastStableBuild;
    }
}
