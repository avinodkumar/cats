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

import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.jenkins.domain.Build;
import com.comcast.cats.jenkins.domain.BuildDetail;

import java.io.IOException;

import junit.framework.Assert;

/**
 * 
 * @author SSugun00c
 * 
 */
public class BuildServiceIT
{
    private final Logger LOGGER       = LoggerFactory.getLogger( getClass() );

    private BuildService buildService = new BuildServiceImpl();

    private String       jobName      = "Stability-JMS-POC";
    private Integer      buildNo      = 11;

    //@Test
    public void getBuildListByJobName()
    {
        List< Build > builds = buildService.getBuildListByJobName( jobName );

        for ( Build build : builds )
        {
            LOGGER.info( build.toString() );
        }
    }

    //@Test
    public void getBuildDetailsByBuildNumber()
    {
        BuildDetail buildDetail = buildService.getBuildDetailsByJobNameAndBuildNumber( jobName, buildNo );
        LOGGER.info( buildDetail.toString() );     
        
    }

    //@Test    
    public void startNewBuild() throws HttpException, IOException
    {

        boolean bool = buildService.startBuild( jobName, "", "");
        LOGGER.info( " status is " + bool );
        Assert.assertTrue( bool );
    }

    //@Test//(dependsOnMethods={"startNewBuild"})
    public void stopBuild() throws HttpException, IOException, InterruptedException
    {
        // stop build
        boolean status = buildService.stopBuild(jobName);
        LOGGER.info( " status is " + status );
        Assert.assertTrue( status );

    }   


}
