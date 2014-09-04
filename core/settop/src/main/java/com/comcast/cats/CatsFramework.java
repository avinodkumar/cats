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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;

import com.comcast.cats.configuration.CatsContext;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.service.SettopDomainService;
import com.comcast.cats.provider.ExclusiveAccessManager;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;

/**
 * Core Cats Framework class responsible for instantiating the appropriate
 * context and providing an interface for retrieving important classes to
 * interact with the CATS' environment.
 * 
 * @author cfrede001
 * 
 */
public class CatsFramework
{
    protected static final Logger LOGGER = LoggerFactory.getLogger( CatsFramework.class );

    protected CatsContext         context;

    private static CatsFramework  frameworkSingleton;

    public CatsFramework()
    {
        this.context = new CatsContext();
        init();
    }

    /**
     * Take a base CatsContext class and create the context.
     * 
     * @param context
     */
    public CatsFramework( CatsContext context )
    {
        this.context = context;
        init();
    }

    /**
     * To initialize context refresh and register shutdown hook.
     * 
     */
	protected void init() {
		Long start = System.currentTimeMillis();
		LOGGER.info("Calling initialization on CatsSettopFrameworkImpl");
		context.refresh();
		Long end = System.currentTimeMillis();
		LOGGER.info("CatsFramework initialized in "	+ Long.toString(end - start) + "ms");
		frameworkSingleton = this;
		registerShutdownHook();
	}

	/**
     * Getter method for settop factory.
     * 
     * @return {@linkplain SettopFactory}
     */
    public SettopFactory getSettopFactory()
    {
        return context.getBean( SettopFactory.class );
    }

    /**
     * Getter method for CatsProperties.
     * 
     * @return {@linkplain CatsProperties}
     */
    public CatsProperties getCatsProperties()
    {
        return context.getBean( CatsProperties.class );
    }

    /**
     * Getter method for SettopExclusiveAccessEnforcer.
     * 
     * @return {@linkplain SettopExclusiveAccessEnforcer}
     */
    public SettopExclusiveAccessEnforcer getSettopLocker()
    {
        return context.getBean( SettopExclusiveAccessEnforcer.class );
    }

    /**
     * Getter method for SettopDomainService.
     * 
     * @return {@linkplain SettopDomainService}
     */
    public SettopDomainService getSettopLookupService()
    {
        return context.getBean( SettopDomainService.class );
    }

    /**
     * Getter method for ApplicationContext.
     * 
     * @return {@linkplain ApplicationContext}
     */
    public ApplicationContext getContext()
    {
        return context;
    }

    private ExclusiveAccessManager getExclusiveAccessManager()
    {
        return context.getBean( ExclusiveAccessManager.class );
    }
    /**
     * To make accessing the CatsFramework easier, add a singleton reference.
     * 
     * @return CatsFramework
     */
    public static CatsFramework getInstance()
    {
        if ( frameworkSingleton == null )
        {
            throw new RuntimeException( "Framework has never been instantiated." );
        }
        return frameworkSingleton;
    }

    /**
	 * register ShutdownHook to the current Runtime and which will release all
	 * the current allocation on JVM exit and also stops Break Allocation Monitoring Thread.
	 * 
	 * But ShutdownHook will not be executed on killing the java process and termination from eclipse.
	 * http://stackoverflow.com/questions/261125
	 * /how-do-i-get-rid-of-java-child-processes-when-my-java-app-exits-crashes
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=38016
	 * 
	 * @param framework
	 */
	private void registerShutdownHook() {
		final SettopExclusiveAccessEnforcer settopExclusiveAccessEnforcer = this
				.getSettopLocker();
		final ExclusiveAccessManager exclusiveAccessManager = this
				.getExclusiveAccessManager();
		LOGGER.info("Registering shutdownHook");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					LOGGER.info("ShutdownHook Executing");
					exclusiveAccessManager.killBreakAllocationThread();
					LOGGER.info("Calling Release method");
					settopExclusiveAccessEnforcer.release();
				} catch (AllocationException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}));
	}
	
}
