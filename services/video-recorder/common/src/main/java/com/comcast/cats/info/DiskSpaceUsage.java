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
package com.comcast.cats.info;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the disk space usage of a volume.
 * 
 * @author ssugun00c
 * 
 */
@XmlRootElement
public class DiskSpaceUsage
{
    /**
     * Total Space in KB
     */
    long           totalSpace;

    /**
     * Usable Space in KB
     */
    long           usableSpace;

    /**
     * Free Space in KB
     */
    long           freeSpace;

    /**
     * File handler
     */
    private String volumeLabel;

    public DiskSpaceUsage()
    {
        super();
    }

    public DiskSpaceUsage( File volume )
    {
        this.volumeLabel = volume.getAbsolutePath();
        this.totalSpace = volume.getTotalSpace() / 1024;
        this.usableSpace = volume.getUsableSpace() / 1024;
        this.freeSpace = volume.getFreeSpace() / 1024;
    }

    @XmlElement
    public long getTotalSpace()
    {
        return totalSpace;
    }

    public void setTotalSpace( long totalSpace )
    {
        this.totalSpace = totalSpace;
    }

    @XmlElement
    public long getUsableSpace()
    {
        return usableSpace;
    }

    public void setUsableSpace( long usableSpace )
    {
        this.usableSpace = usableSpace;
    }

    @XmlElement
    public long getFreeSpace()
    {
        return freeSpace;
    }

    public void setFreeSpace( long freeSpace )
    {
        this.freeSpace = freeSpace;
    }

    public String getVolumeLabel()
    {
        return volumeLabel;
    }

    public void setVolumeLabel( String volumeLabel )
    {
        this.volumeLabel = volumeLabel;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [volumeLabel=" + getVolumeLabel() + ", totalSpace=" + getTotalSpace() / 1024
                / 1024 + " GB, usableSpace=" + getUsableSpace() / 1024 / 1024 + " GB, freeSpace=" + getFreeSpace()
                / 1024 / 1024 + " GB]";
    }
}
