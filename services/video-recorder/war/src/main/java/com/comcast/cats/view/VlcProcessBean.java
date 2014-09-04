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
 * Represents a VLC process.
 * 
 * @author ssugun00c
 * 
 */
@ManagedBean
@RequestScoped
public class VlcProcessBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int               recordingId;
    private String            telnetHost;
    private int               telnetPort;
    private String            fileName;
    private double            size;
    private String            absolutePath;
    private String            httpPath;
    private boolean           playable         = false;
    private boolean           exists           = false;
    private String            command;

    public VlcProcessBean()
    {
        // TODO Auto-generated constructor stub
    }

    public VlcProcessBean( int recordingId, String telnetHost, int telnetPort, String fileName, double size,
            String absolutePath, String httpPath, boolean playable, boolean exists, String command )
    {
        super();
        this.recordingId = recordingId;
        this.telnetHost = telnetHost;
        this.telnetPort = telnetPort;
        this.fileName = fileName;
        this.size = size;
        this.absolutePath = absolutePath;
        this.httpPath = httpPath;
        this.playable = playable;
        this.exists = exists;
        this.command = command;
    }

    public String getTelnetHost()
    {
        return telnetHost;
    }

    public void setTelnetHost( String telnetHost )
    {
        this.telnetHost = telnetHost;
    }

    public int getTelnetPort()
    {
        return telnetPort;
    }

    public void setTelnetPort( int telnetPort )
    {
        this.telnetPort = telnetPort;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    public double getSize()
    {
        return size;
    }

    public void setSize( double size )
    {
        this.size = size;
    }

    public String getAbsolutePath()
    {
        return absolutePath;
    }

    public void setAbsolutePath( String absolutePath )
    {
        this.absolutePath = absolutePath;
    }

    public String getHttpPath()
    {
        return httpPath;
    }

    public void setHttpPath( String httpPath )
    {
        this.httpPath = httpPath;
    }

    public boolean isPlayable()
    {
        return playable;
    }

    public void setPlayable( boolean playable )
    {
        this.playable = playable;
    }

    public boolean isExists()
    {
        return exists;
    }

    public void setExists( boolean exists )
    {
        this.exists = exists;
    }

    public int getRecordingId()
    {
        return recordingId;
    }

    public void setRecordingId( int recordingId )
    {
        this.recordingId = recordingId;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [recordingId=" + getRecordingId() + ", telnetHost=" + getTelnetHost()
                + ", telnetPort=" + getTelnetPort() + ", fileName=" + getFileName() + ", size=" + getSize()
                + ", absolutePath=" + getAbsolutePath() + ", httpPath=" + getHttpPath() + ", playable=" + isPlayable()
                + ", exists=" + isExists() + "]";
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand( String command )
    {
        this.command = command;
    }
}
