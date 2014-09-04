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
package com.comcast.cats.domain.converter;

import static org.easymock.EasyMock.expect;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.easymock.annotation.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.Component;
import com.comcast.cats.domain.Domain;
import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.HardwareConnection;
import com.comcast.cats.domain.HardwareDevice;
import com.comcast.cats.domain.Location;
import com.comcast.cats.domain.Rack;
import com.comcast.cats.domain.Reservation;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopGroup;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.UserGroup;
import com.comcast.cats.domain.util.ContentType;
import com.comcast.cats.domain.util.JAXBHelper;
import com.comcast.cats.domain.util.SimpleListWrapper;

/**
 * Test case for {@link JaxbListHttpMessageConverter}.
 * 
 * @author subinsugunan
 */
@SuppressWarnings( "deprecation" )
@RunWith( PowerMockRunner.class )
@PrepareForTest( JAXBHelper.class )
@PowerMockIgnore( "org.apache.log4j.*" )
public class JaxbListHttpMessageConverterTest
{
    private JaxbListHttpMessageConverter jaxbListHttpMessageConverter = new JaxbListHttpMessageConverter();

    @Mock
    private JAXBHelper                   jaxbHelper;

    @Test
    public void testMediaTypes()
    {
        List< MediaType > supportedMediaTypes = jaxbListHttpMessageConverter.getSupportedMediaTypes();
        Assert.assertEquals( 1, supportedMediaTypes.size() );

        for ( MediaType mediaType : supportedMediaTypes )
        {
            Assert.assertEquals( ContentType.APPLICATION_JAXB_SERIALIZED_LIST.toString(), mediaType.toString() );
        }
    }

    @Test
    public void testIsSupportedClass()
    {
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Domain.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( SettopDesc.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Rack.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Server.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Allocation.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Component.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Environment.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( HardwareConnection.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( HardwareDevice.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Location.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Reservation.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( Service.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( SettopGroup.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( User.class ) );
        Assert.assertTrue( jaxbListHttpMessageConverter.supports( UserGroup.class ) );

        Assert.assertFalse( jaxbListHttpMessageConverter.supports( String.class ) );
        Assert.assertFalse( jaxbListHttpMessageConverter.supports( SimpleListWrapper.class ) );
    }

    @SuppressWarnings( "static-access" )
    @Test
    public void testReadInternal() throws HttpMessageNotReadableException, IOException
    {
        Assert.assertNotNull( jaxbHelper );
        List< Object > list = new ArrayList< Object >();
        list.add( new SettopDesc() );

        InputStream inputStream = EasyMock.createMock( InputStream.class );

        HttpInputMessage inputMessage = EasyMock.createMock( HttpInputMessage.class );
        expect( inputMessage.getBody() ).andReturn( inputStream );
        EasyMock.replay( inputStream, inputMessage );

        PowerMock.mockStatic( JAXBHelper.class );
        EasyMock.expect( jaxbHelper.readExternalList( inputStream, SettopDesc.class ) ).andReturn( list );
        PowerMock.replayAll();

        @SuppressWarnings( "unchecked" )
        List< Object > result = ( List< Object > ) jaxbListHttpMessageConverter.readInternal( SettopDesc.class,
                inputMessage );
        EasyMock.verify( inputMessage );
        PowerMock.verifyAll();
        Assert.assertNotNull( result );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testWriteInternal()
    {
        try
        {
            jaxbListHttpMessageConverter.writeInternal( null, null );
        }
        catch ( HttpMessageNotWritableException e )
        {
            Assert.fail( e.getMessage() );
        }
        catch ( IOException e )
        {
            Assert.fail( e.getMessage() );
        }
    }
}
