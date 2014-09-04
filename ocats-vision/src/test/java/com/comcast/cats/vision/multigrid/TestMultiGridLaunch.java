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
package com.comcast.cats.vision.multigrid;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.Mouse;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.FileChooserHandler;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import com.comcast.cats.vision.CATSVisionTest;

/**
 * Tests for Multi grid video frame.
 * 
 * @author aswathyann
 * 
 */

public class TestMultiGridLaunch extends CATSVisionTest
{
    protected final Log LOGGER = LogFactory.getLog( getClass() );

    /**
     * Test to launch VideoGrid from MySettops tab.
     * 
     * @throws Exception
     */
    public void testLaunchVideoGridFromMySettops() throws Exception
    {

        Window mainWindow = getMainWindow();

        // Launch 2 settops
        launchMultivision( mainWindow, 2 );

        // Launch 3 settops
        launchMultivision( mainWindow, 2, 3, 4 );

        // Launch 5 settops
        launchMultivision( mainWindow, 2, 3, 4, 5, 6 );

    }

    /**
     * Test to launch VideoGrid and allocate all settops in multi grid video.
     * 
     * @throws Exception
     */
    public void testAllocateAllSettops() throws Exception
    {

        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, 2, 3, 4 );

        Panel videoControlPanel = videoGridWindow.getPanel( "videoControlPanel" );

        Panel allocationPanel = videoControlPanel.getPanel( "allocationPanel" );

        CheckBox allocateAllCheckBox = allocationPanel.getCheckBox( "allocateAllCheckBox" );

        // Allocate all settops
        allocateAllCheckBox.click();

        Thread.sleep( 500 );
        // Release all settops
        allocateAllCheckBox.click();

