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
package com.comcast.cats;

import static com.comcast.cats.configuration.ApplicationContextProvider.getApplicationContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.exception.ServiceInstantiationException;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.provider.PowerProviderServiceImpl;
import com.comcast.cats.provider.RemoteProviderServiceImpl;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.OcrProviderFactory;

/**
 * A map which keeps all the CatsEnvironment instances.
 * 
 * @author subinsugunan
 * 
 * @see CatsEnvironment
 * @see SettopFactoryImpl
 */
@Named
public class EnvironmentFactory extends HashMap< String, CatsEnvironment >
{
    private static final long         serialVersionUID      = 1L;
    private final Log                 LOGGER                = LogFactory.getLog( getClass() );

    private static final String       POWER_STUB_FIELD_NAME = "pwService";
    private static final String       IR_STUB_FIELD_NAME    = "irService";
    private static final String       AUDIO_STUB_FIELD_NAME = "audioMonitorService";
    private static final String       OCR_STUB_FIELD_NAME   = "ocrService";


    Map< String, OcrProviderFactory > providerFactoryMap    = new ConcurrentHashMap< String, OcrProviderFactory >();

    /**
     * Wire {@link SettopImpl} with a particular CATS {@link Environment}.
     * 
     * @param settop
     *            {@link SettopImpl}
     * @throws ServiceInstantiationException
     */
    public void wireSettop( SettopImpl settop ) throws ServiceInstantiationException
    {
        AssertUtil.isNullObject( settop, "settop is null" );
        AssertUtil.isNullObject( settop.getSettopInfo(), "settop.getSettopInfo() is null" );
        AssertUtil.isNullOrEmpty(
                settop.getEnvironmentId(),
                "Cannot instantiate services for settop. settop.getEnvironmentId() is null. Make sure ["
                        + settop.getHostMacAddress() + "]  is part of a CATS rack." );

        CatsEnvironment catsEnvironment;
        String environmentId = settop.getEnvironmentId();

        if ( this.containsKey( environmentId ) )
        {
            catsEnvironment = this.get( environmentId );
        }
        else
        {
            catsEnvironment = getApplicationContext().getBean( CatsEnvironment.class );
            this.put( environmentId, catsEnvironment );
        }

        try
        {
            catsEnvironment.wireSettop( settop );
        }
        catch ( ProviderCreationException e )
        {
            throw new ServiceInstantiationException( e );
        }

        if ( LOGGER.isInfoEnabled() )
        {
            printSettopBinding( settop );
        }
    }

    /**
     * Utility method to print the service bindings information in log
     * 
     * @param settop
     */
    private void printSettopBinding( SettopImpl settop )
    {
        LOGGER.info( "----------------Web Service Binding for [" + settop.getHostMacAddress() + "]-------------------" );
        try
        {
            if ( null != settop.getPower() )
            {
                LOGGER.info( getStubField( PowerProviderServiceImpl.class, POWER_STUB_FIELD_NAME ).get(
                        settop.getPower() ) );
            }

            if ( null != settop.getRemote() )
            {
                LOGGER.info( getStubField( RemoteProviderServiceImpl.class, IR_STUB_FIELD_NAME ).get(
                        settop.getRemote() ) );
            }

        }
        catch ( Exception e )
        {
            LOGGER.error( e.getMessage() );
        }
        LOGGER.info( "------------------------------------------------------------------------------" );
    }

    private Field getStubField( final Class< ? > clazz, final String fieldName )
    {
        Field field = null;
        try
        {
            field = clazz.getDeclaredField( fieldName );
            field.setAccessible( true );
        }
        catch ( Exception e )
        {
            LOGGER.error( e.getMessage() );
        }
        return field;
    }
}
