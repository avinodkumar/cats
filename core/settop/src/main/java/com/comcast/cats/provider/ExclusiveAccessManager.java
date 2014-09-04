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
package com.comcast.cats.provider;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.AllocationNotFoundException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.AllocationService;
import com.comcast.cats.event.AllocationEvent;
import com.comcast.cats.event.CatsEventDispatcher;
import com.comcast.cats.event.ReacquireEvent;
import com.comcast.cats.provider.exceptions.ExclusiveAccessException;
import com.comcast.cats.provider.impl.SettopExclusiveAccessToken;

/**
 * This class provides the implementation specifics for managing settop locking
 * including the background thread management for verifying settops are still
 * usable. By reducing the service call frequency to every few minutes, a
 * reduction in network bandwidth and load will result when the allocation
 * service is bound to an external system.
 * 
 * A threaded implementation for verifying and updating allocations was chosen
 * to increase Settop operation performance by limiting the synchronous calls
 * during lock checking.
 * 
 * If a non threaded approach was used, at some point when a settop operation is
 * invoked it would be necessary to verify the current state directly against
 * the Allocation service. This would result in delay associated with potential
 * network latency for verifying the current allocation for the given settop.
 * 
 * @author cfrede001
 * @since 2.0.0
 */
@Named
public class ExclusiveAccessManager extends SettopLockHandler implements ExclusiveAccessProvider, Runnable
{
    private static Logger             logger               = LoggerFactory.getLogger( ExclusiveAccessManager.class );
    /**
     * Check the Allocation service every TIME_DELAY for each active allocation.
     */
    public static final Long          TIME_DELAY           = 2L * 60L * 1000L;
    public static final Integer       RETRY_COUNT          = 3;
    private boolean                   breakAllocationCheck = true;
    private final CatsEventDispatcher dispatcher;
    private Object                    waitObject           = new Object();

    /**
     * constructor
     * 
     * @param allocationService
     *            {@linkplain AllocationService}
     * @param authToken
     * @param catsEventDispatcher
     *            {@linkplain CatsEventDispatcher}
     */
    @Inject
    public ExclusiveAccessManager( AllocationService allocationService, @Value( "#{catsProperties.authToken}" )
    String authToken, CatsEventDispatcher catsEventDispatcher )
    {
        super( allocationService, authToken );
        this.dispatcher = catsEventDispatcher;
        logger.trace( "ExclusiveAccessManager Constructor" );
    }

