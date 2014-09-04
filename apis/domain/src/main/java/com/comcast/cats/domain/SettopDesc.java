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
package com.comcast.cats.domain;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.comcast.cats.info.SettopInfo;

/**
 * Logical representation of a Settop. This description class defines all the
 * necessary interfaces to the Settop hardware and important attributes.
 * 
 * @author cfrede001
 * 
 */
@XmlRootElement
public class SettopDesc extends BaseSettop implements SettopInfo
{
    private static final long         serialVersionUID   = 6049666658921485833L;

    private URI                       audioPath;
    private URI                       powerPath;
    private URI                       videoPath;
    private URI                       videoSelectionPath;
    private URI                       remotePath;
    private URI                       tracePath;
    private URI                       clickstreamPath;
    private URI                       clusterPath;
    private RFPlant                   rfPlant;

    private List< HardwareInterface > hardwareInterfaces = new ArrayList< HardwareInterface >();

    public SettopDesc()
    {
        // Do Nothing default constructor;
    }

    public SettopDesc( String id )
    {
        super( id );
    }

    public SettopDesc( List< HardwareInterface > hardwareInterfaces )
    {
        super();
        this.hardwareInterfaces = hardwareInterfaces;
    }

    public SettopDesc( SettopDesc settopDesc )
    {
        super( settopDesc );
        this.clickstreamPath = settopDesc.clickstreamPath;
        this.clusterPath = settopDesc.clusterPath;
    }

    @XmlAttribute
    public URI getClickstreamPath()
    {
        return clickstreamPath;
    }

    public void setClickstreamPath( URI clickstreamPath )
    {
        this.clickstreamPath = clickstreamPath;
    }

    @XmlAttribute
    public URI getClusterPath()
    {
        return clusterPath;
    }

    public void setClusterPath( URI clusterPath )
    {
        this.clusterPath = clusterPath;
    }

    public void setRFPlant( RFPlant rfPlant )
    {
        this.rfPlant = rfPlant;
    }

    @XmlElement( name = "plant" )
    public RFPlant getRFPlant()
    {
        return rfPlant;
    }

    public Controller getController()
    {
        return ( null != rfPlant ) ? rfPlant.getController() : null;
    }

    @XmlAttribute
    public URI getAudioPath()
    {
        URI connectionPath = audioPath;

        if ( null == connectionPath )
        {
            connectionPath = getConnectionPath( HardwarePurpose.AUDIO );
        }

        return connectionPath;
    }

    @XmlAttribute
    public URI getPowerPath()
    {
        URI connectionPath = powerPath;

        if ( null == connectionPath )
        {
            connectionPath = getConnectionPath( HardwarePurpose.POWER );
        }

        return connectionPath;
    }

    @XmlAttribute
    public URI getVideoPath()
    {
        URI connectionPath = videoPath;

        if ( null == connectionPath )
        {
            connectionPath = getConnectionPath( HardwarePurpose.VIDEOSERVER );
        }

        return connectionPath;
    }

    @XmlAttribute
    public URI getVideoSelectionPath()
    {
        URI connectionPath = videoSelectionPath;

        if ( null == connectionPath )
        {
            connectionPath = getConnectionPath( HardwarePurpose.VIDEOSELECTOR );
        }

        return connectionPath;
    }

    @XmlAttribute
    public URI getRemotePath()
    {
        URI connectionPath = remotePath;

        if ( null == connectionPath )
        {
            connectionPath = getConnectionPath( HardwarePurpose.IR );
        }

        return connectionPath;
    }

    @XmlAttribute
    public URI getTracePath()
    {
        URI connectionPath = tracePath;

        if ( null == connectionPath )
        {
            connectionPath = getConnectionPath( HardwarePurpose.TRACE );
        }

        return connectionPath;
    }

    public void addToHardwareInterface( HardwarePurpose type, HardwareInterface hardwareInterface )
    {
        this.hardwareInterfaces.add( hardwareInterface );
    }

    @XmlElementWrapper( name = "hardwareInterfaces" )
    @XmlElement( name = "hardwareInterface" )
    public List< HardwareInterface > getHardwareInterfaces()
    {
        return this.hardwareInterfaces;
    }

    public void setHardwareInterfaces( List< HardwareInterface > hardwareInterfaces )
    {
        this.hardwareInterfaces = hardwareInterfaces;
    }

    @Override
    public HardwareInterface getHardwareInterfaceByType( HardwarePurpose hardwarePurpose )
    {
        HardwareInterface hardwareInterface = null;

        for ( HardwareInterface tempHardwareInterface : getHardwareInterfaces() )
        {
            if ( hardwarePurpose.equals( tempHardwareInterface.getHardwarePurpose() ) )
            {
                hardwareInterface = tempHardwareInterface;
            }

        }

        return hardwareInterface;
    }

    private URI getConnectionPath( HardwarePurpose hardwarePurpose )
    {
        URI path = null;
        for ( HardwareInterface hardwareInterface : getHardwareInterfaces() )
        {
            if ( hardwarePurpose.equals( hardwareInterface.getHardwarePurpose() ) )
            {
                path = hardwareInterface.getInterfacePath();
            }

        }

        return path;
    }

    public void setAudioPath( URI audioPath )
    {
    	if(audioPath != null && audioPath.getHost() != null){
    		this.audioPath = audioPath;
    	}
    }

    public void setPowerPath( URI powerPath )
    {
    	if(powerPath != null && powerPath.getHost() != null){
    		this.powerPath = powerPath;
    	}
    }

    public void setVideoPath( URI videoPath )
    {
    	if(videoPath != null && videoPath.getHost() != null){
    		this.videoPath = videoPath;
    	}
    }

    public void setVideoSelectionPath( URI videoSelectionPath )
    {
    	if(videoSelectionPath != null && videoSelectionPath.getHost() != null){
    		this.videoSelectionPath = videoSelectionPath;
    	}
    }

    public void setRemotePath( URI remotePath )
    {
    	if(remotePath != null && remotePath.getHost() != null){
    		this.remotePath = remotePath;
    	}
    }

    public void setTracePath( URI tracePath )
    {
    	if(tracePath != null && tracePath.getHost() != null){
    		this.tracePath = tracePath;
    	}
    }

    @Override
    public boolean equals( Object object )
    {
        boolean retVal = false;
        try
        {
            if ( ( ( Component ) object ).getId().equals( this.getId() ) )
            {
                retVal = true;
            }
        }
        catch ( Exception e )
        {
            retVal = false;
        }
        return retVal;
    }

    /**
     * toString() is used in search functionality of CATS Vision. Please check
     * before modification
     */
    @Override
    public String toString()
    {
        return super.toString() + getClass().getName() + " [audioPath=" + getAudioPath() + ", clickStreamPath="
                + getClickstreamPath() + ", clusterPath=" + getClusterPath() + ", powerPath=" + getPowerPath()
                + ", remotePath=" + getRemotePath() + ", tracePath=" + getTracePath() + ", videoPath=" + getVideoPath()
                + ", videoSelectionPath=" + getVideoSelectionPath() + ", rfPlant=" + getRFPlant() + "]]";
    }
}
