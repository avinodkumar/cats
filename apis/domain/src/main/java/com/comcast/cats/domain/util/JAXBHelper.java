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
package com.comcast.cats.domain.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.comcast.cats.domain.Domain;

/**
 * Helper class to support externalization
 * 
 * @author subinsugunan
 * 
 */
public final class JAXBHelper
{
    /**
     * Singleton enforcer.
     */
    private JAXBHelper()
    {
    }

    /**
     * Reads the {@link InputStream} of JAXB marshaled xml and creates an object
     * of the given Class
     * 
     * @param inputStream
     * @param clazz
     *            - The class of returned object.
     * @return An object of the specified class.
     */
    public static Object readExternal( InputStream inputStream, Class< ? > clazz )
    {
        Object object = null;

        try
        {
            JAXBContext context = JAXBContext.newInstance( clazz );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            object = ( Object ) unmarshaller.unmarshal( inputStream );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * Reads the {@link InputStream} of JAXB marshaled {@link List} of objects
     * and unmarshal it to the a {@link List} of objects of the given Class
     * 
     * @param inputStream
     * @param clazz
     *            - The class of the returning {@link List} content.
     * @return A list of objects of the specified class.
     */
    public static List< Object > readExternalList( InputStream inputStream, Class< ? > clazz )
    {
        SimpleListWrapper< Object > simpleListWrapper = readExternalListWrapper( inputStream, clazz );
        List< Object > list = simpleListWrapper.getList();
        return list;
    }

    @SuppressWarnings( "unchecked" )
    public static SimpleListWrapper< Object > readExternalListWrapper( InputStream inputStream, Class< ? > clazz )
    {
        SimpleListWrapper< Object > simpleListWrapper = null;

        try
        {
            JAXBContext context = JAXBContext.newInstance( SimpleListWrapper.class, clazz );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            simpleListWrapper = ( SimpleListWrapper< Object > ) unmarshaller.unmarshal( inputStream );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return simpleListWrapper;
    }

    public static String writeExternal( Object domain )
    {
        return writeExternal( domain, false );
    }

    /**
     * Convert the given object {@link List} of objects to JAXB compactable xml.
     * This make use of {@link SimpleListWrapper}
     * 
     * @param domain
     * @param prettyPrint
     * @return xml string representation of the domain object.
     */
    public static String writeExternal( Object domain, Boolean prettyPrint )
    {
        String xml = null;

        StringWriter writer = new StringWriter();
        try
        {
            JAXBContext context = JAXBContext.newInstance( domain.getClass() );

            Marshaller marshaller = context.createMarshaller();

            if ( prettyPrint )
            {
                marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            }

            marshaller.marshal( domain, writer );
            xml = writer.toString();
        }
        catch ( JAXBException e )
        {
            e.printStackTrace();
        }

        return xml;
    }

    public static String writeExternalList( List< ? extends Domain > domainList )
    {
        return writeExternalList( domainList, false );
    }

    /**
     * Convert the given object to JAXB compactable xml.
     * 
     * @param domain
     * @param prettyPrint
     * @return xml string representation of the domain object list.
     */
    @SuppressWarnings(
        { "unchecked", "rawtypes" } )
    public static String writeExternalList( List< ? extends Domain > domainList, Boolean prettyPrint )
    {
        String xml = null;

        if ( domainList.isEmpty() )
        {
            throw new IllegalArgumentException( "Empty List" );
        }

        StringWriter writer = new StringWriter();
        try
        {
            JAXBContext context = JAXBContext.newInstance( SimpleListWrapper.class, domainList.get( 0 ).getClass() );

            Marshaller marshaller = context.createMarshaller();

            if ( prettyPrint )
            {
                marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            }

            marshaller.marshal( new SimpleListWrapper( domainList ), writer );
            xml = writer.toString();
        }
        catch ( JAXBException e )
        {
            e.printStackTrace();
        }

        return xml;
    }

    /**
     * Check whether the class is a valid CATS domain class. If the class is
     * kind of {@link Domain}, this method will return a true.
     * 
     * @param clazz
     * @return
     */
    public static Boolean isCatsDomainClass( Class< ? > clazz )
    {
        Boolean isSupported = false;
        try
        {
            if ( Domain.class.isInstance( clazz.newInstance() ) )
            {
                isSupported = true;
            }
        }
        catch ( InstantiationException e )
        {
            e.printStackTrace();
        }
        catch ( IllegalAccessException e )
        {
            e.printStackTrace();
        }

        return isSupported;
    }
}
