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
package com.comcast.cats.vision.panel.configuration;

import static com.comcast.cats.vision.panel.configuration.ConfigConstants.LAUNCH_VIDEO;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.REFRESH_SETTOP_LIST;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.SEARCH_SETTOP;
import static com.comcast.cats.vision.panel.configuration.ConfigConstants.SEARCH_TEXT_FIELD;
import static com.comcast.cats.vision.panel.configuration.TableType.ALLOCATED_SETTOPS_TABLE;
import static com.comcast.cats.vision.panel.configuration.TableType.AVAILABLE_SETTOPS_TABLE;
import static com.comcast.cats.vision.panel.configuration.TableType.SETTOP_PROERTIES_TABLE;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.SettopReservationDesc;
import com.comcast.cats.domain.configuration.CatsProperties;
import com.comcast.cats.event.impl.ManagedThreadPool;
import com.comcast.cats.vision.panel.videogrid.VideoGridController;
import com.comcast.cats.vision.task.MySettopsTask;
import com.comcast.cats.vision.util.CatsVisionConstants;
import com.comcast.cats.vision.util.CatsVisionUtils;

/**
 * This class acts as the Controller for the Configuration module.
 * 
 * @author ajith
 * 
 */
@Named
public class ConfigPanelController implements ListSelectionListener, ActionListener, MouseListener, ChangeListener
{

    private ConfigPanel           panel;

    private VideoGridController   videoGridController;

    private ConfigPanelUtil       configPanelUtil;

    private String                authToken;

    private static final Logger   logger            = Logger.getLogger( ConfigPanelController.class );
    private int                   selectedRow;
    private int[]                 indexRowsSelected =
                                                        { -1 };
    private ConfigPanelTableModel availableTableModel;
    private ConfigPanelTableModel allocatedTableModel;

    private JTable                availableSettopsTable;
    private JTable                allocatedSettopsTable;

    private ConfigModel           configModel;
    private ManagedThreadPool     managedThreadPool;
    private int                   hostMacIndex      = 2;
    private int                   reservationIndex  = 4;

    /**
     * Constructor for ConfigPanelController
     * 
     * @param configPanel
     *            instance of ConfigPanel
     * @param configModel
     *            instance of ConfigModel
     * @param videoGridController
     *            instance of VideoGridController
     * @param configPanelUtil
     *            instance of ConfigPanelUtil
     * @param managedThreadPool
     *            instance of ManagedThreadPool
     * @param catsProperties
     *            instance of CatsProperties
     */
    @Inject
    public ConfigPanelController( ConfigPanel configPanel, ConfigModel configModel,
            VideoGridController videoGridController, ConfigPanelUtil configPanelUtil,
            ManagedThreadPool managedThreadPool, CatsProperties catsProperties )
    {
        this.panel = configPanel;
        this.videoGridController = videoGridController;
        this.authToken = catsProperties.getAuthToken();
        this.configModel = configModel;
        this.configPanelUtil = configPanelUtil;
        this.managedThreadPool = managedThreadPool;
        initListeners();

    }

    private void initListeners()
    {
        panel.addMouseListener( this );
        panel.addActionListener( this );
        panel.addChangeListener( this );
        panel.addListSelectionListener( this );
    }