    /**
     * Checks to see if object being checked is a Settop or BaseProvider that
     * contains a parent of Settop.
     * 
     * @param obj
     *            - Object being checked.
     * @return Settop object from class.
     * @throws SettopNotFoundException
     *             - If settop object is unattainable by BaseProvider call.
     */
    private Settop retrieveSettop( Object obj ) throws SettopNotFoundException
    {
        Settop settop = null;
        /**
         * We now need to determine if this is a Settop object through either
         * the BaseProvider reference or if the action was performed directly on
         * the Settop. Move from general Settop to more specific BaseProvider.
         */
        if ( obj instanceof Settop )
        {
            logger.trace( "retrieveSettop object instanceof Settop" );
            settop = ( Settop ) obj;
        }
        else if ( obj instanceof BaseProvider )
        {
            logger.trace( "retrieveSettop object instanceof BaseProvider" );
            BaseProvider bp = ( BaseProvider ) obj;
            if ( bp != null && ( bp.getParent() instanceof Settop ) )
            {
                logger.trace( "Object is BaseProvider has a parent of Settop" );
                settop = ( Settop ) bp.getParent();
            }
            else
            {
                throw new SettopNotFoundException( "Settop Not Found" );
            }
        }
        else
        {
            logger.trace( "Object instanceof " + obj.getClass() );

        }
        return settop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkExclusiveAccess( Object obj ) throws ExclusiveAccessException
    {
        Settop settop;
        try
        {
            settop = retrieveSettop( obj );
        }
        catch ( SettopNotFoundException snfe )
        {
            logger.error( snfe.getMessage() );
            throw new ExclusiveAccessException( snfe.getMessage() );
        }
        logger.trace( "Check to settop lock status." );

        if ( !isLocked( settop ) )
        {
            logger.trace( "SettopToken not found" );
            throw new ExclusiveAccessException(
                    "SettopToken not found. The lock(allocation) on this settop is no more valid" );
        }
        return true;
    }

    /**
     * post constructor
     */
    @PostConstruct
    public void construct()
    {
        logger.info( "Starting SettopExclusiveAccessEnforcer implementation Thread." );
        Thread exclusiveAccessManagerThread = new Thread( this );
        exclusiveAccessManagerThread.setDaemon( true );
        exclusiveAccessManagerThread.start();
    }

    /**
     * Background thread that will run forever keeping tabs on any allocations
     * against the AllocationService.
     */
    @Override
    public void run()
    {
        while ( breakAllocationCheck )
        {
            try
            {
                logger.info( "Checking existing Allocations : " + allocations.values() );
                for ( SettopExclusiveAccessToken token : allocations.values() )
                {
                    try
                    {
                        logger.info( "Checking existing Allocations for token : " + token );
                        boolean isValidAllocation = verifyAllocation( token );
                        if ( !isValidAllocation )
                        {
                            cleanupExpiredToken( token, true );
                        }
                    }
                    catch ( IOException ioException )
                    {
                        logger.error( "Verify call on allocation list failed with:" + ioException.getMessage() );
                        handleTokenRetry( token );
                    }
                    catch ( ExclusiveAccessException eae )
                    {
                        logger.error( "Got ExclusiveAccessException for :" + token.getSettopId() + " retry:"
                                + token.getRetries() );
                        handleTokenRetry( token );
                    }
                }
                synchronized ( waitObject )
                {
					// If the nofication happens during the above for loop, adding the below check to avoid undesirable wait of 'TIME_DELAY'
                    if(breakAllocationCheck)
					{
            			waitObject.wait( TIME_DELAY );
            		}
                }
            }
            catch ( Exception e )
            {
                logger.error( "Thread.sleep exception caught", e );
            }
        }
    }

    private void handleTokenRetry( SettopExclusiveAccessToken token )
    {
        if ( hasRetryExpired( token ) )
        {
            cleanupExpiredToken( token, false );
        }
    }

    private void cleanupExpiredToken( SettopExclusiveAccessToken token, boolean isReleaseConfirmed )
    {
        logger.error( "Going to cleanup allocation for :" + token.getSettop() );
        token.getSettop().setLocked( false );
        allocations.remove( token.getSettopId() );
        /**
         * If cleanup is happening as a result of expired retires, we are not
         * sure if the lock has actually been released . To avoid
         * zombie locks, we are doing a last try to release the
         * lock.This is done for acquired locks alone.
         */
        if ( !token.isReaquire() && !isReleaseConfirmed )
        {
            try
            {
                logger.error( "Retry expired,going to call release on [" + token + "]" );
                release( token.getSettop() );
            }
            catch ( AllocationException e )
            {
                logger.error( "Release call after retry expired failed for[" + token + "] got exception" + e );
            }
        }
        // Don't forget to send the Allocation break event.
        sendAllocationBreakEvent( token );
    }

    /**
     * Check to see if we've reached the retry count. Otherwise update the retry
     * count.
     * 
     * @param token
     * @return
     */
    private boolean hasRetryExpired( SettopExclusiveAccessToken token )
    {
        if ( token.getRetries() > RETRY_COUNT )
        {
            return true;
        }
        else
        {
            token.incrementRetries();
        }
        return false;
    }

    /**
     * Stops the Break Allocation Monitoring Thread.
     */
    public void killBreakAllocationThread()
    {
        logger.info( "Break Allocation Monitor Thread Flag Set to false" );
        breakAllocationCheck = false;
        synchronized ( waitObject )
        {
            waitObject.notifyAll();
        }
    }

    private void sendAllocationBreakEvent( SettopExclusiveAccessToken token )
    {
        AllocationEvent ae = null;
        // TODO - CEF Comment: I'm not sure why it is necessary to define a new
        // class for Reaquire events. This is still an allocation.
        // Boby: The re acquire breakage has to be differentiated so that we can
        // show appropriate message in CATS vision.
        if ( token.isReaquire() )
        {
            ae = new ReacquireEvent( token.getSettopId(), token.getAllocationId(), true, this );
        }
        else
        {
            ae = new AllocationEvent( token.getSettopId(), token.getAllocationId(), true, this );
        }
        if ( null != ae )
        {
            dispatcher.sendCatsEvent( ae );
        }
    }

    /**
     * Verify the allocation against the AllocationService if the time is right.
     * TODO - A background thread responsible for updating the allocation
     * information and verifying the allocation would be preferred. Then utilize
     * this data to allow or deny operations.
     * 
     * @param token
     *            {@linkplain SettopExclusiveAccessToken}
     * @throws {@link ExclusiveAccessException}
     * @throws IOException
     */
    protected boolean verifyAllocation( SettopExclusiveAccessToken token ) throws ExclusiveAccessException, IOException
    {
        // Initial assumption is that the allocation is valid.
        // So if we cant connect to the server, we wont be breaking any
        // allocations.
        boolean isvalid = true;
        try
        {

            if ( !token.isReaquire() )
            {
                logger.trace( "Attempting to update allocation information for ExclusiveAccessToken: "
                        + token.toString() );
                allocationService.update( token.getAllocationId() );
            }
            else
            {
                logger.trace( "Settop was reaquired,doing a verify call for ExclusiveAccessToken:" + token.toString() );
                isvalid = allocationService.verify( token.getAllocationId() );
            }
            token.clearRetries();
            token.update();

        }
        catch ( AllocationNotFoundException e )
        {
            // An Allocation not found exception means we invalidate the settop.
            isvalid = false;
        }// Catch All other exceptions to be on the safe side.
        catch ( Exception e )
        {
            // TODO - CEF comment: This feels very hacky to me...-Boby: will
            // revisit once i get this working
            if ( e.getMessage().contains( "I/O error" ) )
            {
                throw new IOException( e.getMessage() );
            }
            else
            {
                logger.error( "AllocationException caught", e );
                token.clearRetries();
                throw new ExclusiveAccessException( "Settop[" + token.getSettopId()
                        + "] failed exclusive access checking" );
            }
        }
        return isvalid;
    }
}
