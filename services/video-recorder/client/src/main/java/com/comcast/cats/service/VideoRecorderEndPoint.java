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
package com.comcast.cats.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.service.VideoRecorderService;

/**
 * SOAP client for {@link VideoRecorderService}.
 * 
 * @author SSugun00c
 * 
 */
public class VideoRecorderEndPoint extends Service
{
    public VideoRecorderEndPoint() throws MalformedURLException
    {
        this( new URL( VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_WSDL_LOCATION ), new QName(
                VideoRecorderServiceConstants.NAMESPACE,
                VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_LOCAL_PART_NAME ) );
    }

    public VideoRecorderEndPoint( final URL wsdlDocumentLocation )
    {
        super( wsdlDocumentLocation, new QName( VideoRecorderServiceConstants.NAMESPACE,
                VideoRecorderServiceConstants.VIDEO_RECORDER_SERVICE_LOCAL_PART_NAME ) );
    }

    public VideoRecorderEndPoint( final URL wsdlDocumentLocation, final QName serviceName )
    {
        super( wsdlDocumentLocation, serviceName );
    }

    public VideoRecorderService getVideoRecorderServiceImplPort()
    {
        return super.getPort( VideoRecorderService.class );
    }
}
