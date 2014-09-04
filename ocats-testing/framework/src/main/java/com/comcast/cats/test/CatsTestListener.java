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
package com.comcast.cats.test;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.internal.ConstructorOrMethod;

import comcast.cats.annotation.CatsTestCase;
import comcast.cats.annotation.CatsTestStep;

public class CatsTestListener implements ITestListener
{
    private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    public CatsTestListener()
    {
        LOGGER.info( "Creating CatsTestListener" );
    }

    @SuppressWarnings(
        { "rawtypes", "unchecked" } )
    public void onFinish( ITestContext context )
    {
        LOGGER.info( context.getCurrentXmlTest().toXml( "  " ) );
        /**
         * IResultMap results = context.getPassedTests(); Set<ITestResult>
         * testResults = results.getAllResults(); for(ITestResult tResult :
         * testResults) { //tResult. }
         */

        for ( ITestNGMethod method : context.getAllTestMethods() )
        {
            ConstructorOrMethod mOrC = method.getConstructorOrMethod();
            if ( mOrC == null )
            {
                LOGGER.info( "ConstructorOrMethod null" );
                continue;
            }
            Method m = mOrC.getMethod();
            if ( m == null )
            {
                LOGGER.info( "Method null" );
                continue;
            }
            Class c = m.getDeclaringClass();
            if ( c == null )
            {
                LOGGER.info( "Class null" );
                continue;
            }
            LOGGER.info( c.getCanonicalName() + ":" + m.getName() );
            CatsTestStep catsTestStepAnnotation = m.getAnnotation( CatsTestStep.class );
            CatsTestCase catsTestCaseAnnotation = ( CatsTestCase ) c.getAnnotation( CatsTestCase.class );
            if ( catsTestStepAnnotation == null || catsTestCaseAnnotation == null )
            {
                LOGGER.info( "Annotations not found" );
                continue;
            }
            LOGGER.info( catsTestCaseAnnotation.name() + ":" + catsTestStepAnnotation.name() );

        }
        LOGGER.info( "On Finish Found" );
    }

    public void onTestStart( ITestResult result )
    {
    }

    public void onTestSuccess( ITestResult result )
    {
    }

    public void onTestFailure( ITestResult result )
    {
    }

    public void onTestSkipped( ITestResult result )
    {
    }

    public void onTestFailedButWithinSuccessPercentage( ITestResult result )
    {
    }

    public void onStart( ITestContext context )
    {
    }
}
