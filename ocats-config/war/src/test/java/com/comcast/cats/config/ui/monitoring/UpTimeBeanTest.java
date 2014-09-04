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

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

import com.comcast.cats.config.ui.monitoring.reboot.UpTimeBean;

public class UpTimeBeanTest
{

    @Test
    public void beanTest(){
        String dummyString = "dummy";
        UpTimeBean bean = new UpTimeBean();
        bean.setRebootDetectionTime( dummyString );
        bean.setSettopId( dummyString );
        bean.setSettopIP( dummyString );
        bean.setSettopMac( dummyString );
        bean.setSettopMCardMac( dummyString );
        bean.setSettopName( dummyString );
        bean.setSettopType( dummyString );
        bean.setUpTime( dummyString );
        
        assertEquals( dummyString, bean.getRebootDetectionTime() );
        assertEquals( dummyString, bean.getSettopId() );
        assertEquals( dummyString, bean.getSettopIP() );
        assertEquals( dummyString, bean.getSettopMac() );
        assertEquals( dummyString, bean.getSettopMCardMac() );
        assertEquals( dummyString, bean.getSettopName() );
        assertEquals( dummyString, bean.getSettopType() );
        assertEquals( dummyString, bean.getUpTime() );
    }
    
    @Test
    public void toStringTest(){
        String dummyString = null;
        UpTimeBean bean = new UpTimeBean();
        bean.setRebootDetectionTime( dummyString );
        bean.setSettopId( dummyString );
        bean.setSettopIP( dummyString );
        bean.setSettopMac( dummyString );
        bean.setSettopMCardMac( dummyString );
        bean.setSettopName( dummyString );
        bean.setSettopType( dummyString );
        bean.setUpTime( dummyString );
        bean.toString();
    }
}
