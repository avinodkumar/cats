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

import java.net.MalformedURLException;
import java.net.URI;

import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.VideoProviderFactory;
import com.comcast.cats.video.service.VideoServiceImpl;

/**
 * Provider factory implementation for video server
 * 
 * @author SSugun00c
 * 
 * @see VideoProvider
 */
public class VideoProviderFactoryImpl extends ProviderFactoryImpl< VideoProvider > implements VideoProviderFactory
{
    private CatsEventDispatcher dispatcher;

    public VideoProviderFactoryImpl( CatsEventDispatcher dispatcher )
    {
        super();
        this.dispatcher = dispatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VideoProvider getProvider( SettopInfo settop ) throws ProviderCreationException
    {
        LOGGER.trace( "Creating VideoProvider for [" + settop.getHostMacAddress() + "]" );

        AssertUtil.isNull( settop.getVideoPath(), "Cannot create VideoProvider. VideoPath is null" );

        VideoServiceImpl video = null;
        try
        {
            video = new VideoServiceImpl( dispatcher, settop.getVideoPath() );
            video.setParent( settop );
        }
        catch ( MalformedURLException e )
        {
            throw new ProviderCreationException( "Error creating VideoProvider. " + e.getMessage() );
        }

        return video;
    }

    @Override
    public VideoProvider getProvider( SettopInfo settop, URI string ) throws ProviderCreationException
    {
        throw new UnsupportedOperationException( "getProvider(SettopInfo settop, URI string) for "
                + getClass().getName()
                + " is not implemented. Please use getProvider(SettopInfo settop) constructor instead." );
    }

}
