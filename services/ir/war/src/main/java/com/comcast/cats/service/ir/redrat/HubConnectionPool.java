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
package com.comcast.cats.service.ir.redrat;

import static com.comcast.cats.service.ir.redrat.RedRatConstants.DEFAULT_POOL_SIZE;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.POOL_WAIT_TIME;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.REDRATHUB_POOL_SIZE;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.REDRAT_PROMPT_STRING_1;
import static com.comcast.cats.service.ir.redrat.RedRatConstants.REDRAT_PROPERTIES_FILE;

import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;

import nf.fr.eraasoft.pool.ObjectPool;
import nf.fr.eraasoft.pool.PoolException;
import nf.fr.eraasoft.pool.PoolSettings;
import nf.fr.eraasoft.pool.PoolableObjectBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hubconnection pool using furious_objectPool.
 * @author skurup00c
 *
 */
public class HubConnectionPool
{
    private static final Logger               logger = LoggerFactory.getLogger( HubConnectionPool.class );

    ObjectPool< RedRatHubConnection > connectionPool;
    private int                 poolSize = DEFAULT_POOL_SIZE;

    private String hubIp;
    private Integer hubPort;
 
    public HubConnectionPool(String hubIp, Integer hubPort){
    	this.hubIp = hubIp;
    	this.hubPort = hubPort;		
    	
    	init();
    }
    
    public void init()
    {
        readPoolSize();

        PoolSettings< RedRatHubConnection > poolSettings = 
        		new PoolSettings< RedRatHubConnection >( new PoolObject(this, hubIp, hubPort ) );
        poolSettings.min( poolSize ).max( poolSize ).maxWait( POOL_WAIT_TIME ) ; // wait 60 sec before
                                                       // timeout.
        connectionPool = poolSettings.pool();
        logger.info( "hubconnectionPool " + connectionPool );
    }

    private void readPoolSize()
    {
        Properties props = new Properties();
        try
        {
            props.load( HubConnectionPool.class.getClassLoader().getResourceAsStream( REDRAT_PROPERTIES_FILE ) );
            poolSize = Integer.parseInt( props.getProperty( REDRATHUB_POOL_SIZE ) );
            logger.info( "poolSize from properties file " + poolSize );
        }
        catch ( Exception e ) // specifically includes IOException,
                              // NumberFormatException
        {
            logger.warn( "Couldnt load redrat.props file " + e.getMessage() );
            poolSize = DEFAULT_POOL_SIZE;

            logger.info( "poolSize from default setting " + poolSize );
        }
    }

    public synchronized RedRatHubConnection getConnection()
    {
        RedRatHubConnection telnetConnection = null;
        try
        {
            telnetConnection = connectionPool.getObj();
            if(telnetConnection == null){
            	logger.warn( "connectionPool getConnection " + telnetConnection );
            }
            else
            {
            	logger.trace( "connectionPool getConnection " + telnetConnection );
            }
        }
        catch ( PoolException e )
        {
            logger.warn( "connectionPool getConnection PoolException " + e.getMessage() );
        }

        return telnetConnection;
    }

    public void releaseConnection( RedRatHubConnection redRatHubConnection )
    {
        logger.trace( "connectionPool getConnection releaseConnection " + redRatHubConnection );
        connectionPool.returnObj( redRatHubConnection );
    }
}

/**
 * Pool Object.
 * @author skurup00c
 *
 */
class PoolObject extends PoolableObjectBase< RedRatHubConnection >
{
    HubConnectionPool pool;
    private static final Logger               logger = LoggerFactory.getLogger( HubConnectionPool.class );
    private String hubIp;
    private Integer hubPort;
    
    static int count = 0;
    
    public PoolObject( HubConnectionPool pool, String hubIp, Integer hubPort)
    {
        this.pool = pool;
    	this.hubIp = hubIp;
    	this.hubPort = hubPort;
    }

    @Override
    public RedRatHubConnection make() throws PoolException
    {
        RedRatHubConnection telnetConnection = new RedRatHubConnection( hubIp,
        		hubPort , REDRAT_PROMPT_STRING_1, pool );
        logger.debug("HubConnectionPoolHubConnectionPool Making a connection "+count++);
        return telnetConnection;
    }

    @Override
    public void activate( RedRatHubConnection t ) throws PoolException
    {
    	if(t != null){
	        try
	        {
	            if(!t.isConnected()){
	                t.connect( false );
	            }
	        }
	        catch ( SocketException e )
	        {
	            logger.warn("Could not connect telnet by pool "+e.getMessage());
	        }
	        catch ( IOException e )
	        {
	            logger.warn("Could not connect telnet by pool "+e.getMessage());
	        }
    	}
    }
    
    @Override
	public void passivate(RedRatHubConnection t) {
 	}
}
