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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.cats.config.ui.AuthController;
import com.comcast.cats.reboot.MonitorTarget;
import com.comcast.cats.reboot.RebootDetectionStatus;
import com.comcast.cats.reboot.RebootInfo;
import com.comcast.cats.reboot.RebootUtil;

/**
 * Managed Bean responsible for managing with view the latest upTime and reboot
 * stats.
 * 
 * @author skurup00c
 * 
 */
@ManagedBean
@SessionScoped
public class UpTimeAndRebootStatusBean
{

    private static Logger               logger         = LoggerFactory.getLogger( UpTimeAndRebootStatusBean.class );

    private UpTimeBean                  selectedUpTimeBean;

    @Inject
    private LazyDataModel< RebootInfo > rebootHistoryLazyModel;

    @Inject
    RebootMonitorService                rebootMonitorService;

    public static final String          UNKNOWN_STATUS = "Unknown";

    boolean                             showDataTable  = false;

    public UpTimeAndRebootStatusBean()
    {
    }

    public RebootMonitorService getRebootMonitorService()
    {
        return rebootMonitorService;
    }

    public void setRebootMonitorService( RebootMonitorService rebootMonitorService )
    {
        this.rebootMonitorService = rebootMonitorService;
    }

    /**
     * Checks if a upTime is less than one day.
     * 
     * @param upTime
     * @return true, if it is less than one day. False otherwise or if it cant
     *         be determined.
     */
    public boolean isLessThanOneDay( String upTime )
    {
        boolean retVal = false;
        try
        {
            if ( upTime != null && !upTime.isEmpty() )
            {
                if ( Integer.parseInt( upTime.substring( 0, 2 ) ) < 1 )
                {
                    retVal = true;
                }
            }
        }
        catch ( NumberFormatException e )
        {
            logger.debug( "isLessThanOneDay " + e.getMessage() );
            retVal = false;
        }
        logger.debug( "isLessThanOneDay " + upTime + " retVal " + retVal );
        return retVal;
    }

    public UpTimeBean getSelectedUpTimeBean()
    {
        return selectedUpTimeBean;
    }

    public void setSelectedUpTimeBean( UpTimeBean selectedUpTimeBean )
    {
        logger.info( "selectedUpTimeBean " + selectedUpTimeBean );
        if ( rebootHistoryLazyModel != null && selectedUpTimeBean != null )
        {
            ( ( RebootHistoryLazyDataModel ) rebootHistoryLazyModel ).setMacAddress( selectedUpTimeBean.getSettopMac() );
            ( ( RebootHistoryLazyDataModel ) rebootHistoryLazyModel ).setIpAddress( selectedUpTimeBean.getSettopIP() );
        }
        this.selectedUpTimeBean = selectedUpTimeBean;
    }

    public LazyDataModel< RebootInfo > getRebootHistoryLazyModel()
    {
        return rebootHistoryLazyModel;
    }

    public void setRebootHistorylazyModel( LazyDataModel< RebootInfo > rebootHistoryLazyModel )
    {
        this.rebootHistoryLazyModel = rebootHistoryLazyModel;
    }

