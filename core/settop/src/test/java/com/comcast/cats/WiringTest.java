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
package com.comcast.cats;

import static com.comcast.cats.RemoteCommand.GUIDE;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.exception.AllocationException;
import com.comcast.cats.domain.exception.SettopNotFoundException;
import com.comcast.cats.provider.RemoteProvider;
import com.comcast.cats.provider.SettopExclusiveAccessEnforcer;

public class WiringTest
{
    protected final Logger          LOGGER     = LoggerFactory.getLogger( getClass() );
    public final ApplicationContext ctx;
    public final String             serverBase = "http://localhost:8080/";

    public WiringTest()
    {
        // Uncomment this to obtain a reference to the settop-context.xml.
        System.setProperty( "cats.server.url", serverBase );
        ctx = new ClassPathXmlApplicationContext( "classpath:settop-context-stub.xml" );
    }

    @Test
    // This is commented out so it doesn't break the but it works.
    public void testSpringWireConfiguration() throws URISyntaxException, AllocationException, SettopNotFoundException
    {
        // URL irWsdl = (URL) ctx.getBean("irServiceWsdlLocation");
        // LOGGER.info(irWsdl);
        // The real test, can I get the IRService.
        // IRService irService = (IRService) ctx.getBean("irService");
        // DeviceSearchServiceDummy dss = (DeviceSearchServiceDummy)
        // ctx.getBean("deviceSearchService");

        SettopExclusiveAccessEnforcer provider = ( SettopExclusiveAccessEnforcer ) ctx
                .getBean( "exclusiveAccessManager" );
        /*
         * ExclusiveAccessBridge bridge = ExclusiveAccessBridge.getInstance();
         * bridge.setExclusiveAccessProvider(provider);
         */
        SettopDesc settopDesc = new SettopDesc();

        settopDesc.setId( "ID-00:19:A6:6E:B5:CB" );
        settopDesc.setHostMacAddress( "00:19:A6:6E:B5:CB" );
        settopDesc.setAudioPath( new URI( "chromamxx://192.168.120.2/?port=3" ) );
        settopDesc.setRemotePath( new URI( "gc100://192.168.120.2/?port=2" ) );
        settopDesc.setTracePath( new URI( "traceserver://192.168.120.2/" ) );
        settopDesc.setVideoPath( new URI( "axis://192.168.120.2/?camera=1" ) );
        settopDesc.setManufacturer( "Motorola" );
        settopDesc.setRemoteType( "COMCAST" );
        settopDesc.setModel( "DCH3200" );
        settopDesc.setSerialNumber( "" );
        settopDesc.setUnitAddress( "000-03342-87134-163" );

        // dss.addSettop(settopDesc);

        SettopFactory settopFactory = ( SettopFactory ) ctx.getBean( "settopFactory" );
        Settop settop = settopFactory.findSettopByHostMac( "00:19:A6:6E:B5:CB" );
        provider.lock( settop );
        LOGGER.info( "Manufacturer = " + settop.getManufacturer() );
        RemoteProvider remote = settop.getRemote();
        remote.pressKey( GUIDE );
        // settop.pressKey(EXIT);
        provider.release( settop );
    }
}
