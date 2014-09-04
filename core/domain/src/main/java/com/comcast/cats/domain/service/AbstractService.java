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
package com.comcast.cats.domain.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.comcast.cats.domain.AllocationCategory;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.DomainServiceException;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.domain.util.CommonUtil;
import com.comcast.cats.domain.util.HtmlUtil;
import com.comcast.cats.domain.util.SSLUtil;

/**
 * <p>
 * Common features of Domain services. {@link RestTemplate} ,
 * {@link DomainProperties} and logger instances are available to subclass.
 * </p>
 * <p>
 * Since the configuration management system is running under HTTPS,
 * Any Java client which uses domain APIs need to have a valid SSL certificate
 * installed in the client machine.
 * </p>
 * <p>
 * As installing SSL client certificate in every client machine is not feasible,
 * we need to disable the SSL certificate validation. The static block here will
 * do exactly the same.
 * </p>
 * 
 * @author subinsugunan
 * 
 * @param <Type>
 */
@Named
public abstract class AbstractService< Type >
{
    static
    {
        SSLUtil.disableCertificateValidation();
    }

    @Inject
    protected RestTemplateProducer restTemplateProducer;

    @Inject
    protected CatsProperties       properties;

    /**
     * All subclass should use this.
     */
    protected final Logger         logger              = LoggerFactory.getLogger( getClass() );

    /**
     * Common constants used by domain service classes
     */

    protected static final Integer DEFAULT_COUNT       = 0;
    protected static final Integer DEFAULT_OFFSET      = 0;

    // Special characters
    protected static final String  BACK_SLASH          = "/";
    protected static final String  PARAM_SEPARATOR     = "&";
    protected static final String  QUESTION_MARK       = "?";
    protected static final String  EQUALS              = "=";

    // URL Parameters
    protected static final String  ALL                 = "all";
    protected static final String  LIST                = "list";
    protected static final String  LIST_AVAILABLE      = "list-available";
    protected static final String  LIST_ACTIVE         = "list-active";
    protected static final String  COUNT               = "count";
    protected static final String  COUNT_AVAILABLE     = "count-available";
    protected static final String  COUNT_ACTIVE        = "count-active";
    protected static final String  ACTIVE              = "active";
    protected static final String  AUTH_TOKEN          = "token";
    protected static final String  OFFSET              = "offset";
    protected static final String  PROPERTY            = "property";
    protected static final String  PROPERTY_NAME       = "name";
    protected static final String  PROPERTY_VALUE      = "value";
    protected static final String  CRITERIA            = "criteria";
    protected static final String  SHALLOW             = "shallow";
    protected static final String  ID                  = "id";
    protected static final String  MAC_ID              = "macid";
    protected static final String  DURATION            = "duration";
    protected static final String  SHOW                = "show";
    protected static final String  UPDATE              = "update";
    protected static final String  RELEASE             = "release";
    protected static final String  ALLOCATE            = "allocate";
    protected static final String  ALLOCATED           = "allocated";
    protected static final String  AVAILABLE           = "available";
    protected static final String  VERIFY              = "verify";

    protected static final String  ALLOCATION_CATEGORY = "category";
    protected static final String  REACQUIRE_FLAG      = "reacquire";
    // Domains
    protected static final String  SETTOP              = "settop";
    protected static final String  SETTOPS             = "settops";
    protected static final String  SETTOP_GROUP        = "settopGroup";
    protected static final String  SETTOP_GROUPS       = "settopGroups";
    protected static final String  RESERVATION         = "reservation";
    protected static final String  RESERVATIONS        = "reservations";
    protected static final String  RACK                = "rack";
    protected static final String  RACKS               = "racks";
    protected static final String  SLOTS               = "slots";
    protected static final String  ALLOCATION          = "allocation";
    protected static final String  ALLOCATIONS         = "allocations";
    protected static final String  COMPONENT           = "component";
    protected static final String  USER                = "user";
    protected static final String  USERS               = "users";
    protected static final String  USER_GROUPS         = "userGroups";
    protected static final String  SERVER              = "server";
    protected static final String  SERVICE             = "service";
    protected static final String  ENVIRONMENT         = "environment";

    protected static final String  MDS                 = "mds";

    private static final String    UTF_8               = "UTF-8";