    /**
     * Get the last reboot detected time for the selected settop.
     * 
     * @return Time as String.
     */
    public String getLastRebootTime()
    {
        String lastRebootDetectedTime = UNKNOWN_STATUS;

        if ( selectedUpTimeBean != null )
        {
            // get the current uptime and work out the last reboot time.
            MonitorTarget rebootInfo = rebootMonitorService.getUptime( selectedUpTimeBean.getSettopMac() );

            if ( rebootInfo != null && rebootInfo.getUpTime() > 0 )
            {
                Date latestRebootTime = calculateActualSettopRebootTime( rebootInfo.getExecutionDate(),
                        rebootInfo.getUpTime() );
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        UpTimeMonitoringConstants.REBOOT_DETECT_TIME_DISPLAY_FORMAT );
                lastRebootDetectedTime = dateFormat.format( latestRebootTime );
            }
        }
        logger.debug( "getLastRebootTime selectedUpTimeBean" + selectedUpTimeBean );
        return lastRebootDetectedTime;
    }

    /**
     * Get the uptime before the last reboot of the selected settop.
     * 
     * @return
     */
    public String getLastUptime()
    {
        String lastUpTime = UNKNOWN_STATUS;
        logger.info( "getLastUptime selectedUpTimeBean " + selectedUpTimeBean );
        if ( selectedUpTimeBean != null )
        {
            List< RebootInfo > rebootInfoList = rebootMonitorService.listAllReboots( selectedUpTimeBean.getSettopMac() );
            logger.info( "getLastUptime rebootInfoList " + rebootInfoList );
            if ( rebootInfoList != null && !rebootInfoList.isEmpty() )
            {
                if ( rebootInfoList.size() >= 2 )
                { // need atleast two records to calculate last uptime.
                    Date latestRebootTime;
                    RebootInfo latestRebootInfo = null;
                    int index = 0;

                    for ( RebootInfo rebootInfo : rebootInfoList )
                    {
                        index++;
                        if ( rebootInfo.getStatus().equals( RebootDetectionStatus.REBOOT_DETECTED ) )
                        {
                            latestRebootInfo = rebootInfo;
                            break;
                        }
                    }
                    if ( latestRebootInfo != null && index < rebootInfoList.size() )
                    {

                        latestRebootTime = calculateActualSettopRebootTime( latestRebootInfo.getExecutionDate(),
                                latestRebootInfo.getUpTime() );
                        logger.debug( "getLastUptime latestRebootTime " + latestRebootTime );

                        RebootInfo previousRebootInfo = null;
                        for ( int i = index; i < rebootInfoList.size(); i++ )
                        {
                            if ( rebootInfoList.get( i ).getUpTime() != null && rebootInfoList.get( i ).getUpTime() > 0 )
                            {
                                previousRebootInfo = rebootInfoList.get( i );
                                break;
                            }
                        }
                        if ( previousRebootInfo != null )
                        {
                            Date previousRebootTime = calculateActualSettopRebootTime(
                                    previousRebootInfo.getExecutionDate(), previousRebootInfo.getUpTime() );
                            logger.debug( "getLastUptime previousRebootTime " + previousRebootTime );
                            long upTimeMsOfBox = latestRebootTime.getTime() - previousRebootTime.getTime();
                            logger.debug( "getLastUptime upTimeHoursOfBox " + upTimeMsOfBox );
                            lastUpTime = convertMsToUpTimeFormat( upTimeMsOfBox );
                            logger.info( "getLastUptime lastUpTime " + lastUpTime );
                        }
                    }
                }
                else
                {
                    logger.info( "getLastUptime latestRebootTime. Not enough reboot data to calculate. " );
                }
            }
        }

        return lastUpTime;
    }

    private List< RebootInfo > filterActualRebootsFromList( List< RebootInfo > rebootInfoList )
    {
        List< RebootInfo > actualReboots = new ArrayList< RebootInfo >();
        if ( rebootInfoList != null )
        {

            for ( RebootInfo rebootInfo : rebootInfoList )
            {
                if ( rebootInfo.getStatus().equals( RebootDetectionStatus.REBOOT_DETECTED ) )
                {
                    actualReboots.add( rebootInfo );
                }
            }
        }
        return actualReboots;
    }

    /**
     * Get actual reboot time based on detectedtime and uptime.
     * 
     * @param rebootInfo
     * @return
     * @throws ParseException
     */
    private Date calculateActualSettopRebootTime( Date executionDate, long upTimeInTicks )
    {
        Date actualRebootTime = null;
        // Date executionDate = rebootInfo.getExecutionDate();
        long upTimeMilliSeconds = getAsMs( upTimeInTicks );
        logger.debug( "calculateActualSettopRebootTime executionDate " + executionDate + " upTimeMilliSeconds "
                + upTimeMilliSeconds );
        long timeDifference = executionDate.getTime() - upTimeMilliSeconds;
        actualRebootTime = new Date( timeDifference );
        logger.debug( "calculateActualSettopRebootTime actualRebootTime " + actualRebootTime );
        return actualRebootTime;
    }

    private String convertMsToUpTimeFormat( long upTimeMsOfBox )
    {
        long ticks = ( long ) ( upTimeMsOfBox * 0.1 ); // one ms = 0.1 ticks
        String upTime = RebootUtil.formatUptime( ticks );
        return upTime;
    }

    public int getRebootCountForLast5days()
    {
        int rebootCount = -1;
        logger.debug( "getRebootCountForLast5days selectedUpTimeBean " + selectedUpTimeBean );
        if ( selectedUpTimeBean != null )
        {
            Date currentTime = new Date();
            long dateBefore5DaysMillis = currentTime.getTime() - ( 5 * 24 * 60 * 60 * 1000 ); // 5
                                                                                              // days
                                                                                              // back
            Date pastDate = new Date( dateBefore5DaysMillis );
            List< RebootInfo > rebootsInLast5Days = rebootMonitorService.listAllReboots(
                    selectedUpTimeBean.getSettopMac(), currentTime, pastDate );
            List< RebootInfo > actualReboots = filterActualRebootsFromList( rebootsInLast5Days );
            if ( actualReboots != null && !actualReboots.isEmpty())
            {
                rebootCount = actualReboots.size();
            }
        }
        logger.trace( "getRebootCountForLast5days rebootCount " + rebootCount );
        return rebootCount;
    }

    public int getTotalRebootsCount()
    {
        int rebootCount = -1;
        logger.debug( "getTotalRebootsCount selectedUpTimeBean " + selectedUpTimeBean );
        if ( selectedUpTimeBean != null )
        {
            List< RebootInfo > rebootInfoList = rebootMonitorService.listAllReboots( selectedUpTimeBean.getSettopMac() );
            if(rebootInfoList != null && !rebootInfoList.isEmpty()){
                List< RebootInfo > actualReboots = filterActualRebootsFromList( rebootInfoList );
                if ( actualReboots != null && !actualReboots.isEmpty())
                {
                    rebootCount = actualReboots.size();
                }
            }
        }
        logger.trace( "getTotalRebootsCount rebootCount " + rebootCount );
        return rebootCount;
    }

    public int getAverageUpTime()
    {
        int averageUpTimeHrs = -1;
        logger.debug( "getAverageUpTime selectedUpTimeBean " + selectedUpTimeBean );
        if ( selectedUpTimeBean != null )
        {
            Date currentTime = new Date();
            long dateBefore5DaysMillis = currentTime.getTime() - ( 5 * 24 * 60 * 60 * 1000 );
            Date pastDate = new Date( dateBefore5DaysMillis );
            List< RebootInfo > rebootsInLast5Days = rebootMonitorService.listAllReboots(
                    selectedUpTimeBean.getSettopMac(), currentTime, pastDate );

            if ( rebootsInLast5Days != null && rebootsInLast5Days.size() > 0 )
            {
                List< RebootInfo > actualReboots = filterActualRebootsFromList( rebootsInLast5Days );

                int totalHours = 0;
                int count = 0;
                int average = -1;
                logger.debug( "getAverageUpTime rebootsInLast5Days " + rebootsInLast5Days );
                if ( actualReboots.size() > 1 ) // more than one reboot
                                                // available to calculate
                                                // average
                {
                    for ( RebootInfo rebootInfo : actualReboots )
                    {
                        count++;
                        long upTime = rebootInfo.getUpTime();
                        logger.debug( "getAverageUpTime rebootInfo.getUpTime() " + rebootInfo.getUpTime() );
                        long hours = upTime / RebootUtil.TICKS_PER_HOUR;
                        totalHours += hours;
                        average = totalHours / count;
                    }
                    averageUpTimeHrs = average;
                }
                else if ( actualReboots.size() == 1 )
                { // only one reboot happened, return the previous time
                    int index = 0;
                    for ( RebootInfo rebootInfo : rebootsInLast5Days )
                    {
                        index++;
                        if ( rebootInfo.getStatus().equals( RebootDetectionStatus.REBOOT_DETECTED ) )
                        {
                            if ( index < rebootsInLast5Days.size() )
                            {
                                averageUpTimeHrs = ( int ) ( rebootsInLast5Days.get( index ).getUpTime() / RebootUtil.TICKS_PER_HOUR );
                            }
                            else
                            {
                                averageUpTimeHrs = ( int ) ( rebootsInLast5Days.get( 0 ).getUpTime() / RebootUtil.TICKS_PER_HOUR ); // else
                                                                                                                           // return
                                                                                                                           // the
                                                                                                                           // latest
                                                                                                                           // uptime
                            }
                        }
                    }
                }
                else if ( actualReboots.size() == 0 )
                {
                    averageUpTimeHrs = ( int ) ( rebootsInLast5Days.get( 0 ).getUpTime() / RebootUtil.TICKS_PER_HOUR ); // else
                                                                                                               // return
                                                                                                               // the
                                                                                                               // latest
                                                                                                               // uptime
                }
            }
        }
        logger.trace( "getAverageUpTime averageUpTimeHrs " + averageUpTimeHrs );
        return averageUpTimeHrs;
    }

    private long getAsMs( long uptimeInTicks )
    {
        logger.debug( "getAsHours uptimeInTicks " + uptimeInTicks );
        long totalSeconds = uptimeInTicks / RebootUtil.TICKS_PER_SECOND;
        return totalSeconds * 1000; // in ms
    }

    public void postProcessXls( Object document )
    {
        logger.trace( "postProcessXls start document " + document );
        if ( document != null )
        {
            HSSFWorkbook workBook = ( HSSFWorkbook ) document;
            HSSFSheet sheet = workBook.getSheetAt( 0 );

            HSSFRow headerRow = sheet.getRow( 0 );

            for ( int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++ )
            {
                sheet.setColumnWidth( i, 30 * 265 ); // width for 40 characters
            }

            sheet.shiftRows( 0, sheet.getLastRowNum(), 5 ); // shift rows 0 to n
                                                            // by 1 to get space
                                                            // for header
            sheet.addMergedRegion( CellRangeAddress.valueOf( "A1:F3" ) );

            HSSFFont headerFont = workBook.createFont();
            headerFont.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );

            HSSFCellStyle headerCellStyle = workBook.createCellStyle();
            headerCellStyle.setFillForegroundColor( HSSFColor.LIGHT_CORNFLOWER_BLUE.index );
            headerCellStyle.setFillPattern( HSSFCellStyle.SOLID_FOREGROUND );
            headerCellStyle.setFont( headerFont );
            headerCellStyle.setAlignment( HSSFCellStyle.ALIGN_CENTER );
            headerCellStyle.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );

            HSSFCell headerCell = headerRow.createCell( 0 );
            headerCell.setCellStyle( headerCellStyle );
            headerCell.setCellValue( "CATS Uptime and Reboot Status : " + ( new Date() ) );

            HSSFCellStyle metaDataCellStyle = workBook.createCellStyle();
            metaDataCellStyle.setFont( headerFont );

            HSSFRow metaDataRow = sheet.getRow( 3 );
            if ( metaDataRow == null )
            {
                metaDataRow = sheet.createRow( 3 );
            }
            HSSFCell metaDataKey = metaDataRow.createCell( 0 );
            metaDataKey.setCellStyle( metaDataCellStyle );
            metaDataKey.setCellValue( "CATS Instance" );

            HSSFCell metaDataValue = metaDataRow.createCell( 1 );
            metaDataValue.setCellStyle( metaDataCellStyle );
            metaDataValue.setCellValue( AuthController.getHostAddress() );

            HSSFCellStyle datatTableHeaderCellStyle = workBook.createCellStyle();
            datatTableHeaderCellStyle.setFillForegroundColor( HSSFColor.LIGHT_YELLOW.index );
            datatTableHeaderCellStyle.setFillPattern( HSSFCellStyle.SOLID_FOREGROUND );
            datatTableHeaderCellStyle.setFont( headerFont );

            HSSFRow actualDataTableHeaderRow = sheet.getRow( 5 );
            for ( int i = 0; i < actualDataTableHeaderRow.getPhysicalNumberOfCells(); i++ )
            {
                HSSFCell cell = actualDataTableHeaderRow.getCell( i );
                if ( cell != null )
                {
                    String cellValue = cell.getStringCellValue();
                    cellValue = cellValue.replace( "<br/> ", "" ); // replace
                                                                   // any line
                                                                   // breaks
                    cell.setCellValue( cellValue );
                    cell.setCellStyle( datatTableHeaderCellStyle );
                }
            }

        }
        logger.trace( "postProcessXls end" );
    }

    public boolean isShowDataTable()
    {
        return showDataTable;
    }

    public void setShowDataTable( boolean showDataTable )
    {
        this.showDataTable = showDataTable;
    }

    public void onRowSelect( SelectEvent event )
    {
        showDataTable = rebootMonitorService.isSNMPRebootServiceIsReachable();
    }

    public void onRowUnselect( UnselectEvent event )
    {
        showDataTable = false;
    }

    public String getFileName()
    {
        String fileName = "";
        Calendar calendar = Calendar.getInstance();

        fileName += ( calendar.get( Calendar.MONTH ) + 1 ) + "_"; // January is
                                                                  // 0 : odd!!!
        fileName += calendar.get( Calendar.DAY_OF_MONTH ) + "_";
        fileName += calendar.get( Calendar.YEAR );

        return fileName;
    }
}
