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
package com.comcast.cats.vision.imagecompare;

import javax.swing.JLabel;

import org.apache.log4j.Logger;
import org.uispec4j.MenuBar;
import org.uispec4j.MenuItem;
import org.uispec4j.Mouse;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.TextBox;
import org.uispec4j.ToggleButton;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.FileChooserHandler;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import com.comcast.cats.vision.CATSVisionTest;
import com.comcast.cats.vision.panel.imagecompare.ImageCompareUtil;
import com.comcast.cats.vision.util.CatsVisionConstants;

public class TestImageCompareAndOcr extends CATSVisionTest
{

    private static final Logger logger = Logger.getLogger( TestImageCompareAndOcr.class );

    public void testAllImageCompareRegions() throws Exception
    {
        logger.info( "testAllImageCompareRegions() started" );

        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem testAllRegMenuItem = loadSnapShotAndGetMenuItem( icWindow, "Test All Image Compares",
                "src/test/resources/imagecompare/all_ic_regions.xml" );

        testAllRegMenuItem.click();

        Thread.sleep( 9000 );

        checkPassFailLabel( icWindow, "Fail" );

        logger.info( "testAllImageCompareRegions() completed" );
    }

    public void testImageComapreCurrentRegion() throws Exception
    {
        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem testCurrentRegMenuItem = loadSnapShotAndGetMenuItem( icWindow, "Test Current Region",
                "src/test/resources/imagecompare/current_ic_region.xml" );

        testCurrentRegMenuItem.click();

        Thread.sleep( 9000 );

        checkPassFailLabel( icWindow, "Fail" );
    }

    public void testSaveCurrentRegion() throws Exception
    {
        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem testAllRegMenuItem = loadSnapShotAndGetMenuItem( icWindow, "Test Current Region",
                "src/test/resources/imagecompare/current_ic_region.xml" );

        testAllRegMenuItem.click();

        Thread.sleep( 9000 );

        checkPassFailLabel( icWindow, "Fail" );

        MenuItem saveSnapshotAsMenuItem = getImageWindowMenuItem( icWindow, "Save Snapshot As" );

        WindowInterceptor.init( saveSnapshotAsMenuItem.triggerClick() ).process(
                FileChooserHandler.init().titleEquals( "Save" ).assertIsSaveDialog().select(
                        "src/test/resources/imagecompare/save_ic.xml" ) ).run();

        Thread.sleep( 500 );

        MenuItem saveSnapshotMenuItem = getImageWindowMenuItem( icWindow, "Save Snapshot" );

        saveSnapshotMenuItem.click();
    }
    
   
    public void testLoadRegion() throws Exception
    {
        logger.info( "testLoadRegion() started" );
        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem optionsMenu = loadSnapShotAndGetOptionsMenu( icWindow,
                "src/test/resources/imagecompare/all_ic_regions.xml" );

        MenuItem loadRegionMenuItem = optionsMenu.getSubMenu( "Load Region" );

        Mouse.click( loadRegionMenuItem );
        /*
         * loadRegionMenuItem.click();
         * 
         * Mouse.pressed ( optionsMenu, 5, 0 );
         * 
         * Mouse.drag( optionsMenu, 5, 500 );
         * 
         * Mouse.released ( loadRegionMenuItem, 5, 500 );
         */

        assertTrue( "Expected sub menu items were 'main_menu' and 'Xfinity'", loadRegionMenuItem.contentEquals(
                "main_menu", "Xfinity" ) );

        MenuItem subMenuItem = loadRegionMenuItem.getSubMenu( "main_menu" );

        subMenuItem.click();

        Panel regionInfoPanel = icWindow.getPanel( "regionInfoPanel" );

        TextBox txtBoxRegionName = regionInfoPanel.getTextBox( "txtRegionName" );

        assertTrue( "Expected name of the region is 'main_menu'", txtBoxRegionName.getText()
                .contentEquals( "main_menu" ) );

    }

    public void testClearCurrentRegion() throws Exception
    {
        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem testClearRegMenuItem = loadSnapShotAndGetMenuItem( icWindow, "Clear Current Region",
                "src/test/resources/imagecompare/current_ic_region.xml" );

        testClearRegMenuItem.click();
    }
    
    public void testDeleteRegion() throws Exception
    {
        logger.info( "testLoadRegion() started" );
        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem optionsMenu = loadSnapShotAndGetOptionsMenu( icWindow,
                "src/test/resources/imagecompare/all_ic_regions.xml" );

        MenuItem deleteRegionMenuItem = optionsMenu.getSubMenu( "Delete Region" );

        Mouse.click( deleteRegionMenuItem );
        /*
         * loadRegionMenuItem.click();
         * 
         * Mouse.pressed ( optionsMenu, 5, 0 );
         * 
         * Mouse.drag( optionsMenu, 5, 500 );
         * 
         * Mouse.released ( loadRegionMenuItem, 5, 500 );
         */

        assertTrue( "Expected sub menu items were 'main_menu' and 'Xfinity'", deleteRegionMenuItem.contentEquals(
                "main_menu", "Xfinity" ) );

        MenuItem subMenuItem = deleteRegionMenuItem.getSubMenu( "main_menu" );

        subMenuItem.click();

/*        MenuItem deleteRegionMenuItem1 = optionsMenu.getSubMenu( "Delete Region" );

        Panel regionInfoPanel = icWindow.getPanel( "regionInfoPanel" );

        TextBox txtBoxRegionName = regionInfoPanel.getTextBox( "txtRegionName" );

        assertTrue( "Expected name of the region is 'main_menu'", txtBoxRegionName.getText()
                .contentEquals( "main_menu" ) );*/

    }
    
