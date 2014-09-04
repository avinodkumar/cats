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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.comcast.cats.domain.Domain;
import com.comcast.cats.domain.util.ContentType;
import com.comcast.cats.domain.util.JAXBHelper;

/**
 * A custom implementation of
 * {@link org.springframework.http.converter.HttpMessageConverter
 * HttpMessageConverter} that can read JAXB marshalled,serialized objects. <br>
 * <br>
 * <b>Guidelines to code Server Side.</b>
 * 
 * <br>
 * <br>
 * e.g. <i>Servlet code</i>
 * 
 * <pre>
 * {
 *     &#064;code
 *     SettopDesc settop = new SettopDesc();
 *     // set properties
 *     // response.setContentType( &quot;application/octet-stream&quot; );
 *     response.setContentType( &quot;application/x-java-serialized-object&quot; );
 *     OutputStream out = response.getOutputStream();
 *     ObjectOutputStream objStream = new ObjectOutputStream( out );
 * 
 *     try
 *     {
 *         settop.writeExternal( objStream );
 *     }
 *     catch ( Exception e )
 *     {
 *         e.printStackTrace();
 *     }
 *     finally
 *     {
 *         if ( objStream != null )
 *         {
 *             objStream.close();
 *         }
 *         if ( out != null )
 *         {
 *             out.close();
 *         }
 *     }
 * }
 * </pre>
 * 
 * <br>
 * e.g. <i>Groovy code</i>
 * 
 * <pre>
 *      {@code
 *      Refer 'Servlet code' above
 *      }
 * </pre>
 * 
 * @author subinsugunan
 * 
 * @param <T>
 *            Any class that extends {@link Domain}
 * 
 * @see <a href="https://jira.springsource.org/browse/SPR-7002">SPR-7002</a>
 * @see <a href="https://jira.springsource.org/browse/SPR-7023">SPR-7023</a>
 */
public class JaxbSerializedHttpMessageConverter< T extends Domain > extends AbstractHttpMessageConverter< Object >
{
    /**
     * @see Domain
     */
    private static final String READ_METHOD_NAME = "readExternalObject";

    public JaxbSerializedHttpMessageConverter()
    {
        super( new MediaType( ContentType.APPLICATION_JAXB_SERIALIZED_OBJECT.getType(),
                ContentType.APPLICATION_JAXB_SERIALIZED_OBJECT.getSubtype() ) );
    }

    /**
     * Indicates whether the given class is supported by this converter.
     * 
     * @param clazz
     *            the class to test for support
     * @return <code>true</code> if supported; <code>false</code> otherwise
     */
    @Override
    public boolean supports( Class< ? > clazz )
    {
        return JAXBHelper.isCatsDomainClass( clazz );
    }

    /**
     * Reads the actualy object.
     * 
     * @param clazz
     *            the type of object to return
     * @param inputMessage
     *            the HTTP input message to read from
     * @return the converted object
     * @throws IOException
     *             in case of I/O errors
     * @throws HttpMessageNotReadableException
     *             in case of conversion errors
     */
    @Override
    protected Object readInternal( Class< ? > clazz, HttpInputMessage inputMessage ) throws IOException,
            HttpMessageNotReadableException
    {
        Object resultObj = null;

        try
        {
            Object domainObj = clazz.newInstance();
            InputStream responseStream = inputMessage.getBody();
            ObjectInputStream input = new ObjectInputStream( responseStream );
            Method readExternalObjectMethod = clazz.getMethod( READ_METHOD_NAME, ObjectInput.class );
            resultObj = ( Object ) readExternalObjectMethod.invoke( domainObj, input );
        }
        catch ( Exception e )
        {
            throw new HttpMessageNotReadableException( e.getMessage() );
        }

        return resultObj;
    }

    /**
     * Writes the actual object.
     * 
     * @param t
     *            the object to write to the output message
     * @param outputMessage
     *            the message to write to
     * @throws IOException
     *             in case of I/O errors
     * @throws HttpMessageNotWritableException
     *             in case of conversion errors
     */
    @Override
    protected void writeInternal( Object t, HttpOutputMessage outputMessage ) throws IOException,
            HttpMessageNotWritableException
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }

}