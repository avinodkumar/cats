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
package com.comcast.cats.vision;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uispec4j.AbstractSwingUIComponent;
import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.Mouse;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.ToggleButton;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;

import com.comcast.cats.vision.test.utils.CommonTestUtils;

public class CATSVisionTest extends UISpecTestCase
{
    protected final Log LOGGER = LogFactory.getLog( getClass() );

    protected void setUp() throws Exception
    {
        System.setProperty( "uispec4j.test.library", "junit" );

        // CatsFramework framework = new CatsFrameworkLocal();

        UISpec4J.setWindowInterceptionTimeLimit( 360000 );
        String[] args =
            {

                    "--server=http://cats-dev2.cable.comcast.com:8080/",
                    "--mac=00:22:10:21:A4:17",
                    "--authToken=dummy-auth-token" };

        setAdapter( new MainClassAdapter( CATSVisionApplication.class, args ) );

    }

    protected void enableComponent( AbstractSwingUIComponent component )
    {
        component.getAwtComponent().setEnabled( true );
    }

    protected Assertion selectCheckBox( CheckBox checkBox )
    {
        enableComponent( checkBox );

        if ( !checkBox.isSelected().isTrue() )
        {
            checkBox.getAwtComponent().doClick();

        }
        return checkBox.isSelected();
    }

    protected Assertion deselectCheckBox( CheckBox checkBox )
    {
        enableComponent( checkBox );

        if ( checkBox.isSelected().isTrue() )
        {
            checkBox.getAwtComponent().doClick();
        }
        return checkBox.isSelected();
    }

    protected JLabel getLabel( Panel parentPanel, String labelName )
    {
        JLabel label = null;

        Component[] components = parentPanel.getSwingComponents( JLabel.class );

        for ( Component component : components )
        {
            if ( component.getName() == labelName )
            {
                label = ( JLabel ) component;

                break;
            }
        }
        return label;
    }

    protected void streamAndAllocateSettop( Window window )
    {
        Panel panel = window.getPanel( "mainPanel" );

        Panel mainControlPanel = panel.getPanel( "mainControlPanel" );

        Panel settopSelectionPanel = mainControlPanel.getPanel( "settopSelectionPanel" );

        ToggleButton jStreamToggleButton = settopSelectionPanel.getToggleButton( "jStreamToggleButton" );

        if ( jStreamToggleButton.getAwtComponent().getText().equals( "Stream" ) )
        {
            jStreamToggleButton.getAwtComponent().doClick();
        }

        try
        {
            Thread.sleep( 500 );
        }
        catch ( InterruptedException interruptedException )
        {
            fail( interruptedException.getMessage() );
        }
        /*
         * Locking Settop
         */
        CheckBox checkBoxLock = settopSelectionPanel.getCheckBox( "Lock" );

        assertTrue( "Unable to lock settop", selectCheckBox( checkBoxLock ) );

    }

    protected void releaseSettop( Window window )
    {
        Panel panel = window.getPanel( "mainPanel" );

        Panel mainControlPanel = panel.getPanel( "mainControlPanel" );

        Panel settopSelectionPanel = mainControlPanel.getPanel( "settopSelectionPanel" );
        CheckBox checkBoxLock = settopSelectionPanel.getCheckBox( "Lock" );
        assertFalse( "Unable to release settop", deselectCheckBox( checkBoxLock ) );
    }

    protected void testRemoteKeyPress( Window window, String... buttonNames ) throws Exception
    {
        Panel panel = window.getPanel( "mainPanel" );

        streamAndAllocateSettop( window );

        Panel remotePanel = panel.getPanel( "remotePanel" );

        for ( String buttonName : buttonNames )
        {
            Button button = remotePanel.getButton( buttonName );

            assertTrue( button.getLabel().contentEquals( buttonName ) );

            Mouse.click( button );

            Thread.sleep( 1500 );
        }
        releaseSettop( window );
    }

    protected Window launchMultivision( Window mainWindow, int... rows ) throws Exception
    {

        Window videoGridWindow = null;

        JPanel configPanel = CommonTestUtils.getConfigPanel( mainWindow );

        final Button launchVideoButton = CommonTestUtils.getConfigPanelButton( mainWindow, "launchVideoButton" );

        Table uiTable = CommonTestUtils.getTableFromConfigPanel( configPanel, "availableSettopsScrollPane",
                "Available Settops", 0 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.AVAILABLE_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.AVAILABLE_TABLE_STRINGS ) );

        assertTrue( uiTable.getRowCount() > ( rows.length + 1 ) );

        uiTable.selectRows( rows );

        assertTrue( "No Settops selected", uiTable.rowsAreSelected( rows ) );

        videoGridWindow = WindowInterceptor.run( launchVideoButton.triggerClick() );

        assertTrue( videoGridWindow.titleEquals( "CATS MultiVision" ) );

        return videoGridWindow;
    }

    protected int[] searchAvailableSettopsAndGetRowsSelected( Window mainWindow, String keyword )
    {

        JPanel configPanel = CommonTestUtils.getConfigPanel( mainWindow );

        final Button searchButton = CommonTestUtils.getConfigPanelButton( mainWindow, "searchButton" );

        final JTextField searchTextField = CommonTestUtils.getConfigPanelTextField( mainWindow, "searchTextField" );

        Table uiTable = CommonTestUtils.getTableFromConfigPanel( configPanel, "availableSettopsScrollPane",
                "Available Settops", 0 );

        assertTrue( "Expected header for the table is - " + Arrays.asList( CommonTestUtils.AVAILABLE_TABLE_STRINGS )
                + "\n but actual header is - " + Arrays.asList( uiTable.getHeader().getColumnNames() ), Arrays.equals(
                uiTable.getHeader().getColumnNames(), CommonTestUtils.AVAILABLE_TABLE_STRINGS ) );

        int numberOfRows = ( uiTable.getRowCount() > 0 ) ? uiTable.getRowCount() : 0;

        assertTrue( "'Available Settops' donot have any rows", numberOfRows > 0 );

        searchTextField.setText( keyword );

        searchButton.click();

        Table uiTableAfterSearch = CommonTestUtils.getTableFromConfigPanel( configPanel, "availableSettopsScrollPane",
                "Available Settops", 0 );

        int rows = ( uiTableAfterSearch.getRowCount() > 0 ) ? uiTableAfterSearch.getRowCount() : 0;

        assertTrue( "No matching settop found", rows > 0 );

        uiTableAfterSearch.selectRow( 0 );

        searchTextField.setText( "" );

        searchButton.click();

        uiTableAfterSearch = CommonTestUtils.getTableFromConfigPanel( configPanel, "availableSettopsScrollPane",
                "Available Settops", 0 );

        JTable table = uiTableAfterSearch.getAwtComponent();

        int numberOfRowsAfterSearch = ( table.getSelectedRows().length > 0 ) ? table.getSelectedRows().length : 0;
        assertTrue( numberOfRowsAfterSearch <= numberOfRows );
        return table.getSelectedRows();
    }

    protected CheckBox getAllocateAllCheckBox( Window multivisionWindow ) throws InterruptedException
    {
        Panel videoControlPanel = multivisionWindow.getPanel( "videoControlPanel" );

        Panel allocationPanel = videoControlPanel.getPanel( "allocationPanel" );

        return allocationPanel.getCheckBox( "allocateAllCheckBox" );

    }
}
