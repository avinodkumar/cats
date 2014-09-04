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
package com.comcast.cats.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * 
 * @author SSugun00c
 * 
 */
public class JenkinsClientPropertiesTest
{
    private final Logger            LOGGER       = LoggerFactory.getLogger( getClass() );

    private JenkinsClientProperties propertyUtil = new JenkinsClientProperties();

    @Test
    public void getJenkinsHost()
    {
        LOGGER.info( propertyUtil.getJenkinsHost() );
    }

    @Test
    public void getJenkinsPort()
    {
        LOGGER.info( propertyUtil.getJenkinsPort() );
    }

    @Test
    public void getJenkinsServieBaseUrl()
    {
        LOGGER.info( propertyUtil.getJenkinsServieBaseUrl() );
    }

    @Test
    public void getJenkinsUsername()
    {
        LOGGER.info( propertyUtil.getJenkinsUsername() );
    }

    @Test
    public void getJenkinsPassword()
    {
        LOGGER.info( propertyUtil.getJenkinsPassword() );
    }
}