    @Override
    public void valueChanged( ListSelectionEvent listSelectionEvent )
    {
        if ( listSelectionEvent.getValueIsAdjusting() )
        {
            return;
        }
        /*
         * To make the selection is empty
         */
        selectedRow = -1;
        if ( indexRowsSelected.length > 0 )
        {
            for ( int i = 0; i < indexRowsSelected.length; i++ )
            {
                indexRowsSelected[ i ] = -1;
            }
        }

        final ListSelectionModel listSelectionModel = ( ListSelectionModel ) listSelectionEvent.getSource();

        if ( !( listSelectionModel.isSelectionEmpty() ) )
        {

            final Component selectedComponent = panel.getTabbedPane().getSelectedComponent();

            if ( panel.getAvailableSettopsScrollPane().equals( selectedComponent ) )
            {
                availableSettopsTable = panel.getAvailableSettopsTable();

                indexRowsSelected = availableSettopsTable.getSelectedRows();

                if ( indexRowsSelected.length == 1 )
                {

                    selectedRow = indexRowsSelected[ 0 ];
                    String key = availableSettopsTable.getValueAt( selectedRow, hostMacIndex ).toString();
                          //  + availableSettopsTable.getValueAt( selectedRow, reservationIndex ).toString();
                    // Fill settop property table
                    System.out.println("key "+key);
                    fillPropertyValueTable( configModel.getAvailableSettops().get( key ) );

                }
                else if ( indexRowsSelected.length > 1 )
                {
                    /*
                     * Clear settop property table
                     */
                    clearPropertyValueTable();

                }
                /*
                 * Find the selected Mac Ids and also remove the previously
                 * selected mac Ids from the macIDMap.
                 */
                findSelectedMacIDs( getSearchKeyword() );
            }
            else if ( panel.getAllocatedSettopsScrollPane().equals( selectedComponent ) )
            {
                allocatedSettopsTable = panel.getAllocatedSettopsTable();

                selectedRow = listSelectionModel.getMinSelectionIndex();
                String key = allocatedSettopsTable.getValueAt( selectedRow, hostMacIndex ).toString()
                        + allocatedSettopsTable.getValueAt( selectedRow, reservationIndex ).toString();

                fillPropertyValueTable( configModel.getAllocatedSettops().get( key ) );

            }
            panel.autoResizeTable( SETTOP_PROERTIES_TABLE );
        }
    }

    /**
     * Method to fill the data in Available and Allocated JTables. This method
     * will be called during the start up of CATS Vision.
     */
    public void fillConfigDataTables()
    {
        enableLaunchVideoButton();

        fillAvailableSettopTab( authToken );
        fillAllocatedSettopTab( authToken );
    }

    private void updateTable()
    {
        // To clear the selected row in each of the tabs
        panel.getAvailableSettopsTable().clearSelection();
        panel.getAllocatedSettopsTable().clearSelection();

        configModel.getHighlightedRowMap().clear();

        // Clearing the Property value table
        ConfigPanelTableModel tableModel = panel.getPropertyValueModel();
        tableModel.getDataVector().removeAllElements();

        panel.updateTableData( SETTOP_PROERTIES_TABLE, tableModel );
        panel.autoResizeTable( SETTOP_PROERTIES_TABLE );
        fillDataInSelectedTab( authToken );
    }

    /**
     * Method to call appropriate method to fill data in JTable.
     */
    private void fillDataInSelectedTab( String authToken )
    {
        JTabbedPane resultsTabbedPane = panel.getTabbedPane();
        final Component selectedComponent = resultsTabbedPane.getSelectedComponent();

        if ( panel.getAvailableSettopsScrollPane().equals( selectedComponent ) )
        {
            fillAvailableSettopTab( authToken );

        }
        else if ( panel.getAllocatedSettopsScrollPane().equals( selectedComponent ) )
        {
            fillAllocatedSettopTab( authToken );

        }
    }

    private void enableLaunchVideoButton()
    {
        JTabbedPane resultsTabbedPane = panel.getTabbedPane();
        final Component selectedComponent = resultsTabbedPane.getSelectedComponent();

        if ( panel.getAvailableSettopsScrollPane().equals( selectedComponent ) )
        {
            panel.getLaunchVideoButton().setEnabled( true );
            panel.getLaunchVideoCheckBox().setEnabled( true );

        }
        else if ( panel.getAllocatedSettopsScrollPane().equals( selectedComponent ) )
        {
            panel.getLaunchVideoButton().setEnabled( false );
            panel.getLaunchVideoCheckBox().setEnabled( false );
        }
    }

    /**
     * Fill the data to the Available STB table. This gets the data from the
     * allocation service. The previous data is flushed and new data will be
     * populated.
     */
    private void fillAvailableSettopTab( String authToken )
    {
        clearAvailableSettopTable();

        if ( authToken != null && !authToken.isEmpty() )
        {

            MySettopsTask mySettopsTask = new MySettopsTask( configPanelUtil, configModel, availableTableModel, panel,
                    AVAILABLE_SETTOPS_TABLE );
            managedThreadPool.addTask( mySettopsTask );
        }

    }

