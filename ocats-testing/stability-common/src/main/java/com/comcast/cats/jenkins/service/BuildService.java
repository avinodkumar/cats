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

import com.comcast.cats.jenkins.domain.Build;
import com.comcast.cats.jenkins.domain.BuildDetail;

/**
 * Interface for BuildService to provide remote Jenkins build operations such as
 * start/stop etc.
 * 
 * @author SSugun00c
 * 
 */
public interface BuildService
{
    /**
     * Get BuildList by Job Name
     * 
     * @param jobName
     */
    List< Build > getBuildListByJobName( String jobName );

    /**
     * Get Build Details By JobName and BuildNumber
     * 
     * @param jobName
     * @param buildNumber
     */
    BuildDetail getBuildDetailsByJobNameAndBuildNumber( String jobName, Integer buildNumber );

    /**
     * Get last build number. Use
     * JENKINS_URL/job/Boby_Test2/api/xml/?xpath=/mavenModuleSet
     * /lastBuild/number for getting the last build number.
     * 
     * @param jobName
     */
    String getLastBuildNumber( String jobName );

    /**
     * Start jenkins build. Use
     * JENKINS_URL/view/All/job/Boby_Test2/build?token=TOKEN_NAME or
     * buildWithParameters?token=TOKEN_NAME for starting a new build.
     * 
     * @param jobName
     * @param token
     * @param params
     */
    boolean startBuild( String jobName, String token, String params );

    /**
     * Stop jenkins build. Use http://HUDSON_URL/job/JOBNAME/BUILDNUMBER/stop
     * for stopping a build.
     * 
     * @param jobName
     * @param buildNumber
     */
    boolean stopBuild( String jobName );

}
