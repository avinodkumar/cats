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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootHostStatus;
import com.comcast.cats.reboot.RebootInfo;
import com.comcast.cats.service.RebootDetection;

public class MockRebootDetectionImpl implements RebootDetection
{
    MonitorTarget mockMonitorTarget =  new MonitorTarget();
    RebootInfo mockRebootInfo =  new RebootInfo();
    boolean errorStatus = false;
    List<RebootInfo> rebootInfoList = new ArrayList< RebootInfo >();
    
    public void setErrorConditions(boolean errorStatus){
        System.out.println("irr "+errorStatus);
        this.errorStatus = errorStatus;
    }
    
    @Override
    public String version()
    {
        return null;
    }

    @Override
    public MonitorTarget current()
    {
        if(errorStatus){
            return null;
        }
        return mockMonitorTarget;
    }

//    @Override
//    public RebootInfo execute( String ip, String ecmMacAddress )
//    {
//        if(errorStatus){
//            return null;
//        }
//        return mockRebootInfo;
//    }

    @Override
    public Integer count()
    {
        System.out.println("or "+errorStatus);
        if(errorStatus){
            return null;
        }
        return 0;
    }

    @Override
    public List< RebootInfo > listAll()
    {
        if(errorStatus){
            return null;
        }
        return new ArrayList< RebootInfo >();
    }

    @Override
    public List< RebootInfo > list( Integer offset, Integer max, Date start, Date end )
    {
        if(errorStatus){
            return null;
        }
        rebootInfoList.add( mockRebootInfo );
        return rebootInfoList;
    }

    @Override
    public void add( String ip, String ecmMacAddress, RebootHostStatus status )
    {

    }

    @Override
    public void update( String ip, String ecmMacAddress, RebootHostStatus status )
    {

    }

    @Override
    public void delete()
    {
        
    }

}
