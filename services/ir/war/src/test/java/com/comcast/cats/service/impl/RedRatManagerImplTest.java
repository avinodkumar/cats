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
package com.comcast.cats.service.impl;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.createPartialMock;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.keymanager.domain.IrDeviceDTO;
import com.comcast.cats.service.IRHardwareEnum;
import com.comcast.cats.service.KeyManagerProxy;
import com.comcast.cats.service.ir.redrat.RedRatHub;
import com.comcast.cats.service.ir.redrat.RedRatManagerImpl;

public class RedRatManagerImplTest
{ 
    RedRatManagerImpl manager;
    List<IrDeviceDTO> netBoxList = new ArrayList<IrDeviceDTO>();
    RedRatHub hub;
    
    @Before
    public void setup() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException 
    {
        hub = new MockRedRatHub("1.2.3.4",1);
        
        manager = new RedRatManagerImpl();
        manager.init();
        
        Field field = manager.getClass().getDeclaredField("redRatHub");
        field.setAccessible(true);
        field.set(manager, hub);
        
        for(int i=0;i<5;i++){
            IrDeviceDTO netBox = new IrDeviceDTO();
            netBox.setId( i );
            netBox.setIpAdress( "1.2.3."+i );
            netBox.setDeviceType( IRHardwareEnum.IRNETBOXPRO3 );
            netBoxList.add( netBox );
        }
    }
    
    @Test
    public void refreshTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        
        KeyManagerProxy mockProxy = createMock( KeyManagerProxy.class );
        
        KeyManagerProxyProvider mockProvider = createPartialMock( KeyManagerProxyProvider.class, "get" );
        expect( mockProvider.get() ).andReturn( mockProxy ).anyTimes();
        expect( mockProxy.getIrDevices()).andReturn( netBoxList ).anyTimes();
        replayAll();
        
        Field field = manager.getClass().getDeclaredField("keyManagerProxyProvider");
        field.setAccessible(true);
        field.set(manager, mockProvider);
        

        manager.refresh();
        
        manager.refresh(); // refreshSecond time
    }
    
    @Test
    public void refreshNegativeTest(){
        
        KeyManagerProxy mockProxy = createMock( KeyManagerProxy.class );
        
        KeyManagerProxyProvider mockProvider = createPartialMock( KeyManagerProxyProvider.class, "get" );
        expect( mockProvider.get() ).andReturn( mockProxy );
        expect( mockProxy.getIrDevices()).andReturn( null );
        replayAll();

        manager.refresh();
    }
    
    @Test
    public void refreshNegative1Test(){

        KeyManagerProxyProvider mockProvider = createPartialMock( KeyManagerProxyProvider.class, "get" );
        expect( mockProvider.get() ).andReturn( null );
        replayAll();

        manager.refresh();
    }
    
    @Test
    public void getIrDevicesTest(){
        assertNotNull( manager.getIrDevices());
    }
    
    @Test
    public void getIrDeviceTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
       assertNull( manager.getIrDevice( "1.2.3.4" ));
       
       KeyManagerProxy mockProxy = createMock( KeyManagerProxy.class );
       
       KeyManagerProxyProvider mockProvider = createMock( KeyManagerProxyProvider.class);
       expect( mockProvider.get() ).andReturn( mockProxy );
       expect( mockProxy.getIrDevices()).andReturn( netBoxList );
       replayAll();
       
       Field field = manager.getClass().getDeclaredField("keyManagerProxyProvider");
       field.setAccessible(true);
       field.set(manager, mockProvider);

       manager.refresh();
       
       assertNotNull( manager.getIrDevice( "1.2.3.4" ));
    }
}