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
package com.comcast.cats.jenkins.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.util.JenkinsClientProperties;

/**
 * An abstraction of common code to connect to the web service. All service
 * classes, which talks to the web service should extends this.
 * 
 * @author SSugun00c
 * 
 */
public abstract class AbstractService
{
    protected final Logger          LOGGER                  = LoggerFactory.getLogger( getClass() );

    private static final String     REQUEST_URL_SUFFIX      = "/api/xml";
    private static final String     CONTENT_TYPE            = "Content-Type";
    private static final String     APPLICATION_XML         = "application/xml";

    private JenkinsClientProperties jenkinsClientProperties = new JenkinsClientProperties();

    /**
     * Typical web service client class to get a string representation of the
     * response.
     * 
     * @param requestUrl
     *            Relative request URL
     * @param mapperClass
     *            Class to which the response should cast to.
     * @return JAXB deserialized response
     */
    protected Object getForObject( final String requestUrl, Class< ? > mapperClass, String parameters )
    {
        Object domainObject = null;
        HttpClient client = new HttpClient();

        try
        {
            PostMethod request = new PostMethod();
            request.setURI( new URI( getAbsoluteUrl( requestUrl + REQUEST_URL_SUFFIX + parameters ), false ) );
            request.addRequestHeader( CONTENT_TYPE, APPLICATION_XML );
            String apiToken = jenkinsClientProperties.getJenkinsApiToken();
            if ( !apiToken.isEmpty() )
            {
                request.setDoAuthentication( true );
            }
            domainObject = sendRequestToJenkins( mapperClass, domainObject, client, request, apiToken );

        }
        catch ( Exception e )
        {
            LOGGER.error( e.getMessage() );
        }

        return domainObject;
    }

    /**
     * Sends Http request to Jenkins server and read the respose.
     * 
     * @param mapperClass
     * @param domainObject
     * @param client
     * @param request
     * @return
     * @throws NumberFormatException
     * @throws IOException
     * @throws HttpException
     * @throws URIException
     */
    private Object sendRequestToJenkins( Class< ? > mapperClass, Object domainObject, HttpClient client,
            HttpMethodBase request, String apiToken ) throws NumberFormatException, IOException, HttpException,
            URIException
    {
        String passwdord = apiToken;
        if ( apiToken.isEmpty() )
        {
            // Set jenkins password if no API token is present
            passwdord = jenkinsClientProperties.getJenkinsPassword();
        }
        client.getState().setCredentials(
                new AuthScope( jenkinsClientProperties.getJenkinsHost(), new Integer(
                        jenkinsClientProperties.getJenkinsPort() ), AuthScope.ANY_REALM ),
                new UsernamePasswordCredentials( jenkinsClientProperties.getJenkinsUsername(), passwdord ) );
        if ( !apiToken.isEmpty() )
        {
            client.getParams().setAuthenticationPreemptive( true );
        }

        int responseCode = client.executeMethod( request );

        LOGGER.info( "[REQUEST][" + request.getURI().toString() + "]" );
        LOGGER.info( "[STATUS][" + request.getStatusLine().toString() + "]" );

        if ( HttpStatus.SC_OK == responseCode )
        {
            try
            {
                Serializer serializer = new Persister();
                domainObject = serializer.read( mapperClass, request.getResponseBodyAsStream(), false );
            }
            catch ( Exception e )
            {
                LOGGER.error( e.getMessage() );
            }
        }
        return domainObject;
    }

    /**
     * Get fully qualified path for the request
     * 
     * @param requestUrl
     * @return
     */
    private String getAbsoluteUrl( String requestUrl )
    {
        String absoluteUrl = jenkinsClientProperties.getJenkinsServieBaseUrl();

        if ( null != requestUrl )
        {
            absoluteUrl += requestUrl;
        }
        return absoluteUrl;
    }

    /**
     * Perform build Operations Via GET
     * 
     * @param requestUrl
     *            Relative request URL
     * @param mapperClass
     *            Class to which the response should cast to.
     * @return JAXB deserialized response
     */
    protected Object performBuildOperationsViaGet( final String requestUrl, Class< ? > mapperClass )
    {
        Object domainObject = null;
        HttpClient client = new HttpClient();
        try
        {
            GetMethod request = new GetMethod();
            request.setURI( new URI( getAbsoluteUrl( requestUrl ), false ) );
            request.addRequestHeader( CONTENT_TYPE, APPLICATION_XML );
            String apiToken = jenkinsClientProperties.getJenkinsApiToken();
            if ( !apiToken.isEmpty() )
            {
                request.setDoAuthentication( true );
            }

            domainObject = sendRequestToJenkins( mapperClass, domainObject, client, request, apiToken );
        }
        catch ( Exception e )
        {
            LOGGER.error( e.getMessage() );
        }

        return domainObject;
    }

    // TODO: Not tested
    /**
     * Create Project Via GET
     * 
     * @param requestUrl
     *            Relative request URL
     * @param mapperClass
     *            Class to which the response should cast to.
     * @return JAXB deserialized response
     */
    protected Object createProject( final String requestUrl, Class< ? > mapperClass, File config )
    {
        Object domainObject = null;
        HttpClient client = new HttpClient();
        try
        {
            PostMethod request = new PostMethod( getAbsoluteUrl( requestUrl ) );
            request.addRequestHeader( CONTENT_TYPE, APPLICATION_XML );
            RequestEntity entity = new FileRequestEntity( config, "text/xml; charset=UTF-8" );
            request.setRequestEntity( entity );
            String apiToken = jenkinsClientProperties.getJenkinsApiToken();
            if ( !apiToken.isEmpty() )
            {
                request.setDoAuthentication( true );
            }

            domainObject = sendRequestToJenkins( mapperClass, domainObject, client, request, apiToken );

        }
        catch ( Exception e )
        {
            LOGGER.error( e.getMessage() );
        }

        return domainObject;
    }

}