    /**
     * To get the response as domain type.
     * 
     * @param requestUrl
     * @param domainClass
     * @return Type
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected Type getResponseAsDomain( String requestUrl, Class< Type > domainClass ) throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.GET, requestUrl );
        Type type = null;
        try
        {
            type = ( Type ) restTemplateProducer.getRestTemplate().getForObject( requestUrl, domainClass );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.GET, requestUrl );
        }

        return type;
    }

    /**
     * To get the response as String.
     * 
     * @param requestUrl
     * @return String
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected String getResponseAsString( String requestUrl ) throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.GET, requestUrl );
        String response = null;

        try
        {
            response = ( String ) restTemplateProducer.getRestTemplate().getForObject( requestUrl, String.class );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.GET, requestUrl );
        }

        return response;
    }

    /**
     * To get the response as number.
     * 
     * @param requestUrl
     * @return Integer
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected Integer getResponseAsNumber( String requestUrl ) throws DomainServiceException
    {
        Integer count = 0;
        count = Integer.parseInt( getResponseAsString( requestUrl ) );
        return count;
    }

    /**
     * To get the response as Boolean.
     * 
     * @param requestUrl
     * @return Boolean
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected Boolean getResponseAsBoolean( String requestUrl ) throws DomainServiceException
    {
        Boolean response = false;
        response = Boolean.parseBoolean( getResponseAsString( requestUrl ) );
        return response;
    }

    /**
     * To get the response as domain type.
     * 
     * @param requestUrl
     * @param clazz
     * @return List of Type
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    @SuppressWarnings( "unchecked" )
    protected List< Type > getResponseAsDomainList( String requestUrl, Class< Type > clazz )
            throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.GET, requestUrl );
        List< Type > responselist = Collections.emptyList();

        try
        {
            responselist = ( List< Type > ) restTemplateProducer.getRestTemplate().getForObject( requestUrl, clazz );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.GET, requestUrl );
        }
        return responselist;
    }

    /**
     * Post for domain object
     * 
     * @param requestUrl
     * @param clazz
     * @return Type
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected Type postForDomainObject( String requestUrl, Class< Type > clazz ) throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.POST, requestUrl );
        Type type = null;

        try
        {
            type = ( Type ) restTemplateProducer.getRestTemplate().postForObject( requestUrl, null, clazz );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.POST, requestUrl );
        }

        return type;
    }

    /**
     * Post for String
     * 
     * @param requestUrl
     * @return String
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected String postForString( String requestUrl ) throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.POST, requestUrl );
        String type = null;

        try
        {
            type = ( String ) restTemplateProducer.getRestTemplate().postForObject( requestUrl, null, String.class );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.POST, requestUrl );
        }

        return type;
    }

    /**
     * to put request
     * 
     * @param requestUrl
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected void put( String requestUrl ) throws DomainServiceException
    {
        validateAndLogRequest( HttpMethod.PUT, requestUrl );

        try
        {
            restTemplateProducer.getRestTemplate().put( requestUrl, null );
        }
        catch ( Exception e )
        {
            handleException( e, HttpMethod.PUT, requestUrl );
        }
    }

    /**
     * to handle exception
     * 
     * @param e
     * @param httpMethod
     * @param requestUrl
     * @throws DomainServiceException
     *             {@link DomainServiceException}
     */
    protected void handleException( Exception e, HttpMethod httpMethod, String requestUrl )
            throws DomainServiceException
    {
        if ( e instanceof HttpClientErrorException )
        {
            handleHttpClientErrorException( ( ( HttpClientErrorException ) e ), httpMethod, requestUrl );
        }
        throw new RuntimeException( "[REST-" + httpMethod
                + "] Error communicating with server. Make sure all the inputs are valid. " + e.getMessage() );
    }

    private void handleHttpClientErrorException( HttpClientErrorException httpClientErrorException,
            HttpMethod httpMethod, String requestUrl ) throws DomainServiceException
    {
        HttpStatus statusCode = httpClientErrorException.getStatusCode();
        String responseBody = httpClientErrorException.getResponseBodyAsString();
        String responseMessage = null;

        if ( ( null != responseBody ) && ( !responseBody.isEmpty() ) )
        {
            responseMessage = HtmlUtil.getErrorMessage( httpClientErrorException.getResponseBodyAsString() );
        }
        else
        {
            responseMessage = httpClientErrorException.getMessage();
        }

        logger.error( "[REST-" + httpMethod + "][HTTP-" + statusCode + "][URL-" + requestUrl + "] " + responseMessage );

        throw new DomainServiceException( responseMessage, statusCode.value() );
    }

    /**
     * to validate and log the request.
     * 
     * @param method
     * @param requestUrl
     */
    protected void validateAndLogRequest( HttpMethod method, String requestUrl )
    {
        AssertUtil.isNullOrEmpty( requestUrl, "Cannot process request. requestUrl cannot be null or empty" );
        logger.info( "[REST-" + method + "][URL-" + requestUrl + "]" );
    }

