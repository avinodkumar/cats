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

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.comcast.cats.configuration.BaseConfiguration;
import com.comcast.cats.configuration.HardwareServiceConfiguration;
import com.comcast.cats.domain.configuration.CatsProperties;

public class CiscoDTABaseClass
{
    protected ApplicationContext ctx;

    public CiscoDTABaseClass()
    {
        System.setProperty( "cats.server.url", "http://cats-dev:8080/" );
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register( BaseConfiguration.class );
        context.register( CatsProperties.class );
        context.register( HardwareServiceConfiguration.class );

        context.refresh();
        ctx = context;
    }
}