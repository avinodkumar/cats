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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.config.ui.monitoring.reboot.RebootMonitorService;
import com.comcast.cats.config.ui.monitoring.reboot.UpTimeAndRebootStatusBean;
import com.comcast.cats.config.ui.monitoring.reboot.UpTimeBean;
import com.comcast.cats.config.ui.monitoring.reboot.UpTimeMonitoringConstants;
import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootDetectionStatus;
import com.comcast.cats.reboot.RebootInfo;
import com.comcast.cats.reboot.RebootUtil;

/*
 * Not all methods of UptimeAndRebootStatusBean can be unit tested because
 * of java.lang.ClassFormatError: Absent Code attribute in method that is not native or abstract in class file javax/faces/model/DataModel.
 */
public class UpTimeAndRebootStatusBeanTest
{
    
    UpTimeAndRebootStatusBean testBean;
    RebootMonitorService rebootMonitorService;
    String ipAddress  = "1.2.3.4";
    String macAddress = "12:12:12:12:12:12";
    
    @Before
    public void setup() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        AuthController.setHostAddress( "localhost:8080" );
        testBean = new UpTimeAndRebootStatusBean();
        
        rebootMonitorService = EasyMock.createMock( RebootMonitorService.class );
        testBean.setRebootMonitorService( rebootMonitorService );
    }

    @After
    public void tearDown()
    {
        testBean = null;
    }

    @Test
    public void isLessThanOneDayTest()
    {
       assertTrue( testBean.isLessThanOneDay( "00d:12h:12m:12.12s" ));
       assertFalse( testBean.isLessThanOneDay( "02d:12h:12m:12.12s" ));
       assertFalse( testBean.isLessThanOneDay( "01d:00h:00m:00.0s" ));
       assertFalse( testBean.isLessThanOneDay( null ));
       assertFalse( testBean.isLessThanOneDay( "IllgealArgument" ));
    }
    
    @Test
    public void beanTest()
    {
       UpTimeBean upTimeBean = new UpTimeBean();
       testBean.setSelectedUpTimeBean( upTimeBean );
       assertEquals( testBean.getSelectedUpTimeBean(), upTimeBean );
       
       assertFalse( testBean.isShowDataTable() );
       testBean.setShowDataTable( true );
       assertTrue( testBean.isShowDataTable() );
    }
    
    @Test
    public void getLastRebootTimeTest()
    {
        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopMac( macAddress );
        upTimeBean.setSettopIP( ipAddress );
        testBean.setSelectedUpTimeBean( upTimeBean );
        
        MonitorTarget monitorTarget = new MonitorTarget();    
        monitorTarget.setExecutionDate( new Date());
        monitorTarget.setUpTime( 0L );
        
        EasyMock.expect( rebootMonitorService.getUptime(macAddress) ).andReturn( monitorTarget );
        EasyMock.replay( rebootMonitorService );
        assertEquals( testBean.UNKNOWN_STATUS, testBean.getLastRebootTime());
    }
    
    @Test
    public void getLastRebootTimeNullTest()
    {        
        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopMac( null );
        upTimeBean.setSettopIP( null );
        testBean.setSelectedUpTimeBean( upTimeBean );
               
        EasyMock.expect( rebootMonitorService.getUptime(null) ).andReturn( null );
        EasyMock.replay( rebootMonitorService );
        assertEquals( UpTimeAndRebootStatusBean.UNKNOWN_STATUS, testBean.getLastRebootTime());
    }
    
    @Test
    public void getLastUpTimeTest()
    {

        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopMac( macAddress );
        upTimeBean.setSettopIP( ipAddress );
        testBean.setSelectedUpTimeBean( upTimeBean );
        
        ArrayList< RebootInfo > rebootInfoList = new ArrayList< RebootInfo >();
        Calendar calendar = Calendar.getInstance();
        RebootInfo rebootInfo = new RebootInfo();
        rebootInfo.setStatus( RebootDetectionStatus.REBOOT_DETECTED );
        rebootInfo.setExecutionDate( calendar.getTime());
        rebootInfo.setUpTime( 1 * RebootUtil.TICKS_PER_HOUR );
        rebootInfoList.add( rebootInfo );
        
        Calendar prev_calendar = Calendar.getInstance();
        prev_calendar.add( Calendar.HOUR, -2 );
        RebootInfo previousReboot = new RebootInfo();
        previousReboot.setStatus( RebootDetectionStatus.REBOOT_DETECTED );
        previousReboot.setExecutionDate( prev_calendar.getTime() );
        previousReboot.setUpTime( 22 * RebootUtil.TICKS_PER_HOUR);
        rebootInfoList.add( previousReboot );
        
        EasyMock.expect( rebootMonitorService.listAllReboots(macAddress) ).andReturn( rebootInfoList );
        EasyMock.replay( rebootMonitorService );
       // assertEquals(  "00d:23h:00m:00s.00", testBean.getLastUptime());
        assertNotNull( testBean.getLastUptime() );
    }
    
    @Test
    public void getLastUpTimeWIthNoPrevRebootsTest()
    {        
        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopMac( macAddress );
        upTimeBean.setSettopIP( ipAddress );
        testBean.setSelectedUpTimeBean( upTimeBean );
        
        ArrayList< RebootInfo > rebootInfoList = new ArrayList< RebootInfo >();
        Calendar calendar = Calendar.getInstance();
        RebootInfo rebootInfo = new RebootInfo();
        rebootInfo.setExecutionDate( calendar.getTime());
        rebootInfo.setUpTime(  1 * RebootUtil.TICKS_PER_HOUR );
        rebootInfoList.add( rebootInfo );

        EasyMock.expect( rebootMonitorService.listAllReboots(macAddress) ).andReturn( rebootInfoList );
        EasyMock.replay( rebootMonitorService );
        assertEquals(  UpTimeAndRebootStatusBean.UNKNOWN_STATUS, testBean.getLastUptime());
    }
    
    @Test
    public void getLastUpTimeNullTest()
    {        
        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopMac( macAddress );
        upTimeBean.setSettopIP( ipAddress );
        testBean.setSelectedUpTimeBean( upTimeBean );

        EasyMock.expect( rebootMonitorService.listAllReboots(macAddress) ).andReturn( null );
        EasyMock.replay( rebootMonitorService );
        assertEquals(  UpTimeAndRebootStatusBean.UNKNOWN_STATUS, testBean.getLastUptime());
    }
    
    @Test
    public void getTotalRebootsCountTest()
    {

        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopMac( macAddress );
        upTimeBean.setSettopIP( ipAddress );
        testBean.setSelectedUpTimeBean( upTimeBean );
       
        ArrayList< RebootInfo > rebootInfoList = new ArrayList< RebootInfo >();
        Calendar calendar = Calendar.getInstance();
        RebootInfo rebootInfo = new RebootInfo();
        rebootInfo.setStatus( RebootDetectionStatus.REBOOT_DETECTED );
        rebootInfo.setExecutionDate( calendar.getTime());
        rebootInfo.setUpTime(  1 * RebootUtil.TICKS_PER_HOUR );
        rebootInfoList.add( rebootInfo );

        EasyMock.expect( rebootMonitorService.listAllReboots(macAddress) ).andReturn( rebootInfoList );
        EasyMock.replay( rebootMonitorService );
        assertEquals(  1, testBean.getTotalRebootsCount());
    }
    
    @Test
    public void getTotalRebootsCountNullTest()
    {

        UpTimeBean upTimeBean = new UpTimeBean();
        upTimeBean.setSettopMac( macAddress );
        upTimeBean.setSettopIP( null );
        testBean.setSelectedUpTimeBean( upTimeBean );

        EasyMock.expect( rebootMonitorService.listAllReboots(macAddress) ).andReturn( null );
        EasyMock.replay( rebootMonitorService );
        
        assertEquals(  -1, testBean.getTotalRebootsCount());
    }
    
    
//    @Test
//    public void formatUpTimeTest()
//    {
//        assertEquals( "00d:11h:22m:12.12s", testBean.formatUpTime( "00:11:22:12.12" ) );
//        assertEquals( "00d:01h:02m:1.1s", testBean.formatUpTime( "0:1:2:1.1" ) );
//        assertNull( testBean.formatUpTime(null ));
//    }
    
}
