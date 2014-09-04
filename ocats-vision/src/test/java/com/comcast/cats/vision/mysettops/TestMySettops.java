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
package com.comcast.cats.vision.mysettops;

import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.Window;

import com.comcast.cats.vision.CATSVisionTest;
import com.comcast.cats.vision.test.utils.CommonTestUtils;

public class TestMySettops extends CATSVisionTest
{
    
    private String searchSettop = "00:19:47:25:AC:B8";

    public void testAvailableSettops() throws Exception
    {
        Window window = getMainWindow();

        JPanel configPanel = CommonTestUtils.getConfigPanel( window );

        Table uiTable = CommonTestUtils.getTableFromConfigPanel( configPanel, "availableSettopsScrollPane",
                "Available Settops", 0 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.AVAILABLE_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.AVAILABLE_TABLE_STRINGS ) );

        int numberOfRows = ( uiTable.getRowCount() > 0 ) ? uiTable.getRowCount() : 0;

        assertTrue( "'Available Settops' donot have any rows", numberOfRows > 0 );

        uiTable.addRowToSelection( 1 );
        assertTrue( "Row is not selected.", uiTable.rowIsSelected( 1 ) );

    }

    public void testLaunchCatsVisionFromMySettops() throws Exception
    {
        Window window = getMainWindow();

        JPanel configPanel = CommonTestUtils.getConfigPanel( window );

        Table uiTable = CommonTestUtils.getTableFromConfigPanel( configPanel, "availableSettopsScrollPane",
                "Available Settops", 0 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.AVAILABLE_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.AVAILABLE_TABLE_STRINGS ) );

        int numberOfRows = ( uiTable.getRowCount() > 0 ) ? uiTable.getRowCount() : 0;

        assertTrue( "'Available Settops' donot have any rows", numberOfRows > 0 );

        /*
         * uiTable.selectRow( 1 ); assertTrue( "Row is not selected.",
         * uiTable.rowIsSelected( 1 ) ); uiTable.doubleClick( 1, 2 ); String
         * macId = ( String ) uiTable.getContentAt( 1, 2 );
         */
        int[] rowSelected = searchAvailableSettopsAndGetRowsSelected( window, searchSettop );

        assertTrue( "No rows are selected.", rowSelected.length > 0 );

        uiTable.selectRow( rowSelected[ 0 ] );
        assertTrue( "Row is not selected.", uiTable.rowIsSelected( rowSelected[ 0 ] ) );
        uiTable.doubleClick( rowSelected[ 0 ], 2 );
        String macId = ( String ) uiTable.getContentAt( rowSelected[ 0 ], 2 );

        Thread.sleep( 9000 );

        assertTrue( "window.getTitle() = ", window.getTitle().equals( macId ) );

    }

    public void testReLaunchSameSettopFromMySettops() throws Exception
    {
        Window window = getMainWindow();

        JPanel configPanel = CommonTestUtils.getConfigPanel( window );

        Table uiTable = CommonTestUtils.getTableFromConfigPanel( configPanel, "availableSettopsScrollPane",
                "Available Settops", 0 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.AVAILABLE_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.AVAILABLE_TABLE_STRINGS ) );

        int numberOfRows = ( uiTable.getRowCount() > 0 ) ? uiTable.getRowCount() : 0;

        assertTrue( "'Available Settops' donot have any rows", numberOfRows > 0 );
        int[] rowSelected = searchAvailableSettopsAndGetRowsSelected( window, searchSettop );

        assertTrue( "No rows are selected.", rowSelected.length > 0 );

        uiTable.selectRow( rowSelected[ 0 ] );
        assertTrue( "Row is not selected.", uiTable.rowIsSelected( rowSelected[ 0 ] ) );
        uiTable.doubleClick( rowSelected[ 0 ], 2 );
        String macId = ( String ) uiTable.getContentAt( rowSelected[ 0 ], 2 );

        Thread.sleep( 9000 );

        assertTrue( "window.getTitle() = ", window.getTitle().equals( macId ) );

        // Relaunch the same settop

        uiTable.selectRow( rowSelected[ 0 ] );
        assertTrue( "Row is not selected.", uiTable.rowIsSelected( rowSelected[ 0 ] ) );
        uiTable.doubleClick( rowSelected[ 0 ], 2 );

    }

    public void testAvailableSettopsSearch() throws Exception
    {/*
      * 
      * Window mainWindow = getMainWindow();
      * 
      * JPanel configPanel = CommonTestUtils.getConfigPanel( mainWindow );
      * 
      * final Button searchButton = CommonTestUtils.getConfigPanelButton(
      * mainWindow, "searchButton" );
      * 
      * final JTextField searchTextField =
      * CommonTestUtils.getConfigPanelTextField( mainWindow, "searchTextField"
      * );
      * 
      * Table uiTable = CommonTestUtils.getTableFromConfigPanel( configPanel,
      * "availableSettopsScrollPane", "Available Settops", 0 );
      * 
      * assertTrue( "Expected header for the table is - " + Arrays.asList(
      * CommonTestUtils.AVAILABLE_TABLE_STRINGS ) + "\n but actual header is - "
      * + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
      * uiTable.getHeader().getColumnNames(),
      * CommonTestUtils.AVAILABLE_TABLE_STRINGS ) );
      * 
      * int numberOfRows = ( uiTable.getRowCount() > 0 ) ? uiTable.getRowCount()
      * : 0;
      * 
      * assertTrue( "'Available Settops' donot have any rows", numberOfRows > 0
      * );
      * 
      * uiTable.selectRow( 0 );
      * 
      * searchTextField.setText( "Settop." );
      * 
      * searchButton.click();
      * 
      * Table uiTableAfterSearch = CommonTestUtils.getTableFromConfigPanel(
      * configPanel, "availableSettopsScrollPane", "Available Settops", 0 );
      * 
      * int numberOfRowsAfterSearch = ( uiTableAfterSearch.getRowCount() > 0 ) ?
      * uiTableAfterSearch.getRowCount() : 0;
      * 
      * assertTrue( "No matching rows found for the keyword - '" +
      * searchTextField.getText() + "'", numberOfRowsAfterSearch > 0 );
      */
        searchAvailableSettopsAndGetRowsSelected( getMainWindow(), searchSettop );

    }

    

    public void testAllocatedSettopsSearch() throws Exception
    {
        Window mainWindow = getMainWindow();

        final Button searchButton = CommonTestUtils.getConfigPanelButton( mainWindow, "searchButton" );

        final JTextField searchTextField = CommonTestUtils.getConfigPanelTextField( mainWindow, "searchTextField" );

        Window videoGridWindow = launchMultivision( mainWindow, searchAvailableSettopsAndGetRowsSelected( mainWindow,
                searchSettop ) );

        CheckBox allocateAllCheckBox = getAllocateAllCheckBox(videoGridWindow);
        // Allocate all settops
        allocateAllCheckBox.click();

        JPanel configPanel = CommonTestUtils.getConfigPanel( getMainWindow() );

        Table uiTable = CommonTestUtils.getTableFromConfigPanel( configPanel, "allocatedSettopsScrollPane",
                "Allocated Settops", 1 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.ALLOCATED_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.ALLOCATED_TABLE_STRINGS ) );

        assertTrue( "No Allocated Settops found.", uiTable.getRowCount() > 0 );

        int numberOfRows = ( uiTable.getRowCount() > 0 ) ? uiTable.getRowCount() : 0;

        // uiTable.selectRow( 0 );

        searchTextField.setText( searchSettop );

        searchButton.click();

        Table uiTableAfterSearch = CommonTestUtils.getTableFromConfigPanel( configPanel, "allocatedSettopsScrollPane",
                "Allocated Settops", 1 );
        int numberOfRowsAfterSearch = ( uiTableAfterSearch.getRowCount() > 0 ) ? uiTableAfterSearch.getRowCount() : 0;

        assertTrue( "No matching rows found for the keyword - '" + searchTextField.getText() + "'",
                numberOfRowsAfterSearch > 0 );

        assertTrue( numberOfRowsAfterSearch <= numberOfRows );

        // Release all settops
        allocateAllCheckBox.click();

        uiTable = CommonTestUtils.getTableFromConfigPanel( CommonTestUtils.getConfigPanel( getMainWindow() ),
                "allocatedSettopsScrollPane", "Allocated Settops", 1 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.ALLOCATED_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.ALLOCATED_TABLE_STRINGS ) );

        assertTrue( "Settops are not released", uiTable.getRowCount() == 0 );
    }



    public void testAllocatedSettops() throws Exception
    {

        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, searchAvailableSettopsAndGetRowsSelected( mainWindow,
                searchSettop ) );

        Panel videoControlPanel = videoGridWindow.getPanel( "videoControlPanel" );

        Panel allocationPanel = videoControlPanel.getPanel( "allocationPanel" );

        CheckBox allocateAllCheckBox = allocationPanel.getCheckBox( "allocateAllCheckBox" );

        // Allocate all settops
        allocateAllCheckBox.click();

        Thread.sleep( 500 );

        Table uiTable = CommonTestUtils.getTableFromConfigPanel( CommonTestUtils.getConfigPanel( getMainWindow() ),
                "allocatedSettopsScrollPane", "Allocated Settops", 1 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.ALLOCATED_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.ALLOCATED_TABLE_STRINGS ) );

        assertTrue( "No Allocated Settops found.", uiTable.getRowCount() > 0 );

        /*
         * uiTable.selectRow( 0 );
         * 
         * assertTrue( "Unable to select row.", uiTable.rowIsSelected( 0 ) );
         */

        // Release all settops
        allocateAllCheckBox.click();

        uiTable = CommonTestUtils.getTableFromConfigPanel( CommonTestUtils.getConfigPanel( getMainWindow() ),
                "allocatedSettopsScrollPane", "Allocated Settops", 1 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.ALLOCATED_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.ALLOCATED_TABLE_STRINGS ) );

        assertTrue( "Settops are not released", uiTable.getRowCount() == 0 );
    }
}
