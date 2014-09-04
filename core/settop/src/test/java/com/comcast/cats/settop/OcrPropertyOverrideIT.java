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
package com.comcast.cats.settop;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.comcast.cats.CatsAbstarctIT;
import com.comcast.cats.Settop;
import com.comcast.cats.SettopFactory;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.domain.service.SettopDomainService;

/**
 * Integration test for to verify Support OCR Service override in cats.props
 * <br>
 * 
 * 
 * <pre>
 * Must have the following in cats.props
 * 
 * cats.user.authToken        =   1111111-222-33333-444-5555555555
 * cats.ocr.server.url        =   http://localhost:8080/
 * 
 * </pre>
 * 
 * @author ssugun00c
 * 
 */
public class OcrPropertyOverrideIT extends CatsAbstarctIT
{
    private static final String MAC_01 = "00:22:10:21:A2:E0";
    private static final String MAC_02 = "00:22:10:21:A4:B0";

    private SettopFactory       settopFactory;
    private SettopDomainService settopDomainService;

    public OcrPropertyOverrideIT()
    {
        settopFactory = catsFramework.getSettopFactory();
        settopDomainService = catsFramework.getContext().getBean( SettopDomainService.class );
    }

    @Test
    public void findSettopByHostMac() throws SettopNotFoundException
    {
        assertNotNull( settopFactory );
        assertNotNull( settopDomainService );

        Settop settop_01 = settopFactory.findSettopByHostMac( MAC_01 );
        assertNotNull( settop_01 );
        assertNotNull( "RemoteProvider should not be  null", settop_01.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop_01.getVideo() );
        assertNotNull( "ImageCompareProvider should not be  null", settop_01.getImageCompareProvider() );
        LOGGER.info( "settop_01", settop_01 );

        Settop settop_02 = settopFactory.findSettopByHostMac( MAC_02 );
        assertNotNull( settop_02 );
        assertNotNull( "RemoteProvider should not be  null", settop_02.getRemote() );
        assertNotNull( "VideoProvider should not be  null", settop_02.getVideo() );
        assertNotNull( "ImageCompareProvider should not be  null", settop_02.getImageCompareProvider() );
        LOGGER.info( "settop_02", settop_02 );

        LOGGER.info( "[VideoURL]" + settop_01.getVideo().getVideoURL() );
        LOGGER.info( "[VideoPath]" + settop_01.getVideoPath() );

        LOGGER.info( "[VideoURL]" + settop_02.getVideo().getVideoURL() );
        LOGGER.info( "[VideoPath]" + settop_02.getVideoPath() );
    }
}
