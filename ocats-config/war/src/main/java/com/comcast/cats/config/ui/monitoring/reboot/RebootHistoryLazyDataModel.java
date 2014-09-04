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
package com.comcast.cats.config.ui.monitoring.reboot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.reboot.RebootInfo;

@Named
public class RebootHistoryLazyDataModel extends LazyDataModel< RebootInfo >
{
    private static final long serialVersionUID = -1185781441860099431L;

    List< RebootInfo >        rebootInfoList;
    String                    macAddress;
    String                    ipAddress;
    
    @Inject
    RebootMonitorService rebootMonitorService;
    
    private static Logger logger = LoggerFactory.getLogger( RebootHistoryLazyDataModel.class );


    public RebootHistoryLazyDataModel()
    {
    }

    @Override
    public List< RebootInfo > load( int first, int pageSize, String sortField, SortOrder sortOrder,
            Map< String, String > filters )
    {
        logger.trace( "lazy loading rebootInfo" );
        List< RebootInfo > rebootInfoList = new ArrayList< RebootInfo >();
        if ( macAddress != null && !macAddress.isEmpty() && ipAddress != null && !ipAddress.isEmpty() )
        {
           // rebootInfoList = rebootMonitorService.listReboots( macAddress, first, pageSize, null, null );
            rebootInfoList = rebootMonitorService.listAllReboots( macAddress );
            if(rebootInfoList != null){
                this.setRowCount(rebootInfoList.size() );
            }else{
                this.setRowCount(0);
            }
            this.rebootInfoList = rebootInfoList;
        }
        
        return rebootInfoList;
    }
    
    public String getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress( String macAddress )
    {
        this.macAddress = macAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress( String ipAddress )
    {
        this.ipAddress = ipAddress;
    }
}
