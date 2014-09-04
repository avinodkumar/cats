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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.comcast.cats.domain.util.PropertiesAdapter;

/**
 * A shallow representation of a Settop. This description class defines only the
 * basic attributes of a Settop.
 * 
 * @author ssugun00c
 * 
 */
@XmlRootElement
public class BaseSettop extends Component
{
    private static final long     serialVersionUID = 1L;

    private String                content;
    private String                rackId;
    private String                environmentId;
    private String                firmwareVersion;
    private String                hardwareRevision;
    private String                hostMacAddress;
    private String                mcardMacAddress;
    private String                hostIp4Address;
    private String                hostIp6Address;
    private String                make;
    private String                model;
    private String                manufacturer;
    private String                serialNumber;
    private String                mCardSerialNumber;
    private String                unitAddress;
    private String                remoteType;
    private Map< String, String > extraProperties  = new HashMap< String, String >();

    public BaseSettop()
    {
        // Do Nothing default constructor;
    }

    public BaseSettop( BaseSettop settopDesc )
    {
        super( settopDesc );
        this.content = settopDesc.content;
        this.rackId = settopDesc.rackId;
        this.environmentId = settopDesc.environmentId;
        this.firmwareVersion = settopDesc.firmwareVersion;
        this.hardwareRevision = settopDesc.hardwareRevision;
        this.hostMacAddress = settopDesc.hostMacAddress;
        this.mcardMacAddress = settopDesc.mcardMacAddress;
        this.hostIp4Address = settopDesc.hostIp4Address;
        this.hostIp6Address = settopDesc.hostIp6Address;
        this.make = settopDesc.make;
        this.model = settopDesc.model;
        this.manufacturer = settopDesc.manufacturer;
        this.serialNumber = settopDesc.serialNumber;
        this.mCardSerialNumber = settopDesc.mCardSerialNumber;
        this.unitAddress = settopDesc.unitAddress;
        this.remoteType = settopDesc.remoteType;

        this.extraProperties = settopDesc.extraProperties;
    }

    public BaseSettop( String id )
    {
        super( id );
    }

    public String getContent()
    {
        return content;
    }

    public void setContent( String content )
    {
        this.content = content;
    }

    @XmlAttribute
    public String getFirmwareVersion()
    {
        return firmwareVersion;
    }

    public void setFirmwareVersion( String firmwareVersion )
    {
        this.firmwareVersion = firmwareVersion;
    }

    @XmlAttribute
    public String getHardwareRevision()
    {
        return hardwareRevision;
    }

    public void setHardwareRevision( String hardwareRevision )
    {
        this.hardwareRevision = hardwareRevision;
    }

    @XmlAttribute
    public String getHostMacAddress()
    {
        return hostMacAddress;
    }

    public void setHostMacAddress( String hostMacAddress )
    {
        this.hostMacAddress = hostMacAddress;
    }

    @XmlAttribute
    public String getHostIpAddress()
    {
        return hostIp4Address;
    }

    public void setHostIpAddress( String hostIp4Address )
    {
        this.hostIp4Address = hostIp4Address;
    }

    @XmlAttribute
    public String getHostIp4Address()
    {
        return hostIp4Address;
    }

    public void setHostIp4Address( String hostIp4Address )
    {
        this.hostIp4Address = hostIp4Address;
    }

    @XmlAttribute
    public String getHostIp6Address()
    {
        return hostIp6Address;
    }

    public void setHostIp6Address( String hostIp6Address )
    {
        this.hostIp6Address = hostIp6Address;
    }

    @XmlAttribute
    public String getMake()
    {
        return make;
    }

    public void setMake( String make )
    {
        this.make = make;
    }

    @XmlAttribute
    public String getModel()
    {
        return model;
    }

    public void setModel( String model )
    {
        this.model = model;
    }

    @XmlAttribute
    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer( String manufacturer )
    {
        this.manufacturer = manufacturer;
    }

    @XmlAttribute
    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber( String serialNumber )
    {
        this.serialNumber = serialNumber;
    }

    @XmlAttribute
    public String getUnitAddress()
    {
        return unitAddress;
    }

