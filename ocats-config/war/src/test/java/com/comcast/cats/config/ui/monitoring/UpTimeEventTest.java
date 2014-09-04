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

import com.comcast.cats.config.ui.monitoring.reboot.UpTimeEvent;
import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootInfo;

public class UpTimeEventTest
{

    @Test
    public void beanTest(){
        MonitorTarget monitorTarget = new MonitorTarget();
        UpTimeEvent bean = new UpTimeEvent(monitorTarget);
        assertEquals( monitorTarget, bean.getMonitorTarget() );
    }
    
    @Test
    public void toStringTest(){
        MonitorTarget monitorTarget = new MonitorTarget();
        UpTimeEvent bean = new UpTimeEvent(monitorTarget);
        bean.toString();
        
        MonitorTarget monitorTarget1 = null;
        UpTimeEvent bean1 = new UpTimeEvent(monitorTarget1);
        bean1.toString();
    }
}