    /**
     * Fills the data to the allocated Settops Tab. The previous data is flushed
     * and new data will be populated.
     */
    private void fillAllocatedSettopTab( String authToken )
    {
        clearAllocatedSettopTable();
        if ( authToken != null && !authToken.isEmpty() )
        {

            MySettopsTask mySettopsTask = new MySettopsTask( configPanelUtil, configModel, allocatedTableModel, panel,
                    ALLOCATED_SETTOPS_TABLE );
            managedThreadPool.addTask( mySettopsTask );
        }

    }

    private void clearAllocatedSettopTable()
    {
        allocatedTableModel = panel.getAllocatedSettopModel();
        allocatedTableModel.getDataVector().removeAllElements();
        allocatedTableModel.setNumRows( 0 );
        allocatedTableModel.fireTableDataChanged();
        panel.updateTableData( ALLOCATED_SETTOPS_TABLE, allocatedTableModel );

    }

    private void clearAvailableSettopTable()
    {
        availableTableModel = panel.getAvailableSettopModel();
        availableTableModel.getDataVector().removeAllElements();
        availableTableModel.setNumRows( 0 );
        availableTableModel.fireTableDataChanged();
        panel.updateTableData( AVAILABLE_SETTOPS_TABLE, availableTableModel );

    }

    /**
     * Method to fill the Property value table.
     * 
     * @param settopReservationDesc
     *            instance of SettopReservationDesc
     */
    private void fillPropertyValueTable( final SettopReservationDesc settopReservationDesc )
    {
        // Remove any existing properties.
        ConfigPanelTableModel tableModel = panel.getPropertyValueModel();
        tableModel.getDataVector().removeAllElements();
        // Settop Properties can be identified as getXXX methods in SettopDesc
        // class.
        // To identify the different properties, we need to identify all the
        // getXXX methods in SettopDesc.
        tableModel = populateSettopDescData( tableModel, settopReservationDesc );

        panel.updateTableData( SETTOP_PROERTIES_TABLE, tableModel );
        panel.autoResizeTable( SETTOP_PROERTIES_TABLE );
    }

    private ConfigPanelTableModel populateSettopDescData( ConfigPanelTableModel tableModel, SettopDesc settDesc )
    {

        Method[] methods = new SettopDesc().getClass().getDeclaredMethods();

        for ( Method method : methods )
        {
            // Get all "getXXX" methods that has no arguments.
            if ( method.getName().startsWith( "get" ) && method.getModifiers() == Modifier.PUBLIC && method.getParameterTypes().length == 0 )
            {

                // We need to show only properties that have a non-null vale and
                // is not a list.
                Object object = null;
                try
                {

                    object = method.invoke( settDesc );

                    if ( object != null && !( object instanceof List< ? > ) )
                    {
                        // Check for hashmap to deal with extraproperties.
                        // FIXME: Is this right???
                        final Vector< Object > propertyValueData = new Vector< Object >();
                        if ( object instanceof HashMap< ?, ? > )
                        {
                            Set< ? > keySet = ( ( HashMap< ?, ? > ) object ).keySet();
                            for ( Object key : keySet )
                            {
                                propertyValueData.add( key.toString() );
                                propertyValueData.add( ( ( HashMap< ?, ? > ) object ).get( key ) );
                            }
                        }
                        else
                        {
                            propertyValueData.add( method.getName().substring( 3 ) );
                            propertyValueData.add( object );
                        }

                        tableModel.addRow( propertyValueData );
                    }
                }
                catch ( IllegalArgumentException e )
                {
                    e.printStackTrace();
                }
                catch ( IllegalAccessException e )
                {
                    e.printStackTrace();
                }
                catch ( InvocationTargetException e )
                {
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                }
            }
        }
        return tableModel;
    }

    /**
     * Method to clear the Property value table.
     */
    private void clearPropertyValueTable()
    {
        ConfigPanelTableModel tableModel = panel.getPropertyValueModel();
        tableModel.getDataVector().removeAllElements();
        panel.updateTableData( SETTOP_PROERTIES_TABLE, tableModel );
    }