        mainWindow = null;
    }

    /**
     * Test to launch VideoGrid and perform remote key presses.
     * 
     * @throws Exception
     */
    public void testAllocateSettopsAndPerformKeyPress() throws Exception
    {
        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, 2, 3, 4 );

        Panel videoControlPanel = videoGridWindow.getPanel( "videoControlPanel" );

        Panel allocationPanel = videoControlPanel.getPanel( "allocationPanel" );

        CheckBox allocateAllCheckBox = allocationPanel.getCheckBox( "allocateAllCheckBox" );

        // Allocate all settops
        allocateAllCheckBox.click();

        Panel gridRemotePanel = videoControlPanel.getPanel( "gridRemotePanel" );

        String[] buttonNames =
            { "MENU" };
        // Perform button key presses

        for ( String buttonName : buttonNames )
        {
            Button button = gridRemotePanel.getButton( buttonName );

            assertTrue( button.getLabel().contentEquals( buttonName ) );

            Mouse.click( button );

            Thread.sleep( 500 );
        }
        // Release all settops
        allocateAllCheckBox.click();

        mainWindow = null;
    }

    /**
     * Test to save and clear log in multi grid video frame.
     * 
     * @throws Exception
     */
    public void testSaveAndClearLog() throws Exception
    {

        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, 2, 3, 4 );

        Panel videoControlPanel = videoGridWindow.getPanel( "videoControlPanel" );

        Panel allocationPanel = videoControlPanel.getPanel( "allocationPanel" );

        CheckBox allocateAllCheckBox = allocationPanel.getCheckBox( "allocateAllCheckBox" );

        // Allocate all settops
        allocateAllCheckBox.click();

        Panel gridRemotePanel = videoControlPanel.getPanel( "gridRemotePanel" );

        String[] buttonNames =
            { "MENU" };

        // Perform button key presses

        for ( String buttonName : buttonNames )
        {
            Button button = gridRemotePanel.getButton( buttonName );

            assertTrue( button.getLabel().contentEquals( buttonName ) );

            Mouse.click( button );

            Thread.sleep( 500 );
        }
        // Release all settops
        allocateAllCheckBox.click();

        Panel logPanel = videoGridWindow.getPanel( "logPanel" );

        Button saveButton = logPanel.getButton( "saveButton" );

        // Delete 'log.txt' if it is already present
        assertTrue( deleteFileIfExists( "src/test/resources/videogrid/log.txt" ) );

        // Save log in the file 'log.txt'
        WindowInterceptor
                .init( saveButton.triggerClick() )
                .process(
                        FileChooserHandler.init().titleEquals( "Save" ).assertIsSaveDialog()
                                .select( "src/test/resources/videogrid/log.txt" ) ).run();

        Button clearButton = logPanel.getButton( "clearButton" );

        // Clear log
        clearButton.click();

    }

    /**
     * Test to DirectTune through Multigrid view..
     * 
     * @throws Exception
     */
    public void testDirectTune() throws Exception
    {

        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, 2, 3, 4 );

        Panel videoControlPanel = videoGridWindow.getPanel( "videoControlPanel" );

        Panel allocationPanel = videoControlPanel.getPanel( "allocationPanel" );

        CheckBox allocateAllCheckBox = allocationPanel.getCheckBox( "allocateAllCheckBox" );

        // Allocate all settops
        allocateAllCheckBox.click();

        Panel gridRemotePanel = videoControlPanel.getPanel( "gridRemotePanel" );
        Panel directTunePanel = gridRemotePanel.getPanel( "DirectTunePanel" );
        TextBox textBox = directTunePanel.getTextBox( "DirectTuneTextField" );
        textBox.setText( "12" );
        Button direcTuneButton = directTunePanel.getButton( "DirectTuneButton" );
        direcTuneButton.click();
        // Perform button key presses
        allocateAllCheckBox.click();

        // Test directTune without allocating
        textBox.setText( "12" );
        WindowInterceptor.init( mouseClick( direcTuneButton ) ).process( new WindowHandler()
        {
            @Override
            public Trigger process( Window window ) throws Exception
            {
                LOGGER.info( "Window " + window.getTitle() );
                assertTrue( "Warning :Unable to perform the operation".equals( window.getTitle() ) );
                return window.getButton( "OK" ).triggerClick();
            }
        } ).run();

        // Test directTune without wrong string
        textBox.setText( "ABC" );
        WindowInterceptor.init( mouseClick( direcTuneButton ) ).process( new WindowHandler()
        {
            @Override
            public Trigger process( Window window ) throws Exception
            {
                LOGGER.info( "Window " + window.getTitle() );
                assertTrue( "Error: Not a valid channel number :".equals( window.getTitle() ) );
                return window.getButton( "OK" ).triggerClick();
            }
        } ).run();

    }

    /**
     * Test to allocate single settop in multi grid video frame.
     * 
     * @throws Exception
     */
    public void testAllocateSingleSettop() throws Exception
    {

        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, 2, 3, 4 );

        Panel videoControlPanel = videoGridWindow.getPanel( "videoGridPanel" );

        Panel videoPanel = videoControlPanel.getPanel( "videoPanel0" );

        CheckBox allocationCheckBox = videoPanel.getCheckBox( "allocationCheckBox" );

        // Allocate settop
        allocationCheckBox.click();

        // Release settop
        allocationCheckBox.click();

        mainWindow = null;
    }

    /**
     * Test to zoom a video panel in multi grid video frame.
     * 
     * @throws Exception
     */
    public void testZoomPanel() throws Exception
    {
        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, 2, 3, 4 );

        Panel videoGridPanel = videoGridWindow.getPanel( "videoGridPanel" );

        Panel videoPanel0 = videoGridPanel.getPanel( "videoPanel0" );

        // Zooming video panel
        Window zoomWindow = WindowInterceptor.run( doubleClick( videoPanel0 ) );

        assertTrue( "Expected title of the frame was 'Zoomed video', but the actual title is '" + zoomWindow.getTitle()
                + "'", zoomWindow.getTitle().contentEquals( "Zoomed video" ) );

        // Zooming another video panel
        Panel videoPanel1 = videoGridPanel.getPanel( "videoPanel1" );

        Window zoomWindowSecond = WindowInterceptor.run( doubleClick( videoPanel1 ) );

        assertTrue(
                "Expected title of the frame was 'Zoomed video', but the actual title is '"
                        + zoomWindowSecond.getTitle() + "'", zoomWindowSecond.getTitle().contentEquals( "Zoomed video" ) );
    }

    private Trigger doubleClick( final Panel panel )
    {
        return new Trigger()
        {
            public void run()
            {
                Mouse.doubleClick( panel );
            }
        };
    }

    private Trigger mouseClick( final Button button )
    {
        return new Trigger()
        {

            @Override
            public void run() throws Exception
            {
                Mouse.click( button );

            }
        };
    }

    private boolean deleteFileIfExists( String pathName )
    {
        File file = new File( pathName );

        if ( file.exists() )
        {
            return file.delete();
        }
        else
        {
            return true;
        }
    }

    /**
     * Test to launch VideoGrid and perform remote key presses.
     * 
     * @throws Exception
     */
    public void testPerformKeyPressWithoutAllocation() throws Exception
    {
        Window mainWindow = getMainWindow();

        Window videoGridWindow = launchMultivision( mainWindow, 2, 3, 4 );

        Panel videoControlPanel = videoGridWindow.getPanel( "videoControlPanel" );

        Panel gridRemotePanel = videoControlPanel.getPanel( "gridRemotePanel" );

        String[] buttonNames =
            { "MENU" };

        // Perform button key presses
        for ( String buttonName : buttonNames )
        {
            Button button = gridRemotePanel.getButton( buttonName );

            assertTrue( button.getLabel().contentEquals( buttonName ) );

            WindowInterceptor.init( mouseClick( button ) ).process( new WindowHandler()
            {
                @Override
                public Trigger process( Window window ) throws Exception
                {
                    assertTrue( "Warning :Unable to perform the operation".equals( window.getTitle() ) );
                    return window.getButton( "OK" ).triggerClick();
                }
            } ).run();

            Thread.sleep( 500 );
        }

        mainWindow = null;
    }
}
