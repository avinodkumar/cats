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
package com.comcast.cats.domain;

import java.io.ObjectOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.junit.Test;

public class HardwareConnectionTest
{
    String deviceHost = "192.168.100.2";
    int    devicePort = 8003;
    int    connPort   = 99;

    @Test
    @SuppressWarnings( "deprecation" )
    public void testPath() throws Exception
    {
        HardwareConnection connection = getHardwareConnection();
        String expected = "gc100://192.168.100.2:8003/?connectionport=99";
        Assert.assertEquals( expected, connection.getConnectionPath().toString() );
    }

    @Test
    @SuppressWarnings( "deprecation" )
    public void testMarshaller() throws Exception
    {
        HardwareConnection connection = getHardwareConnection();
        JAXBContext context = JAXBContext.newInstance( SettopDesc.class );
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        marshaller.marshal( connection, System.out );
    }

    @Test
    @SuppressWarnings( "deprecation" )
    public void testExternalization() throws Exception
    {
        HardwareConnection connection = getHardwareConnection();
        ObjectOutputStream objStream = new ObjectOutputStream( System.out );
        connection.writeExternal( objStream );
    }

    @SuppressWarnings( "deprecation" )
    private HardwareConnection getHardwareConnection()
    {
        HardwareConnection connection = new HardwareConnection();
        HardwareDevice hardwareDevice = new HardwareDevice( HardwareType.IR );
        hardwareDevice.setComponentType( "GC100" );
        hardwareDevice.setHost( deviceHost );
        hardwareDevice.setPort( devicePort );
        connection.setHardwareDevice( hardwareDevice );
        connection.setPort( connPort );
        return connection;
    }
}