    /**
     * Filter Settops based on the focused table
     */
    private void filterSettops()
    {
        String keyword = getSearchKeyword();
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Search keyword = " + keyword );
        }
        if ( ( panel != null ) && ( panel.getFocusedTable() != null ) )
        {
            TableType focusedTableType = panel.getFocusedTable();

            if ( focusedTableType.equals( AVAILABLE_SETTOPS_TABLE ) )
            {
                filterAvailableSettops( keyword );
            }
            else if ( focusedTableType.equals( ALLOCATED_SETTOPS_TABLE ) )
            {
                filterAllocatedSettops( keyword );
            }
        }
    }

    /**
     * Filter Settops in AvailableSettops table
     * 
     * @param keyword
     *            Search keyword
     */
    private void filterAvailableSettops( final String keyword )
    {
        if ( logger.isDebugEnabled() )
        {
            logger.debug( "Filter AvailableSettops " );
        }
        /*
         * Clear the contents in table
         */
        ConfigPanelTableModel tableFilterModel = panel.getAvailableSettopModel();
        tableFilterModel.getDataVector().removeAllElements();
        tableFilterModel.setNumRows( 0 );
        tableFilterModel.fireTableDataChanged();
        if ( availableSettopsTable != null )
        {
            availableSettopsTable.clearSelection();
            clearPropertyValueTable();
        }

        if ( !keyword.isEmpty() )
        {
            /*
             * Filter settops based on the keyword
             */
            for ( SettopReservationDesc settopDesc : configModel.getAvailableSettops().values() )
            {
                if ( ( settopDesc.toString().toLowerCase() ).contains( keyword.toLowerCase() ) )
                {

                    tableFilterModel.addRow( settopDesc, AVAILABLE_SETTOPS_TABLE );
                }
            }
        }
        else
        {
            /*
             * Unfilter settops
             */
            for ( SettopReservationDesc settopDesc : configModel.getAvailableSettops().values() )
            {

                tableFilterModel.addRow( settopDesc, AVAILABLE_SETTOPS_TABLE );
            }
        }
        panel.updateTableData( AVAILABLE_SETTOPS_TABLE, tableFilterModel );
        highlightSettops( keyword );
    }

    /**
     * Filter Settops in AllocatedSettops table
     * 
     * @param keyword
     *            Search keyword
     */
    private void filterAllocatedSettops( final String keyword )
    {
        ConfigPanelTableModel tableModel = panel.getAllocatedSettopModel();
        tableModel.getDataVector().removeAllElements();
        tableModel.setNumRows( 0 );
        tableModel.fireTableDataChanged();

        if ( allocatedSettopsTable != null )
        {
            allocatedSettopsTable.clearSelection();
            clearPropertyValueTable();
        }
        if ( !keyword.isEmpty() )
        {
            for ( SettopReservationDesc settopDesc : configModel.getAllocatedSettops().values() )
            {
                if ( ( settopDesc.toString().toLowerCase() ).contains( keyword.toLowerCase() ) )
                {

                    tableModel.addRow( settopDesc, ALLOCATED_SETTOPS_TABLE );

                }
            }
        }
        else
        {
            for ( SettopReservationDesc settopDesc : configModel.getAllocatedSettops().values() )
            {

                tableModel.addRow( settopDesc, ALLOCATED_SETTOPS_TABLE );
            }
        }
        panel.updateTableData( ALLOCATED_SETTOPS_TABLE, allocatedTableModel );
    }

    /**
     * Highlight the settops which are already selected by user(This neccessary
     * because after each search the highlighted settops get cleared)
     * 
     * @param keyword
     *            Search keyword (Acts as a key)
     */
    private void highlightSettops( final String keyword )
    {
        /*
         * Get all the selected settops' macID
         */
        Set< String > highlightedRows = configModel.getHighlightedRowIds();

        if ( panel.getFocusedTable().equals( AVAILABLE_SETTOPS_TABLE ) && !highlightedRows.isEmpty() )
        {
            List< String > highLightedRowIds = new LinkedList< String >( highlightedRows );

            JTable table = panel.getAvailableSettopsTable();
            /*
             * Check if the table contains any selected macIDs, if so highlight
             * those table rows.
             */
            if ( table != null )
            {
                for ( int i = 0; i < table.getRowCount(); i++ )
                {
                    for ( String highLightedRowId : highLightedRowIds )
                    {
                        if ( ( table.getValueAt( i, 2 ) != null ) && ( table.getValueAt( i, 4 ) != null ) )
                        {
                            String identifier = ( String ) ( table.getValueAt( i, 2 ) )
                                    + ( String ) ( table.getValueAt( i, 4 ) );
                            if ( identifier.equals( highLightedRowId ) )
                            {
                                // Highlighting the table row
                                table.addRowSelectionInterval( i, i );
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * Get search keyword from the config panel
     * 
     * @return search keyword
     */
    private String getSearchKeyword()
    {
        String searchKeyword = panel.getSearchTextField().getText();

        return searchKeyword.trim();
    }

    /*
     * Find the newly highlighted Mac Ids and also remove the previously
     * highlighted mac Ids from the macIDMap.
     */
    private void findSelectedMacIDs( final String keyword )
    {
        Set< String > oldHighlightedRowIds = new LinkedHashSet< String >();

        if ( keyword.isEmpty() )
        {
            /*
             * If search keyword is empty, then need to get all highlighted
             * settops.(When no keyword is given unfiltering happens)
             */
            oldHighlightedRowIds = configModel.getHighlightedRowIds();
        }
        else
        {
            oldHighlightedRowIds = configModel.getHighlightedRowMap().get( keyword );
        }
        Set< String > newHighlightedRowIds = getNewHighlightedRowIds( indexRowsSelected );

        Set< String > unselectedRowIDs = findUnselectedRowIDs( oldHighlightedRowIds, newHighlightedRowIds );

        if ( logger.isTraceEnabled() )
        {
            logger.trace( "Old Highlighted Row Identifiers =  = " + oldHighlightedRowIds + "\n"
                    + "New Highlighted Row Identifiers = " + newHighlightedRowIds + "\n" + "unselectedRowIDs ="
                    + unselectedRowIDs );
        }
        /*
         * Remove the unselected row ID from the map
         */
        if ( unselectedRowIDs != null )
        {
            removeUnselectedRowIDs( unselectedRowIDs );
        }
        if ( newHighlightedRowIds != null )
        {
            configModel.getHighlightedRowMap().put( keyword, newHighlightedRowIds );
        }
    }

    /*
     * Remove the unselected row IDs from the map
     */
    private void removeUnselectedRowIDs( final Set< String > deselectedMacIds )
    {
        Map< String, Set< String >> macIDMap = configModel.getHighlightedRowMap();
        for ( String key : macIDMap.keySet() )
        {
            Set< String > macIDs = macIDMap.get( key );
            for ( String deselectedMacId : deselectedMacIds )
            {
                if ( macIDs.contains( deselectedMacId ) )
                {
                    macIDs.remove( deselectedMacId );
                }
            }
        }
    }

    /*
     * Find unselected Settops
     */
    private Set< String > findUnselectedRowIDs( final Set< String > oldHighlightedRowIDs,
            final Set< String > newHighlightedRowIDs )
    {
        Set< String > deselectedRowIDs = new LinkedHashSet< String >();

        if ( ( oldHighlightedRowIDs != null ) && ( newHighlightedRowIDs != null ) )
        {
            for ( String oldHighlightedRowID : oldHighlightedRowIDs )
            {
                /*
                 * Checks if the newly highlighted row ids contains previously
                 * highlighted row id, If not the previously highlighted row id
                 * will be marked as deselected.
                 */

                if ( !newHighlightedRowIDs.contains( oldHighlightedRowID ) )
                {
                    deselectedRowIDs.add( oldHighlightedRowID );
                }
            }
        }
        return deselectedRowIDs;
    }

    /*
     * Get highlighted rows mac id
     */
    private Set< String > getNewHighlightedRowIds( final int[] rowsSelected )
    {
        Set< String > highlightedRows = new LinkedHashSet< String >();
        JTable table = panel.getAvailableSettopsTable();
        if ( rowsSelected.length != 0 )
        {
            for ( int i = 0; i < rowsSelected.length; i++ )
            {
                if ( rowsSelected[ i ] != -1 )
                {
                    // Settop macId + Reservation name is used as unique
                    // identifier
                    String identifier = ( String ) table.getValueAt( rowsSelected[ i ], 2 )
                            + ( String ) table.getValueAt( rowsSelected[ i ], 4 );
                    highlightedRows.add( identifier );
                }
            }
        }
        return highlightedRows;
    }

    @Override
    public void actionPerformed( ActionEvent actionEvent )
    {
        if ( actionEvent.getSource() instanceof JButton )
        {

            JButton button = ( JButton ) actionEvent.getSource();

            String buttonText = button.getText();

            if ( buttonText.equals( LAUNCH_VIDEO ) )
            {
                /*
                 * Get the selected settops
                 */
                Set< String > macIdSet = configModel.getHighlightedMacIds();

                if ( !macIdSet.isEmpty() )
                {
                    try
                    {
                        videoGridController.launchCatsVision( macIdSet );

                    }
                    catch ( MalformedURLException e )
                    {
                        logger.debug( "MalformedURLException : " + e );
                    }
                    catch ( URISyntaxException e )
                    {
                        logger.debug( "URISyntaxException : " + e );
                    }

                }
                else
                {
                    CatsVisionUtils.showWarning( "No Settops Selected",
                            "Please select Settops before launching "+CatsVisionConstants.APPLICATION_TITLE );
                }
            }
            else if ( buttonText.equals( SEARCH_SETTOP ) )
            {
                filterSettops();
            }
            else if ( buttonText.equals( REFRESH_SETTOP_LIST ) )
            {
                updateTable();
            }
        }
        else if ( actionEvent.getSource() instanceof JTextField )
        {
            JTextField textField = ( JTextField ) actionEvent.getSource();

            if ( textField.getName().equals( SEARCH_TEXT_FIELD ) )
            {
                filterSettops();
            }
        }
    }

    @Override
    public void mouseClicked( MouseEvent evt )
    {
        if ( evt.getSource() instanceof JTable )
        {
            JTable selectedtable = ( JTable ) evt.getSource();

            if ( evt.getClickCount() == 2 )
            {
                if ( null != selectedtable.getValueAt( selectedRow, 2 ) )
                {
                    String macAddress = selectedtable.getValueAt( selectedRow, 2 ).toString();
                    /*
                     * If the user has double clicked a row in the given table,
                     * the activate settop in that row.
                     */
                    if ( null != macAddress && !macAddress.isEmpty() && evt.getClickCount() == 2 )
                    {
                        logger.debug( "Double clicked the row containing mac address -" + macAddress );

                        try
                        {
                            videoGridController.launchCatsVision( macAddress );

                        }
                        catch ( MalformedURLException e )
                        {
                            logger.error( "MalformedURLException : " + e );
                        }
                        catch ( URISyntaxException e )
                        {
                            logger.error( "URISyntaxException : " + e );
                        }
                    }
                }
                else if ( null == selectedtable.getValueAt( selectedRow, 2 ) )
                {
                    CatsVisionUtils.showNoMacFoundError();
                }
            }
        }
    }

    @Override
    public void mousePressed( MouseEvent evt )
    {
        /*
         * This is to handle table row deselection.
         */

        if ( evt.isControlDown() && ( panel.getFocusedTable() == TableType.AVAILABLE_SETTOPS_TABLE ) )
        {
            indexRowsSelected = panel.getAvailableSettopsTable().getSelectedRows();
            findSelectedMacIDs( getSearchKeyword() );
        }
    }

    @Override
    public void mouseEntered( MouseEvent e )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited( MouseEvent e )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
        // TODO Auto-generated method stub

    }

    /**
     * This will be called on the allocated and available settop tab change.
     */
    @Override
    public void stateChanged( ChangeEvent evt )
    {
        JTabbedPane resultsTabbedPane = panel.getTabbedPane();
        final Component selectedComponent = resultsTabbedPane.getSelectedComponent();

        if ( panel.getAvailableSettopsScrollPane().equals( selectedComponent ) )
        {
            panel.setFocusedTable( AVAILABLE_SETTOPS_TABLE );
            panel.getLaunchVideoButton().setEnabled( true );
            panel.getLaunchVideoCheckBox().setEnabled( true );
        }
        else if ( panel.getAllocatedSettopsScrollPane().equals( selectedComponent ) )
        {
            panel.setFocusedTable( ALLOCATED_SETTOPS_TABLE );
            panel.getLaunchVideoButton().setEnabled( false );
            panel.getLaunchVideoCheckBox().setEnabled( false );
        }
    }
}