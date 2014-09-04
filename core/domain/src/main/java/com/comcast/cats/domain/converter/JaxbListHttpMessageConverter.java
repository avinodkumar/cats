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
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.comcast.cats.domain.util.ContentType;
import com.comcast.cats.domain.util.JAXBHelper;
import com.comcast.cats.domain.util.SimpleListWrapper;

/**
 * A custom implementation of
 * {@link org.springframework.http.converter.HttpMessageConverter
 * HttpMessageConverter} that can read JAXB marshalled {@link List} . <br>
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
 *     List&lt; SettopDesc &gt; settopList = new ArrayList&lt; SettopDesc &gt;();
 *     settopList.add( settop01 );
 *     settopList.add( settop02 );
 * 
 *     String xml = JAXBHelper.writeExternalList( settopList );
 * 
 *     response.setContentType( &quot;text/xml&quot; );
 *     PrintWriter printWriter = response.getWriter();
 *     printWriter.write( xml );
 * }
 * </pre>
 * 
 * <br>
 * e.g. <i>Groovy code</i>
 * 
 * <pre>
 *      {@code
 *      def settopDescListXml = JAXBHelper.writeExternalList(settopDescInstances as List)
 *      render(text:settopDescListXml,contentType:"text/xml",encoding:"UTF-8")
 *      }
 * </pre>
 * 
 * @author subinsugunan
 * @see JAXBHelper
 * @see SimpleListWrapper
 */
public class JaxbListHttpMessageConverter extends AbstractHttpMessageConverter< Object >
{

    public JaxbListHttpMessageConverter()
    {
        super( new MediaType( ContentType.APPLICATION_JAXB_SERIALIZED_LIST.getType(),
                ContentType.APPLICATION_JAXB_SERIALIZED_LIST.getSubtype() ) );
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
        List< Object > simpleListWrapper = null;

        InputStream responseStream = inputMessage.getBody();
        simpleListWrapper = JAXBHelper.readExternalList( responseStream, clazz );

        return simpleListWrapper;
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