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
package com.comcast.cats.vision.task;

import static com.comcast.cats.vision.panel.configuration.TableType.ALLOCATED_SETTOPS_TABLE;
import static com.comcast.cats.vision.panel.configuration.TableType.AVAILABLE_SETTOPS_TABLE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.event.impl.AbstractManagedTask;
import com.comcast.cats.vision.panel.configuration.ConfigModel;
import com.comcast.cats.vision.panel.configuration.ConfigPanel;
import com.comcast.cats.vision.panel.configuration.ConfigPanelTableModel;
import com.comcast.cats.vision.panel.configuration.ConfigPanelUtil;
import com.comcast.cats.vision.panel.configuration.TableType;

/**
 * This task is responsible for populating 'Available Settops' and 'Allocated
 * Settops' tab in CATS Vision 'My Settops'. The execution of this class will be
 * handled by ManagedThreads.
 * 
 * @author aswathyann
 * 
 */
public class MySettopsTask extends AbstractManagedTask
{

    private ConfigPanelUtil       configPanelUtil;

    private ConfigModel           configModel;

    private ConfigPanelTableModel configPanelTableModel;

    private ConfigPanel           configPanel;

    private TableType             tableType;

    private static Logger         logger = Logger.getLogger( MySettopsTask.class );

    /**
     * Constructor for MySettopsTask
     * 
     * @param configPanelUtil
     *            instance of ConfigPanelUtil
     * @param configModel
     *            instance of ConfigModel
     * @param configPanelTableModel
     *            instance of ConfigPanelTableModel
     * @param configPanel
     *            instance of ConfigPanel
     * @param tableType
     *            Table type
     */
    public MySettopsTask( ConfigPanelUtil configPanelUtil, ConfigModel configModel,
            ConfigPanelTableModel configPanelTableModel, ConfigPanel configPanel, TableType tableType )
    {

        this.configPanelUtil = configPanelUtil;

        this.configModel = configModel;

        this.configPanelTableModel = configPanelTableModel;

        this.configPanel = configPanel;

        this.tableType = tableType;
    }

    @Override
    public void run()
    {
        handleMySettopsEvent( configPanelUtil, configModel, configPanelTableModel, tableType );

    }

    private void handleMySettopsEvent( ConfigPanelUtil configPanelUtil, ConfigModel configModel,
            ConfigPanelTableModel configPanelTableModel, TableType tableType )
    {

        logger.debug( "Inside handleMySettopsEvent()" );
        /*
        if ( tableType == TableType.ALLOCATED_SETTOPS_TABLE )
        {
            getAllocatedSettops();
        }
        else */ if ( tableType == TableType.AVAILABLE_SETTOPS_TABLE )
        {
            getAvailableSettops();
        }
    }

    @Override
    public Object getIdentifier()
    {
        return tableType;
    }

