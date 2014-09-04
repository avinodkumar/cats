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

import static javax.xml.ws.Service.create;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.comcast.cats.configuration.ApplicationContextProvider;
import com.comcast.cats.configuration.StringsDMHandler;
import com.comcast.cats.domain.Environment;
import com.comcast.cats.domain.RFPlant;
import com.comcast.cats.domain.Server;
import com.comcast.cats.domain.Service;
import com.comcast.cats.domain.ServiceType;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.DomainNotFoundException;
import com.comcast.cats.domain.exception.ServiceInstantiationException;
import com.comcast.cats.domain.service.EnvironmentService;
import com.comcast.cats.domain.util.AssertUtil;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.TraceEventDispatcher;
import com.comcast.cats.info.SettopInfo;
import com.comcast.cats.provider.exceptions.ProviderCreationException;
import com.comcast.cats.provider.factory.AudioProviderFactory;
import com.comcast.cats.provider.factory.ImageCompareProviderFactory;
import com.comcast.cats.provider.factory.OcrProviderFactory;
import com.comcast.cats.provider.factory.PowerProviderFactory;
import com.comcast.cats.provider.factory.RFControlProviderFactory;
import com.comcast.cats.provider.factory.RecorderProviderFactory;
import com.comcast.cats.provider.factory.RemoteProviderFactory;
import com.comcast.cats.provider.factory.TraceProviderFactory;
import com.comcast.cats.provider.factory.VideoProviderFactory;
import com.comcast.cats.provider.factory.impl.ImageCompareProviderFactoryImpl;
import com.comcast.cats.provider.factory.impl.PowerProviderFactoryImpl;
import com.comcast.cats.provider.factory.impl.RecorderProviderFactoryImpl;
import com.comcast.cats.provider.factory.impl.RemoteProviderFactoryImpl;
import com.comcast.cats.provider.factory.impl.VideoProviderFactoryImpl;
import com.comcast.cats.service.CatsWebService;
import com.comcast.cats.service.IRService;
import com.comcast.cats.service.IRServiceConstants;
import com.comcast.cats.service.PowerService;
import com.comcast.cats.service.PowerServiceConstants;

/**
 * Represents a CATS <code>Environment</code> , which effectively owns a series
 * of Racks and consequently a set of settops and servers. , which in turn
 * contains a <code>List</code> of <code>Service</code>.
 * 
 * Bean Scope is set to <code>prototype</code>, which means, Spring context will
 * return a new instance of this bean for every lookup.
 * 
 * @author subinsugunan
 * 
 * @see Environment
 * @see Server
 * @see Service
 * @see ApplicationContextProvider
 */
@Named
@Scope( "prototype" )
public class CatsEnvironment
{
    private EnvironmentService          envService;
    private StringsDMHandler            stringsDMHandler;
    private TraceEventDispatcher        traceDispatcher;
    private CatsEventDispatcher         dispatcher;

    private TraceProviderFactory        traceProviderFactory;
    private VideoProviderFactory        videoProviderFactory;
    private ImageCompareProviderFactory imageCompareProviderFactory;

    private RemoteProviderFactory       remoteProviderFactory;
    private PowerProviderFactory        powerProviderFactory;
    private AudioProviderFactory        audioProviderFactory;
    private OcrProviderFactory          ocrProviderFactory;
    private RecorderProviderFactory     recorderProviderFactory;
    private RFControlProviderFactory rfProviderFactory = null;
    
    private CatsProperties catsProperties;
    
    private static final String REQUEST_TIMEOUT_PROPERTY = "com.sun.xml.internal.ws.request.timeout";
    private static final String CONNECT_TIMEOUT_PROPERTY = "com.sun.xml.internal.ws.connect.timeout";

    protected static final int DEFAULT_REQUEST_TIMEOUT = 30 * 60 * 1000; //30 minutes
    protected static final int DEFAULT_CONNECT_TIMEOUT = 10 * 60 * 1000; //10 minutes
    