    /**
     * to get the base url.
     * 
     * @param domain
     * @return String
     */
    protected String getBaseUrl( String domain )
    {
        return properties.getConfigServerUrl() + domain + BACK_SLASH;
    }

    /**
     * to get the parameter map by token.
     * 
     * @return Map
     */
    protected Map< String, String > getParamMapByToken()
    {
        Map< String, String > params = new HashMap< String, String >();
        addAuthTokenToParamMap( params );
        return params;
    }

    /**
     * to get the parameter map by token.
     * 
     * @param authToken
     * @return Map
     */
    protected Map< String, String > getParamMapByToken( String authToken )
    {
        Map< String, String > params = new HashMap< String, String >();
        addAuthTokenToParamMap( params, authToken );
        return params;
    }

    /**
     * to get the parameter map by id
     * 
     * @param id
     * @param offset
     * @param count
     * @param shallow
     * @return Map
     */
    protected Map< String, String > getParamMapById( String id, Integer offset, Integer count, Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addOffsetToParamMap( params, offset, count );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * 
     * @param id
     * @param offset
     * @param count
     * @return
     */
    protected Map< String, String > getParamMapById( String id, Integer offset, Integer count )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addOffsetToParamMap( params, offset, count );
        return params;
    }

    /**
     * to get the parameter map by mac id.
     * 
     * @param macId
     * @return Map
     */
    protected Map< String, String > getParamMapByMacId( String macId )
    {
        Map< String, String > params = new HashMap< String, String >();
        addMacIdToParamMap( params, macId );
        return params;
    }

    /**
     * to get the parameter map by id and token.
     * 
     * @param id
     * @return Map
     */
    protected Map< String, String > getParamMapByIdAndToken( String id )
    {
        return getParamMapByIdAndToken( id, 0, 0 );
    }

