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
package com.comcast.cats.config.ui.monitoring.memory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.MeterGaugeChartModel; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.info.DiskSpaceUsage;
import com.comcast.cats.info.VideoRecorderServiceConstants;
import com.comcast.cats.service.util.HttpClientUtil;

@ManagedBean
@ApplicationScoped
public class MemoryManagementBean
{
    private MeterGaugeChartModel meterGaugeModel;

    private static final int FIRST_THRESHOLD_INDEX = 0;
    private static final int SECOND_THRESHOLD_INDEX = 1;
    private static final int TOTAL_MEMORY_INDEX = 2;

    private long freeMemory = 0;
    private long usedMemory = 0;
    private boolean thresholdLevelReached = false;
    
    private static Logger logger            = LoggerFactory.getLogger( MemoryManagementBean.class );
    
    public MemoryManagementBean() {  
       
    }
    
    @PostConstruct
    public void getInitialMemoryDetails(){
        refreshMemoryDetails();
    }
  
    public void refreshMemoryDetails(){
        DiskSpaceUsage usage = getDiskUsage();
        createMeterGaugeModel(usage);
    }
    
    private DiskSpaceUsage getDiskUsage(){
        DiskSpaceUsage usage = null;
        if(AuthController.getHostAddress() != null){
            try{
                usage = (DiskSpaceUsage)HttpClientUtil.getForObject( getRequestUri( VideoRecorderServiceConstants.REST_REQUEST_GET_DISKSPACE_USAGE ),
                    null );
                logger.trace("Usgae "+usage);
            }catch(Exception e){
                //if connection cant be established.
                usage = null;
            }
        }
        return usage;
        
        
    }
    
    private void createMeterGaugeModel(DiskSpaceUsage usage) {  
  
        List<Number> intervals = new ArrayList<Number>();
        if(usage != null){
            long totalSpace = convertToGB(usage.getTotalSpace());
            //first threshold is 50% of total space
            intervals.add( FIRST_THRESHOLD_INDEX , (totalSpace * 0.5) );
    
            //Second threshold is 75% of total space
            intervals.add( SECOND_THRESHOLD_INDEX , (totalSpace * 0.75) );
            
            //total space is the last value
            intervals.add( TOTAL_MEMORY_INDEX , totalSpace );
            freeMemory = convertToGB(usage.getUsableSpace());

            usedMemory = totalSpace - freeMemory;
            meterGaugeModel = new MeterGaugeChartModel(usedMemory, intervals);
            setMeterGaugeModel( meterGaugeModel );

            if(usedMemory > (totalSpace * 0.75)){
                FacesContext.getCurrentInstance().addMessage("response", new FacesMessage(FacesMessage.SEVERITY_WARN,"Disk Space Alert ", "Low disk space on video recorder server : "+freeMemory+" GB"));
            }
        } else{
            meterGaugeModel = null;
            FacesContext.getCurrentInstance().addMessage("response", new FacesMessage(FacesMessage.SEVERITY_WARN,"", "Cannot obtain Video Recorder Server Disk usage information"));
        }
    }
    
    private long convertToGB(long sizeInKB){
        return sizeInKB/(1024* 1024);
    }
    
    public long getFreeMemory(){
        return freeMemory;
    }

    public long getUsedMemory()
    {
        return usedMemory;
    }

    public void setUsedMemory( long usedMemory )
    {
        this.usedMemory = usedMemory;
    }

    public MeterGaugeChartModel getMeterGaugeModel()
    {
        return meterGaugeModel;
    }

    public void setMeterGaugeModel( MeterGaugeChartModel meterGaugeModel )
    {
        this.meterGaugeModel = meterGaugeModel;
        if(meterGaugeModel.getValue().longValue() > meterGaugeModel.getIntervals().get( SECOND_THRESHOLD_INDEX ).longValue()){
            thresholdLevelReached = true;
        }else{
            thresholdLevelReached = false;
        }
    }
    
    public boolean isThresholdLevelReached(){
        return thresholdLevelReached;
    }
    
    private String getRequestUri( String restRequest )
    {
        String requestUri = "http://" + AuthController.getHostAddress()
                + VideoRecorderServiceConstants.REST_REQUEST_EXTERNAL_PATH + restRequest;
        return requestUri;
    }
}