    public void setUnitAddress( String unitAddress )
    {
        this.unitAddress = unitAddress;
    }

    @XmlAttribute
    public String getRemoteType()
    {
        return remoteType;
    }

    public void setRemoteType( String remoteType )
    {
        this.remoteType = remoteType;
    }

    public Inet4Address getHostIp4InetAddress()
    {
        return null;
    }

    public Inet6Address getHostIp6InetAddress()
    {
        return null;
    }

    public InetAddress getHostIpInetAddress()
    {
        return null;
    }

    /**
     * Sets the MCard Serial Number of the settop device.
     * 
     * @param mCardSerialNumber
     */
    public void setMCardSerialNumber( String mCardSerialNumber )
    {
        this.mCardSerialNumber = mCardSerialNumber;
    }

    /**
     * Returns the MCard Serial Number for the settop device.
     */
    @XmlAttribute
    public String getMCardSerialNumber()
    {
        return mCardSerialNumber;
    }

    /**
     * Sets the MCard MAC address of the settop device.
     * 
     * @param mcardMacAddress
     */
    public void setMcardMacAddress( String mcardMacAddress )
    {
        this.mcardMacAddress = mcardMacAddress;
    }

    /**
     * Returns the MCard MAC Address for the settop device.
     */
    @XmlAttribute
    public String getMcardMacAddress()
    {
        return mcardMacAddress;
    }

    @XmlAttribute
    public String getRackId()
    {
        return rackId;
    }

    public void setRackId( String rackId )
    {
        this.rackId = rackId;
    }

    @XmlAttribute
    public String getEnvironmentId()
    {
        return environmentId;
    }

    public void setEnvironmentId( String environmentId )
    {
        this.environmentId = environmentId;
    }

    @XmlElement
    @XmlJavaTypeAdapter( PropertiesAdapter.class )
    public Map< String, String > getExtraProperties()
    {
        return extraProperties;
    }

    /**
     * @param extraProperties
     *            All properties we failed to consider when defining this class.
     *            It is used for properties that people in test want to
     *            associate with a settop, but that we have not accounted for in
     *            advance.
     */
    public void setExtraProperties( final Map< String, String > extraProperties )
    {
        this.extraProperties = extraProperties;
    }

    public String findExtraProperty( String key )
    {
        /*
         * Common case where the key exists within the properties.
         */
        if ( getExtraProperties().containsKey( key ) )
        {
            return getExtraProperties().get( key );
        }
        /*
         * Let's try a more sophisticated search that removes whitespace and
         * ignores case.
         */
        String cleanedRequestKey = key.replaceAll( "\\s", "" );
        Set< String > keys = getExtraProperties().keySet();
        for ( String k : keys )
        {
            String cleanedKey = k.replaceAll( "\\s", "" );
            if ( cleanedKey.equalsIgnoreCase( cleanedRequestKey ) )
            {
                return getExtraProperties().get( k );
            }
        }
        return null;
    }

    @Override
    public boolean equals( Object object )
    {
        boolean retVal = false;
        try
        {
            if ( ( ( BaseSettop ) object ).getId().equals( this.getId() ) )
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
        return super.toString() + getClass().getName() + " [rackId=" + getRackId() + ", environmentId="
                + getEnvironmentId() + ", mcardMacAddress = " + getMcardMacAddress() + ", content=" + getContent()
                + ", firmwareVersion=" + getFirmwareVersion() + ", hardwareRevision=" + getHardwareRevision()
                + ", hostIp4Address=" + getHostIp4Address() + ", hostIp6Address=" + getHostIp6Address()
                + ", hostMacAddress=" + getHostMacAddress() + ", id=" + getId() + ", make=" + getMake()
                + ", manufacturer=" + getManufacturer() + ", model=" + getModel() + ", remoteType=" + getRemoteType()
                + ", serialNumber=" + getSerialNumber() + ", mCardSerialNumber = " + getMCardSerialNumber()
                + ", unitAddress=" + getUnitAddress() + ", properties[" + extraProperties.toString() + "]]";
    }
}