    /**
     * to get the parameter map by id and token.
     * 
     * @param id
     * @param offset
     * @param count
     * @return Map
     */
    protected Map< String, String > getParamMapByIdAndToken( String id, Integer offset, Integer count )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addOffsetToParamMap( params, offset, count );
        addAuthTokenToParamMap( params );
        return params;
    }

    /**
     * to get the parameter map by id and token.
     * 
     * @param id
     * @param offset
     * @param count
     * @param isShallow
     * @return Map
     */
    protected Map< String, String > getParamMapByIdAndToken( String id, Integer offset, Integer count, Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addOffsetToParamMap( params, offset, count );
        addAuthTokenToParamMap( params );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * to get the parameter map by id and token.
     * 
     * @param id
     * @param authToken
     * @return Map
     */
    protected Map< String, String > getParamMapByIdAndToken( String id, String authToken )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addAuthTokenToParamMap( params, authToken );

        return params;
    }

    /**
     * to get the parameter map by id and duration.
     * 
     * @param id
     * @param duration
     * @return Map
     */
    protected Map< String, String > getParamMapByIdDurationAndToken( String id, Integer duration )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addDurationToParamMap( params, duration );
        addAuthTokenToParamMap( params );

        return params;
    }

    /**
     * to get the parameter map by id, duration and token.
     * 
     * @param id
     * @param duration
     * @param authToken
     * @return Map
     */
    protected Map< String, String > getParamMapByIdDurationAndToken( String id, Integer duration, String authToken )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addDurationToParamMap( params, duration );
        addAuthTokenToParamMap( params, authToken );
        return params;
    }

    /**
     * to get the parameter map by id, duration, token and allocationCategory.
     * 
     * @param id
     * @param duration
     * @param authToken
     * @param allocationCategory
     *            {@link AllocationCategory}
     * @param reacquire
     * @return Map
     */
    protected Map< String, String > getParamMapByIdDurationAndToken( String id, Integer duration, String authToken,
            AllocationCategory allocationCategory, Boolean reacquire )
    {
        Map< String, String > params = new HashMap< String, String >();
        addIdToParamMap( params, id );
        addDurationToParamMap( params, duration );
        addAuthTokenToParamMap( params, authToken );
        addAllocationCategoryToParamMap( params, allocationCategory.toString() );
        addReacquireFlagToParamMap( params, reacquire );
        return params;
    }

    /**
     * to get the parameter map by name.
     * 
     * @param name
     * @param offset
     * @param count
     * @return Map
     */
    protected Map< String, String > getParamMapByName( String name, Integer offset, Integer count, Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addNameToParamMap( params, name );
        addOffsetToParamMap( params, offset, count );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * to get the parameter map by name and token.
     * 
     * @param name
     * @param offset
     * @param count
     * @return Map
     */
    protected Map< String, String > getParamMapByNameAndToken( String name, Integer offset, Integer count,
            Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addNameToParamMap( params, name );
        addOffsetToParamMap( params, offset, count );
        addAuthTokenToParamMap( params );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * to get the parameter map by property values.
     * 
     * @param property
     * @param values
     * @param offset
     * @param count
     * @return Map
     */
    protected Map< String, String > getParamMapByPropertyValues( String property, String[] values, Integer offset,
            Integer count, Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addPropertyValuesToParamMap( params, property, values );
        addOffsetToParamMap( params, offset, count );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * to get the parameter map by property values and token.
     * 
     * @param property
     * @param values
     * @param offset
     * @param count
     * @param isShallow
     * @return Map
     */
    protected Map< String, String > getParamMapByPropertyValuesAndToken( String property, String[] values,
            Integer offset, Integer count, Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addPropertyValuesToParamMap( params, property, values );
        addOffsetToParamMap( params, offset, count );
        addAuthTokenToParamMap( params );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * to get the parameter map by criteria.
     * 
     * @param criteria
     * @param offset
     * @param count
     * @return Map
     */
    protected Map< String, String > getParamMapByCriteria( Map< String, String > criteria, Integer offset,
            Integer count, Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addCriteriaToParamMap( params, criteria );
        addOffsetToParamMap( params, offset, count );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * to get the parameter map by criteria and token.
     * 
     * @param criteria
     * @param offset
     * @param count
     * @return Map
     */
    protected Map< String, String > getParamMapByCriteriaAndToken( Map< String, String > criteria, Integer offset,
            Integer count, Boolean isShallow )
    {
        Map< String, String > params = new HashMap< String, String >();
        addCriteriaToParamMap( params, criteria );
        addOffsetToParamMap( params, offset, count );
        addAuthTokenToParamMap( params );
        addShallowToPramMap( params, isShallow );
        return params;
    }

    /**
     * to get the parameter map by offset and token.
     * 
     * @param offset
     * @param count
     * @return Map
     */
    protected Map< String, String > getParamMapByOffsetAndTocken( Integer offset, Integer count )
    {
        Map< String, String > params = new HashMap< String, String >();
        addOffsetToParamMap( params, offset, count );
        addAuthTokenToParamMap( params );
        return params;
    }

    protected Map< String, String > getParamMapByOffsetAndNoTocken( Integer offset, Integer count )
    {
        Map< String, String > params = new HashMap< String, String >();
        addOffsetToParamMap( params, offset, count );
        return params;
    }

    private void addIdToParamMap( Map< String, String > params, String id )
    {
        params.put( ID, id );
    }

    private void addMacIdToParamMap( Map< String, String > params, String macId )
    {
        params.put( MAC_ID, macId );
    }

    private void addNameToParamMap( Map< String, String > params, String name )
    {
        try
        {
            params.put( PROPERTY_NAME, URLEncoder.encode( name, UTF_8 ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
    }

    private void addAuthTokenToParamMap( Map< String, String > params )
    {
        params.put( AUTH_TOKEN, properties.getAuthToken() );
    }

    private void addAuthTokenToParamMap( Map< String, String > params, String authToken )
    {
        params.put( AUTH_TOKEN, authToken );
    }

    private void addAllocationCategoryToParamMap( Map< String, String > params, String authToken )
    {
        params.put( ALLOCATION_CATEGORY, authToken );
    }

    private void addReacquireFlagToParamMap( Map< String, String > params, Boolean reacquire )
    {
        params.put( REACQUIRE_FLAG, Boolean.toString( reacquire ) );
    }

    private void addDurationToParamMap( Map< String, String > params, Integer duration )
    {
        params.put( DURATION, Integer.toString( duration ) );
    }

    private void addPropertyValuesToParamMap( Map< String, String > params, String property, String[] values )
    {
        params.put( PROPERTY_NAME, property );
        params.put( PROPERTY_VALUE, CommonUtil.arrayToString( values ) );
    }

    private void addCriteriaToParamMap( Map< String, String > params, Map< String, String > criteria )
    {
        params.put( CRITERIA, CommonUtil.mapToString( criteria ) );
    }

    private void addShallowToPramMap( Map< String, String > params, Boolean isShallow )
    {
        params.put( SHALLOW, Boolean.toString( isShallow ) );
    }

    private void addOffsetToParamMap( Map< String, String > params, Integer offset, Integer count )
    {
        if ( count > 0 )
        {
            params.put( OFFSET, Integer.toString( offset ) );
            params.put( COUNT, Integer.toString( count ) );
        }
    }

    /**
     * To set the RestTemplate. mainly to support Testing
     * 
     * @param restTemplate
     */
    protected void setRestTemplate( RestTemplate restTemplate )
    {
        this.restTemplateProducer.setRestTemplate( restTemplate );
    }
}
