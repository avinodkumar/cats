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

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * By default this class will look for system properties. If it fails to find it in
 * system property list, it will try to read the properties from
 * classpath:jenkins-client.properties
 * 
 * @author SSugun00c
 * 
 */
public class JenkinsClientProperties
{
    private final Logger        LOGGER                  = LoggerFactory.getLogger( getClass() );

    private Properties          properties              = new Properties();
    private static final String DEFAULT_FILE_NAME       = "jenkins-client.properties";

    public static final String  JENKINS_HOST            = "jenkins.host";
    public static final String  JENKINS_PORT            = "jenkins.port";
    public static final String  JENKINS_SERVIE_BASE_URL = "jenkins.servie.base.url";
    public static final String  JENKINS_USERNAME        = "jenkins.username";
    public static final String  JENKINS_PASSWORD        = "jenkins.password";
    public static final String  JENKINS_API_TOKEN       = "jenkins.api.token";

    public void setProperties( Properties properties )
    {
        this.properties = properties;
    }

    public JenkinsClientProperties()
    {
        try
        {
            properties.load( getClass().getClassLoader().getResourceAsStream( DEFAULT_FILE_NAME ) );
        }
        catch ( IOException ex )
        {
            LOGGER.error( ex.getMessage() );
        }
    }

    public String getJenkinsHost()
    {
        String jenkinsHost = System.getProperty( JENKINS_HOST );

        if ( null == jenkinsHost )
        {
            jenkinsHost = properties.getProperty( JENKINS_HOST );
        }

        return jenkinsHost;
    }

    public String getJenkinsPort()
    {

        String jenkinsPort = System.getProperty( JENKINS_PORT );

        if ( null == jenkinsPort )
        {
            jenkinsPort = properties.getProperty( JENKINS_PORT );
        }

        return jenkinsPort;
    }

    public String getJenkinsServieBaseUrl()
    {
        String jenkinsServieBaseUrl = System.getProperty( JENKINS_SERVIE_BASE_URL );

        if ( null == jenkinsServieBaseUrl )
        {
            jenkinsServieBaseUrl = properties.getProperty( JENKINS_SERVIE_BASE_URL );
        }

        return jenkinsServieBaseUrl;
    }

    public String getJenkinsUsername()
    {
        String jenkinsUsername = System.getProperty( JENKINS_USERNAME );

        if ( null == jenkinsUsername )
        {
            jenkinsUsername = properties.getProperty( JENKINS_USERNAME );

            if ( null == jenkinsUsername )
            {
                jenkinsUsername = "";
            }
        }

        return jenkinsUsername;
    }

    public String getJenkinsPassword()
    {
        String jenkinsPassword = System.getProperty( JENKINS_PASSWORD );

        if ( null == jenkinsPassword )
        {
            jenkinsPassword = properties.getProperty( JENKINS_PASSWORD );

            if ( null == jenkinsPassword )
            {
                jenkinsPassword = "";
            }
        }

        return jenkinsPassword;
    }

    public String getJenkinsApiToken()
    {
        String jenkinsApiToken = System.getProperty( JENKINS_API_TOKEN );

        if ( null == jenkinsApiToken )
        {
            jenkinsApiToken = properties.getProperty( JENKINS_API_TOKEN );

            if ( null == jenkinsApiToken )
            {
                jenkinsApiToken = "";
            }
        }

        return jenkinsApiToken;
    }

}
