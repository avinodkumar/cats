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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.jenkins.domain.Build;
import com.comcast.cats.jenkins.domain.BuildDetail;
import com.comcast.cats.jenkins.domain.BuildSet;

/**
 * 
 * @author SSugun00c
 * 
 */
public class BuildServiceImpl extends AbstractService implements BuildService
{

    private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    @Override
    public List< Build > getBuildListByJobName( String jobName )
    {
        String requestUrl = "job/" + jobName;
        return ( ( BuildSet ) getForObject( requestUrl, BuildSet.class, "" ) ).getBuilds();
    }

    @Override
    public BuildDetail getBuildDetailsByJobNameAndBuildNumber( String jobName, Integer buildNumber )
    {
        String requestUrl = "job/" + jobName + "/" + buildNumber;
        return ( BuildDetail ) getForObject( requestUrl, BuildDetail.class, "" );
    }

    @Override
    public String getLastBuildNumber( String jobName )
    {
        String requestUrl = "job/" + jobName;
        return ( String ) getForObject( requestUrl, String.class, "?xpath=/mavenModuleSet/lastBuild/number" );
    }

    @Override
    public boolean startBuild( String jobName, String token, String params )
    {
        boolean status = false;
        String requestUrl = "job/" + jobName + "/buildWithParameters?";
        
        if ( ( null != token ) && !token.isEmpty() )
        {
            requestUrl = requestUrl + "token=" + token;
        }
        
        if ( ( null != params ) && !params.isEmpty() )
        {
            requestUrl = requestUrl + "&" + params;
        }
        
        LOGGER.info( "requestUrl " + requestUrl );
        
        Build build = ( Build ) performBuildOperationsViaGet( requestUrl, Build.class );
        LOGGER.info( "Build " + build );
        if ( build != null )
        {
            status = true;
        }
        return status;
    }

    @Override
    public boolean stopBuild( String jobName )
    {
        boolean status = false;
        // Get last build number
        String buildNumber = getLastBuildNumber( jobName );

        String requestUrl = "job/" + jobName + "/" + buildNumber + "/stop";
        Build build = ( Build ) performBuildOperationsViaGet( requestUrl, Build.class );
        LOGGER.info( "Build " + build );
        if ( build != null )
        {
            status = true;
        }
        return status;
    }

}