    private void getAvailableSettops()
    {
        // Showing the loading animation image.        
        String searchKeyword = configPanel.getSearchTextField().getText().trim(); 
        configPanel.getAvailableSettopsScrollPane().setViewportView( configPanel.getAvailableLoadingLabel() );
        
        List< SettopReservationDesc > allAvailableSettops = configModel
                .splitSettopReservationDescBasedOnActiveReservations( configPanelUtil.getAllAvailableSettops() );
       
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "AllAvailableSettops " + allAvailableSettops );
        }
        
        Map< String, SettopReservationDesc>   availableSettops    = new HashMap< String, SettopReservationDesc>();
        configPanelTableModel.getDataVector().removeAllElements();
        configPanelTableModel.setRowCount( 0 );
        String key;
    //    String reservationName ;
        
        for ( SettopReservationDesc settopReservationDesc : allAvailableSettops )
        {     
           
            key = settopReservationDesc.getHostMacAddress();
            //Settop macId + Reservation name is used as unique identifier
            if ( ( settopReservationDesc.getActiveReservationList() != null ) )
            {
    //            reservationName = settopReservationDesc.getActiveReservationList().get( 0 ).getName(); 
                key = settopReservationDesc.getHostMacAddress();// + reservationName ;
            }
            availableSettops.put( key, settopReservationDesc );
            if ( !searchKeyword.isEmpty() ){
                if ( ( settopReservationDesc.toString().toLowerCase() ).contains( searchKeyword.toLowerCase() ) )
                {   
                    configPanelTableModel.addRow( settopReservationDesc, AVAILABLE_SETTOPS_TABLE );                   
                } 
            }
            else{
                configPanelTableModel.addRow( settopReservationDesc, AVAILABLE_SETTOPS_TABLE );   
            }   
        }
        configModel.setAvailableSettops( availableSettops );     
        configPanel.updateTableData( AVAILABLE_SETTOPS_TABLE, configPanelTableModel );
        
        // Adding new sorter will destroy all the existing sorter associated 
        RowSorter<ConfigPanelTableModel> availableSorter = new TableRowSorter<ConfigPanelTableModel>(configPanelTableModel);
        configPanelTableModel.fireTableDataChanged();
        configPanel.setAvailableSorter( availableSorter );
        configPanel.getAvailableSettopsScrollPane().setViewportView( configPanel.getAvailableSettopsTable() );

        configPanel.setFocusedTable( AVAILABLE_SETTOPS_TABLE );

        configPanel.autoResizeTable( AVAILABLE_SETTOPS_TABLE );
    }

    private void getAllocatedSettops()
    {
        // Showing the loading animation image.
        String searchKeyword = configPanel.getSearchTextField().getText().trim();   
        configPanel.getAllocatedSettopsScrollPane().setViewportView( configPanel.getAllocatedLoadingLabel() );
        
        final List< SettopReservationDesc > allAllocatedSettops = configPanelUtil.getAllAllocatedSettops();
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "allAllocatedSettops " + allAllocatedSettops );

        }
        Map< String, SettopReservationDesc>   allocatedSettops    = new HashMap< String, SettopReservationDesc>();
        configPanelTableModel.getDataVector().removeAllElements();
        configPanelTableModel.setRowCount( 0 );
       
        String key;
        String reservationName ;
        for ( SettopReservationDesc settopReservationDesc : allAllocatedSettops )
        {
            key = settopReservationDesc.getHostMacAddress();
            //Settop macId + Reservation name is used as unique identifier
            if ( ( settopReservationDesc.getActiveReservationList() != null ) )
            {
                reservationName = settopReservationDesc.getActiveReservationList().get( 0 ).getName(); 
                key = settopReservationDesc.getHostMacAddress() + reservationName ;
            }
            
            allocatedSettops.put( key , settopReservationDesc );
            if ( !searchKeyword.isEmpty() ){
                if ( ( settopReservationDesc.toString().toLowerCase() ).contains( searchKeyword.toLowerCase() ) )
                {     
                    configPanelTableModel.addRow( settopReservationDesc, ALLOCATED_SETTOPS_TABLE );                   
                } 
            }
            else{
                configPanelTableModel.addRow( settopReservationDesc, ALLOCATED_SETTOPS_TABLE );
            }
        }
        configModel.setAllocatedSettops( allocatedSettops );
        configPanel.updateTableData( ALLOCATED_SETTOPS_TABLE, configPanelTableModel ); 
        
        // Adding new sorter will destroy all the existing sorter associated 
        RowSorter<ConfigPanelTableModel> allocatdSorter = new TableRowSorter<ConfigPanelTableModel>(configPanelTableModel);
        configPanelTableModel.fireTableDataChanged();
        configPanel.setAllocationSorter( allocatdSorter );
        configPanel.getAllocatedSettopsScrollPane().setViewportView( configPanel.getAllocatedSettopsTable() );

        configPanel.setFocusedTable( ALLOCATED_SETTOPS_TABLE );

        configPanel.autoResizeTable( ALLOCATED_SETTOPS_TABLE );

    }
}
