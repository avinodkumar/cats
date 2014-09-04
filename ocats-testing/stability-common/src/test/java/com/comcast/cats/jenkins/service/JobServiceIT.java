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

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.jenkins.domain.Job;

/**
 * 
 * @author SSugun00c
 * 
 */
public class JobServiceIT
{
    private final Logger LOGGER     = LoggerFactory.getLogger( getClass() );

    private JobService   jobService = new JobServiceImpl();

    @Test
    public void getAllJobs()
    {
        List< Job > jobs = jobService.getAllJobs();

        for ( Job job : jobs )
        {
            LOGGER.info( job.toString() );
        }
    }
    
    // TODO: Not tested
    @Test
    @Ignore
    public void createProject() throws HttpException, IOException
    {
        File input = new File("config.xml");
        boolean status = jobService.createJob("NewProject", input);
        LOGGER.info( " status is " + status );
        Assert.assertTrue( status );
    }

}