    private static final String CONNECT_TIMOEUT_TYPE = "connect";
    private static final String REQUEST_TIMOEUT_TYPE = "request";
    private String timeoutPropertyPattern = "cats.service.serviceType.timeoutType.timeout";
    private boolean                     isProxyCreated = false;
    private final Logger                LOGGER         = LoggerFactory.getLogger( getClass() );

    /**
     * Constructor
     * 
     * @param envService
     *            {@linkplain EnvironmentService}
     * @param stringsDMHandler
     *            {@linkplain StringsDMHandler}
     * @param traceDispatcher
     *            {@linkplain TraceEventDispatcher}
     * @param dispatcher
     *            {@linkplain CatsEventDispatcher}
     */
    @Inject
    public CatsEnvironment( EnvironmentService envService, StringsDMHandler stringsDMHandler,
            TraceEventDispatcher traceDispatcher, CatsEventDispatcher dispatcher,
            CatsProperties catsProperties)
    {
        this.envService = envService;
        this.stringsDMHandler = stringsDMHandler;
        this.traceDispatcher = traceDispatcher;
        this.dispatcher = dispatcher;
        this.catsProperties = catsProperties;
    }

    /**
     * To wire the settop with minimal services.
     * 
     * @param settop
     *            {@linkplain SettopImpl}
     * @throws {@linkplain ProviderCreationException}
     */
    public void wireSettop( SettopImpl settop ) throws ProviderCreationException
    {
        AssertUtil.isNullObject( settop );
        AssertUtil.isNullObject( settop.getSettopInfo() );
        AssertUtil.isNullOrEmpty( settop.getEnvironmentId() );

        if ( !isProxyCreated() )
        {
            /**
             * If control comes here it means that the environment was not
             * initialized. Note: if there are 10 settops on a given
             * environment, this will happen only once if the first try itself
             * succeeds, else it will happen 10 times(worst case).
             * 
             * also if the first init was only able to init minimal services,
             * all subsequent settops also will get only minimal services.
             */
            try
            {
                initEnvironment( settop.getEnvironmentId() );
            }
            catch ( ServiceInstantiationException e )
            {
                LOGGER.error( " Init of environment has errors " + e.getMessage() );
            }
            finally
            {
                // this check is to allow catsvision to come up if at least IR
                // and video provider is initialized
                // in the environment.
                if ( remoteProviderFactory == null || videoProviderFactory == null )
                {
                    throw new ProviderCreationException( "Could not initialize minimal services IR and Video" );
                }
                // if the exception is not thrown,it means we have minimal
                // services.
                setProxyCreated( true );

            }

        }

        settop.setVideo( ( null == videoProviderFactory ) ? null : videoProviderFactory
                .getProvider( ( SettopInfo ) settop ) );
        settop.setImageCompareProvider( ( null == imageCompareProviderFactory ) ? null : imageCompareProviderFactory
                .getProvider( ( SettopInfo ) settop ) );
        settop.setRemote( ( null == remoteProviderFactory ) ? null : remoteProviderFactory
                .getProvider( ( SettopInfo ) settop ) );
        settop.setPower( ( null == powerProviderFactory ) ? null : powerProviderFactory
                .getProvider( ( SettopInfo ) settop ) );
        settop.setTrace( ( null == traceProviderFactory ) ? null : traceProviderFactory
                .getProvider( ( SettopInfo ) settop ) );
        settop.setAudio( ( null == audioProviderFactory ) ? null : audioProviderFactory
                .getProvider( ( SettopInfo ) settop ) );
        settop.setRfControl( ( null == rfProviderFactory ) ? null : rfProviderFactory.getProvider( settop ));

        try
        {
            settop.setOCRProvider( ( null == ocrProviderFactory ) ? null : ocrProviderFactory.getProvider(
                    ( SettopInfo ) settop, new URI( settop.getVideo().getVideoURL() ) ) );
        }
        catch ( URISyntaxException uriSyntaxException )
        {
            throw new ProviderCreationException( "Failed to override OCR service interface for [MAC]["
                    + settop.getHostMacAddress() + "]. [URISyntaxException] Make sure videoURL is valid."
                    + uriSyntaxException.getMessage() );
        }

        settop.setRecorderProvider( ( null == recorderProviderFactory ) ? null : recorderProviderFactory
                .getProvider( ( SettopInfo ) settop ) );

    }

