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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.comcast.cats.recorder.domain.Recording;
import com.comcast.cats.service.WebServiceReturn;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * Web service response specific to video recording.
 * 
 * @author SSugun00c
 * 
 */
@XmlRootElement
public class VideoRecorderResponse extends WebServiceReturn
{
    private static final long serialVersionUID = 1L;

    private Recording         recording;

    private List< Recording > recordingList;

    private String            exception;

    public VideoRecorderResponse()
    {
        super();
    }

    public VideoRecorderResponse( WebServiceReturnEnum result, String message )
    {
        super( result, message );
    }

    public VideoRecorderResponse( WebServiceReturnEnum result )
    {
        super( result );
    }

    @XmlElement
    public Recording getRecording()
    {
        return recording;
    }

    public void setRecording( Recording recording )
    {
        this.recording = recording;
    }

    @XmlElementWrapper( name = "recordingList" )
    @XmlElement( name = "recording" )
    public List< Recording > getRecordingList()
    {
        return recordingList;
    }

    public void setRecordingList( List< Recording > recordingList )
    {
        this.recordingList = recordingList;
    }

    @XmlElement
    public String getException()
    {
        return exception;
    }

    public void setException( String exception )
    {
        this.exception = exception;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + " [result=" + getResult() + ", message=" + getMessage() + ", recording="
                + getRecording() + ", recordingList=" + getRecordingList() + "]";
    }

}
