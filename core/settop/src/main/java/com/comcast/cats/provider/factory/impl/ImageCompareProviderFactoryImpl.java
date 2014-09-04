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
import java.util.Properties;

import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.ImageCompareProvider;
import com.comcast.cats.provider.VideoProvider;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.ImageCompareProviderFactory;
import com.comcast.cats.provider.factory.VideoProviderFactory;
import com.comcast.cats.provider.impl.ImageCompareProviderImpl;

/**
 * Provider factory implementation for ImageCompare
 * 
 * @author SSugun00c
 * 
 * @see ImageCompareProvider
 * @see VideoProviderFactory
 */
public class ImageCompareProviderFactoryImpl extends ProviderFactoryImpl< ImageCompareProvider > implements
        ImageCompareProviderFactory
{

    private static final String  IMAGECOMPARE_PROPERTIES_FILE = "imagecompare.properties";

    private VideoProviderFactory videoProviderFactory;

    /**
	 * Constructor
	 * 
	 * @param videoProviderFactory {@linkplain VideoProviderFactory}
	 */
    public ImageCompareProviderFactoryImpl( VideoProviderFactory videoProviderFactory )
    {
        this.videoProviderFactory = videoProviderFactory;
    }

    /**
	 * to get the provider.
	 * 
	 * @param settop {@linkplain SettopInfo}
	 * @return ImageCompareProvider
	 * @throws ProviderCreationException
	 */
    @Override
    public ImageCompareProvider getProvider( SettopInfo settop ) throws ProviderCreationException
    {
        LOGGER.trace( "Creating ImageCompareProvider for [" + settop.getHostMacAddress() + "]" );

        VideoProvider videoProvider = videoProviderFactory.getProvider( settop );
        ImageCompareProvider icProvider = getProvider( videoProvider );
        return icProvider;
    }
    

    /**
	 * to get the provider.
	 * 
	 * @param videoProvider {@linkplain VideoProvider}
	 * @return ImageCompareProvider
	 * @throws ProviderCreationException
	 */
    public static ImageCompareProvider getProvider( VideoProvider videoProvider ) throws ProviderCreationException
    {
        AssertUtil.isNull( videoProvider, "Cannot create ImageCompareProvider. VideoProvider is null" );

        ImageCompareProvider icProvider = null;
        Properties props = new Properties();
        try
        {
            props.load( ImageCompareProviderFactoryImpl.class.getClassLoader().getResourceAsStream( IMAGECOMPARE_PROPERTIES_FILE ) );
            icProvider = new ImageCompareProviderImpl( ImageCompareProviderFactoryImpl.class, videoProvider, 0, 0, props );
        }
        catch ( Exception e )
        {
            throw new ProviderCreationException( "Error creating ImageCompareProvider. " + e.getMessage() );
        }

        return icProvider;
    }
    
    /**
	 * to get the provider.
	 * 
	 * @param videoProvider {@linkplain VideoProvider}
	 * @param resourceClass
	 * @return ImageCompareProvider
	 * @throws ProviderCreationException
	 */
    public static ImageCompareProvider getProvider( VideoProvider videoProvider, Class<?> resourceClass ) throws ProviderCreationException
    {
        AssertUtil.isNull( videoProvider, "Cannot create ImageCompareProvider. VideoProvider is null" );

        ImageCompareProvider icProvider = null;
        Properties props = new Properties();
        try
        {
            props.load( ImageCompareProviderFactoryImpl.class.getClassLoader().getResourceAsStream( IMAGECOMPARE_PROPERTIES_FILE ) );
            icProvider = new ImageCompareProviderImpl( resourceClass, videoProvider, 0, 0, props );
        }
        catch ( Exception e )
        {
            throw new ProviderCreationException( "Error creating ImageCompareProvider. " + e.getMessage() );
        }

        return icProvider;
    }

    @Override
    public ImageCompareProvider getProvider( SettopInfo settop, URI string ) throws ProviderCreationException
    {
        throw new UnsupportedOperationException( "getProvider(SettopInfo settop, URI string) for "
                + getClass().getName()
                + " is not implemented. Please use getProvider(SettopInfo settop) constructor instead." );
    }

}
