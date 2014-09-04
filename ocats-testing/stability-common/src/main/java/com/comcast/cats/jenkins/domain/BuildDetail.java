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
public class BuildDetail extends BaseJenkinsDomain
{
    private static final long serialVersionUID = 1L;

    @ElementList( inline = true )
    private List< Action >    actions;

    @Element( required = false )
    private boolean           building;

    @Element( required = false )
    private int               duration;

    @Element( required = false )
    private int               estimatedDuration;

    @Element( required = false )
    private String            fullDisplayName;

    @Element( required = false )
    private String            id;

    @Element( required = false )
    private boolean           keepLog;

    @Element( required = false )
    private int               number;

    @Element( required = false )
    private String            result;

    @Element( required = false )
    private long              timestamp;

    @Element( required = false )
    private String            url;

    @Element( required = false )
    private String            builtOn;

    @Element( required = false )
    private ChangeSet         changeSet;

    @Element( required = false )
    private Culprit           culprit;

    @Element( required = false )
    private String            mavenVersionUsed;

    public List< Action > getActions()
    {
        return actions;
    }

    public void setActions( List< Action > actions )
    {
        this.actions = actions;
    }

    public boolean isBuilding()
    {
        return building;
    }

    public void setBuilding( boolean building )
    {
        this.building = building;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration( int duration )
    {
        this.duration = duration;
    }

    public int getEstimatedDuration()
    {
        return estimatedDuration;
    }

    public void setEstimatedDuration( int estimatedDuration )
    {
        this.estimatedDuration = estimatedDuration;
    }

    public String getFullDisplayName()
    {
        return fullDisplayName;
    }

    public void setFullDisplayName( String fullDisplayName )
    {
        this.fullDisplayName = fullDisplayName;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public boolean isKeepLog()
    {
        return keepLog;
    }

    public void setKeepLog( boolean keepLog )
    {
        this.keepLog = keepLog;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber( int number )
    {
        this.number = number;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult( String result )
    {
        this.result = result;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( long timestamp )
    {
        this.timestamp = timestamp;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getBuiltOn()
    {
        return builtOn;
    }

    public void setBuiltOn( String builtOn )
    {
        this.builtOn = builtOn;
    }

    public ChangeSet getChangeSet()
    {
        return changeSet;
    }

    public void setChangeSet( ChangeSet changeSet )
    {
        this.changeSet = changeSet;
    }

    public Culprit getCulprit()
    {
        return culprit;
    }

    public void setCulprit( Culprit culprit )
    {
        this.culprit = culprit;
    }

    public String getMavenVersionUsed()
    {
        return mavenVersionUsed;
    }

    public void setMavenVersionUsed( String mavenVersionUsed )
    {
        this.mavenVersionUsed = mavenVersionUsed;
    }
}
