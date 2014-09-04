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

import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.Mouse;
import org.uispec4j.Panel;
import org.uispec4j.Window;

import com.comcast.cats.vision.CATSVisionTest;

public class TestMultiVisionPower extends CATSVisionTest
{
    private String searchSettop = "00:19:47:25:B6:1E";

    @Test
    public void testRemoteKeyPress() throws Exception
    {
        Window mainWindow = getMainWindow();

        String[] buttonNames =
            { "On", "Off", "Reboot" };

        Window multivisionWindow = launchMultivision( mainWindow, searchAvailableSettopsAndGetRowsSelected( mainWindow,
                searchSettop ) );

        testMultiVisionPowerPanelKeyPress( multivisionWindow, buttonNames );
    }

    protected void testMultiVisionPowerPanelKeyPress( Window multivisionWindow, String... buttonNames )
            throws Exception
    {
        CheckBox allocateAllCheckBox = getAllocateAllCheckBox( multivisionWindow );
        // Allocate All settops
        allocateAllCheckBox.click();

        Thread.sleep( 500 );
        Panel gridPowerPanel = multivisionWindow.getPanel( "gridPowerPanel" );

        for ( String buttonName : buttonNames )
        {
            Button button = gridPowerPanel.getButton( buttonName );

            assertTrue( button.getLabel().contentEquals( buttonName ) );

            Mouse.click( button );
            LOGGER.info( "invoking :" + buttonName );
        }
        // Release All settops
        allocateAllCheckBox.click();
    }
}