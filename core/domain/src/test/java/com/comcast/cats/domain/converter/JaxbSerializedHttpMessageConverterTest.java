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
import com.comcast.cats.domain.converter.JaxbSerializedHttpMessageConverter;
import com.comcast.cats.domain.util.ContentType;
import com.comcast.cats.domain.util.SimpleListWrapper;

/**
 * Test case for {@link JaxbSerializedHttpMessageConverter}.
 * 
 * @author subinsugunan
 * 
 */
@SuppressWarnings( "deprecation" )
public class JaxbSerializedHttpMessageConverterTest
{
    private JaxbSerializedHttpMessageConverter< ? > jaxbSerializedHttpMessageConverter = new JaxbSerializedHttpMessageConverter< Domain >();

    @Test
    public void testMediaTypes()
    {
        List< MediaType > supportedMediaTypes = jaxbSerializedHttpMessageConverter.getSupportedMediaTypes();
        Assert.assertEquals( 1, supportedMediaTypes.size() );

        for ( MediaType mediaType : supportedMediaTypes )
        {
            Assert.assertEquals( ContentType.APPLICATION_JAXB_SERIALIZED_OBJECT.toString(), mediaType.toString() );
        }
    }

    @Test
    public void testIsSupportedClass()
    {
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Domain.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( SettopDesc.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Rack.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Server.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Allocation.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Component.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Environment.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( HardwareConnection.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( HardwareDevice.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Location.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Reservation.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( Service.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( SettopGroup.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( User.class ) );
        Assert.assertTrue( jaxbSerializedHttpMessageConverter.supports( UserGroup.class ) );

        Assert.assertFalse( jaxbSerializedHttpMessageConverter.supports( String.class ) );
        Assert.assertFalse( jaxbSerializedHttpMessageConverter.supports( SimpleListWrapper.class ) );
    }

    @Test( expected = HttpMessageNotReadableException.class )
    public void testReadInternalException() throws IOException
    {
        List< Object > list = new ArrayList< Object >();
        list.add( new SettopDesc() );

        InputStream inputStream = EasyMock.createMock( InputStream.class );

        HttpInputMessage inputMessage = EasyMock.createMock( HttpInputMessage.class );
        expect( inputMessage.getBody() ).andThrow( new HttpMessageNotReadableException( "" ) );
        EasyMock.replay( inputStream, inputMessage );

        Object obj = jaxbSerializedHttpMessageConverter.readInternal( SettopDesc.class, inputMessage );
        Assert.assertNull( obj );
        EasyMock.verify( inputMessage );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testWriteInternal()
    {
        try
        {
            jaxbSerializedHttpMessageConverter.writeInternal( null, null );
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
