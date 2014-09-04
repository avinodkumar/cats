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
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.SettopNotFoundException;

/**
 * Integration test for {@link SettopDiagnostic}.
 * 
 * @author ssugun00c
 * 
 */
public class SettopDecoratorIT extends CatsAbstarctIT
{
    private static final String DIAG_SUPPORTDE_MAC   = "00:21:1E:EA:7E:CD";
    private static final String DIAG_UNSUPPORTDE_MAC = "00:1A:C3:20:93:F3";

    private SettopFactory       settopFactory;

    public SettopDecoratorIT()
    {
        settopFactory = catsFramework.getSettopFactory();
        assertNotNull( settopFactory );
    }

    @Test
    public void testSettopDiagnostic() throws SettopNotFoundException
    {
        Settop settop = settopFactory.findSettopByHostMac( DIAG_SUPPORTDE_MAC );

        assertNotNull( settop );

        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );
        assertNotNull( "ImageCompareProvider should not be  null", settop.getImageCompareProvider() );

        Assert.assertTrue( settop instanceof SettopDiagnostic );

        if ( settop instanceof SettopDiagnostic )
        {
            SettopDiagnostic settopDiagnostic = ( SettopDiagnostic ) settop;
            settopDiagnostic.showDiagMenu();

            assertNotNull( "PowerProvider in settopDiagnostic should not be  null", settopDiagnostic.getPower() );
            assertNotNull( "RemoteProvider in settopDiagnostic should not be  null", settopDiagnostic.getRemote() );
            assertNotNull( "RemoteProvider in settopDiagnostic should not be  null", settopDiagnostic.getRemote() );
            assertNotNull( "VideoProvider in settopDiagnostic should not be  null", settopDiagnostic.getVideo() );
            assertNotNull( "ImageCompareProvider in settopDiagnostic should not be  null",
                    settopDiagnostic.getImageCompareProvider() );
        }
        else
        {
            // This else block will never execute because of this assert
            // statement.
            // Assert.assertTrue( settop instanceof SettopDiagnostic );

            throw new UnsupportedOperationException(
                    "SettopDiagnostic is not supported for componentType="
                            + ( ( SettopDesc ) settop.getSettopInfo() ).getComponentType()
                            + ". Please request the feature addition through an intake ticket with detailed information of remote command sequence.\nAdditional Settop information.\n"
                            + settop );
        }
        LOGGER.info( settop.toString() );
    }

    @Test
    public void testSettopDiagnosticUnsupported() throws SettopNotFoundException
    {
        Settop settop = settopFactory.findSettopByHostMac( DIAG_UNSUPPORTDE_MAC );

        assertNotNull( settop );

        assertNotNull( "PowerProvider should not be  null", settop.getPower() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "RemoteProvider should not be  null", settop.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop.getVideo() );
        assertNotNull( "ImageCompareProvider should not be  null", settop.getImageCompareProvider() );

        Assert.assertFalse( settop instanceof SettopDiagnostic );

        LOGGER.info( settop.toString() );
    }

}
