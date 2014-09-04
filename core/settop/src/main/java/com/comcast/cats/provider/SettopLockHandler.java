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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.Settop;
import com.comcast.cats.domain.Allocation;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.service.AllocationService;
import com.comcast.cats.provider.impl.SettopExclusiveAccessToken;

/**
 * Minimum implementation for interacting with Allocation service given a known
 * authToken is available.
 * 
 * @author cfrede001
 * 
 */
class SettopLockHandler implements SettopExclusiveAccessEnforcer
{
    private static Logger                                     logger              = LoggerFactory
                                                                                          .getLogger( SettopExclusiveAccessEnforcer.class );

    protected final AllocationService                         allocationService;
    protected String                                          authToken;

    private StringBuilder                                     lockErrorMessage    = new StringBuilder( "" );
    private StringBuilder                                     releaseErrorMessage = new StringBuilder( "" );

    /**
     * Need to ensure that allocations are referenced through thread safe Map.
     */
    protected final Map< String, SettopExclusiveAccessToken > allocations         = new ConcurrentHashMap< String, SettopExclusiveAccessToken >();
    
    public SettopLockHandler( AllocationService allocationService, String authToken )
    {
        this.allocationService = allocationService;
        this.authToken = authToken;
    }

    /**
     * {@inheritDoc}
     */
    public void lock( Settop settop ) throws AllocationException
    {

        logger.trace( "executing settop lock" );

        if ( allocationService == null )
        {
            logger.error( "AllocationService not configured" );
            throw new AllocationException( "AllocationService not configured" );
        }

        Allocation allocation = allocationService.createByComponentId( settop.getId());

        //Generate and add the AllocationToken.
        createAllocationToken(settop, allocation, false);
    }
    /**
     * {@inheritDoc}
     */
    public void reacquire( Settop settop ) throws AllocationException
    {

        logger.trace( "executing settop reacquire" );

        if ( allocationService == null )
        {
            logger.error( "AllocationService not configured" );
            throw new AllocationException( "AllocationService not configured" );
        }

        Allocation allocation = allocationService.reacquireByComponentId( settop.getId());
        
        //Generate and add the AllocationToken.
        createAllocationToken(settop, allocation, true);
    }

    protected void createAllocationToken(Settop settop, Allocation allocation, boolean reaquire) throws AllocationException {
    	if ( null == allocation )
        {
    		String message=reaquire?"Reacquire":"Acquire";
            throw new AllocationException( message+" Failed for:"+settop.getHostMacAddress() );
        }
    	String allocationId = allocation.getId();
        logger.trace( "Allocation created successfully, using information to create token." );

        SettopExclusiveAccessToken token = new SettopExclusiveAccessToken( authToken, settop, allocationId, reaquire);
        allocations.put( settop.getId(), token );

        settop.setLocked( true );

        logger.trace( "ExclusiveAccessToken Created with Information: " + token.toString() );
    }
    
    /**
     * Return the allocation settop token if it is present in either the 
     * reacquired list or in allocation list.
     * 
     * @param settop
     *            - Settop attempting to be located.
     * @return SettopExclusiveAccessToken for the settop or null if doesn't
     *         exist.
     */
    private SettopExclusiveAccessToken getSettopExclusiveAccessToken( Settop settop )
    {
    	SettopExclusiveAccessToken token=getSettopTokenFromAllocatonList(settop);
        return token;
    }
    /**
     * Return the allocation settop token from the allocations list
     * 
     * @param settop
     *            - Settop attempting to be located.
     * @return SettopExclusiveAccessToken for the settop or null if doesn't
     *         exist.
     */
    private SettopExclusiveAccessToken getSettopTokenFromAllocatonList( Settop settop )
    {
    	return allocations.get( settop.getId() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void release( Settop settop ) throws AllocationException
    {
        SettopExclusiveAccessToken token = getSettopTokenFromAllocatonList( settop );
        if ( token != null )
        {
        	if(!token.isReaquire()) {
        		allocationService.release( token.getAllocationId() );
        	}
            logger.trace( "ExclusiveAccessToken Information:\n" + token.toString() );
            logger.trace( "ExclusiveAccessToken is being destroyed" );
            allocations.remove( settop.getId() );
            settop.setLocked( false );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLocked( Settop settop )
    {
        if ( getSettopExclusiveAccessToken( settop ) != null )
        {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify( Settop settop ) throws AllocationException
    {
        SettopExclusiveAccessToken token = getSettopExclusiveAccessToken( settop );
        if ( null == token )
        {
            String msg = "No settop token found for settop " + settop.toString();
            logger.warn( msg );
            throw new AllocationException( msg );
        }
        return allocationService.verify( token.getAllocationId() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Settop > lock( List< Settop > settopList ) throws AllocationException
    {
        return lock( settopList, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Settop > lock( List< Settop > settopList, boolean failOnLockError ) throws AllocationException
    {
        List< Settop > allocatedSettops = new LinkedList< Settop >();

        for ( Settop settop : settopList )
        {
            try
            {
                lock( settop );
                logger.debug( "Locked -" + settop.getHostMacAddress() );

            }
            catch ( Exception exception )
            {
                logger.error( "Exception on locking settop " + settop.toString(), exception );
                lockErrorMessage.append( "Error on locking settop " + settop.getHostMacAddress().toString() + " - "
                        + exception + "\n" );
                if ( failOnLockError )
                {
                    throw new AllocationException( exception.getMessage() );
                }
            }
        }
        // If no settops could be locked throw an exception.
        if ( allocatedSettops.size() <= 0 )
        {
            throw new AllocationException( "No Settops could be locked from list" );
        }
        return allocatedSettops;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release( List< Settop > settopList ) throws AllocationException
    {
        if ( settopList.isEmpty() )
        {
            logger.warn( "No settops found to release" );
        }
        else
        {
            int successfulReleases = 0;

            for ( Settop settop : settopList )
            {
                try
                {
                    release( settop );
                    logger.debug( "Released -" + settop.getHostMacAddress() );
                    successfulReleases++;
                }
                catch ( Exception exception )
                {
                    logger.error( "Error on releasing settop" + settop.toString(), exception );
                    releaseErrorMessage.append( "Error on releasing settop " + settop.getHostMacAddress().toString()
                            + " - " + exception + "\n" );
                }
            }
            if ( successfulReleases <= 0 )
            {
                throw new AllocationException( "No settops could be successfully released" );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release() throws AllocationException
    {
            release( getActiveSettops() );
    }

    /**
     * to get lock error message
     * 
     * @return String
     */
    @Override
    public String getLockErrorMessage()
    {
        return lockErrorMessage.toString();
    }

    /**
     * to get release error message
     * 
     * @return String
     */
    @Override
    public String getReleaseErrorMessage()
    {
        return releaseErrorMessage.toString();
    }

    /**
     * Convenience method to get all the currently allocated settops.
     * @return list of currently allocated {@linkplain Settop}.
     */    
    @Override
    public List< Settop > getActiveSettops()
    {
        List< Settop > settops = new ArrayList< Settop >( allocations.size() );
        for ( SettopExclusiveAccessToken settopToken : allocations.values() )
        {
            settops.add( settopToken.getSettop() );
        }
        return settops;
    }
}
