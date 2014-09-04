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

import static com.comcast.cats.service.IRHardwareEnum.GC100;
import static com.comcast.cats.service.IRHardwareEnum.GC100_12;
import static com.comcast.cats.service.IRHardwareEnum.GC100_6;
import static com.comcast.cats.service.IRHardwareEnum.IRNETBOXPRO3;
import static com.comcast.cats.service.IRHardwareEnum.ITACH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.comcast.cats.service.IRServiceProvider;
import com.comcast.cats.service.IRServiceVersionGetter;
import com.comcast.cats.service.ir.redrat.RedRatIRServiceFacade;
import com.comcast.cats.service.ir.redrat.RedRatIRServiceHandler;

public class IRServiceFacadeRetrieverTest
{ 
    IRServiceProvider irServiceProvider;
    IRServiceVersionGetter versionGetter;
    LegacyIRServiceFacade legacyFacade;
    RedRatIRServiceFacade redRatFacade;
    
    
    @Before
    public void setup() throws URISyntaxException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        
        
        irServiceProvider =new  IRServiceFacadeRetriever();
        legacyFacade = new LegacyIRServiceHandler();
        redRatFacade  = new RedRatIRServiceHandler();
        
        Field field = irServiceProvider.getClass().getDeclaredField("legacyFacade");
        field.setAccessible(true);
        field.set(irServiceProvider, legacyFacade);
        
        Field field1 = irServiceProvider.getClass().getDeclaredField("redRatFacade");
        field1.setAccessible(true);
        field1.set(irServiceProvider, redRatFacade);
    }
    
    @Test
    public void getIRServiceNullTest(){
        assertNull( irServiceProvider.getIRService( null ));
    }
    
    @Test
    public void getIRServiceInvalidPathTest() throws URISyntaxException{
        assertNull( irServiceProvider.getIRService( new URI("invalid://localhost/?port=1") ));
    }
    
    @Test
    public void getIRServiceTest() throws URISyntaxException{
        assertEquals( legacyFacade, irServiceProvider.getIRService( new URI(GC100.getScheme()+"://localhost/?port=1") ));
        assertEquals( legacyFacade, irServiceProvider.getIRService( new URI(GC100_12.getScheme()+"://localhost/?port=1") ));
        assertEquals( legacyFacade, irServiceProvider.getIRService( new URI(GC100_6.getScheme()+"://localhost/?port=1") ));
        assertEquals( legacyFacade, irServiceProvider.getIRService( new URI(ITACH.getScheme()+"://localhost/?port=1") ));
        
        assertEquals( redRatFacade, irServiceProvider.getIRService( new URI(IRNETBOXPRO3.getScheme()+"://localhost/?port=1") ));
    }
}
