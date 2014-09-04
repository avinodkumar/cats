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

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.comcast.cats.jenkins.domain.JobSet;
import com.comcast.cats.jenkins.domain.BuildSet;
import com.comcast.cats.jenkins.domain.BuildDetail;

/**
 * 
 * @author SSugun00c
 *
 */
public class DeserializerTest
{
    public static void main( String[] args )
    {
        try
        {
            Serializer serializer = new Persister();

            File hudsonFile = new File( "src/test/resources/hudson.xml" );
            JobSet hudson = serializer.read( JobSet.class, hudsonFile );
            System.out.println( hudson );

            File mavenModuleSetFile = new File( "src/test/resources/maven-module-set.xml" );
            BuildSet mavenModuleSet = serializer.read( BuildSet.class, mavenModuleSetFile );
            System.out.println( mavenModuleSet );

            File mavenModuleSetBuildFile = new File( "src/test/resources/maven-module-set-build.xml" );
            BuildDetail mavenModuleSetBuild = serializer.read( BuildDetail.class,
                    mavenModuleSetBuildFile );
            System.out.println( mavenModuleSetBuild );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

}
