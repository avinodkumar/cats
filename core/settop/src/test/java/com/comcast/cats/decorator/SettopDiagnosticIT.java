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
package com.comcast.cats.decorator;

import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.junit.Test;

import com.comcast.cats.CatsAbstarctIT;
import com.comcast.cats.RemoteCommand;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * Integration test for {@link SettopDiagnostic} specific for each settop
 * family.
 * 
 * @author ssugun00c
 * 
 */
public class SettopDiagnosticIT extends CatsAbstarctIT
{
    private static final String CISCO_LEGACY_MAC    = "00:19:47:25:AD:7E";
    // Not working
    private static final String RNG_MAC             = "00:1A:C3:20:93:F3";
    private static final String DTA_MAC             = "00:00:40:CD:41:CF";
    private static final String HD_DTA_MAC          = "12:BF:00:03:BA:D3";
    private static final String MOTOROLA_LEGACY_MAC = "00:00:00:94:C5:0B";
    // I couldn't find a valid MAC in CHIMSP to test this.
    private static final String PARKER_X1_MAC       = "";
    // I couldn't find a valid MAC in CHIMSP to test this.
    private static final String SAMSUNG_MAC         = "";

    private static final long   KEY_PREE_DELAY      = 2 * 10000;

    private SettopFactory       settopFactory;

    public SettopDiagnosticIT()
    {
        settopFactory = catsFramework.getSettopFactory();
        assertNotNull( settopFactory );
    }

    @Test
    public void settopDiagnosticCiscoLegacy() throws SettopNotFoundException, InterruptedException
    {
        Settop settop = settopFactory.findSettopByHostMac( CISCO_LEGACY_MAC );

        assertNotNull( "Settop should not be  null", settop );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );

        Assert.assertTrue(
                "SettopDiagnostic is not supported for componentType="
                        + ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType()
                        + ". Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                        + settop, settop instanceof SettopDiagnostic );

        SettopDiagnostic settopDiagnostic = ( SettopDiagnostic ) settop;
        settopDiagnostic.pressKey( RemoteCommand.INFO );

        Assert.assertTrue( "showDiagMenu() failed", settopDiagnostic.showDiagMenu() );
        Thread.sleep( KEY_PREE_DELAY );
        Assert.assertTrue( "nextDiagScreen() failed", settopDiagnostic.nextDiagScreen() );
        Thread.sleep( KEY_PREE_DELAY );
        Assert.assertTrue( "nextDiagScreen() failed", settopDiagnostic.nextDiagScreen() );
        Thread.sleep( KEY_PREE_DELAY );
        Assert.assertTrue( "prevDiagScreen() failed", settopDiagnostic.prevDiagScreen() );
        Thread.sleep( KEY_PREE_DELAY );
        Assert.assertTrue( "exitDiagScreen() failed", settopDiagnostic.exitDiagScreen() );

    }

    @Test
    public void settopDiagnosticRng() throws SettopNotFoundException, InterruptedException
    {
        Settop settop = settopFactory.findSettopByHostMac( RNG_MAC );

        assertNotNull( "Settop should not be  null", settop );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );

        Assert.assertTrue(
                "SettopDiagnostic is not supported for componentType="
                        + ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType()
                        + ". Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                        + settop, settop instanceof SettopDiagnostic );

        SettopDiagnostic settopDiagnostic = ( SettopDiagnostic ) settop;
        settopDiagnostic.pressKey( RemoteCommand.INFO );

        Assert.assertTrue( "showDiagMenu() failed", settopDiagnostic.showDiagMenu() );
        Thread.sleep( KEY_PREE_DELAY );
    }

    @Test
    public void settopDiagnosticDta() throws SettopNotFoundException, InterruptedException
    {
        Settop settop = settopFactory.findSettopByHostMac( DTA_MAC );

        assertNotNull( "Settop should not be  null", settop );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );

        Assert.assertTrue(
                "SettopDiagnostic is not supported for componentType="
                        + ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType()
                        + ". Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                        + settop, settop instanceof SettopDiagnostic );

        SettopDiagnostic settopDiagnostic = ( SettopDiagnostic ) settop;
        settopDiagnostic.pressKey( RemoteCommand.INFO );

        Assert.assertTrue( "showDiagMenu() failed", settopDiagnostic.showDiagMenu() );
        Thread.sleep( KEY_PREE_DELAY / 2 );
        Assert.assertTrue( "downDiagItem() failed", settopDiagnostic.downDiagItem() );
        Thread.sleep( KEY_PREE_DELAY );
        Assert.assertTrue( "updiagItem() failed", settopDiagnostic.updiagItem() );
        Thread.sleep( KEY_PREE_DELAY );
        Assert.assertTrue( "updiagItem() failed", settopDiagnostic.updiagItem() );
        Thread.sleep( KEY_PREE_DELAY );
        Assert.assertTrue( "enterDiagScreen() failed", settopDiagnostic.enterDiagScreen() );

    }

    @Test
    public void settopDiagnosticHdDta() throws SettopNotFoundException, InterruptedException
    {
        Settop settop = settopFactory.findSettopByHostMac( HD_DTA_MAC );

        assertNotNull( "Settop should not be  null", settop );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );

        Assert.assertTrue(
                "SettopDiagnostic is not supported for componentType="
                        + ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType()
                        + ". Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                        + settop, settop instanceof SettopDiagnostic );

        SettopDiagnostic settopDiagnostic = ( SettopDiagnostic ) settop;
        settopDiagnostic.pressKey( RemoteCommand.INFO );

        Assert.assertTrue( "showDiagMenu() failed", settopDiagnostic.showDiagMenu() );
    }

    @Test
    public void settopDiagnosticMotorolaLegacy() throws SettopNotFoundException, InterruptedException
    {
        Settop settop = settopFactory.findSettopByHostMac( MOTOROLA_LEGACY_MAC );

        assertNotNull( "Settop should not be  null", settop );
        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );

        Assert.assertTrue(
                "SettopDiagnostic is not supported for componentType="
                        + ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType()
                        + ". Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                        + settop, settop instanceof SettopDiagnostic );

        SettopDiagnostic settopDiagnostic = ( SettopDiagnostic ) settop;
        settopDiagnostic.pressKey( RemoteCommand.INFO );

        Assert.assertTrue( "showDiagMenu() failed", settopDiagnostic.showDiagMenu() );
    }

    @Test
    public void settopSamsung() throws SettopNotFoundException, InterruptedException
    {
    }
}
