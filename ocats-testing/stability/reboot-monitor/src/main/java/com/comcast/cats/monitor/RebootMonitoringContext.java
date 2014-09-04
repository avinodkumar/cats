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
package com.comcast.cats.monitor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * RebootMonitoringContext context help class to make instantiation easier.
 * 
 * @author skurup00c
 * 
 */
public class RebootMonitoringContext extends AnnotationConfigApplicationContext
{

    /**
     * You must run refresh on this class after instantiation before retrieving
     * objects. This is being used
     */
    public RebootMonitoringContext()
    {
        super();
        initContext();
    }

    private void initContext()
    {
        addCatsPackagesToContext();
        configure();
    }

    /**
     * Convenience method for adding common packages and classes to this
     * ApplicationContext. If this method is not invoked then this class is just
     * a standard AnnotationConfigApplicationContext.
     */
    protected void addCatsPackagesToContext()
    {
        this.scan( "com.comcast.cats.monitor" );
        this.scan( "com.comcast.cats.monitor.reboot" );
    }

    /**
     * Configure any essential directories and system properties.
     */
    protected void configure()
    {
        // Placeholder for any additional configuration.
    }
}