    private void initEnvironment( String environmentId ) throws ServiceInstantiationException
    {
        try
        {
            /**
             * expecting no exception or errors in the following 3 object
             * creations.
             */
            videoProviderFactory = new VideoProviderFactoryImpl( dispatcher );
            imageCompareProviderFactory = new ImageCompareProviderFactoryImpl( videoProviderFactory );

            Environment environment = envService.findById( environmentId );

            List< Server > servers = environment.getServers();

            if ( ( null != servers ) && ( !servers.isEmpty() ) )
            {
                for ( Server server : servers )
                {
                    List< Service > services = server.getServices();

                    if ( ( null != services ) && ( !services.isEmpty() ) )
                    {
                        for ( Service service : services )
                        {
                            createServiceProxy( service );
                        }
                    }
                    else
                    {
                        LOGGER.error( "No services found on server= " + server );
                        // throw new ServiceInstantiationException(
                        // "No services found in server= " + server );
                    }
                    createRestFullServiceProxies(server);
                }
            }
            else
            {
                throw new ServiceInstantiationException( "No servers found in environment = " + environment );
            }

        }
        catch ( DomainNotFoundException e )
        {
            throw new ServiceInstantiationException( e.getMessage() );
        }
    }
    private void createRestFullServiceProxies(Server server)
    {
    	if(server != null){
	        String catsServer = server.getHost();
	        if(catsServer != null && server.getPort() > 0 ){
	            catsServer += ":"+server.getPort();
	        }
	        LOGGER.info( "Creating proxy for RF Service: catsServer "+catsServer );
    	}
    }

    private void configureService( CatsWebService service, ServiceType serviceType )
    {
        LOGGER.trace( "configuring service "+service );
       
         int requestTimeout = getTimeoutValue(serviceType.toString().toLowerCase(), REQUEST_TIMOEUT_TYPE);
         int connectTimeout = getTimeoutValue(serviceType.toString().toLowerCase(), CONNECT_TIMOEUT_TYPE);
         
         LOGGER.info( "Request Timeout for "+serviceType+" is "+requestTimeout);
         LOGGER.info( "Connect Timeout for "+serviceType+" is "+connectTimeout );
         

        ((BindingProvider)service).getRequestContext().put(REQUEST_TIMEOUT_PROPERTY, requestTimeout );
        ((BindingProvider)service).getRequestContext().put(CONNECT_TIMEOUT_PROPERTY, connectTimeout );
    }
    
    /**
     * Gets the timeoutValue by walking through the 'property tree' to find if any values has to be set
     * to the webservices.
     * 
     * Please see {@link https://rally1.rallydev.com/#/4843363647d/detail/task/10627019604}
     * 
     * @param serviceType
     * @param timeoutType
     * @return
     */
    private int getTimeoutValue (String serviceType, String timeoutType){
        String timeoutProperty =  timeoutPropertyPattern.replace( "serviceType", serviceType ).replace( "timeoutType", timeoutType );
        String servicetTimeout = getPropertyValue(timeoutProperty, serviceType,timeoutType);

        Integer timeout = null;
        if(servicetTimeout != null && !servicetTimeout.isEmpty() ){
          try{
              timeout = Integer.parseInt( servicetTimeout.trim() ) * 1000; //convert to millis
          }catch(NumberFormatException e){
              LOGGER.warn( timeoutType+" Timeout Property for service "+serviceType+" is not in the right format "+e.getMessage() );
          }
        }
        if(timeout == null){
            if(timeoutType == CONNECT_TIMOEUT_TYPE){
                timeout = DEFAULT_CONNECT_TIMEOUT;
            }else if (timeoutType == REQUEST_TIMOEUT_TYPE){
                timeout = DEFAULT_REQUEST_TIMEOUT;
            }
        }
        return timeout;
    }
    
