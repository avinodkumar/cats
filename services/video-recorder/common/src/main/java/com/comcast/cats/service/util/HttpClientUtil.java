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
package com.comcast.cats.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.comcast.cats.info.VideoRecorderResponse;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.recorder.domain.Recording;

/**
 * A simple REST client API based on Apache HttpClient/SnakeYAML.
 * 
 * @author ssugun00c
 * 
 */
public class HttpClientUtil
{
    /**
     * log4j logger instance of this class.
     */
    private static Logger logger = LoggerFactory.getLogger( HttpClientUtil.class );

    /**
     * Singleton enforcer.
     */
    private HttpClientUtil()
    {
    }

    public static synchronized Object getForObject( String uri, Map< String, String > paramMap )
    {
        Object responseObject = new Object();

        HttpMethod httpMethod = new GetMethod( uri );

        if ( ( null != paramMap ) && ( !paramMap.isEmpty() ) )
        {
            httpMethod.setQueryString( getNameValuePair( paramMap ) );
        }

        Yaml yaml = new Yaml();

        HttpClient client = new HttpClient();
        InputStream responseStream = null;
        Reader inputStreamReader = null;

        try
        {
            int responseCode = client.executeMethod( httpMethod );

            if ( HttpStatus.SC_OK != responseCode )
            {
                logger.error( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.error( "[ METHOD   ] " + httpMethod.getName() );
                logger.error( "[ STATUS   ] " + responseCode );
            }
            else
            {
                logger.trace( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.trace( "[ METHOD   ] " + httpMethod.getName() );
                logger.trace( "[ STATUS   ] " + responseCode );
            }

            responseStream = httpMethod.getResponseBodyAsStream();
            inputStreamReader = new InputStreamReader( responseStream, VideoRecorderServiceConstants.UTF );
            responseObject = yaml.load( inputStreamReader );
        }
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }
        finally
        {
            cleanUp( inputStreamReader, responseStream, httpMethod );
        }

        return responseObject;
    }

    public static synchronized Object postForObject( String uri, Map< String, String > paramMap )
    {
        Object responseObject = new Object();

        HttpMethod httpMethod = new PostMethod( uri );

        if ( ( null != paramMap ) && ( !paramMap.isEmpty() ) )
        {
            httpMethod.setQueryString( getNameValuePair( paramMap ) );
        }

        Yaml yaml = new Yaml();

        HttpClient client = new HttpClient();
        InputStream responseStream = null;
        Reader inputStreamReader = null;

        try
        {
            int responseCode = client.executeMethod( httpMethod );

            if ( HttpStatus.SC_OK != responseCode )
            {
                logger.error( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.error( "[ METHOD   ] " + httpMethod.getName() );
                logger.error( "[ STATUS   ] " + responseCode );
            }
            else
            {
                logger.trace( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.trace( "[ METHOD   ] " + httpMethod.getName() );
                logger.trace( "[ STATUS   ] " + responseCode );
            }

            responseStream = httpMethod.getResponseBodyAsStream();
            inputStreamReader = new InputStreamReader( responseStream, VideoRecorderServiceConstants.UTF );
            responseObject = yaml.load( inputStreamReader );
        }
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }
        finally
        {
            cleanUp( inputStreamReader, responseStream, httpMethod );
        }

        return responseObject;
    }

    public static synchronized Object deleteForObject( String uri, Map< String, String > paramMap )
    {
        Object responseObject = new Object();

        HttpMethod httpMethod = new DeleteMethod( uri );

        if ( ( null != paramMap ) && ( !paramMap.isEmpty() ) )
        {
            httpMethod.setQueryString( getNameValuePair( paramMap ) );
        }

        Yaml yaml = new Yaml();

        HttpClient client = new HttpClient();
        InputStream responseStream = null;
        Reader inputStreamReader = null;

        try
        {
            int responseCode = client.executeMethod( httpMethod );

            if ( HttpStatus.SC_OK != responseCode )
            {
                logger.error( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.error( "[ METHOD   ] " + httpMethod.getName() );
                logger.error( "[ STATUS   ] " + responseCode );
            }
            else
            {
                logger.trace( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.trace( "[ METHOD   ] " + httpMethod.getName() );
                logger.trace( "[ STATUS   ] " + responseCode );
            }

            responseStream = httpMethod.getResponseBodyAsStream();
            inputStreamReader = new InputStreamReader( responseStream, VideoRecorderServiceConstants.UTF );
            responseObject = yaml.load( inputStreamReader );
        }
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }
        finally
        {
            cleanUp( inputStreamReader, responseStream, httpMethod );
        }

        return responseObject;
    }

    
    public static synchronized Object postForObject( String requestUrl, byte[] payload )
    {
        Object responseObject = new Object();

        PostMethod httpMethod = new PostMethod( requestUrl );

        InputStream responseStream = null;
        Reader inputStreamReader = null;

        httpMethod.addRequestHeader( VideoRecorderServiceConstants.CONTENT_TYPE,
                VideoRecorderServiceConstants.APPLICATION_XML );

        RequestEntity requestEntity = new ByteArrayRequestEntity( payload, VideoRecorderServiceConstants.UTF );
        httpMethod.setRequestEntity( requestEntity );

        try
        {
            int responseCode = new HttpClient().executeMethod( httpMethod );

            if ( HttpStatus.SC_OK != responseCode )
            {
                logger.error( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.error( "[ METHOD   ] " + httpMethod.getName() );
                logger.error( "[ STATUS   ] " + responseCode );
            }
            else
            {
                logger.trace( "[ REQUEST  ] " + httpMethod.getURI().toString() );
                logger.trace( "[ METHOD   ] " + httpMethod.getName() );
                logger.trace( "[ STATUS   ] " + responseCode );
            }

            responseStream = httpMethod.getResponseBodyAsStream();
            inputStreamReader = new InputStreamReader( responseStream, VideoRecorderServiceConstants.UTF );
            responseObject = new Yaml().load( inputStreamReader );
        }
        catch ( IOException ioException )
        {
            ioException.printStackTrace();
        }
        finally
        {
            cleanUp( inputStreamReader, responseStream, httpMethod );
        }

        return responseObject;
    }

    public static synchronized byte[] getPayload( Object domain, boolean prettyPrint )
    {
        byte[] payload = null;

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
            logger.error( "[ JAXBException   ] " + e.getMessage() );
        }

        logger.trace( "[ PAYLOAD  ] " + xml );

        if ( null != xml )
        {
            payload = xml.getBytes();
        }

        return payload;
    }

    public static synchronized VideoRecorderResponse stopRecordingByRecordingId( int recordingId )
    {
        VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) postForObject(
                getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STOP ),
                getParamMapByRecordingId( recordingId ) );
        return videoRecorderResponse;
    }

    public static synchronized VideoRecorderResponse startRecording( Recording recording )
    {
        VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) postForObject(
                getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_SUBMIT ), getPayload( recording, true ) );
        return videoRecorderResponse;
    }

    public static synchronized VideoRecorderResponse getStatusByRecordingId( int recordingId )
    {
        VideoRecorderResponse videoRecorderResponse = ( VideoRecorderResponse ) getForObject(
                getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_STATUS ),
                getParamMapByRecordingId( recordingId ) );
        return videoRecorderResponse;
    }

    /**
     * A generic method to create NameValuePair from Map.
     * 
     * @param paramMap
     * @return
     */
    private static synchronized NameValuePair[] getNameValuePair( Map< String, String > paramMap )
    {
        List< NameValuePair > parametersList = new ArrayList< NameValuePair >();

        for ( Map.Entry< String, String > entry : paramMap.entrySet() )
        {
            String key = entry.getKey();
            String value = entry.getValue();

            if ( ( null != key ) && ( null != value ) && ( !key.isEmpty() ) && ( !value.isEmpty() ) )
            {
                NameValuePair nameValuePair = new NameValuePair( key, value );
                parametersList.add( nameValuePair );
            }
        }

        NameValuePair[] parameters = parametersList.toArray( new NameValuePair[ parametersList.size() ] );

        return parameters;
    }

    private static synchronized String getRequestUri( String restRequest )
    {
        String requestUri = System
                .getProperty( VideoRecorderServiceConstants.SYSTEM_PROPERTY_CATS_PVR_REST_API_BASE_URL )
                + VideoRecorderServiceConstants.REST_REQUEST_INTERNAL_BASE_PATH + restRequest;
        return requestUri;
    }

    private static synchronized Map< String, String > getParamMapByRecordingId( int recordingId )
    {
        Map< String, String > paramMap = new HashMap< String, String >();
        paramMap.put( VideoRecorderServiceConstants.REST_QUERY_PARAM_RECORDING_ID, String.valueOf( recordingId ) );
        return paramMap;
    }

    /**
     * Helper method to clean up resources. This is important as the number of
     * open files/streams are limited in an operating system.
     * 
     * @param bufferedReader
     * @param inputStreamReader
     * @param responseStream
     * @param httpMethod
     * @param client
     */
    private static synchronized void cleanUp( Reader inputStreamReader, InputStream responseStream,
            HttpMethod httpMethod )
    {
        if ( inputStreamReader != null )
        {
            try
            {
                inputStreamReader.close();
            }
            catch ( IOException e )
            {
                logger.warn( "Exception caught trying to close inputStreamReader", e );
            }
        }
        if ( responseStream != null )
        {
            try
            {
                responseStream.close();
            }
            catch ( IOException e )
            {
                logger.warn( "Exception caught trying to close responseStream", e );
            }
        }
        if ( httpMethod != null )
        {
            httpMethod.releaseConnection();
        }
    }
}
