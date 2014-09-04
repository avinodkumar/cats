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
package com.comcast.cats.provider.factory.impl;

import java.net.URI;

import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.RecorderProvider;
import com.comcast.cats.provider.VideoRecorderRESTProviderImpl;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.RecorderProviderFactory;

/**
 * Provider factory implementation for Video Recorder Provider
 * 
 * @author sajayjk
 * 
 * @see SettopRecorderProvider
 */
public class RecorderProviderFactoryImpl extends ProviderFactoryImpl< RecorderProvider > implements
        RecorderProviderFactory
{

    String videoRecorderServerHost;

    /**
     * Constructor
     * 
     * @param videoRecorderServerHost
     */
    public RecorderProviderFactoryImpl( String videoRecorderServerHost )
    {
        super();
        this.videoRecorderServerHost = videoRecorderServerHost;
    }

    /**
     * to get the provider.
     * 
     * @param settop
     * @return RecorderProvider
     */
    @Override
    public RecorderProvider getProvider( SettopInfo settop ) throws ProviderCreationException
    {
        VideoRecorderRESTProviderImpl recorderImpl = null;
        if ( settop != null && videoRecorderServerHost != null && !videoRecorderServerHost.isEmpty() )
        {
            URI videoPath = settop.getVideoPath();
            if ( videoPath != null )
            {
                String host = parseVideoIpAddress( videoPath );
                Integer camera = parseVideoPort( videoPath );
                if ( host == null || host.isEmpty() || camera == -1 )
                {
                    throw new ProviderCreationException(
                            "VideoPath not recognized format by VideoRecoderProviderFactpry "+videoPath );
                }
                recorderImpl = new VideoRecorderRESTProviderImpl( host, camera, videoRecorderServerHost,
                        settop.getHostMacAddress() );
            }
            else
            {
                throw new ProviderCreationException( "Settop does not have a valid video path" );
            }
        }
        else
        {
            throw new ProviderCreationException( "Settop is null or "
                    + "RecorderProviderFactoryImpl is not configured properly : serverHost " + videoRecorderServerHost
                    + " Settop " + settop );
        }
        return recorderImpl;
    }

    private String parseVideoIpAddress( URI videoPath )
    {
        // axis://:/?camera=1&version=
        return videoPath.getHost();
    }

    private Integer parseVideoPort( URI videoPath )
    {
        // axis://:/?camera=1&version=
        Integer camera = -1;

        int index = videoPath.toString().indexOf( "camera=", 0 );
        if ( index != -1 )
        {
            index += "camera=".length();
            try
            {
                camera = Integer.parseInt( videoPath.toString().substring( index, index + 1 ) );
            }
            catch ( StringIndexOutOfBoundsException e )
            {
                LOGGER.warn( "VideoPath not recognized format by VideoRecoderProviderFactpry " );
                camera = -1;
            }
            catch ( NumberFormatException e )
            {
                LOGGER.warn( "VideoPath not recognized format by VideoRecoderProviderFactpry " );
                camera = -1;
            }
        }
        return camera;
    }

    @Override
    public RecorderProvider getProvider( SettopInfo settop, URI string ) throws ProviderCreationException
    {
        throw new UnsupportedOperationException( "getProvider(SettopInfo settop, URI string) for "
                + getClass().getName()
                + " is not implemented. Please use getProvider(SettopInfo settop) constructor instead." );
    }
}