    /**
     * Recursive method that will check for values that can be set as timeout value for the web service.
     * 
     * Order in which properties are looked up:
     * 1) cats.service.ir.request.timeout
     * 2) cats.service.ir.timeout
     * 3) cats.service.request.timeout
     * 4) cats.service.timeout
     *  
     * @param property
     * @param serviceType
     * @param timeoutType
     * @return
     */
    private String getPropertyValue(String property, String serviceType, String timeoutType ){
        String propertyValueString = (String)catsProperties.getProperty( property);

        if(propertyValueString == null){
            String nextProperty = getNextTimeoutProperty(property,serviceType,timeoutType);
            if(nextProperty != null && !nextProperty.isEmpty()){
                propertyValueString = getPropertyValue(nextProperty,serviceType,timeoutType);
            }else{
                propertyValueString = null;
            }
        }
        return propertyValueString;
    }
    
    /**
     * Logic is very specific to the property definitions.
     * 
     * Order in which properties are looked up:
     * 1) cats.service.ir.request.timeout
     * 2) cats.service.ir.timeout
     * 3) cats.service.request.timeout
     * 4) cats.service.timeout
     *
     * @param poperty
     * @return
     */
    private String getNextTimeoutProperty(String currentProperty, String serviceType, String timeoutType ){
        String nextProperty= "";

        if(currentProperty.contains( serviceType ) && currentProperty.contains( timeoutType )){
            nextProperty = currentProperty.replace( timeoutType+".", "" );
        }else if(currentProperty.contains( serviceType ) && !currentProperty.contains( timeoutType )){
            nextProperty = currentProperty.replace( serviceType, timeoutType );
        }else if (!currentProperty.contains( serviceType ) && currentProperty.contains( timeoutType )){
            nextProperty = CatsProperties.SERVICE_TIMEOUT_PROPERTY;
        }

        return nextProperty;
    }

    private void createServiceProxy( Service domainService ) throws ServiceInstantiationException
    {
        try
        {
            AssertUtil.isNullObject( domainService, domainService.getServiceType(), domainService.getPath() );
            switch ( domainService.getServiceType() )
            {
            case IR:
                createRemoteServiceProxy( domainService.getPath() );
                LOGGER.info( "IR proxy created at :" + domainService.getPath() );
                break;
            case POWER:
                createPowerServiceProxy( domainService.getPath() );
                LOGGER.info( "Power proxy created at :" + domainService.getPath() );
                break;
//            case RECORDER: // server side recording. Uses VLC at the server
//                           // side.
//                createRecorderServiceProxy( domainService.getPath() );
//                LOGGER.info( "Video Recorder proxy created at :" + domainService.getPath() );
//                break;
            default:
                break;
            }
        }
        catch ( Exception e )
        {
            /**
             * if we throw exception here, further iteration on other servers
             * will be compromised
             */
            LOGGER.error( "Service instantiation failed " + e.getMessage() );
            // throw new ServiceInstantiationException( e.getMessage() );
        }

    }

    private void createRemoteServiceProxy( URL wsdlPath )
    {
        javax.xml.ws.Service service = create( wsdlPath, new QName( IRServiceConstants.NAMESPACE,
                IRServiceConstants.IMPL_STRING ) );
        IRService irService = service.getPort( IRService.class ) ;
        configureService( irService, ServiceType.IR );
        remoteProviderFactory = new RemoteProviderFactoryImpl( irService );
    }

    private void createPowerServiceProxy( URL wsdlPath )
    {
        javax.xml.ws.Service service = create( wsdlPath, new QName( PowerServiceConstants.NAMESPACE,
                PowerServiceConstants.IMPL_STRING ) );
        PowerService powerService = service.getPort( PowerService.class ) ;
        configureService( powerService, ServiceType.POWER );
        powerProviderFactory = new PowerProviderFactoryImpl( powerService );
    }

    private void createRecorderServiceProxy( URL serverURL )
    {

        recorderProviderFactory = new RecorderProviderFactoryImpl( serverURL.getHost() );
    }

    /**
     * check for proxy creation
     * 
     * @return boolean
     */
    public boolean isProxyCreated()
    {
        return isProxyCreated;
    }

    /**
     * set for proxy creation
     * 
     * @param isProxyCreated
     */
    public void setProxyCreated( boolean isProxyCreated )
    {
        this.isProxyCreated = isProxyCreated;
    }
}
