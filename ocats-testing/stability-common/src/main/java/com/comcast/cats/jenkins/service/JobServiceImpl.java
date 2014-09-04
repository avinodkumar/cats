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
import java.util.List;

import com.comcast.cats.jenkins.domain.Build;
import com.comcast.cats.jenkins.domain.Job;
import com.comcast.cats.jenkins.domain.JobSet;

/**
 * 
 * @author SSugun00c
 * 
 */
public class JobServiceImpl extends AbstractService implements JobService
{
    @Override
    public List< Job > getAllJobs()
    {
        // Emprt string in this case
        String requestUrl = "";
        return ( ( JobSet ) getForObject( requestUrl, JobSet.class, "" ) ).getJobs();
    }
    
    
    // TODO: Not tested
    @Override    
    public boolean createJob(String jobName, File config){
        
        // For creating a project use ${jenkinsHost}/createItem?name=${projectName}"  
        
        String requestUrl = "/createItem?name=" + jobName;
        Build build = ( Build ) createProject( requestUrl, Build.class, config);
        LOGGER.info( "Build " + build.toString() );
        
        
        return true;
    }

}
