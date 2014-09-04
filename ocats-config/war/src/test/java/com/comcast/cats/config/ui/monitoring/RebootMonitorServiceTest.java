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
package com.comcast.cats.config.ui.monitoring;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.jboss.resteasy.client.ProxyFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.config.ui.monitoring.reboot.RebootMonitorService;
import com.comcast.cats.reboot.RebootInfo;
import com.comcast.cats.service.RebootDetection;

@PrepareForTest(ProxyFactory.class)
@RunWith(PowerMockRunner.class)
public class RebootMonitorServiceTest
{
    String                     macAddress = "12:12:12:12:12:12";
    String  ecmMacAddress = "13:13:13:13:13:13";
    String restPath = "http://localhost:8080/snmp-service-reboot/rest/reboot/detection/"+macAddress+"/";
    RebootMonitorService       rebootMonitorService;
    List< RebootInfo > rebootInfoList = new ArrayList< RebootInfo >();
    RebootInfo rebootInfo = new RebootInfo();
    MockRebootDetectionImpl rebootDetectionService;
    
    @Before
    public void setup()
    {
        AuthController.setHostAddress( "localhost:8080" );
        rebootMonitorService = new RebootMonitorService();
        rebootMonitorService.init();
        rebootDetectionService = new MockRebootDetectionImpl();
    }

    @After
    public void tearDown()
    {
        rebootMonitorService = null;
        rebootInfoList.clear();
        rebootDetectionService = null;
    }

    @Test
    public void initTest()
    {
        rebootMonitorService.init();
    }
    
    @Test
    public void getUptimeTest(){
        
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertEquals( rebootDetectionService.mockMonitorTarget, rebootMonitorService.getUptime( macAddress));
    }
    
    @Test
    public void getUptimeNullTest(){
        rebootDetectionService.setErrorConditions( true );
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( null );
        PowerMock.replay( ProxyFactory.class );
        assertNull(rebootMonitorService.getUptime( macAddress));
    }
    
    @Test
    public void getUptimeNull1Test(){
        rebootDetectionService.setErrorConditions( true );
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertNull(rebootMonitorService.getUptime( macAddress));
    }
    
    
    @Test
    public void listRebootTest(){
        
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertEquals( rebootDetectionService.rebootInfoList, rebootMonitorService.listAllReboots( macAddress, new Date(), new Date() ));
    }
    
    @Test
    public void listRebootNullTest(){
        rebootDetectionService.setErrorConditions( true );
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( null );
        PowerMock.replay( ProxyFactory.class );
        assertNull( rebootMonitorService.listAllReboots( macAddress, new Date(), new Date() ));
    }
    
    @Test
    public void listRebootEmptyListTest(){
        rebootDetectionService.rebootInfoList = new ArrayList< RebootInfo >();
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertNotNull( rebootMonitorService.listAllReboots( macAddress, new Date(), new Date() ));
    }
    
    @Test
    public void listRebootNullArgsTest(){
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertEquals( null, rebootMonitorService.listAllReboots( macAddress, null, null ));
    }
    
    @Test
    public void listRebootNullArgs1Test(){
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertNull( rebootMonitorService.listAllReboots( null, null, null ));
    }

    @Test
    public void getRebootCountTest(){
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertEquals( 0, rebootMonitorService.getRebootCount( macAddress ));
    }
    
    @Test
    public void getRebootCountNullTest(){
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( null );
        PowerMock.replay( ProxyFactory.class );
        assertEquals( -1, rebootMonitorService.getRebootCount( macAddress ));
    }
    
    @Test
    public void getRebootCountNull1Test(){
        rebootDetectionService.setErrorConditions( true );
        PowerMock.mockStatic( ProxyFactory.class );
        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
        PowerMock.replay( ProxyFactory.class );
        assertEquals( -1, rebootMonitorService.getRebootCount( macAddress ));
    }
    
//    @Test
//    public void getLatestRebootInfoTest(){
//        PowerMock.mockStatic( ProxyFactory.class );
//        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
//        PowerMock.replay( ProxyFactory.class );
//        assertEquals( rebootDetectionService.mockRebootInfo, rebootMonitorService.getLatestRebootInfo( macAddress  ));
//    }
//    
//    @Test
//    public void getLatestRebootInfoNullTest(){
//        rebootDetectionService.setErrorConditions( true );
//        PowerMock.mockStatic( ProxyFactory.class );
//        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( null );
//        PowerMock.replay( ProxyFactory.class );
//        assertNull(rebootMonitorService.getLatestRebootInfo( macAddress, ipAddress ));
//    }
//    
//    @Test
//    public void getLatestRebootInfoNull1Test(){
//        rebootDetectionService.setErrorConditions( true );
//        PowerMock.mockStatic( ProxyFactory.class );
//        EasyMock.expect(ProxyFactory.create( RebootDetection.class,restPath )).andReturn( rebootDetectionService );
//        PowerMock.replay( ProxyFactory.class );
//        assertNull(rebootMonitorService.getLatestRebootInfo(  macAddress, ipAddress ));
//    }
    
}
