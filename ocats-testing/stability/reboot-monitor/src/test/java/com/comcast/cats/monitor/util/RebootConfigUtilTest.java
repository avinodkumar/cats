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
package com.comcast.cats.monitor.util;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.comcast.cats.Settop;
import com.comcast.cats.SettopImpl;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.monitor.RebootMonitoringContext;

/**
 * Test cases for {@link RebootConfigUtil}.
 * 
 * @author SSugun00c
 * 
 */
public class RebootConfigUtilTest
{
    private static final Logger LOGGER      = LoggerFactory.getLogger( RebootConfigUtilTest.class );

    public final String         TEST_MAC_ID = "E4:48:C7:A7:B9:90";

    @BeforeClass
    public static void setUp()
    {
        RebootMonitoringContext context = new RebootMonitoringContext();
        context.refresh();

    }

    @Test
    public void getTraceFilepathTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getTraceFilepath( getSettop() ) );
        LOGGER.info( "TraceFilepath =[" + RebootConfigUtil.getTraceFilepath( getSettop() ) + "]" );
    }

    @Test
    public void getSettopDirectoryTest()
    {
        Assert.assertEquals( "E448C7A7B990", RebootConfigUtil.getSettopDirectory( "E4:48:C7:A7:B9:90" ) );
    }

    @Test
    public void getCatsHomeDirectoryTest()
    {
        Assert.assertEquals( System.getenv().get( RebootConfigUtil.CATS_HOME ), RebootConfigUtil.getCatsHomeDirectory() );
        LOGGER.info( "CATS home [" + RebootConfigUtil.getCatsHomeDirectory() + "]" );
    }

    @Test
    public void getRebootConfigFileLocationTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getRebootConfigFileLocation() );
        LOGGER.info( "RebootConfigFileLocation [" + RebootConfigUtil.getRebootConfigFileLocation() + "]" );
    }

    @Test
    public void getAllRegexTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getAllRegex() );
        Assert.assertFalse( RebootConfigUtil.getAllRegex().isEmpty() );
        LOGGER.info( "All Regex [" + RebootConfigUtil.getAllRegex() + "]" );
    }

    @Test
    public void getAllCMD2000RegexTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getAllCMD2000Regex() );
        Assert.assertFalse( RebootConfigUtil.getAllCMD2000Regex().isEmpty() );
        LOGGER.info( "AllCMD2000Regex [" + RebootConfigUtil.getAllCMD2000Regex() + "]" );
    }

    @Test
    public void getAllSerialRegexTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getAllSerialRegex() );
        Assert.assertFalse( RebootConfigUtil.getAllSerialRegex().isEmpty() );
        LOGGER.info( "AllSerialRegex [" + RebootConfigUtil.getAllSerialRegex() + "]" );
    }

    @Test
    public void getCmd2000LogFileDirectoryTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getCmd2000LogFileDirectory() );
        LOGGER.info( "Cmd2000LogFileDirectory =[" + RebootConfigUtil.getCmd2000LogFileDirectory() + "]" );
    }

    @Test
    public void getCmd2000FtpUsernameTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getCmd2000FtpUsername() );
        LOGGER.info( "Cmd2000FtpUsername =[" + RebootConfigUtil.getCmd2000FtpUsername() + "]" );
    }

    @Test
    public void getCmd2000FtpPasswordTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getCmd2000FtpPassword() );
        LOGGER.info( "Cmd2000FtpPassword =[" + RebootConfigUtil.getCmd2000FtpPassword() + "]" );
    }

    @Test
    public void getCiscoLegacySettopTypesTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getCiscoLegacySettopTypes() );
        Assert.assertFalse( RebootConfigUtil.getCiscoLegacySettopTypes().isEmpty() );
        LOGGER.info( "CiscoLegacySettopTypes[" + RebootConfigUtil.getCiscoLegacySettopTypes() + "]" );
    }

    @Test
    public void getCmd2000LogFileNameTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getCmd2000LogFileName() );
        LOGGER.info( "Cmd2000LogFileName =[" + RebootConfigUtil.getCmd2000LogFileName() + "]" );
    }

    @Test
    public void getCmd2000HostTest()
    {
        Assert.assertNotNull( RebootConfigUtil.getCmd2000Host( getSettop() ) );
        LOGGER.info( "Cmd2000Host =[" + RebootConfigUtil.getCmd2000Host( getSettop() ) + "]" );
    }

    @Test
    public void getServerHostTestFromCatsProperties()
    {
        System.clearProperty( "cats.server.url" );
        Assert.assertNotNull( RebootConfigUtil.getServerHost() );
        Assert.assertEquals( CatsProperties.DEFAULT_CATS_SERVER, RebootConfigUtil.getServerHost() );
        LOGGER.info( "ServerHost =[" + RebootConfigUtil.getServerHost() + "]" );
    }

    @Test
    public void getServerHostTest()
    {
        String catsSerevr = "http://cats-stag.cable.comcast.com:8080/";
        String catsHost = "cats-stag.cable.comcast.com";
        System.setProperty( "cats.server.url", catsSerevr );
        Assert.assertNotNull( RebootConfigUtil.getServerHost() );
        Assert.assertEquals( catsHost, RebootConfigUtil.getServerHost() );
        LOGGER.info( "ServerHost =[" + RebootConfigUtil.getServerHost() + "]" );
    }

    @Test
    public void getServerHostTestException()
    {
        System.setProperty( "cats.server.url", "cats-wrong-serevr.cable.comcast.com:8080/" );
        Assert.assertNotNull( RebootConfigUtil.getServerHost() );
        Assert.assertEquals( CatsProperties.DEFAULT_CATS_SERVER, RebootConfigUtil.getServerHost() );
        LOGGER.info( "ServerHost =[" + RebootConfigUtil.getServerHost() + "]" );
    }

    private Settop getSettop()
    {
        SettopImpl settop = new SettopImpl();
        SettopDesc settopInfo = new SettopDesc();
        String hostMacAddress = TEST_MAC_ID;
        settopInfo.setHostMacAddress( hostMacAddress );
        settop.setSettopInfo( settopInfo );

        settop.getExtraProperties().put( "Controller", "DNCS1" );

        return settop;
    }
}
