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
package com.comcast.cats.vision.power;

import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.Mouse;
import org.uispec4j.Panel;
import org.uispec4j.Window;

import com.comcast.cats.vision.CATSVisionTest;

public class PowerPanelTest extends CATSVisionTest
{

    @Test
    public void testRemoteKeyPress() throws Exception
    {
        Window window = getMainWindow();

        String[] buttonNames =
            { "On", "Off", "Reboot" };

        testPowerPannelKeyPress( window, buttonNames );
    }

    protected void testPowerPannelKeyPress( Window window, String... buttonNames ) throws Exception
    {
        Panel panel = window.getPanel( "mainPanel" );

        streamAndAllocateSettop( window );

        Panel powerPanel = panel.getPanel( "powerPanel" );

        for ( String buttonName : buttonNames )
        {
            Button button = powerPanel.getButton( buttonName );

            assertTrue( button.getLabel().contentEquals( buttonName ) );

            Mouse.click( button );
            LOGGER.info( "invoking :" + buttonName );
        }
        releaseSettop( window );
    }

}