    public void testNewRegion() throws Exception
    {
        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem testNewRegMenuItem = getImageWindowMenuItem(icWindow,"New Region");
        MenuItem testICRegion = testNewRegMenuItem.getSubMenu( "Image Compare" );
        testICRegion.click();
        

        MenuItem testOCRRegion = testNewRegMenuItem.getSubMenu( "OCR" );
        testOCRRegion.click();

        Thread.sleep( 9000 );

    }

    public void testOcrCurrentRegion() throws Exception
    {
        MenuItem snapImageMenuItem = getSnapImageMenuItem();
        /*
         * Clicking 'Snap Image' menu item
         */
        Window icWindow = WindowInterceptor.run( snapImageMenuItem.triggerClick() );

        assertTrue( "Image Window".equals( icWindow.getTitle() ) );

        MenuItem testCurrentRegMenuItem = loadSnapShotAndGetMenuItem( icWindow, "Test Current Region",
                "src/test/resources/imagecompare/ocr_region.xml" );

        WindowInterceptor.init( testCurrentRegMenuItem.triggerClick() ).process( new WindowHandler()
        {
            public Trigger process( Window window )
            {
                return window.getButton( "OK" ).triggerClick();
            }
        } ).run();

        Thread.sleep( 9000 );

        checkPassFailLabel( icWindow, "Fail" );
    }

    private MenuItem getSnapImageMenuItem()
    {

        Window window = getMainWindow();

        TabGroup mainTabGroup = window.getTabGroup( "visionTabs" );

        mainTabGroup.selectTab( CatsVisionConstants.APPLICATION_TITLE );

        Panel mainPanel = mainTabGroup.getSelectedTab();

        Panel settopSelectionPanel = mainPanel.getPanel( "settopSelectionPanel" );

        ToggleButton jStreamToggleButton = settopSelectionPanel.getToggleButton( "jStreamToggleButton" );
        /*
         * Streaming settop video
         */
        if ( jStreamToggleButton.getAwtComponent().getText().equals( "Stream" ) )
        {
            jStreamToggleButton.click();
        }

        /*
         * Getting menu bar from 'CATS Vision' frame.
         */
        MenuBar menuBar = window.getMenuBar();

        MenuItem menuItem = menuBar.getMenu( "Options" );

        MenuItem snapImageMenuItem = menuItem.getSubMenu( "Snap Image" );

        return snapImageMenuItem;
    }

    private MenuItem loadSnapShotAndGetOptionsMenu( Window icWindow, String filePath )
    {
        MenuBar icMenuBar = icWindow.getMenuBar();

        MenuItem optionsMenu = icMenuBar.getMenu( "Options" );

        MenuItem loadImageMenuItem = optionsMenu.getSubMenu( "Load Snapshot" );

        WindowInterceptor.init( loadImageMenuItem.triggerClick() ).process(
                FileChooserHandler.init().titleEquals( "Open" ).assertAcceptsFilesOnly().select( filePath ) ).run();

        return optionsMenu;
    }

    private MenuItem loadSnapShotAndGetMenuItem( Window icWindow, String menuName, String filePath )
    {
        MenuItem optionsMenu = loadSnapShotAndGetOptionsMenu( icWindow, filePath );

        MenuItem testAllMenuItem = optionsMenu.getSubMenu( menuName );

        return testAllMenuItem;
    }

    private MenuItem getImageWindowMenuItem( Window icWindow, String menuName )
    {
        MenuBar icMenuBar = icWindow.getMenuBar();

        MenuItem icMenuItem = icMenuBar.getMenu( "Options" );

        MenuItem menuItem = icMenuItem.getSubMenu( menuName );

        return menuItem;
    }

    private void checkPassFailLabel( Window icWindow, String status )
    {

        Panel addUpdatePanel = icWindow.getPanel( "addUpdatePanel" );

        JLabel passFailLabel = getLabel( addUpdatePanel, "lblPassFail" );

        assertTrue( "Expected text in label is '" + status + "', but text found is '" + passFailLabel.getText() + "'.",
                passFailLabel.getText().equals( status ) );
    }

    public void testICUtils()
    {

        assertEquals( "file.jpg", ImageCompareUtil.changeExtensionToJPG( "file.xml" ) );

        try
        {
            ImageCompareUtil.changeExtensionToJPG( null );
            fail();
        }
        catch ( IllegalArgumentException e )
        {}

        try
        {
            ImageCompareUtil.changeExtensionToJPG( "" );
            fail();
        }
        catch ( IllegalArgumentException e )
        {}

        assertEquals( "file.xml", ImageCompareUtil.changeExtensionToXML( "file.jpg" ) );
        try
        {
            ImageCompareUtil.changeExtensionToXML( null );
            fail();
        }
        catch ( IllegalArgumentException e )
        {}

        try
        {
            ImageCompareUtil.changeExtensionToXML( "" );
            fail();
        }
        catch ( IllegalArgumentException e )
        {}
        assertEquals( "file.jpg", ImageCompareUtil.getFileNameFromFilePath( "\\path\\file.jpg" ) );
        try
        {
            ImageCompareUtil.getFileNameFromFilePath( null );
            fail();
        }
        catch ( IllegalArgumentException e )
        {}

        try
        {
            ImageCompareUtil.changeExtensionToXML( "" );
            fail();
        }
        catch ( IllegalArgumentException e )
        {}
        assertNull( ImageCompareUtil.loadImageFromFile( null ) );

        assertNotNull( ImageCompareUtil.loadImageFromFile( "src\\test\\resources\\imagecompare\\all_ic_regions.jpg" ) );
    }
}
